package com.movie.site.service;

import com.movie.site.entity.User;
import com.movie.site.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }


    
    public User getById(int id) {
        return userRepository.findById(id).get();
    }

    
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    @Transactional
    
    public User update(int id, User User) {
        User current = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));


        return userRepository.save(current);
    }

    @Transactional
    
    public void deleteById(int id){
        userRepository.deleteById(id);
    }




}
