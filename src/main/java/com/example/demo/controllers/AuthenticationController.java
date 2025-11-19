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

import com.example.demo.common.BaseResponse;
import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.CreateUserDto;
import com.example.demo.dto.auth.LoginDTO;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.models.User;
import com.example.demo.service.MyUserDetailsService;

import java.util.Collections;

import javax.validation.Valid;

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
    public BaseResponse<?> createAuthenticationToken(@Valid @RequestBody LoginDTO authenticationRequest) {
        try {
            // String hash = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("123456");
            // logger.info(hash);
            logger.info("LoggerService will run in 3s");

            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

            if (userDetails == null) {
                throw new Exception("Tên người dùng không tồn tại trong hệ thống.");
            }

            final String jwt = jwtUtil.generateToken(userDetails);
            return BaseResponse.success(jwt);
        } catch (AuthenticationException e) {
            logger.error("====== AuthenticationException ERROR ======");
            logger.error("Chi tiết lỗi: ", e);
            return BaseResponse.failed("Sai thông tin đăng nhập");
        } catch (Exception e) {
            logger.error("====== UNEXPECTED ERROR ======");
            logger.error("Chi tiết lỗi: ", e);
            return BaseResponse.failed("Có lỗi xảy ra, vui lòng thử lại");
        }
    }
    
    @PostMapping("/register")
    public ApiResponse<Object> register(@Valid @RequestBody CreateUserDto reCreateUserDto) {
    	try {
    		return new ApiResponse<>(true, "Success", "200", userDetailsService.register(reCreateUserDto));
    	} catch (Exception e) {
            logger.error("====== UNEXPECTED ERROR ======");
            logger.error("Chi tiết lỗi: ", e);
            return new ApiResponse<>(
                false,
                e.getMessage(),
                "500",
                null
            );
        }
	}
}
