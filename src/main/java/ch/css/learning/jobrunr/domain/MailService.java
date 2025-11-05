package ch.css.learning.jobrunr.domain;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MailService {
    @Inject
    MailRepository mailRepository;

    @Inject
    EntityManager entityManager;

    @Transactional
    public void send(Long userId, String mailTemplateKey) {
        System.out.println("Sending email to " + mailTemplateKey);
        User user = new User();
        user.setId(userId);
        Mail mail = new Mail();
        mail.setUser(user);
        mail.setMailTemplateKey(mailTemplateKey);
        entityManager.persist(mail);
    }
}
