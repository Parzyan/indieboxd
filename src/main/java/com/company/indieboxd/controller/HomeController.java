package com.company.indieboxd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        //List<Movie> movies = movieRepository.findAll();
        //model.addAttribute("movies", movies);
        return "home";
    }
}