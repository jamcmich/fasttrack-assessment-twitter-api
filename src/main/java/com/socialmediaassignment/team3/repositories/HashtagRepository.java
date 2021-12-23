package com.socialmediaassignment.team3.repositories;

import com.socialmediaassignment.team3.entities.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Hashtag findByLabel(String label);
}
