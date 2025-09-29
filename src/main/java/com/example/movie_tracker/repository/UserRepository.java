package com.example.movie_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.movie_tracker.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
