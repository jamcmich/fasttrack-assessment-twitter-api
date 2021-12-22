package com.socialmediaassignment.team3.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String label;

    /*
    TODO: Add fields for firstUsed and lastUsed
     */

    @ManyToMany
    private List<Tweet> tweets;
}
