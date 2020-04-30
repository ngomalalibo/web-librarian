package com.pc.weblibrarian.security;

import com.pc.weblibrarian.views.LoginPage;
import com.pc.weblibrarian.views.users.NewUserPage;
import com.pc.weblibrarian.views.users.UserEmailValidationPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener
{
    @Override
    public void serviceInit(ServiceInitEvent event)
    {
        event.getSource().addUIInitListener(uiEvent ->
                                            {
                                                final UI ui = uiEvent.getUI();
                                                ui.addBeforeEnterListener(this::authenticateNavigation);
                                            });
    }
    
    private void authenticateNavigation(BeforeEnterEvent event)
    {
        if (!SecurityUtils.isAccessGranted(event.getNavigationTarget()))//going somewhere without access
        {
            if (!LoginPage.class.equals(event.getNavigationTarget()) && !NewUserPage.class.equals(event.getNavigationTarget()) && !UserEmailValidationPage.class.equals(event.getNavigationTarget()))
            {
                if (SecurityUtils.isUserLoggedIn())
                {
                    log.info("Vaadin Forbidden -> Access Control");
                    throw new AccessDeniedException("Vaadin Web Librarian - Forbidden -> Access Control");
                    // event.rerouteToError(AccessDeniedException.class);
                    // event.rerouteTo(RouteExceptionPage.class);
                }
                else
                {
                    event.rerouteTo(LoginPage.class);
                }
            }
        }
    }
    
    /*private void beforeEnter(BeforeEnterEvent event)
    {
        if (!SecurityUtils.isAccessGranted(event.getNavigationTarget()))
        { //
            if (SecurityUtils.isUserLoggedIn())
            { //
                event.rerouteToError(NotFoundException.class); //
            }
            else
            {
                event.rerouteTo(LoginView.class); //
            }
        }
    }*/
}