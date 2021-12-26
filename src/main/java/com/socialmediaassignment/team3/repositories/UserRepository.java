package com.socialmediaassignment.team3.repositories;

import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.entities.embeddable.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCredentialUsername(String username);

    Optional<User> findOneByCredential(Credential credential);
//    User findByCredentialUsername(String username);
}
