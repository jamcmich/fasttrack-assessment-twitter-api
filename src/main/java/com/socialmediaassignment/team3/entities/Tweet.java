package com.socialmediaassignment.team3.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Tweet {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull
    @JsonIgnore
    private User author;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(name = "tweet_hashtag_mapping",
            joinColumns = {@JoinColumn(name = "hashtag_id")},
            inverseJoinColumns = {@JoinColumn(name = "tweet_id")}
    )
    @JsonIgnore
    private Set<Hashtag> hashtags = new HashSet<>();

    @ManyToMany(mappedBy = "mentions")
    @JsonIgnore
    private Set<User> usersMentioned = new HashSet<>();

    @ManyToMany(mappedBy = "likedTweets")
    @JsonIgnore
    private Set<User> likes = new HashSet<>();

    @OneToMany(mappedBy = "repostOf")
    @JsonIgnore
    private Set<Tweet> reposts = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "repost_id")
    @JsonIgnore
    private Tweet repostOf;

    @OneToMany(mappedBy = "inReplyTo")
    @JsonIgnore
    private Set<Tweet> replies = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "reply_to_id")
    @JsonIgnore
    private Tweet inReplyTo;

    private String content;

    private boolean deleted;

    @Column(name = "created_on")
    @CreationTimestamp
    private Date posted;

    public void addMentionedUser(User user) {
        this.usersMentioned.add(user);
        user.getMentions().add(this);
    }

    public void removeMentionedUser(User user) {
        this.usersMentioned.remove(user);
        user.getMentions().remove(this);
    }

    public void addLike(User user) {
        this.likes.add(user);
        user.getLikedTweets().add(this);
    }

    public void removeLike(User user) {
        this.likes.add(user);
        user.getLikedTweets().remove(this);
    }

    public void addHashtag(Hashtag hashtag) {
        this.hashtags.add(hashtag);
        hashtag.getTweets().add(this);
    }

    public void removeHashtag(Hashtag hashtag) {
        this.hashtags.remove(hashtag);
        hashtag.getTweets().remove(this);
    }
}
