package com.socialmediaassignment.team3;

import com.socialmediaassignment.team3.entities.Hashtag;
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

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    public void run(String... args) throws Exception {
        Credential cred1 = new Credential("felo", "pass123");
        Profile prof1 = new Profile("Felo", "Foo", "felo@gmail.com", "987-654-3210");
        User user1 = new User();
        user1.setCredential(cred1);
        user1.setDeleted(false);
        user1.setProfile(prof1);

        User user1Saved = userRepository.saveAndFlush(user1);

        Credential cred2 = new Credential("angulo", "pass123");
        Profile prof2 = new Profile("Angulo", "Foo", "angulo@gmail.com", "987-654-3210");
        User user2 = new User();
        user2.setCredential(cred2);
        user2.setDeleted(false);
        user2.setProfile(prof2);

        User user2Saved = userRepository.saveAndFlush(user2);

        user1Saved.addFollowing(user2Saved);
        user2Saved.addFollower(user1Saved);
        user2Saved.addFollowing(user1Saved);
        user1Saved.addFollower(user2Saved);

        User s1 = userRepository.saveAndFlush(user1Saved);
        User s2 = userRepository.saveAndFlush(user2Saved);

        Tweet tweet = new Tweet();
        tweet.setAuthor(s1);
        tweet.setContent("Super tweet");
        Tweet savedTweet = tweetRepository.saveAndFlush(tweet);

        Tweet tweet1 = new Tweet();
        tweet1.setAuthor(s1);
        tweet1.setContent("Tweet 2");
        tweet1.setRepostOf(savedTweet);
        tweet1.setInReplyTo(savedTweet);
        Tweet savedTweet1 = tweetRepository.saveAndFlush(tweet1);

        savedTweet1.addLike(s2);
        savedTweet1.addMentionedUser(s1);

        Tweet tw1 = tweetRepository.saveAndFlush(savedTweet1);
        userRepository.saveAndFlush(s1);
        userRepository.saveAndFlush(s2);

        Hashtag hashtag = new Hashtag();
        hashtag.setLabel("some-hashtag-label");
        hashtag.setTweets(Set.of(tweet, tweet1));
        hashtagRepository.saveAndFlush(hashtag);

        Set<User> testUsers = new HashSet<>();
        Set<Tweet> testTweets = new HashSet<>();
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

            Tweet t = new Tweet();
            t.setContent("test to mention @angulo by " + user.getCredential().getUsername());
            t.setAuthor(user);
            t.setPosted(new Date());
            t.setDeleted(new Random().nextBoolean());

            testTweets.add(tweetRepository.saveAndFlush(t));
        }

        for (User user : testUsers) {
            for (User toFollow : testUsers.stream().filter(u -> u.getId() != user.getId()).collect(Collectors.toList())) {
                user.addFollowing(toFollow);
                toFollow.addFollower(user);
            }
        }
        for (User user : testUsers) {
            userRepository.saveAndFlush(user);
        }
    }
}
