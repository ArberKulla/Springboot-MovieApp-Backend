package com.movie.site.repository;

import com.movie.site.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Integer> {
    Page<Watchlist> findAll(Pageable pageable);

    Page<Watchlist> findByUserId(int userId, Pageable pageable);

    boolean existsByMovieIdAndUserId(String movieId, int userId);

}
