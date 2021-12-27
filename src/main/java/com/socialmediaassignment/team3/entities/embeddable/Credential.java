package com.socialmediaassignment.team3.entities.embeddable;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credential {
    // TODO: Is 'username' a required property?
    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;
}
