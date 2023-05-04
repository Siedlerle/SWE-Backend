package com.eventmaster.backend.services;

import com.eventmaster.backend.EmailService.EmailService;
import com.eventmaster.backend.entities.MessageResponse;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    private final SimpleMailMessage mailMessage = new SimpleMailMessage();


    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            TokenService tokenService,
            AuthenticationManager authenticationManager,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }


    /**
     * Creates a new user and sends an Email
     *
     * @param request Userobject
     * @return success message
     */
    public MessageResponse register(User request) {

        User checkUser = request;

        if (userRepository.findByEmailAdress(checkUser.getEmailAdress()) != null) {
            return MessageResponse.builder()
                    .message("Email ist bereits vergeben.")
                    .build();
        }

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
                //+ "http://localhost:4200/login?authToken=" + jwtToken + "\n"
                + "http://ftb-eventmaster.de/login?authToken=" + jwtToken + "\n"
                + "WARNING: The token is only valid up to 15 Minutes");
        emailService.sendEmail(mailMessage);
        //System.out.println(mailMessage.getText());

        return MessageResponse.builder()
                .message("Sie wurden erfolgreich registriert.\nBitte prüfen Sie ihre Mails.")
                .build();
    }

    /**
     * Verifying a user after registration
     *
     * @param authToken jwt Token
     * @return VerificationResponse
     */
    public VerificationResponse verify(String authToken) {
        String emailAdress = jwtService.extractUsername(authToken);
        User verifyUser = userRepository.findByEmailAdress(emailAdress);

        List<Token> tokens = verifyUser.getTokens();

        for (Token token : tokens) {
            if (token.getToken().equals(authToken)) {
                if (!token.isRevoked() || !token.isExpired()) {
                    verifyUser.setEnabled(true);
                    userRepository.save(verifyUser);
                    return VerificationResponse.builder()
                            .message("Verifikation war erfolgreich.\nSie können sich jetzt anmelden.")
                            .build();
                }
            }
        }
        return VerificationResponse.builder()
                .message("Verifikation war erfolglos")
                .build();
    }

    /**
     * Creates a new Token for the corresponding user
     *
     * @param request Userobject
     * @return AuthenticationResponse
     */
    public AuthenticationResponse login(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmailAdress(),
                        request.getPassword()
                )
        );
        User requestinUserg = userRepository.findByEmailAdress(request.getEmailAdress());

        List<Token> allTokens = tokenService.findAllTokensByUser(requestinUserg.getId());

        for (Token token : allTokens) {
            if (token.isExpired() && token.isRevoked()) {
                tokenService.deleteToken(token);
            }
        }

        revokeAllUserTokens(requestinUserg);

        var user = userRepository.findByEmailAdress(request.getEmailAdress());
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Saves the tokens for the corresponding user
     *
     * @param user     Userobject
     * @param jwtToken Token
     */
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

    /**
     * Sends a mail to user to verify password reset request
     *
     * @param emailAdress EMail of the corresponding user
     * @return success message
     */
    public String requestPasswordReset(String emailAdress) {
        try {
            User resetUserPwd = userRepository.findByEmailAdress(emailAdress);

            revokeAllUserTokens(resetUserPwd);


            var jwtToken = jwtService.generateToken(resetUserPwd);
            var refreshToken = jwtService.generateRefreshToken(resetUserPwd);
            saveUserToken(resetUserPwd, jwtToken);

            mailMessage.setFrom("ftb-solutions@outlook.de");
            mailMessage.setTo(emailAdress);
            mailMessage.setSubject("Change your Password");
            mailMessage.setText("Hello " + resetUserPwd.getFirstname() + "," +
                    "\nto confirm the password change click the link below : \n"
                    + "Token to authenticate reset: " + jwtToken + "\n"
                    + "WARNING: The token is only valid up to 15 Minutes");
            emailService.sendEmail(mailMessage);
            //System.out.println(mailMessage.getText());
            return "reset-request sent";
        } catch (Exception e) {
            e.printStackTrace();
            return "request failed";
        }
    }

    /**
     * Resets the users password
     *
     * @param user Userobject of the corresponding user
     * @return AuthenticationResponse
     */
    public AuthenticationResponse resetPassword(User user) {
        try {
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

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a user by his Id
     *
     * @param userId Id of the corresponding user
     * @return Userobject
     */
    public User getUserById(long userId) {
        return userRepository.findUserById(userId);
    }

    /**
     * Retrieves a user by his EMail
     *
     * @param userMail EMail of the corresponding user
     * @return Userobject
     */
    public User getUserByMail(String userMail) {
        try {
            return userRepository.findByEmailAdress(userMail);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes a user
     *
     * @param emailAdress EMail of the corresponding user
     * @return success message
     */
    public String deleteUser(String emailAdress) {
        try {
            User user = userRepository.findByEmailAdress(emailAdress);
            tokenService.deleteTokens(user.getId());
            userRepository.delete(user);

            return LocalizedStringVariables.USERDELETEDMESSAGE + user.getFirstname() + " " + user.getLastname();
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.USERNOTDELETEDMESSAGE;
        }
    }


    /**
     * Refreshes the jwt token for a user
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return refreshed token
     * @throws IOException
     */
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
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
                return ResponseEntity.ok(authResponse);
            }
        }
        return null;
    }

    /**
     * Revokes all active tokens for user
     *
     * @param user Userobject of corresponding user
     */
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

    /**
     * System Admin Funktion to add an authenticated user directly to the database.
     * @param user User to be added.
     * @return "saved"
     */
    public String saveUser(User user) {
        User save = new User();
        save.setFirstname(user.getFirstname());
        save.setLastname(user.getLastname());
        save.setEmailAdress(user.getEmailAdress());
        save.setEnabled(true);
        save.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(save);
        return "saved";
    }

}
