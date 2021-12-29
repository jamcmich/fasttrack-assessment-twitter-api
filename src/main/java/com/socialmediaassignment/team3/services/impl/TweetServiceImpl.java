package com.socialmediaassignment.team3.services.impl;


import com.socialmediaassignment.team3.dtos.*;
import com.socialmediaassignment.team3.entities.Hashtag;
import com.socialmediaassignment.team3.entities.Tweet;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.entities.embeddable.Credential;
import com.socialmediaassignment.team3.exceptions.BadRequestException;
import com.socialmediaassignment.team3.exceptions.UnauthorizedException;
import com.socialmediaassignment.team3.mappers.TweetMapper;
import com.socialmediaassignment.team3.mappers.UserMapper;
import com.socialmediaassignment.team3.repositories.HashtagRepository;
import com.socialmediaassignment.team3.repositories.TweetRepository;
import com.socialmediaassignment.team3.repositories.UserRepository;
import com.socialmediaassignment.team3.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetMapper tweetMapper;
    private final TweetRepository tweetRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;

    /*
        GET users/@{username}/tweets
        Retrieves all (non-deleted) tweets authored by the user with the given username.
    */
    @Override
    public List<TweetResponseDto> getUserTweets(String username) {
        User user = _getUserByUsername(username);

        if (!isActive(user))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");

        Set<Tweet> tweets = user.getTweets();

        List<Tweet> result = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if (!tweet.isDeleted())
                result.add(tweet);
        }
        result.sort(Comparator.comparing(Tweet::getPosted));
        Collections.reverse(result); // sort list recent -> older

        // test
        for (Tweet tweet : result) {
            System.out.println(tweet.getPosted());
        }

        return tweetMapper.entitiesToDtos(result);
    }

    /*
        GET users/@{username}/mentions
        Retrieves all (non-deleted) tweets in which the user with the given username is mentioned.
    */
    @Override
    public List<TweetResponseDto> getTweetsByMention(String username) {
        User user = _getUserByUsername(username);

        if (!isActive(user))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");

        List<Tweet> tweets = tweetRepository.findAll();
        System.out.println(tweets);

        List<Tweet> result = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if (!tweet.isDeleted() && tweet.getContent().contains("@" + username))
                result.add(tweet);
        }
        result.sort(Comparator.comparing(Tweet::getPosted));
        Collections.reverse(result); // sort list recent -> older

        // test
        for (Tweet tweet : result) {
            System.out.println(tweet.getPosted() + ", " + tweet.getContent());
        }

        return tweetMapper.entitiesToDtos(result);
    }

    /*
        GET users/@{username}/feed
        Retrieves all (non-deleted) tweets authored by the user with the given username,
        as well as all (non-deleted) tweets authored by users the given user is following.
    */
    @Override
    public List<TweetResponseDto> getUserFeed(String username) {
        User user = _getUserByUsername(username);

        if (!isActive(user))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");

        List<Tweet> result = new ArrayList<>();
        Set<User> following = user.getFollowing();

        List<Tweet> tweets = tweetRepository.findAll();
        for (Tweet tweet : tweets) {
            // if the tweet's author's username == requested username
            // OR if tweet's author is in the list of people that the requested user is following
            if (!tweet.isDeleted()) {
                if (Objects.equals(tweet.getAuthor().getCredential().getUsername(), username) || following.contains(tweet.getAuthor())) {
                    result.add(tweet);
                }
            }
        }
        result.sort(Comparator.comparing(Tweet::getPosted));
        Collections.reverse(result); // sort list recent -> older

        for (Tweet r : result) {
            System.out.println("AUTHOR: " + r.getAuthor());
            System.out.println("FOLLOWING: " + r.getAuthor().getFollowing());
            System.out.println("CONTENT: " + r.getContent());
            System.out.println("DATE POSTED: " + r.getPosted());
        }

        return tweetMapper.entitiesToDtos(result);
    }

    @Override
    public List<TweetResponseDto> getActiveTweets() {
        return tweetMapper.entitiesToDtos(tweetRepository.findAll().stream().filter(tweet -> !tweet.isDeleted()).collect(Collectors.toList()));
    }

    @Override
    public TweetResponseDto getTweetById(Long id) {
        return tweetMapper.entityToDto(_getActiveTweetById(id));
    }

    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
        User author = _authorizeCredential(tweetRequestDto.getCredentials());
        Tweet tweet = new Tweet();
        tweet.setAuthor(author);
        tweet.setContent(tweetRequestDto.getContent());

        // TODO: Create logic for updating hashtag's lastUsed property.
