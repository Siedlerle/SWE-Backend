package com.eventmaster.backend.security.authorization;

import com.eventmaster.backend.security.authorization.helper.OrgaIdFinder;
import com.eventmaster.backend.security.authorization.helper.RoleInOrgaFinder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

public class AdminAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    OrgaIdFinder orgaFinder;
    RoleInOrgaFinder roleFinder;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestAuthorizationContext) {
        long organisationId = orgaFinder.getOrgaIdFromPath(requestAuthorizationContext
                .getRequest()
                .getServletPath());
        if (organisationId < 0) {
            return new AuthorizationDecision(false);
        }
        return new AuthorizationDecision(
                roleFinder.isUserAdminInOrga(organisationId,
                        authentication.get().getAuthorities()));
    }
}
