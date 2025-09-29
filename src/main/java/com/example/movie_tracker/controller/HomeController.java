package com.example.movie_tracker.controller;

import com.example.movie_tracker.service.MovieService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final MovieService movieService;
    public HomeController(MovieService movieService) { this.movieService = movieService; }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        var page = movieService.findAll(PageRequest.of(0, 50)); // first 50 movies
        model.addAttribute("movies", page.getContent());
        return "home";
    }
}
