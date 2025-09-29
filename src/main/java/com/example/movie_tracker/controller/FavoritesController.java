package com.example.movietracker.controller;

import com.example.movietracker.service.FavoriteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/favorites")
public class FavoritesController {

    private final FavoriteService favoriteService;
    public FavoritesController(FavoriteService favoriteService) { this.favoriteService = favoriteService; }

    @GetMapping
    public String list(Model model, Principal principal) {
        var list = favoriteService.listForUser(principal.getName());
        model.addAttribute("favorites", list);
        return "favorites";
    }

    @PostMapping("/{movieId}/add")
    public String add(@PathVariable Long movieId, Principal principal) {
        favoriteService.addFavorite(principal.getName(), movieId);
        return "redirect:/favorites";
    }

    @PostMapping("/{movieId}/remove")
    public String remove(@PathVariable Long movieId, Principal principal) {
        favoriteService.removeFavorite(principal.getName(), movieId);
        return "redirect:/favorites";
    }
}
