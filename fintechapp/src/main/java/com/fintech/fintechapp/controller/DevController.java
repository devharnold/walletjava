package com.fintech.fintechapp.controller;

import com.fintech.fintechapp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dev")
public class DevController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/generate-token")
    public String generateToken() {
        return jwtUtil.generateToken();
    }
}