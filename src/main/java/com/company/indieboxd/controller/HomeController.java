package com.company.indieboxd.controller;

import com.company.indieboxd.model.Movie;
import com.company.indieboxd.model.User;
import com.company.indieboxd.repository.MovieRepository;
import com.company.indieboxd.service.MovieService;
import com.company.indieboxd.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final MovieRepository movieRepository;
    private final SessionService sessionService;

    @Autowired
    public HomeController(MovieRepository movieRepository, SessionService sessionService) {
        this.movieRepository = movieRepository;
        this.sessionService = sessionService;
    }

    @GetMapping("/")
    public String home(Model model) {
        User user = sessionService.getCurrentUser();
        if (user == null) {
            List<Movie> movies = movieRepository.findAll();
            List<Movie> firstTwelveMovies = movies.stream().limit(12).collect(Collectors.toList());
            model.addAttribute("movies", firstTwelveMovies);
            return "home";
        }
        model.addAttribute("user", user);
        List<Movie> movies = movieRepository.findAll();
        List<Movie> firstTwelveMovies = movies.stream().limit(12).collect(Collectors.toList());
        model.addAttribute("movies", firstTwelveMovies);
        return "home-logged";
    }
}