package com.lcwd.electronic.store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.entites.User;
import com.lcwd.electronic.store.repositories.UserRepositories;

@Service("userDetailsService")
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
    private UserRepositories userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User with given email not found !!"));
        return user;
    }

}
