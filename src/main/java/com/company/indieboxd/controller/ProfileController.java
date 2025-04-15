package com.company.indieboxd.controller;

import com.company.indieboxd.model.Movie;
import com.company.indieboxd.model.Review;
import com.company.indieboxd.model.User;
import com.company.indieboxd.service.MovieService;
import com.company.indieboxd.service.ReviewService;
import com.company.indieboxd.service.SessionService;
import com.company.indieboxd.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final SessionService sessionService;
    private final UserService userService;
    private final ReviewService reviewService;
    private final MovieService movieService;

    public ProfileController(SessionService sessionService, UserService userService, ReviewService reviewService, MovieService movieService) {
        this.sessionService = sessionService;
        this.userService = userService;
        this.reviewService = reviewService;
        this.movieService = movieService;
    }

    @GetMapping
    public String profile(Model model) {
        User user = sessionService.getCurrentUser();
        if (user == null) {
            return "redirect:/auth/login";
        }
        List<Movie> userMovies = movieService.findMoviesByUser(user);
        //List<Movie> userFavorites = favoriteService.findFavoritesByUser(user);

        model.addAttribute("movies", userMovies);
        //model.addAttribute("favorites", userFavorites);
        List<Review> reviews = userService.getReviewsByUserId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("reviews", reviews);
        return "profile";
    }
}
