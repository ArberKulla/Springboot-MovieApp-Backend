package com.movie.site.service;

import com.movie.site.dto.WatchlistDTO;
import com.movie.site.dto.WatchlistResponse;
import com.movie.site.entity.Watchlist;
import com.movie.site.entity.User;
import com.movie.site.entity.UserRole;
import com.movie.site.repository.WatchlistRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchlistService {

    int pageSize = 20;
    @Autowired
    WatchlistRepository watchlistRepository;

    public Page<WatchlistResponse> getAllWatchlists(Integer page, Principal connectedUser){
        if (page==null){
            page = 1;
        }
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if(user.getUserRole()!= UserRole.ADMIN){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot fetch this watchlist");
        }

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Watchlist> watchlistPage =  watchlistRepository.findAll(pageable);
        return watchlistPage.map(watchlist -> new WatchlistResponse(
                watchlist.getId(),
                watchlist.getMovieId(),
                watchlist.getTitle(),
                watchlist.getDescription(),
                watchlist.getRating(),
                watchlist.getType(),
                watchlist.getReleaseYear(),
                watchlist.getPosterUrl(),
                watchlist.getBackdropUrl()
        ));
    }

    public boolean existsByMovieIdAndUser(String movieId, Principal connectedUser) {

        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        return watchlistRepository.existsByMovieIdAndUserId(movieId, user.getId());
    }

    public Page<WatchlistResponse> getFromUser(Principal connectedUser, Integer page) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // Default page value if not provided
        if (page == null) {
            page = 0;  // Default to page 1 if not provided
        }

        // Pageable with page size defined elsewhere
        Pageable pageable = PageRequest.of(page, pageSize);  // Adjust to 0-indexed page

        Page<Watchlist> watchlistPage;

        watchlistPage = watchlistRepository.findByUserId(user.getId(), pageable);


        // Mapping Watchlist to WatchlistResponse
        return watchlistPage.map(watchlist -> new WatchlistResponse(
                watchlist.getId(),
                watchlist.getMovieId(),
                watchlist.getTitle(),
                watchlist.getDescription(),
                watchlist.getRating(),
                watchlist.getType(),
                watchlist.getReleaseYear(),
                watchlist.getPosterUrl(),
                watchlist.getBackdropUrl()
        ));
    }


    @Transactional
    public Watchlist create(WatchlistDTO watchlistDTO, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        watchlist.setTitle(watchlistDTO.getTitle());
        watchlist.setDescription(watchlistDTO.getDescription());
        watchlist.setMovieId(watchlistDTO.getMovieId()); // make sure this maps to movie_id
        watchlist.setPosterUrl(watchlistDTO.getPosterUrl());
        watchlist.setType(watchlistDTO.getType());
        watchlist.setReleaseYear(watchlistDTO.getReleaseYear());
        watchlist.setBackdropUrl(watchlistDTO.getBackdropUrl());
        watchlist.setRating(watchlistDTO.getRating());

        return watchlistRepository.save(watchlist);
    }


    @Transactional
    public WatchlistResponse getById(int id, Principal connectedUser) throws IOException, SQLException {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Watchlist current = watchlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Watchlist not found with ID: " + id));

        if(current.getUser().getId()!=user.getId()  && user.getUserRole()!= UserRole.ADMIN){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot fetch this watchlist");
        }


        WatchlistResponse watchlist = new WatchlistResponse(
                current.getId(),
                current.getMovieId(),
                current.getTitle(),
                current.getDescription(),
                current.getRating(),
                current.getType(),
                current.getReleaseYear(),
                current.getPosterUrl(),
                current.getBackdropUrl()
        );

        return watchlist;
    }

    @Transactional
    public Watchlist update(int id, WatchlistDTO watchlistDTO, Principal connectedUser) throws IOException, SQLException {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Watchlist current = watchlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Watchlist not found with ID: " + id));

        if(current.getUser().getId()!=user.getId()  && user.getUserRole()!= UserRole.ADMIN){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot edit this watchlist");
        }

        return watchlistRepository.save(current);
    }

    @Transactional
    public void deleteById(int id, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Watchlist current = watchlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Watchlist not found with ID: " + id));

        
        if(current.getUser().getId()!=user.getId()  && user.getUserRole()!= UserRole.ADMIN){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete this watchlist");
        }

        watchlistRepository.deleteById(id);
    }

}
