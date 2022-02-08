package com.hardiksethi.jwt;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hardiksethi.jwt.models.AuthenticationRequest;
import com.hardiksethi.jwt.models.AuthenticationResponse;
import com.hardiksethi.jwt.services.MyUserDetailsService;
import com.hardiksethi.jwt.util.JwtUtil;

@RestController
public class HelloResource {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	 
	private JwtUtil jwtUtil = new JwtUtil();
	
	@GetMapping("hello")
	public String hello() {
		return "hello";
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		final UserDetails userDetails  = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
		
	}

}
