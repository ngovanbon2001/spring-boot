package com.example.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CreateUserDto {
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private String password;
    
    @NotBlank
    private String username;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public CreateUserDto(@NotBlank String email, @NotBlank String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public CreateUserDto(@NotBlank @Email String email, @NotBlank String password, @NotBlank String username) {
		super();
		this.email = email;
		this.password = password;
		this.username = username;
	}

	public CreateUserDto() {
		super();
	}
	
	
}
