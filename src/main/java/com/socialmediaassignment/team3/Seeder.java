package com.socialmediaassignment.team3;

import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User();
        user1.setName("Manolo");

        User user2 = new User();
        user2.setName("Felo");

        userRepository.saveAndFlush(user1);
        userRepository.saveAndFlush(user2);
    }
}
