package com.socialmediaassignment.team3.entities.embeddable;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    private String firstName;

    private String lastName;

    private String email;

    private String phone;
}
