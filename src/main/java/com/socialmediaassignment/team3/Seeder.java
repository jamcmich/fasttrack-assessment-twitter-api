package com.socialmediaassignment.team3;

import com.socialmediaassignment.team3.entities.Tweet;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.repositories.TweetRepository;
import com.socialmediaassignment.team3.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User();
        user1.setName("Manolo");

        User user2 = new User();
        user2.setName("Felo");

        Tweet tweet1 = new Tweet();
        tweet1.setAuthor(user1);

        Tweet tweet2 = new Tweet();
        tweet2.setAuthor(user2);

        userRepository.saveAndFlush(user1);
        userRepository.saveAndFlush(user2);

        tweetRepository.saveAllAndFlush(List.of(
                tweet1, tweet2)
        );
    }
}
