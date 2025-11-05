package ch.css.learning.jobrunr.batches;

import ch.css.learning.jobrunr.domain.MailService;
import ch.css.learning.jobrunr.domain.User;
import ch.css.learning.jobrunr.domain.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jobrunr.scheduling.JobScheduler;

import java.util.stream.Stream;

@ApplicationScoped
public class NewsletterService {

    @Inject
    UserRepository userRepository;

    @Inject
    MailService mailService;

    @Inject
    JobScheduler jobScheduler;


    @Transactional
    public void sendEmailsToAllSubscribers(String mailTemplateKey) {
        jobScheduler.startBatch(() -> this.blubb(mailTemplateKey));
    }

    @Transactional
    public void blubb(String mailTemplateKey) {
        Stream<Long> userIdStream = userRepository.streamAll().map(User::getId);
        jobScheduler.enqueue(
                userIdStream,
                (userId) -> mailService.send(userId, mailTemplateKey));
    }
}