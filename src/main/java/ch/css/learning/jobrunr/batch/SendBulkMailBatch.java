package ch.css.learning.jobrunr.batch;

import ch.css.learning.jobrunr.domain.User;
import ch.css.learning.jobrunr.domain.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.JobBuilder;
import org.jobrunr.scheduling.JobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.jobrunr.scheduling.JobBuilder.aJob;

@ApplicationScoped
public class SendBulkMailBatch {

    public static final Logger LOGGER = LoggerFactory.getLogger(SendBulkMailBatch.class);
    @Inject
    UserRepository userRepository;

    @Inject
    SendSingleMailJob sendMailJob;

    @Inject
    JobScheduler jobScheduler;

    @Transactional
    public void startSendBulkMailBatch(String mailTemplateKey) {
        jobScheduler.startBatch(() -> this.sendEMailToAllSubscribers(mailTemplateKey, JobContext.Null));
    }

    @Transactional
    @Job(name = "BulkSend", retries = 1)
    public void sendEMailToAllSubscribers(String mailTemplateKey, JobContext jobContext) {
        AtomicInteger counter = new AtomicInteger();

        Stream<JobBuilder> jobBuilderStream = userRepository
                .streamAll()
                .map(User::getId)
                .map(id -> aJob()
                        .withId(UUID.randomUUID())
                        .withAmountOfRetries(0)
                        .withName("send-mail:" + id)
                        .withLabels(getBatchJobIdentifier(mailTemplateKey, jobContext))
                        .withDetails(() -> sendMailJob.send(id, mailTemplateKey, JobContext.Null))
                );

        jobScheduler.create(jobBuilderStream);
        LOGGER.info("Enqueued emails to all subscribers with template: {}", mailTemplateKey);
    }

    private static String getBatchJobIdentifier(String mailTemplateKey, JobContext jobContext) {
        // Make JobName max 8 chars to avoid exceeding label length limits
        String batchJobName = jobContext.getJobName();
        batchJobName = trunc(batchJobName, 8);
        return "batch:" + trunc(batchJobName, 8) + "-" + trunc(mailTemplateKey, 8);
    }

    private static String trunc(String s, int maxLength) {
        if (s.length() > maxLength) {
            s = s.substring(0, maxLength);
        }
        return s;
    }
}