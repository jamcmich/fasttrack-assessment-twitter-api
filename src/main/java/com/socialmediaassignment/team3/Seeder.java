package com.socialmediaassignment.team3;

import com.socialmediaassignment.team3.entities.Tweet;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.repositories.HashtagRepository;
import com.socialmediaassignment.team3.repositories.TweetRepository;
import com.socialmediaassignment.team3.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User();
        user1.setUsername("Manolo");
        user1.setJoined(new Timestamp(new Date().getTime()));

        User user2 = new User();
        user2.setUsername("Felo");
        user2.setJoined(new Timestamp(new Date().getTime()));

        User user3 = new User();
        user3.setUsername("John");
        user3.setJoined(new Timestamp(new Date().getTime()));

        user1.setFollowing(List.of(user3));
        user2.setFollowing(List.of(user3));
        user3.setFollowers(List.of(user1, user2));

//        Tweet tweet1 = new Tweet();
//        tweet1.setAuthor();
//
//        Tweet tweet2 = new Tweet();
//        tweet2.setAuthor();

//        Hashtag hashtag1 = new Hashtag();
//        hashtag1.setLabel("some-label");
//
//        Hashtag hashtag2 = new Hashtag();
//        hashtag2.setLabel("some-label-2");

        userRepository.saveAllAndFlush(List.of(user1, user2, user3));

//        tweetRepository.saveAllAndFlush(List.of(
//                        tweet1, tweet2
//                )
//        );

//        hashtagRepository.saveAllAndFlush(List.of(
//                        hashtag1, hashtag2
//                )
//        );
    }
}
