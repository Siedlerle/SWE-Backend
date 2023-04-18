package com.eventmaster.backend.security.Token;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository){
        this.tokenRepository = tokenRepository;
    }

    public void saveToken(Token token){
        tokenRepository.save(token);
    }

    public Token findByToken(String token){
        return tokenRepository.findByToken(token);
    }
}
