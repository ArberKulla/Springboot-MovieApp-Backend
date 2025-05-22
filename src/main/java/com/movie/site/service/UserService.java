package com.movie.site.service;

import com.movie.site.entity.User;
import com.movie.site.repository.TokenRepository;
import com.movie.site.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    public List<User> getAll() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }


    
    public User getById(int id) {
        return userRepository.findById(id).get();
    }

    public Optional<User> getOptionalById(int id) {
        return userRepository.findById(id);
    }


    public User getByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    @Transactional
    public User update(int id, User updatedUser) {
        User current = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // Update fields — add more as needed
        current.setFirstName(updatedUser.getFirstName());
        current.setLastName(updatedUser.getLastName());
        current.setEmail(updatedUser.getEmail());
        current.setUserRole(updatedUser.getUserRole());

        return userRepository.save(current);
    }


    @Transactional
    public void deleteById(int id) {
        tokenRepository.deleteByUserId(id); // Delete tokens first
        userRepository.deleteById(id);      // Then delete the user
    }




}
