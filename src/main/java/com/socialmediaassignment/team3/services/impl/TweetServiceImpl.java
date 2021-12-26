package com.socialmediaassignment.team3.services.impl;


import com.socialmediaassignment.team3.dtos.ContextResponseDto;
import com.socialmediaassignment.team3.dtos.TweetRequestDto;
import com.socialmediaassignment.team3.dtos.TweetResponseDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.Hashtag;
import com.socialmediaassignment.team3.entities.Tweet;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.entities.embeddable.Credential;
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

import java.text.SimpleDateFormat;
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        return userOptional.get();
    }

    private Tweet _getActiveTweetById(Long id) {
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        if (tweetOptional.isEmpty() || tweetOptional.get().isDeleted())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tweet not found");
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

    private User _getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByCredentialUsername(username);
        if (userOptional.isEmpty())
            return null;
        return userOptional.get();
    }
}
