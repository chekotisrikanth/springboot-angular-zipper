package com.filezipper.filezipper.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.filezipper.filezipper.model.User;
import com.filezipper.filezipper.service.JwtTokenService;
import com.filezipper.filezipper.util.JwtTokenUtil;

@RestController
@CrossOrigin
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Map<String,String> login(@RequestBody User user) throws Exception {

		
		Authentication authDetails = authenticate(user.getUsername(),user.getPassword());
		final String token = jwtTokenUtil.generateToken(authDetails);
        Map<String,String> map = new HashMap<>();
        map.put("token", token);
		return map;
	}

	private Authentication authenticate(String username, String password) throws Exception {
		try {
			Authentication authDetails  = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			return authDetails;
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
