package com.company.indieboxd.repository;

import com.company.indieboxd.model.Movie;
import com.company.indieboxd.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByUser_Id(Long userId);
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByUser(User user);

    Page<Movie> findByGenre(String genre, Pageable pageable);

    @Query("SELECT DISTINCT m.genre FROM Movie m WHERE m.genre IS NOT NULL")
    List<String> findDistinctGenres();
}