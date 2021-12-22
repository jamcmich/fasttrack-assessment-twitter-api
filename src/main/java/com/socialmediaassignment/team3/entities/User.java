package com.socialmediaassignment.team3.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity(name = "user")
@NoArgsConstructor
@Data
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private Timestamp joined;

    private Boolean deleted;

    // fields are nullable by default
    private String firstName;
    private String lastName;

    private String email;

    private String phone;


    // TODO: Consider adding optional/nullable = false to each field
    @OneToMany(mappedBy = "author")
    private List<Tweet> tweets;

    @ManyToMany
    @JoinTable(
            name = "likes_table",
            joinColumns = @JoinColumn(name = "likes_id"),
            inverseJoinColumns = @JoinColumn(name = "liked_tweets_id")
    )
    private List<Tweet> likedTweets;

    @ManyToMany(mappedBy = "following")
    private List<User> followers;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "followers_table",
            joinColumns = @JoinColumn(name = "followers_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private List<User> following;

    @ManyToMany
    @JoinTable(
            name = "users_mentioned_table",
            joinColumns = @JoinColumn(name = "users_mentioned_id"),
            inverseJoinColumns = @JoinColumn(name = "mentions_id")
    )
    private List<Tweet> mentions;
}
