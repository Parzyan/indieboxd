package com.company.indieboxd.repository;

import com.company.indieboxd.model.Favorite;
import com.company.indieboxd.model.Movie;
import com.company.indieboxd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserAndMovie(User user, Movie movie);

    @Query("SELECT f.movie FROM Favorite f WHERE f.user = :user")
    List<Movie> findFavoriteMoviesByUser(@Param("user") User user);

    boolean existsByUserAndMovie(User user, Movie movie);

    void deleteByUserAndMovie(User user, Movie movie);
}