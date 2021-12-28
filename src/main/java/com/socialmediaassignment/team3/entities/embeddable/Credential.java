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
    // TODO: 'username' is not a required property. Remove @NotNull.
    @NotNull
    @Column(unique = true)
    private String username;

    // TODO: 'password' is not a required property. Remove @NotNull.
    @NotNull
    private String password;
}
