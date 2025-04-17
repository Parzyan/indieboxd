package com.company.indieboxd.service;

import com.company.indieboxd.model.Movie;
import com.company.indieboxd.model.Review;
import com.company.indieboxd.model.User;
import com.company.indieboxd.repository.FavoriteRepository;
import com.company.indieboxd.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final FavoriteRepository favoriteRepository;

    public MovieService(MovieRepository movieRepository, FavoriteRepository favoriteRepository) {
        this.movieRepository = movieRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    @Transactional
    public void updateMovieRating(Long movieId, double rating) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));
        double currentRating = movie.getRating();
        double currentRatingCount = movie.getReviewCount();
        movie.setReviewCount(currentRatingCount + 1);
        movie.setRating(currentRating + rating);

        movieRepository.save(movie);
    }

    @Transactional
    public Movie saveMovie(Movie movie) {
        if (movie.getStreamingUrl() != null && !movie.getStreamingUrl().isEmpty()) {
            validateStreamingUrl(movie.getStreamingUrl());
        }
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public Optional<Movie> findById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie;
    }

    public Page<Movie> findMovies(int page, int size, String genre, String sort) {
        Pageable pageable = PageRequest.of(page, size, getSort(sort));

        if (genre != null && !genre.isEmpty()) {
            return movieRepository.findByGenre(genre, pageable);
        }
        return movieRepository.findAll(pageable);
    }

    public List<String> findAllGenres() {
        return movieRepository.findDistinctGenres();
    }

    private Sort getSort(String sortOption) {
        switch (sortOption) {
            case "oldest":
                return Sort.by("releaseYear").ascending();
            case "rating":
                return Sort.by("avgRating").descending();
            case "title":
                return Sort.by("title").ascending();
            default:
                return Sort.by("releaseYear").descending();
        }
    }

    public List<Movie> findMoviesByUser(User user) {
        return movieRepository.findByUser(user);
    }

    public List<Movie> getFavoriteMovies(User user) {
        return favoriteRepository.findFavoriteMoviesByUser(user);
    }

    public boolean isMovieFavorite(User user, Movie movie) {
        return favoriteRepository.existsByUserAndMovie(user, movie);
    }

    private void validateStreamingUrl(String url) {
        try {
            new URL(url);
            if (!url.startsWith("https://")) {
                throw new IllegalArgumentException("Streaming URL must use HTTPS");
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid streaming URL format");
        }
    }
}