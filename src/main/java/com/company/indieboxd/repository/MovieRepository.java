package com.company.indieboxd.repository;

import com.company.indieboxd.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByUser_Id(Long userId);
    List<Movie> findByTitleContainingIgnoreCase(String title);
}