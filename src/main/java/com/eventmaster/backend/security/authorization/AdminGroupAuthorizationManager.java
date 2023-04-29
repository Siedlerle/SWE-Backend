package com.eventmaster.backend.security.authorization;

import com.eventmaster.backend.security.authorization.helper.OrgaIdFinder;
import com.eventmaster.backend.security.authorization.helper.RoleInOrgaFinder;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.io.IOException;
import java.util.function.Supplier;

public class AdminGroupAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    OrgaIdFinder orgaFinder;
    RoleInOrgaFinder roleFinder;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestAuthorizationContext) {
        long organisationId;
        try {
             organisationId = orgaFinder.getOrgaIdFromGroupObject(requestAuthorizationContext
                    .getRequest()
                    .getInputStream().readAllBytes());
        } catch (IOException e) {
            return new AuthorizationDecision(false);
        }
        if(organisationId < 0){
            return new AuthorizationDecision(false);
        }
        boolean ret = roleFinder.isUserAdminInOrga(
                organisationId,
                authentication.get().getAuthorities());
        return new AuthorizationDecision(ret);
    }
}
