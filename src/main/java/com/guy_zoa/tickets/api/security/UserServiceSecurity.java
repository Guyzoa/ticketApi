package com.guy_zoa.tickets.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.guy_zoa.tickets.api.exceptions.UsernameNotFoundException;
import com.guy_zoa.tickets.api.repository.UserRepository;

@Service
public class UserServiceSecurity implements UserDetailsService {
    
	@Autowired
    private  UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
    	UserDetails user =  userRepository.findByUsername(username);
    	if(user != null)return user;
    	else throw new UsernameNotFoundException("l'utilisateur de nom "+username+" n'existe pas!");
    }
    
    
}
