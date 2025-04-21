package com.company.indieboxd.controller;

import com.company.indieboxd.model.Movie;
import com.company.indieboxd.model.Review;
import com.company.indieboxd.model.User;
import com.company.indieboxd.service.MovieService;
import com.company.indieboxd.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {
    private final ReviewService reviewService;
    private final MovieService movieService;

    public ReviewController(ReviewService reviewService, MovieService movieService) {
        this.reviewService = reviewService;
        this.movieService = movieService;
    }

    @PostMapping("/movies/{movieId}/reviews")
    public String addReview(
            @PathVariable Long movieId,
            @RequestParam Integer rating,
            @RequestParam String content,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        try {
            reviewService.addReview(currentUser, movieId, rating, content);
            redirectAttributes.addFlashAttribute("success", "Review added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/movies/" + movieId;
    }

    @PostMapping("/reviews/delete/{reviewId}")
    public String removeReview(@PathVariable Long reviewId, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        Review review = reviewService.getReview(reviewId);
        Movie movie = review.getMovie();
        movie.setRating(movie.getRating() - review.getRating());
        movie.setReviewCount(movie.getReviewCount() - 1);
        reviewService.deleteReview(reviewId);
        return "redirect:/profile";
    }
}