package com.example.movie_tracker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movie_tracker.model.*;
import com.example.movie_tracker.repository.*;

import java.util.List;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepo;
    private final UserRepository userRepo;
    private final MovieRepository movieRepo;

    public FavoriteService(FavoriteRepository favoriteRepo, UserRepository userRepo, MovieRepository movieRepo) {
        this.favoriteRepo = favoriteRepo;
        this.userRepo = userRepo;
        this.movieRepo = movieRepo;
    }

    public List<Favorite> listForUser(String username) {
        User u = userRepo.findByUsername(username).orElseThrow();
        return favoriteRepo.findByUser(u);
    }

    public boolean addFavorite(String username, Long movieId) {
        User u = userRepo.findByUsername(username).orElseThrow();
        Movie m = movieRepo.findById(movieId).orElseThrow();
        if (favoriteRepo.findByUserAndMovie(u, m).isPresent()) return false;
        favoriteRepo.save(new Favorite(u, m));
        return true;
    }

    public boolean removeFavorite(String username, Long movieId) {
        User u = userRepo.findByUsername(username).orElseThrow();
        Movie m = movieRepo.findById(movieId).orElseThrow();
        var opt = favoriteRepo.findByUserAndMovie(u, m);
        if (opt.isPresent()) {
            favoriteRepo.delete(opt.get());
            return true;
        }
        return false;
    }

    public long countForUser(String username) {
        User u = userRepo.findByUsername(username).orElseThrow();
        return favoriteRepo.countByUser(u);
    }

    public boolean isFavorite(String username, Long movieId) {
        User u = userRepo.findByUsername(username).orElseThrow();
        Movie m = movieRepo.findById(movieId).orElseThrow();
        return favoriteRepo.findByUserAndMovie(u, m).isPresent();
    }
}
