package com.movie.site.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchlistDTO {

    @NotBlank(message = "Movie ID is required")
    private String movieId;

    @NotBlank(message = "Movie Title is required")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.0")
    @DecimalMax(value = "10.0", inclusive = true, message = "Rating must not exceed 10.0")
    private BigDecimal rating;

    private String type;

    private String releaseYear;

    private String posterUrl;

    private String backdropUrl;
}
