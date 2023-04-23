package com.eventmaster.backend.security.config;

import com.eventmaster.backend.security.Token.Token;
import com.eventmaster.backend.security.Token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

    private TokenService tokenService;

    public LogoutService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        jwt = authHeader.substring(7); // Becaus "Bearer " is cut off
        Token storedToken = tokenService.findByToken(jwt);
        if(storedToken != null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenService.saveToken(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}
