package com.socialmediaassignment.team3.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"tweets"})
public class Hashtag {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true)
    private String label;

    @NotNull
    @ManyToMany(mappedBy = "hashtags")
    private Set<Tweet> tweets = new HashSet<>();

    // TODO: Does this specifically need to be of type 'Timestamp'?
    @NotNull
    @Column(name = "created_on")
    @CreationTimestamp
    private Date firstUsed;

    // TODO: Does this specifically need to be of type 'Timestamp'?
    @NotNull
    private Date lastUsed;

    public void addTweet(Tweet tweet) {
        this.tweets.add(tweet);
        tweet.getHashtags().add(this);
    }

    public void removeTweet(Tweet tweet) {
        this.tweets.remove(tweet);
        tweet.getHashtags().remove(this);
    }
}
