package com.example.movie_tracker.controller;

import com.example.movie_tracker.service.FavoriteService;
import com.example.movie_tracker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
public class ProfileController {
    private final FavoriteService favService;
    private final UserService userService;

    public ProfileController(FavoriteService favService, UserService userService) {
        this.favService = favService;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("username", username);
        model.addAttribute("favoritesCount", favService.countForUser(username));
        return "profile";
    }
}
