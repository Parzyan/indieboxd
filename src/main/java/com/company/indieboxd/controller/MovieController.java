package com.company.indieboxd.controller;

import com.company.indieboxd.model.Movie;
import com.company.indieboxd.model.User;
import com.company.indieboxd.service.MovieService;
import com.company.indieboxd.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final SessionService sessionService;

    @Autowired
    public MovieController(MovieService movieService, SessionService sessionService) {
        this.movieService = movieService;
        this.sessionService = sessionService;
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        User user = sessionService.getCurrentUser();
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("user", user);
        return "add-movie";
    }

    @PostMapping("/add")
    public String addMovie(
            @RequestParam String title,
            @RequestParam(required = false) String imageUrl,
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) String genre,
            RedirectAttributes redirectAttributes) {

        User currentUser = sessionService.getCurrentUser();
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setImageUrl(imageUrl);
        movie.setReleaseYear(releaseYear);
        movie.setDuration(duration);
        movie.setGenre(genre);
        movie.setUser(currentUser);

        movieService.saveMovie(movie);

        redirectAttributes.addFlashAttribute("movie", movie);
        return "redirect:/movies/confirmation";
    }

    @GetMapping("/confirmation")
    public String showConfirmation(Model model) {
        if (!model.containsAttribute("movie")) {
            return "redirect:/movies/add";
        }
        User user = sessionService.getCurrentUser();
        model.addAttribute("user", user);
        return "confirmation";
    }
}