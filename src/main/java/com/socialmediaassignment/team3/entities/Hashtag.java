package com.socialmediaassignment.team3.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Hashtag {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String label;

    @ManyToMany(mappedBy = "hashtags")
    private Set<Tweet> tweets = new HashSet<>();

    @Column(name = "created_on")
    @CreationTimestamp
    private Date firstUsed;

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