//        Set<Hashtag> hashtags = tweet.getHashtags();
//        if (tweet.getHashtags() != null) {
//            for (Hashtag hashtag : hashtags) {
//                hashtag.setLastUsed(tweet.getPosted());
//            }
//        }

        _processTweetContent(tweet);
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweet));
    }

    @Override
    public void likeTweetById(Long id, Credential credential) {
        User user = _authorizeCredential(credential);
        Tweet tweet = _getActiveTweetById(id);
        user.addLikedTweet(tweet);
        userRepository.saveAndFlush(user);
    }

    @Override
    public ContextResponseDto getContextForTweet(Long id) {
        Tweet tweet = _getActiveTweetById(id);
        ContextResponseDto responseDto = new ContextResponseDto();
        responseDto.setTarget(tweet);
        responseDto.setBefore(_getTweetsBefore(tweet));
        responseDto.setAfter(_getTweetsAfter(tweet));
        return responseDto;
    }

    @Override
    public TweetResponseDto deleteTweetById(Long id, Credential credential) {
        Tweet tweet = _getActiveTweetById(id);
        User user = _authorizeCredential(credential);
        if (user != tweet.getAuthor())
            throw new UnauthorizedException("Bad credentials");
        tweet.setDeleted(true);

        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweet));
    }

    @Override
    public TweetResponseDto repostTweetById(Long id, Credential credential) {
        User user = _authorizeCredential(credential);
        Tweet originalTweet = _getActiveTweetById(id);
        Tweet repostTweet = new Tweet();
        repostTweet.setAuthor(user);
        repostTweet.setRepostOf(originalTweet);
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(repostTweet));
    }

    @Override
    public List<TweetResponseDto> getRepostOfTweetById(Long id) {
        Tweet tweet = _getActiveTweetById(id);
        return tweetMapper.entitiesToDtos(tweet.getReposts().stream().collect(Collectors.toList()));
    }

    @Override
    public TweetResponseDto replyTweetById(Long id, TweetRequestDto tweetRequestDto) {
        Tweet tweetToReply = _getActiveTweetById(id);
        User author = _authorizeCredential(tweetRequestDto.getCredentials());

        Tweet tweet = new Tweet();
        tweet.setInReplyTo(tweetToReply);
        tweet.setContent(tweetRequestDto.getContent());
        tweet.setAuthor(author);
        _processTweetContent(tweet);
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweet));
    }

    @Override
    public List<TweetResponseDto> getRepliesToTweetById(Long id) {
        Tweet tweet = _getActiveTweetById(id);
        return tweetMapper.entitiesToDtos(tweet.getReplies().stream().filter(t -> !t.isDeleted()).collect(Collectors.toList()));
    }

    @Override
    public List<UserResponseDto> getMentionInTweetById(Long id) {
        Tweet tweet = _getActiveTweetById(id);
        return userMapper.entitiesToDtos(tweet.getUsersMentioned().stream().filter(u -> !u.isDeleted()).collect(Collectors.toList()));
    }


    private User _authorizeCredential(Credential credential) {
        Optional<User> userOptional = userRepository.findOneByCredential(credential);
        if (userOptional.isEmpty() || userOptional.get().isDeleted())
            throw new UnauthorizedException("Bad credentials");
        return userOptional.get();
    }

    private Tweet _getActiveTweetById(Long id) {
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        if (tweetOptional.isEmpty() || tweetOptional.get().isDeleted())
            throw new BadRequestException("Tweet not found");
        return tweetOptional.get();
    }

    private List<Tweet> _getTweetsBefore(Tweet tweet) {
        List<Tweet> tweetList = new ArrayList<>();
        Tweet actualTweet = tweet;
        while (actualTweet.getInReplyTo() != null) {
            actualTweet = actualTweet.getInReplyTo();
            if (!actualTweet.isDeleted())
                tweetList.add(actualTweet);
        }
        return tweetList;
    }

    private List<Tweet> _getTweetsAfter(Tweet tweet) {
        List<Tweet> tweetList = new ArrayList<>();
        Queue<Tweet> tweetQueue = new LinkedList<>();
        tweetQueue.addAll(tweet.getReplies());

        while (tweetQueue.size() > 0) {
            Tweet actualTweet = tweetQueue.poll();
            if (!actualTweet.isDeleted())
                tweetList.add(actualTweet);
            tweetQueue.addAll(actualTweet.getReplies());
        }
        return tweetList;
    }

    private void _processTweetContent (Tweet tweet) {
        String content = tweet.getContent();

        List<String> mentions = _getMatches(content, "@");
        List<String> tagLabels = _getMatches(content, "#");

        for (String tagLabel : tagLabels) {
            Optional<Hashtag> hashtagOptional = hashtagRepository.findByLabel(tagLabel);
            Hashtag hashtag;
            if (hashtagOptional.isEmpty()) {
                hashtag = new Hashtag();
                hashtag.setLabel(tagLabel);
            } else {
                hashtag = hashtagOptional.get();
            }
            hashtag.setLastUsed(new Date(System.currentTimeMillis()));
            hashtag = hashtagRepository.saveAndFlush(hashtag);
            tweet.addHashtag(hashtag);
        }

        for (String username : mentions) {
            User user = _getUserByUsername(username);
            if (user == null)
                continue;
            tweet.addMentionedUser(user);
        }
    }

    private List<String> _getMatches(String text, String matchBegin) {
        Set<Character> specialChars = Set.of('@', ' ', '#');

        int pointer = 0;
        List<String> matchList = new ArrayList<>();

        while (text.indexOf(matchBegin, pointer) >= 0) {
            pointer = text.indexOf(matchBegin, pointer);
            String username = "";
            int auxPointer = pointer + 1;
            while (auxPointer < text.length() && !specialChars.contains(text.charAt(auxPointer)))
                username = username + text.charAt(auxPointer++);
            matchList.add(username);
            pointer = auxPointer;
        }

        return  matchList;
    }

    // Helper methods
    private User _getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByCredentialUsername(username);
        if (userOptional.isEmpty())
            return null;
        return userOptional.get();
    }

    private User _setCredentialAndProfile (User user, UserRequestDto userRequestDto) {
        user.setCredential(userRequestDto.getCredential());
        user.setProfile(userRequestDto.getProfile());
        return user;
    }

    private boolean isActive(User user) {
        return user != null && !user.isDeleted();
    }
}
