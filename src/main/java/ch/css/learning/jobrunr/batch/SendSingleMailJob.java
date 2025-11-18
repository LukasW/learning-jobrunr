package ch.css.learning.jobrunr.batch;

import ch.css.learning.jobrunr.domain.Mail;
import ch.css.learning.jobrunr.domain.MailRepository;
import ch.css.learning.jobrunr.domain.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jobrunr.jobs.context.JobContext;

import java.io.IOException;
import java.util.Random;

@ApplicationScoped
public class SendSingleMailJob {
    @Inject
    MailRepository mailRepository;

    private static final Random rnd = new Random();

    @Transactional
    public void send(Long userId, String mailTemplateKey, JobContext jobContext) throws IOException {
        jobContext.runStepOnce("Send mail to user " + userId, () -> send(userId, mailTemplateKey));
    }

    private void send(Long userId, String mailTemplateKey) throws IOException {
        User user = new User();
        user.setId(userId);
        Mail mail = new Mail();
        mail.setUser(user);
        mail.setMailTemplateKey(mailTemplateKey);
        mailRepository.persist(mail);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (rnd.nextDouble() < 0.5) {
            throw new IOException("Simulated email sending failure");
        }
    }
}
