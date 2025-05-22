package com.movie.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class WatchlistResponse {
    private int id;
    private String movieId;
    private String title;
    private String description;
    private BigDecimal rating;
    private String type;
    private String releaseYear;
    private String posterUrl;
    private String backdropUrl;
}
