package com.Backend.Back.controller;

import com.Backend.Back.entity.JwtRequest;
import com.Backend.Back.entity.JwtResponse;
import com.Backend.Back.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")

public class JwtController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createJwtToken(@RequestBody JwtRequest jwtRequest) {
        try {
            JwtResponse jwtResponse = jwtService.createJwtToken(jwtRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
