package com.movie.site.controller;

import com.movie.site.dto.WatchlistDTO;
import com.movie.site.dto.WatchlistResponse;
import com.movie.site.entity.Watchlist;
import com.movie.site.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/watchlists")
public class WatchlistRESTController {

    @Autowired
    WatchlistService watchlistService;

    @GetMapping("/get/all")
    public ResponseEntity<Page<WatchlistResponse>> getAll(
            Principal connectedUser,
            @RequestParam(required = false) Integer page) {
        Page<WatchlistResponse> watchlistResponsePage = watchlistService.getAllWatchlists(page,connectedUser);

        return new ResponseEntity<>(watchlistResponsePage, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<Page<WatchlistResponse>> getWatchlists(
            Principal connectedUser,
            @RequestParam(required = false) Integer page
    ){
        // Call the service method with all the parameters
        return new ResponseEntity<>(watchlistService.getFromUser(connectedUser,page), HttpStatus.OK);
    }

    @GetMapping("/get/{watchlist_id}")
    public ResponseEntity<WatchlistResponse> getById(@PathVariable int watchlist_id,Principal connectedUser) throws SQLException, IOException {
        WatchlistResponse createdWatchlist = watchlistService.getById(watchlist_id,connectedUser);
        return new ResponseEntity<>(createdWatchlist, HttpStatus.OK);
    }

    @GetMapping("/get/user")
    public ResponseEntity<Page<WatchlistResponse>> getUserWatchlists(
            Principal connectedUser,
            @RequestParam(required = false) Integer page) {

        // Call the service layer with title and page
        Page<WatchlistResponse> watchlistResponsePage = watchlistService.getFromUser(connectedUser, page);

        return new ResponseEntity<>(watchlistResponsePage, HttpStatus.OK);
    }

    @PostMapping("/watchlist")
    public ResponseEntity<?> create(@RequestBody @Valid WatchlistDTO watchlistDTO, Principal connectedUser) {
        if (watchlistService.existsByMovieIdAndUser(watchlistDTO.getMovieId(), connectedUser)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("This movie is already in your watchlist.");
        }

        Watchlist createdWatchlist = watchlistService.create(watchlistDTO, connectedUser);
        return new ResponseEntity<>(createdWatchlist, HttpStatus.CREATED);
    }


    @PutMapping("/put/{watchlist_id}")
    public ResponseEntity<Watchlist> edit(@RequestBody @Valid WatchlistDTO watchlist,
                                          @PathVariable int watchlist_id,
                                          Principal connectedUser) throws SQLException, IOException {
        Watchlist createdWatchlist = watchlistService.update(watchlist_id, watchlist, connectedUser);
        return new ResponseEntity<>(createdWatchlist, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{watchlist_id}")
    public ResponseEntity<Void> delete(@PathVariable int watchlist_id,Principal connectedUser) {
        watchlistService.deleteById(watchlist_id,connectedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}