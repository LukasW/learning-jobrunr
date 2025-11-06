package ch.css.learning.jobrunr.domain;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jobrunr.jobs.context.JobContext;

import java.io.IOException;
import java.util.Random;

@ApplicationScoped
public class MailService {
    @Inject
    MailRepository mailRepository;

    @Inject
    EntityManager entityManager;

    private static final Random rnd = new Random();

    @Transactional

    public void send(Long userId, String mailTemplateKey, JobContext jobContext) throws IOException {
        //System.out.println("[" + jobContext.getJobId() + "] Sending email to " + userId + " (" + mailTemplateKey + ")");
        User user = new User();
        user.setId(userId);
        Mail mail = new Mail();
        mail.setUser(user);
        mail.setMailTemplateKey(mailTemplateKey);
        entityManager.persist(mail);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (rnd.nextDouble() < 0.05) {
            throw new IOException("Simulated email sending failure");
        }

    }
}
