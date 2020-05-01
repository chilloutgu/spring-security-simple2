package me.chillgu.demo;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.chillgu.demo.user.User;
import me.chillgu.demo.user.UserRepository;

@RestController
@RequestMapping("api/public")
@RequiredArgsConstructor
public class MyController {
	
	private final UserRepository userRepository;
	
	@GetMapping("/hello")
	public String hello() {
		
		return "hello too";
	}
	
	@GetMapping("/my")
	public String my() {
		
		return "my too";
	}
	
	@GetMapping("/users")
	public List<User> allUsers() {
		
		return userRepository.findAll();
	}
	
}
