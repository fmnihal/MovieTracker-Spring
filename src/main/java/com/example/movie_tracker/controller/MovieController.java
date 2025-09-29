package com.example.movietracker.controller;

import com.example.movietracker.service.MovieService;
import com.example.movietracker.service.FavoriteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final FavoriteService favoriteService;
    public MovieController(MovieService movieService, FavoriteService favoriteService) {
        this.movieService = movieService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model, Principal principal) {
        var opt = movieService.findById(id);
        if (opt.isEmpty()) return "redirect:/";
        var movie = opt.get();
        model.addAttribute("movie", movie);
        boolean fav = false;
        if (principal != null) {
            fav = favoriteService.isFavorite(principal.getName(), id);
        }
        model.addAttribute("isFavorite", fav);
        return "details";
    }
}
