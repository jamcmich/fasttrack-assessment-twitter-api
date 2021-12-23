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

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    public void run(String... args) throws Exception {
        /*
            Generators for less redundant code.
         */

        // Generates random test users.
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
            testUsers.add(user);
        }
        userRepository.saveAllAndFlush(testUsers);

        // Generates random test tweets.
        Set<Tweet> testTweets = new HashSet<>();
        ArrayList<User> temp = new ArrayList<>(testUsers);
        for (int i = 0; i < testUsers.size(); i++) {
            Tweet tweet = new Tweet();
            tweet.setAuthor(temp.get(new Random().nextInt(testUsers.size())));
            tweet.setContent("test-tweet-" + i);
            tweet.setDeleted(new Random().nextBoolean());

            testTweets.add(tweet);
        }
        tweetRepository.saveAllAndFlush(testTweets);

        // Generates random followers.
        List<User> tempFollowers = new ArrayList<>(testUsers);
        for (User user : tempFollowers) {
            int rand = new Random().nextInt(tempFollowers.size());
            if (user != tempFollowers.get(rand) && !tempFollowers.contains(user)) {
                user.addFollower(tempFollowers.get(rand));
            }
        }
        userRepository.saveAllAndFlush(tempFollowers);

        // Generates random followings.
        List<User> tempFollowings = new ArrayList<>(testUsers);
        for (User user : tempFollowings) {
            int rand = new Random().nextInt(tempFollowings.size());
            if (user != tempFollowings.get(rand)) {
                user.addFollower(tempFollowings.get(rand));
            }
        }
        userRepository.saveAllAndFlush(tempFollowings);

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

        userRepository.saveAllAndFlush(customUsers);
    }
}
