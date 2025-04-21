package com.company.indieboxd.controller;

import com.company.indieboxd.model.Movie;
import com.company.indieboxd.model.Review;
import com.company.indieboxd.model.User;
import com.company.indieboxd.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;
    private final ReviewService reviewService;
    private final MovieService movieService;
    private final StorageService storageService;
    private final FavoriteService favoriteService;

    public ProfileController(UserService userService, ReviewService reviewService, MovieService movieService, StorageService storageService, FavoriteService favoriteService) {
        this.userService = userService;
        this.reviewService = reviewService;
        this.movieService = movieService;
        this.storageService = storageService;
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public String profile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/auth/login";
        }
        List<Movie> userMovies = movieService.findMoviesByUser(user);
        List<Movie> userFavorites = favoriteService.getUserFavorites(user);
        List<Review> reviews = userService.getReviewsByUserId(user.getId());
        model.addAttribute("movies", userMovies);
        model.addAttribute("favorites", userFavorites);
        model.addAttribute("user", user);
        model.addAttribute("reviews", reviews);
        model.addAttribute("currentUser", user);
        return "profile";
    }

    @GetMapping("/edit")
    public String showEditProfileForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user", user);
        if(user == null) { return "redirect:/auth/login"; }
        return "edit-profile";
    }

    @PostMapping("/edit")
    public String updateProfile(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) String currentPassword,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) MultipartFile profilePic,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        try {
            User currentUser = (User) session.getAttribute("currentUser");

            if (email != null) currentUser.setEmail(email);
            if (bio != null) currentUser.setBio(bio);

            if(profilePic != null && !profilePic.isEmpty()) {
                String imageUrl = storageService.store(profilePic);
                currentUser.setProfilePictureUrl(imageUrl);
            }

            if (currentPassword != null && !currentPassword.isEmpty() &&
                    newPassword != null && !newPassword.isEmpty()) {

                if (!userService.checkPassword(currentUser, currentPassword)) {
                    throw new IllegalArgumentException("Current password is incorrect");
                }

                userService.changePassword(currentUser, newPassword);
            }

            userService.saveUser(currentUser);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
        }

        return "redirect:/profile";
    }

    @GetMapping("/{username}")
    public String viewPublicProfile(@PathVariable String username, Model model, HttpSession session) {
        User profileUser = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == profileUser) {
            return "redirect:/profile";
        }

        List<Movie> userMovies = movieService.findMoviesByUser(profileUser);
        List<Movie> userFavorites = favoriteService.getUserFavorites(profileUser);
        List<Review> reviews = userService.getReviewsByUserId(profileUser.getId());
        model.addAttribute("movies", userMovies);
        model.addAttribute("favorites", userFavorites);
        model.addAttribute("user", profileUser);
        model.addAttribute("reviews", reviews);
        model.addAttribute("currentUser", currentUser);
        return "profile";
    }
}
