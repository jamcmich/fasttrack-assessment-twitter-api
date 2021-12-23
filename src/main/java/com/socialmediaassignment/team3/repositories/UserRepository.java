package com.socialmediaassignment.team3.repositories;

import com.socialmediaassignment.team3.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
<<<<<<< HEAD
    List<User> findByCredentialUsername(String username);
=======
    User findByCredentialUsername(String username);
>>>>>>> a458223bea0a77de757914629001761ebecd1df2
}
