package com.eventmaster.backend.serviceswithouttoken;
import com.eventmaster.backend.entities.Role;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.repositories.UserRepository;
import com.eventmaster.backend.security.Token.Token;
import com.eventmaster.backend.security.Token.TokenService;
import com.eventmaster.backend.security.Token.TokenType;
import com.eventmaster.backend.security.auth.AuthenticationResponse;
import com.eventmaster.backend.security.config.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of users
 *
 * @author Fabian Eilber
 */

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenService tokenService;


    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
    }

    public AuthenticationResponse register(User request){
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .emailAdress(request.getEmailAdress())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.saveToken(token);
    }

    public User getUserById(long userId) {
        return userRepository.findUserById(userId);
    }

    public User findByEmailAdress(String emailAdress){
        return userRepository.findByEmailAdress(emailAdress);
    }

    public String deleteUser(long userId) {
        try {

            User user = this.getUserById(userId);
            this.userRepository.deleteById(userId);

            return "User " + user.getFirstname() + " " + user.getLastname() + " deleted successfully" ;
        }catch (Exception e) {
            e.printStackTrace();
            return "User not deleted";
        }
    }
}
