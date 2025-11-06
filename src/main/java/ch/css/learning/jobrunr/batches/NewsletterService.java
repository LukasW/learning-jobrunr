package ch.css.learning.jobrunr.batches;

import ch.css.learning.jobrunr.MyJobService;
import ch.css.learning.jobrunr.domain.MailService;
import ch.css.learning.jobrunr.domain.User;
import ch.css.learning.jobrunr.domain.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.JobBuilder;
import org.jobrunr.scheduling.JobRequestScheduler;
import org.jobrunr.scheduling.JobScheduler;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.jobrunr.scheduling.JobBuilder.aJob;

@ApplicationScoped
public class NewsletterService {

    @Inject
    UserRepository userRepository;

    @Inject
    MailService mailService;

    @Inject
    JobScheduler jobScheduler;

    @Inject
    JobRequestScheduler jobRequestScheduler;
    @Inject
    MyJobService myJobService;

    @Transactional
    public void startEmailsToAllSubscribersBatch(String mailTemplateKey) {
        jobScheduler.startBatch(() -> this.sendEMailToAllSubscribers(mailTemplateKey));
    }

    @Transactional
    @Job(name = "Send E-Mails to all subscribers", retries = 1)
    public void sendEMailToAllSubscribers(String mailTemplateKey) {
        AtomicInteger counter = new AtomicInteger();


        Stream<JobBuilder> jobBuilderStream = userRepository
                .streamAll()
                .map(User::getId)
                .peek(userId -> {
                    int count = counter.incrementAndGet();
                    if (count % 1000 == 0) {
                        System.out.println("Processed " + count + " userIds, current: " + userId);
                    }
                })
                .map(id -> aJob()
                        .withId(UUID.randomUUID())
                        .withAmountOfRetries(3)
                        .withLabels("batch:" + "blubb")
                        .withDetails(() -> mailService.send(id, mailTemplateKey, JobContext.Null))
                );

        jobScheduler.create(
                jobBuilderStream)
        ;


        System.out.println("Enqueued emails to all subscribers with template: " + mailTemplateKey);
    }
}