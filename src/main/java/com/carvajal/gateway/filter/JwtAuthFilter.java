package com.carvajal.gateway.filter;

import com.carvajal.gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

public class JwtAuthFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public HandlerFilterFunction<ServerResponse, ServerResponse> filter() {
        return (request, next) -> {

            String authHeader = request.headers().firstHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return construirRespuestaError(
                        "Falta el header Authorization con formato 'Bearer <token>'");
            }

            String token = authHeader.substring(7);

            try {
                Claims claims = jwtUtil.validarYObtenerClaims(token);

                ServerRequest modificado = ServerRequest.from(request)
                        .header("X-Usuario", claims.getSubject())
                        .build();

                return next.handle(modificado);

            } catch (JwtException e) {
                return construirRespuestaError("Token invalido o expirado");
            }
        };
    }

    private ServerResponse construirRespuestaError(String mensaje) {
        return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"error\": \"" + mensaje + "\"}");
    }
}