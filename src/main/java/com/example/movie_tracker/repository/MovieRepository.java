package com.example.movie_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.movie_tracker.model.Movie;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByExternalId(String externalId);
}
