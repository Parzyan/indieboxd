package com.company.indieboxd.service;

import com.company.indieboxd.model.Movie;
import com.company.indieboxd.model.Review;
import com.company.indieboxd.model.User;
import com.company.indieboxd.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MovieService movieService;
    private final SessionService sessionService;

    public ReviewService(ReviewRepository reviewRepository, MovieService movieService, SessionService sessionService) {
        this.reviewRepository = reviewRepository;
        this.movieService = movieService;
        this.sessionService = sessionService;
    }

    @Transactional
    public Review addReview(User user, Long movieId, Integer rating, String content) {
        if (reviewRepository.existsByUserIdAndMovieId(user.getId(), movieId)) {
            throw new IllegalStateException("You have already reviewed this movie");
        }

        Movie movie = movieService.getMovieById(movieId);

        Review review = new Review(content, rating, user, movie);
        Review savedReview = reviewRepository.save(review);

        movieService.updateMovieRating(movieId, rating);

        return savedReview;
    }

    public boolean hasUserReviewedMovie(Long userId, Long movieId) {
        return reviewRepository.existsByUserIdAndMovieId(userId, movieId);
    }
}