package com.socialmediaassignment.team3;

import com.socialmediaassignment.team3.entities.Tweet;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.entities.embeddable.Credential;
import com.socialmediaassignment.team3.entities.embeddable.Profile;
import com.socialmediaassignment.team3.repositories.HashtagRepository;
import com.socialmediaassignment.team3.repositories.TweetRepository;
import com.socialmediaassignment.team3.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    public void run(String... args) throws Exception {
        Set<User> testUsers = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            Credential credential = new Credential(
                    "test-username-" + i,
                    "test-password-" + i
            );

            Profile profile = new Profile(
                    "test-firstname-" + i,
                    "test-lastname-" + i,
                    "test-email-" + i + "@email.com",
                    "123-456-78" + i
            );

            User user = new User();
            user.setCredential(credential);
            user.setDeleted(new Random().nextBoolean());
            user.setProfile(profile);
            testUsers.add(userRepository.saveAndFlush(user));
        }

        for (User user : testUsers) {
            for (User toFollow : testUsers.stream().filter(u -> u.getId() != user.getId()).collect(Collectors.toList())) {
                user.addFollowing(toFollow);
                toFollow.addFollower(user);
            }
        }

        Set<User> tempUserSet = new HashSet<>();

        for (User user : testUsers) {
            tempUserSet.add(userRepository.saveAndFlush(user));
        }

        testUsers = tempUserSet;


        // Generates random test tweets.
        Set<Tweet> testTweets = new HashSet<>();
        ArrayList<User> temp = new ArrayList<>(testUsers);
        for (int i = 0; i < testUsers.size(); i++) {
            Tweet tweet = new Tweet();
            tweet.setAuthor(temp.get(new Random().nextInt(testUsers.size())));
            tweet.setContent("test-tweet-" + i);
            tweet.setDeleted(new Random().nextBoolean());

            testTweets.add(tweetRepository.saveAndFlush(tweet));
        }


        /*
            Custom test cases go here.
         */
        Set<User> customUsers = new HashSet<>();

        // TEST: Return multiple followers.
        User user1 = new User();
        user1.setCredential(
                new Credential(
                        "multi-follower-user",
                        "password")
        );
        user1.setFollowers(testUsers);
        System.out.println(user1.getFollowers());
        customUsers.add(user1);

        // TEST: Return multiple tweets.
        User user2 = new User();
        user2.setCredential(
                new Credential(
                        "multi-tweet-user",
                        "password")
        );

        Set<Tweet> tweetsList = new HashSet<Tweet>();
        for (int i = 0; i < 3; i++) {
            Tweet tweet = new Tweet();
            tweet.setContent("some-tweet-" + i);
            tweetsList.add(tweet);
        }
        user2.setTweets(tweetsList);

        System.out.println(user2.getFollowers());
        System.out.println(user2.getTweets());

        customUsers.add(user2);

        userRepository.saveAllAndFlush(customUsers);
    }
}
