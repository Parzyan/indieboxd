package com.company.indieboxd.controller;

import com.company.indieboxd.model.Movie;
import com.company.indieboxd.model.Review;
import com.company.indieboxd.model.User;
import com.company.indieboxd.service.FavoriteService;
import com.company.indieboxd.service.MovieService;
import com.company.indieboxd.service.ReviewService;
import com.company.indieboxd.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final SessionService sessionService;
    private final ReviewService reviewService;
    private final FavoriteService favoriteService;

    @Autowired
    public MovieController(MovieService movieService, SessionService sessionService, ReviewService reviewService, FavoriteService favoriteService) {
        this.movieService = movieService;
        this.sessionService = sessionService;
        this.reviewService = reviewService;
        this.favoriteService = favoriteService;
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
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes) {

        User currentUser = sessionService.getCurrentUser();
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setImageUrl(imageUrl);
        movie.setReleaseYear(releaseYear);
        movie.setDuration(duration);
        movie.setGenre(genre);
        movie.setUser(currentUser);
        movie.setDescription(description);

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

    @GetMapping
    public String listMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "newest") String sort,
            Model model) {

        Page<Movie> moviePage = movieService.findMovies(page, size, genre, sort);
        List<String> allGenres = movieService.findAllGenres();

        User user = sessionService.getCurrentUser();
        model.addAttribute("movies", moviePage);
        model.addAttribute("allGenres", allGenres);
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("sort", sort);
        model.addAttribute("user", user);
        System.out.println(moviePage.isEmpty());
        if(user == null) return "movies";
        return "movies-logged";
    }

    @GetMapping("/{id}")
    public String viewMovie(@PathVariable Long id, Model model) {
        Movie movie = movieService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        User currentUser = sessionService.getCurrentUser();
        boolean hasReviewed = currentUser != null &&
                reviewService.hasUserReviewedMovie(currentUser.getId(), id);

        model.addAttribute("favoriteService", favoriteService);
        model.addAttribute("movie", movie);
        model.addAttribute("hasReviewed", hasReviewed);
        model.addAttribute("user", currentUser);
        if(currentUser == null) return "moviepage";
        return "moviepage-logged";
    }
}