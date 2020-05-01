package com.pc.weblibrarian.security;

import com.pc.weblibrarian.views.LoginPage;
import com.pc.weblibrarian.views.users.NewUserPage;
import com.pc.weblibrarian.views.users.UserEmailValidationPage;
import com.vaadin.flow.router.Location;
import lombok.extern.slf4j.Slf4j;
import org.ilay.Access;
import org.ilay.AccessEvaluator;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoleBasedEvaluator implements AccessEvaluator<SecuredByRole>
{
    @Override
    public Access evaluate(Location location, Class<?> navigationTarget, SecuredByRole annotation)
    { //
        
        if (!SecurityUtils.isAccessGranted(navigationTarget))//going somewhere without access
        {
            if (!LoginPage.class.equals(navigationTarget) && !NewUserPage.class.equals(navigationTarget) && !UserEmailValidationPage.class.equals(navigationTarget))
            {
                if (SecurityUtils.isUserLoggedIn())
                {
                    log.info("RoleBasedEvaluator Forbidden -> Access Control");
                    return Access.restricted(new AccessDeniedException("RolebasedEvaluator Forbidden -> Access Control"), "RolebasedEvaluator Forbidden -> Access Control");
                    //throw new AccessDeniedException("Web Librarian - Forbidden -> Access Control");
                    // event.rerouteToError(AccessDeniedException.class);
                    // event.rerouteTo(RouteExceptionPage.class);
                }
                else
                {
                    return Access.restricted(AccessDeniedException.class);
                }
            }
        }
        return Access.granted();
       /* if (!SecurityUtils.isAccessGranted(navigationTarget))
        {
            if (SecurityUtils.isUserLoggedIn())
            {
                return Access.restricted(AccessDeniedException.class);
            }
            else
            {
                return Access.restricted(LoginPage.ROUTE);
            }
        }
        
        return Access.granted(); //*/
        
        
    }
}