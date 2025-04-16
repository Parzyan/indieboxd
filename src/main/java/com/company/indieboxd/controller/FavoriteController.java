package com.company.indieboxd.controller;

import com.company.indieboxd.model.Movie;
import com.company.indieboxd.model.User;
import com.company.indieboxd.service.FavoriteService;
import com.company.indieboxd.service.MovieService;
import com.company.indieboxd.service.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final MovieService movieService;
    private final SessionService sessionService;

    public FavoriteController(FavoriteService favoriteService,
                              MovieService movieService,
                              SessionService sessionService) {
        this.favoriteService = favoriteService;
        this.movieService = movieService;
        this.sessionService = sessionService;
    }

    @PostMapping("/add/{movieId}")
    public String addFavorite(@PathVariable Long movieId) {
        User user = sessionService.getCurrentUser();
        Movie movie = movieService.getMovieById(movieId);

        favoriteService.addFavorite(user, movie);
        return "redirect:/movies/" + movieId;
    }

    @PostMapping("/remove/{movieId}")
    public String removeFavorite(@PathVariable Long movieId) {
        User user = sessionService.getCurrentUser();
        Movie movie = movieService.getMovieById(movieId);

        favoriteService.removeFavorite(user, movie);
        return "redirect:/movies/" + movieId;
    }

    @GetMapping
    public String getUserFavorites(Model model) {
        User user = sessionService.getCurrentUser();
        model.addAttribute("favorites", favoriteService.getUserFavorites(user));
        return "favorites/list";
    }
}