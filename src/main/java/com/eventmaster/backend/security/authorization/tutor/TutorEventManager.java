package com.eventmaster.backend.security.authorization.tutor;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.security.authorization.authority.CustomAuthority;
import com.eventmaster.backend.security.authorization.deepCopy.PipedDeepCopy;
import com.eventmaster.backend.security.authorization.helper.EventReader;
import com.eventmaster.backend.security.authorization.helper.OrgaIdFinder;
import com.eventmaster.backend.security.authorization.helper.RoleChecker;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

public class TutorEventManager implements AuthorizationManager<RequestAuthorizationContext> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {
        Object credentials = authentication.get().getCredentials();
        Object principal = authentication.get().getPrincipal();
        Object authorities = authentication.get().getAuthorities();
        String request = requestContext.getRequest().getRequestURI();
        //RequestAuthorizationContext copy = (RequestAuthorizationContext) PipedDeepCopy.copy(requestContext);
        byte[] bytesCopy;
        try {
            bytesCopy  = requestContext.getRequest().getInputStream().readAllBytes();
        } catch (Exception e) {
            return new AuthorizationDecision(false);
        }
        Event event = EventReader.getEventIdFromEventObject(bytesCopy);
        boolean isTutor = RoleChecker.isUserTutorForEvent(event.getId(), event.getOrganisation().getId(),(Collection<CustomAuthority>) authorities);
        return new AuthorizationDecision(true);
    }
}
