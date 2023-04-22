package com.eventmaster.backend.serviceswithouttoken;
import com.eventmaster.backend.EmailService.EmailService;
import com.eventmaster.backend.entities.Role;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.repositories.UserRepository;
import local.variables.LocalizedStringVariables;
import com.eventmaster.backend.security.Token.Token;
import com.eventmaster.backend.security.Token.TokenService;
import com.eventmaster.backend.security.Token.TokenType;
import com.eventmaster.backend.security.authentification.AuthenticationResponse;
import com.eventmaster.backend.security.authentification.VerificationResponse;
import com.eventmaster.backend.security.config.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of users
 *
 * @author Fabian Eilber
 */

@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  JwtService jwtService;
    @Autowired
    private  TokenService tokenService;
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private EmailService emailService;

    private final SimpleMailMessage mailMessage = new SimpleMailMessage();


    public UserService(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String register(User request){
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .emailAdress(request.getEmailAdress())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        mailMessage.setFrom("ftb-solutions@outlook.de");
        mailMessage.setTo(user.getEmailAdress());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("Hello " + user.getFirstname() + "," +
                        "\nto confirm your account, please click here : \n"
                +"http://localhost:4200/login?authToken=" + jwtToken + "\n"
                +"WARNING: The token is only valid up to 15 Minutes");
        //emailService.sendEmail(mailMessage);
        System.out.println(mailMessage.getText());

        return "Successfully registered";
    }

    public VerificationResponse verify(String authToken) {
        String emailAdress = jwtService.extractUsername(authToken);
        User verifyUser = userRepository.findByEmailAdress(emailAdress);

        List<Token> tokens = verifyUser.getTokens();

        for (Token token: tokens) {
            if(token.getToken().equals(authToken)) {
                if (!token.isRevoked() || !token.isExpired()) {
                    verifyUser.setEnabled(true);
                    userRepository.save(verifyUser);
                    return VerificationResponse.builder()
                            .message("Verifikation war erfolgreich")
                            .build();
                }
            }
        }
        return VerificationResponse.builder()
                .message("Verifikation war erfolglos")
                .build();
    }

    public AuthenticationResponse login(User request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmailAdress(),
                        request.getPassword()
                )
        );

        User revokeUserTokens = userRepository.findByEmailAdress(request.getEmailAdress());
        revokeAllUserTokens(revokeUserTokens);

        var user = userRepository.findByEmailAdress(request.getEmailAdress());
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
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

    //Todo Link für Änderungsfeld anpassen
    public String requestPasswordReset(String emailAdress){
        try{
            User resetUserPwd = userRepository.findByEmailAdress(emailAdress);

            revokeAllUserTokens(resetUserPwd);


            var jwtToken = jwtService.generateToken(resetUserPwd);
            var refreshToken = jwtService.generateRefreshToken(resetUserPwd);
            saveUserToken(resetUserPwd, jwtToken);

            mailMessage.setFrom("ftb-solutions@outlook.de");
            mailMessage.setTo(emailAdress);
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setText("Hello " + resetUserPwd.getFirstname() + "," +
                    "\nto confirm your account, please click here : \n"
                    +"Token to authenticate reset: " + jwtToken + "\n"
                    +"WARNING: The token is only valid up to 15 Minutes");
            //emailService.sendEmail(mailMessage);
            System.out.println(mailMessage.getText());
            return "reset-request sent";
        }catch (Exception e) {
            e.printStackTrace();
            return "request failed";
        }
    }

    public AuthenticationResponse resetPassword(User user){
        try{
            User changedUser = userRepository.findByEmailAdress(user.getEmailAdress());

            changedUser.setPassword(passwordEncoder.encode(user.getPassword()));

            revokeAllUserTokens(changedUser);

            var savedUser = userRepository.save(changedUser);
            var jwtToken = jwtService.generateToken(changedUser);
            var refreshToken = jwtService.generateRefreshToken(changedUser);
            saveUserToken(savedUser, jwtToken);
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUserById(long userId) {
        return userRepository.findUserById(userId);
    }
    public User getUserByMail(String userMail) {
        try {
            return userRepository.findByEmailAdress(userMail);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User findByEmailAdress(String emailAdress){
        return userRepository.findByEmailAdress(emailAdress);
    }

    public String deleteUser(long userId) {
        try {
            User user = this.getUserById(userId);
            tokenService.deleteToken(user.getId());
            userRepository.delete(userRepository.findByEmailAdress(user.getEmailAdress()));

            return LocalizedStringVariables.USERDELETEDMESSAGE + user.getFirstname() +" "+user.getLastname();
        }catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.USERNOTDELETEDMESSAGE;
        }
    }



    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmailAdress(userEmail);
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenService.findAllValidTokensByUser(user);
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
            tokenService.saveToken(token);
        });
    }

}
