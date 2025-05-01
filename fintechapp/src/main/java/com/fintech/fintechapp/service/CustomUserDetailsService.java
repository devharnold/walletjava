package com.fintech.fintechapp.service;

import java.util.*;

import com.fintech.fintechapp.model.User;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.*;
import com.fintech.fintechapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the user from the database
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Afterwards convert the User entity into SpringSecurity UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getUserId(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(("ROLE_" + user.getRole())))
        );
    }

}