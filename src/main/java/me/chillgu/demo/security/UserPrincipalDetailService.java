package me.chillgu.demo.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.chillgu.demo.user.User;
import me.chillgu.demo.user.UserRepository;

@Service
@RequiredArgsConstructor
public class UserPrincipalDetailService implements UserDetailsService {

	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> byUsername = userRepository.findByUsername(username);
		User user = byUsername.orElseThrow(() -> new UsernameNotFoundException(username + " is not exist"));
		
		return new UserPrincipal(user);
	}
}
