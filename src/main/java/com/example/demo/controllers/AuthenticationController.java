package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.models.User;
import com.example.demo.service.MyUserDetailsService;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User authenticationRequest) {
        try {
            logger.info("LoggerService will run in 3s");
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

            ApiResponse<Object> response = new ApiResponse<>(
                false,
                "User không tồn tại",
                "404",
                null
            );

            if (userDetails != null) {
                final String jwt = jwtUtil.generateToken(userDetails);

                response = new ApiResponse<>(
                    true,
                    "Đăng nhập thành công",
                    "200",
                    Collections.singletonMap("token", jwt)
                );
            } 

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (AuthenticationException e) {
            logger.error("====== AuthenticationException ERROR ======");
            logger.error("Chi tiết lỗi: ", e);
            ApiResponse<Object> response = new ApiResponse<>(
                false,
                "Sai thông tin đăng nhập",
                "404",
                null
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error("====== UNEXPECTED ERROR ======");
            logger.error("Chi tiết lỗi: ", e);
            ApiResponse<Object> response = new ApiResponse<>(
                false,
                "Có lỗi xảy ra, vui lòng thử lại",
                "500",
                null
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}
