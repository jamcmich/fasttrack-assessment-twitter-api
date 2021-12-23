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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        System.out.println(tw1.getUsersMentioned().size());

        Hashtag hashtag = new Hashtag();
        hashtag.setLabel("some-hashtag-label");
        hashtag.setTweets(Set.of(tweet, tweet1));
        hashtagRepository.saveAndFlush(hashtag);
        System.out.println(hashtag.getLabel());

        System.out.println(user1.getCredential().getUsername());
    }
}
