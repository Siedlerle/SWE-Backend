package com.eventmaster.backend.security.Token;

import com.eventmaster.backend.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        for (Token token: deleteTokens) {
            tokenRepository.delete(token);
        }
    }

    public List<Token> findAllValidTokensByUser(User user){
        System.out.println(user.getId());
        List<Token> tokensFromUser = tokenRepository.findTokensByUserId(user.getId());
        List<Token> validTokens = new ArrayList<Token>();
        for (Token token: tokensFromUser) {
            if(!token.isExpired() || !token.isRevoked()){
                validTokens.add(token);
            }
        }
        return validTokens;
    }

    public Token findByToken(String token){
        return tokenRepository.findByToken(token);
    }

}
