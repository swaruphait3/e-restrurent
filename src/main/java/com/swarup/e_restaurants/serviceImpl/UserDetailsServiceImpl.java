package com.swarup.e_restaurants.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.swarup.e_restaurants.model.MyUserDetails;
import com.swarup.e_restaurants.model.User;
import com.swarup.e_restaurants.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
	private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byEmailOrPhone = userRepository.findByEmailOrPhone(username, username);
        if (byEmailOrPhone.isEmpty()) {
            throw new UsernameNotFoundException("Could not find user");
        } else {
           return new MyUserDetails(byEmailOrPhone.get()); 
        }
    }
    
}
