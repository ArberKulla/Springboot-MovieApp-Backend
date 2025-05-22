package com.movie.site.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN")
    ;
    @Getter
    private final String role;

    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.name()));
    }

}
