package com.eventmaster.backend.security.Token;

import org.springframework.stereotype.Service;

import java.util.List;
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

    public void deleteToken(long userId){
        List<Token> deleteTokens = tokenRepository.findTokensByUserId(userId);
        System.out.println(deleteTokens.get(0).getToken());
        for (Token token: deleteTokens) {
            tokenRepository.delete(token);
        }
    }

    public List<Token> findAllValidTokenByUser(Long userId){
        return findAllValidTokenByUser(userId);
    }

    public Token findByToken(String token){
        return tokenRepository.findByToken(token);
    }

    public void saveAll(List<Token> validUserTokens) {
        tokenRepository.saveAll(validUserTokens);
    }
}
