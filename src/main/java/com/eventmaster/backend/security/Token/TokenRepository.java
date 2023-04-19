package com.eventmaster.backend.security.Token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    /**
     *  Custom query to retrieve all valid tokens associated with a specific user.
     *  Valid tokens are those that have not expired or have not been revoked.
     *  @param id the user ID to retrieve tokens for
     *  @return a list of valid tokens associated with the specified user
     */
    /*
    @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUserId(Long id);
    */


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
