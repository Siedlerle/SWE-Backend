package com.eventmaster.backend.security.authorization.user;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

public class UserOrgaManager implements AuthorizationManager<RequestAuthorizationContext> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {
        Object credentials = authentication.get().getCredentials();
        Object principal = authentication.get().getPrincipal();
        Object authorities = authentication.get().getAuthorities();
        String request = requestContext.getRequest().getRequestURI();
        return new AuthorizationDecision(true);
    }
}
