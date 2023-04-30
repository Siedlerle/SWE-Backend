package com.eventmaster.backend.security.Token;

import com.eventmaster.backend.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository){
        this.tokenRepository = tokenRepository;
    }

    public void saveToken(Token token){
        tokenRepository.save(token);
    }

    public void deleteTokens(long userId){
        List<Token> deleteTokens = tokenRepository.findTokensByUserId(userId);
        for (Token token: deleteTokens) {
            tokenRepository.delete(token);
        }
    }

    public void deleteToken(Token token){
        tokenRepository.delete(token);
    }

    public List<Token> findAllValidTokensByUser(User user){
        List<Token> tokensFromUser = tokenRepository.findTokensByUserId(user.getId());
        List<Token> validTokens = new ArrayList<Token>();
        for (Token token: tokensFromUser) {
            if(!token.isExpired() || !token.isRevoked()){
                validTokens.add(token);
            }
        }
        return validTokens;
    }


    public List<Token> findAllTokensByUser(long userId){
        return tokenRepository.findTokensByUserId(userId);
    }

    public Token findByToken(String token){
        return tokenRepository.findByToken(token);
    }

}
