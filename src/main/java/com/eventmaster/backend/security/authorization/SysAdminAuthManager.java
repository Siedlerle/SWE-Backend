package com.eventmaster.backend.security.authorization;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

public class SysAdminAuthManager implements AuthorizationManager<RequestAuthorizationContext> {
    String adminPassword;
    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(13);

    public SysAdminAuthManager(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {
        String[] rq = requestContext.getRequest().getRequestURI().split("/");
        String pwd = rq[rq.length - 1];
        return new AuthorizationDecision(bcrypt.matches(pwd,adminPassword));
    }
}
