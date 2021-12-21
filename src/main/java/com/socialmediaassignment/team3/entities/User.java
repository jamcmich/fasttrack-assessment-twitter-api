package com.socialmediaassignment.team3.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Data
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
