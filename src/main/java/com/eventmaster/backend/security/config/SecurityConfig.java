package com.eventmaster.backend.security.config;

import com.eventmaster.backend.security.authorization.SysAdminAuthManager;
import com.eventmaster.backend.security.authorization.SysAdminProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LogoutHandler logoutHandler;
    private final SysAdminProperties sysAdminProperties;

    public SecurityConfig(AuthenticationProvider authenticationProvider,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          LogoutHandler logoutHandler,
                          SysAdminProperties sysAdminProperties) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.logoutHandler = logoutHandler;
        this.sysAdminProperties = sysAdminProperties;
    }


    /**
     *  Configures the security filter chain for the HTTP requests.
     *  @param http The HttpSecurity object to configure.
     *  @return The SecurityFilterChain object representing the configured security filter chain.
     *  @throws Exception If an error occurs while configuring the HttpSecurity object.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .authorizeHttpRequests()
                    .requestMatchers("/eventimages/**").permitAll()
                    .requestMatchers("/user/auth/**").permitAll()
                    .requestMatchers("/user/orga/**").permitAll()
                    .requestMatchers("/user/event/**").permitAll()
                    .requestMatchers("/organizer/**").permitAll()
                    .requestMatchers("/admin/orga/**").permitAll()
                    .requestMatchers("/tutor/**").permitAll()
                    .requestMatchers("/logout").permitAll()
                .requestMatchers("/sys-admin/**").access(new SysAdminAuthManager(sysAdminProperties.getAdminPassword()))
                    //.requestMatchers("/organizer/**").access(new OrganizerAuthorizationManager())
                    //.anyRequest().access(new CustomAuthorizationManager())
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authenticationProvider(authenticationProvider)
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout( logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}
