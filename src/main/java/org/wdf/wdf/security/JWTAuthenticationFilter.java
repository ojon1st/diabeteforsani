package org.wdf.wdf.security;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.wdf.wdf.entity.AuthUser;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private AuthenticationManager authenticationManager;
	
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
		super();
		this.authenticationManager=authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException,ExpiredJwtException {
		
		AuthUser authUser=null;
		try {
			authUser=new ObjectMapper().readValue(request.getInputStream(), AuthUser.class);
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authUser.getUsername(), authUser.getPassword()));
		
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException,ExpiredJwtException {
		User springUser= (User)authResult.getPrincipal();
		List<String> roles=new ArrayList<>();
		 authResult.getAuthorities().forEach(a->{
	            roles.add(a.getAuthority());
	        });
		String jwt=Jwts.builder()
				.setSubject(springUser.getUsername())
				.setExpiration(new Date(System.currentTimeMillis()+SecuriteParams.EXPIRATION))
				.signWith(SignatureAlgorithm.HS256, SecuriteParams.SECRET)
				.claim("roles", springUser.getAuthorities())
				.compact();
		response.addHeader(SecuriteParams.HEADER_STRING, SecuriteParams.HEADER_PREFIX+jwt);
	}

}
