package ch.css.learning.jobrunr.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MailRepository implements PanacheRepository<Mail> {
}
