package com.example.movietracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.movietracker.model.Movie;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByExternalId(String externalId);
}
