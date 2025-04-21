package com.company.indieboxd.controller;

import com.company.indieboxd.model.Movie;
import com.company.indieboxd.model.User;
import com.company.indieboxd.service.FavoriteService;
import com.company.indieboxd.service.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final MovieService movieService;

    public FavoriteController(FavoriteService favoriteService,
                              MovieService movieService) {
        this.favoriteService = favoriteService;
        this.movieService = movieService;
    }

    @PostMapping("/add/{movieId}")
    public String addFavorite(@PathVariable Long movieId, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        Movie movie = movieService.getMovieById(movieId);

        favoriteService.addFavorite(user, movie);
        return "redirect:/movies/" + movieId;
    }

    @PostMapping("/remove/{movieId}")
    public String removeFavorite(@PathVariable Long movieId, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        Movie movie = movieService.getMovieById(movieId);

        favoriteService.removeFavorite(user, movie);
        return "redirect:/movies/" + movieId;
    }

    @GetMapping
    public String getUserFavorites(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("favorites", favoriteService.getUserFavorites(user));
        return "favorites/list";
    }
}