package ch.css.learning.jobrunr.domain;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@ApplicationScoped
public class UserDataInitializer {
    @Inject
    UserRepository userRepository;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        if (userRepository.count() == 0) {
            IntStream.rangeClosed(1, 200_000).forEach(i -> {
                User user = new User();
                user.setUsername("user" + i);
                user.setEmail("user" + i + "@example.com");
                user.setPassword("password" + i);
                user.setCreatedAt(LocalDateTime.now());
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.persist(user);
            });
        }
    }
}

