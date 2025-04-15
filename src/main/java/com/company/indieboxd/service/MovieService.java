package com.company.indieboxd.service;

import com.company.indieboxd.model.Movie;
import com.company.indieboxd.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
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
}