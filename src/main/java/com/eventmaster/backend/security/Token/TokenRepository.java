package com.eventmaster.backend.security.Token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {

    /**
     * Retrieves an optional token by its value.
     * @param token the token value to retrieve
     * @return an Token
     */
    Token findByToken(String token);

    /**
     * Searches for tokens for corresponding user
     * @param userId Id of the user
     * @return List of tokens for user
     */
    List<Token> findTokensByUserId (Long userId);
}
