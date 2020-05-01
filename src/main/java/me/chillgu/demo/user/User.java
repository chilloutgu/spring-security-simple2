package me.chillgu.demo.user;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;

@Entity
@Getter
public class User {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String username;
	private String password;
	private String authorities;
	
	protected User() {}
	
	public User(String username, String password, String authorities) {
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}
}
