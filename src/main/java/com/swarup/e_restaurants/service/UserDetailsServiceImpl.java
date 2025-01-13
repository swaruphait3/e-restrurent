package com.swarup.e_restaurants.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.swarup.e_restaurants.model.MyUserDetails;
import com.swarup.e_restaurants.model.User;
import com.swarup.e_restaurants.repository.UserRepositiry;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepositiry userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrMobile(username, username).get();
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new MyUserDetails(user);
    }
}
