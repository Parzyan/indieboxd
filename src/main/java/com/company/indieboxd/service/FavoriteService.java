package com.company.indieboxd.service;

import com.company.indieboxd.model.Favorite;
import com.company.indieboxd.model.Movie;
import com.company.indieboxd.model.User;
import com.company.indieboxd.repository.FavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public Favorite addFavorite(User user, Movie movie) {
        if (favoriteRepository.existsByUserAndMovie(user, movie)) {
            throw new IllegalStateException("Movie is already in favorites");
        }

        Favorite favorite = new Favorite(user, movie);
        return favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(User user, Movie movie) {
        favoriteRepository.deleteByUserAndMovie(user, movie);
    }

    @Transactional(readOnly = true)
    public List<Movie> getUserFavorites(User user) {
        return favoriteRepository.findFavoriteMoviesByUser(user);
    }

    @Transactional(readOnly = true)
    public boolean isMovieFavorite(User user, Movie movie) {
        return favoriteRepository.existsByUserAndMovie(user, movie);
    }
}