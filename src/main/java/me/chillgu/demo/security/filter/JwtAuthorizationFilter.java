package me.chillgu.demo.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;
import me.chillgu.demo.properties.JwtProperties;
import me.chillgu.demo.user.User;
import me.chillgu.demo.user.UserRepository;


public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	
	private UserRepository userRepository;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = request.getHeader(JwtProperties.HEADER_STRING);
		
		if(token == null || ! token.startsWith(JwtProperties.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		
		Authentication auth = getUsernamePasswordAuthentication(request);
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		chain.doFilter(request, response);
	}
	
	private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
		String token = request.getHeader(JwtProperties.HEADER_STRING);
		
		if(token != null || token.startsWith(JwtProperties.TOKEN_PREFIX)) {
			DecodedJWT jwt = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
								.build()
								.verify(token.replace(JwtProperties.TOKEN_PREFIX, ""));
			
			String username = jwt.getSubject();

			System.out.println("jwt's subject: " + username);
			
			Optional<User> byUsername = userRepository.findByUsername(username);
			User user = byUsername.get();
			
			if(!StringUtils.isEmpty(username)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, "", Arrays.asList(new SimpleGrantedAuthority(user.getAuthorities())));
				
				return authToken;
			}
			
			return null;
		}
		
		return null;
	}
}
