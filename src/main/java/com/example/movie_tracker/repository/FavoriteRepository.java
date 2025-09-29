package com.example.movie_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.movie_tracker.model.Favorite;
import com.example.movie_tracker.model.User;
import com.example.movie_tracker.model.Movie;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    Optional<Favorite> findByUserAndMovie(User user, Movie movie);
    long countByUser(User user);
}
