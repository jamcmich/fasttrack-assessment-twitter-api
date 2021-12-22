package com.socialmediaassignment.team3.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "tweet_table")
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "author_id",
            referencedColumnName = "id"
    )
    private User author;

    // TODO: Add other fields

    @ManyToMany(mappedBy = "likedTweets")
    private List<User> likes;

    @ManyToMany(mappedBy = "tweets")
    private List<Hashtag> hashtags;

    @ManyToMany(mappedBy = "mentions")
    private List<User> usersMentioned;
}
