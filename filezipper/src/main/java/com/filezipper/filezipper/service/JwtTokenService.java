package com.filezipper.filezipper.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.filezipper.filezipper.repositories.UserRepo;


@Service
public class JwtTokenService implements UserDetailsService {
	
	@Autowired
	UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		com.filezipper.filezipper.model.User appUser = userRepo.findByUsername(username);
		
		if (appUser!=null) {
			Collection<GrantedAuthority> authorities = appUser.getAuthorities().stream().map( auth -> new SimpleGrantedAuthority(auth)).collect(Collectors.toList());
			User user = new User(appUser.getUsername(),appUser.getPassword(),authorities);
			return user;
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
	
	public String getLoggedInUserName() {
		String username = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.isAuthenticated()) {
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        username = userDetails.getUsername();
	    }   
	    return username;
	}
}