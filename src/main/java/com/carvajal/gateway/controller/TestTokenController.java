package com.carvajal.gateway.controller;

import com.carvajal.gateway.util.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
public class TestTokenController {

    private final JwtUtil jwtUtil;

    public TestTokenController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/generar-token-prueba")
    public String generarToken() {
        return Jwts.builder()
                .setSubject("cliente1")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000))
                .signWith(Keys.hmacShaKeyFor(jwtUtil.getSecret().getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}