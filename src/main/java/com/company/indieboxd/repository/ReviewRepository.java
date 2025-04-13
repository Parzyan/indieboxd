package com.company.indieboxd.repository;

import com.company.indieboxd.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUser_Id(Long userId);
    List<Review> findByMovie_Id(Long movieId);
}