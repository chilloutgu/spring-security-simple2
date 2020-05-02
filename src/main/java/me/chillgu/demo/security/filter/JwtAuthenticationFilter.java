package me.chillgu.demo.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.chillgu.demo.form.LoginForm;
import me.chillgu.demo.properties.JwtProperties;
import me.chillgu.demo.security.UserPrincipal;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		LoginForm credentials = null;
		
		try {
			credentials = new ObjectMapper().readValue(request.getInputStream(), LoginForm.class);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println(credentials.getUsername() + ", " + credentials.getPassword());
		UsernamePasswordAuthenticationToken authToken = 
							new UsernamePasswordAuthenticationToken(credentials.getUsername(), 
																	credentials.getPassword(), 
																	new ArrayList<GrantedAuthority>());
		
		Authentication auth = authenticationManager.authenticate(authToken);
		
		return auth;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		UserPrincipal userPrincipal = (UserPrincipal) authResult.getPrincipal();
		
		String token = JWT.create().withSubject(userPrincipal.getUsername())
									.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
									.sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
		
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
	}
}
