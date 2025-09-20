package com.example.demo.dto.auth;

import javax.validation.constraints.NotBlank;

public class LoginDTO {

    @NotBlank(message = "Username or email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    // Getter & Setter
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
}
