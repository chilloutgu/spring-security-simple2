package me.chillgu.demo;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.chillgu.demo.user.User;
import me.chillgu.demo.user.UserRepository;


@Component
@RequiredArgsConstructor
public class DbInit implements ApplicationRunner {

	private final UserRepository UserRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		User donggu = new User("donggu", passwordEncoder.encode("1234"), "ROLE_ADMIN");
		User seulgeun = new User("seulgeun", passwordEncoder.encode("5678"), "ADMIN");
		
		List<User> users = Arrays.asList(donggu, seulgeun);
		
		UserRepository.saveAll(users);
	}
}
