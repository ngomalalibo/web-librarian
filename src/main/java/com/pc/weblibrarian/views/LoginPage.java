package com.pc.weblibrarian.views;

import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.entity.ActivityLog;
import com.pc.weblibrarian.entity.LibraryUser;
import com.pc.weblibrarian.enums.ActivityLogType;
import com.pc.weblibrarian.enums.PersonRoleType;
import com.pc.weblibrarian.security.CustomRequestCache;
import com.pc.weblibrarian.utils.CustomNullChecker;
import com.pc.weblibrarian.views.users.NewUserPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;

@Slf4j
@VaadinSessionScope
@Component
@PageTitle("Login | Vaadin Web Librarian CRM")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@Route(value = LoginPage.ROUTE, absolute = true)
@RouteAlias(value = "", absolute = true)
public class LoginPage extends VerticalLayout implements PageConfigurator, BeforeEnterObserver
{
    private static final long serialVersionUID = 1L;
    
    private static GenericDataService gds = new GenericDataService(new LibraryUser());
    
    public static boolean loginStatus = false;
    
    public static final String ROUTE = "login";
    
    // LoginForm loginForm = new LoginForm();
    LoginOverlay loginForm = new LoginOverlay();
    private Label feedback = new Label();
    
    public LoginPage(AuthenticationManager authenticationManager, CustomRequestCache requestCache)
    {
        super();
        setSizeFull();
        SmallButton registerLink = new SmallButton("Sign up");
        registerLink.addClassNames("btn", "btn-primary", "btn-outline-primary", "text-white");
        registerLink.addClassName("routerlink");
        registerLink.addClickListener(s ->
                                      {
                                          loginForm.close();
                                          UI.getCurrent().navigate(NewUserPage.class);
                                      });
        VerticalLayout titleVL = new VerticalLayout(new H1("Web Librarian and CMS"), registerLink);
        loginForm.setTitle(titleVL);
        loginForm.setOpened(true);
        
        // loginForm.setTitle("Web Librarian and CMS");
        loginForm.setDescription("Web Librarian and CMS");
        
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        
        // addClassName("login-view");
        addClassName("login-page");
        getElement().setProperty("color", "white !important");
        
        
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        
        
        add(loginForm);
        // getElement().appendChild(componentVL.getElement());
        
        // loginForm.setAction("login");
        
        feedback.addClassName("feedbacktxt");
        
        loginForm.addLoginListener(e ->
                                   {
                                       try
                                       {
                                           LibraryUser usere = (LibraryUser) gds.getRecordByEntityProperty("userEmail", e.getUsername());
                
                                           // try to authenticate with given credentials, should always return not null or throw an {@link AuthenticationException}
                                           log.info("authenticate -> " + e.getUsername());
                                           // log.info("password -> " + e.getPassword());
                                           final Authentication authentication = authenticationManager
                                                   .authenticate(new UsernamePasswordAuthenticationToken(e.getUsername(), e.getPassword())); //
                                           // if authentication was successful we will update the security context and redirect to the page requested first
                                           SecurityContextHolder.getContext().setAuthentication(authentication);
                                           // SecurityContextHolder.setStrategyName( SecurityContextHolder.MODE_GLOBAL );
                
                                           if (usere != null)
                                           {
                    
                                               log.info("user roles -> " + usere.getPersonRoleTypes());
                    
                                               VaadinSession session = VaadinSession.getCurrent();
                    
                                               session.setAttribute("libraryuser", usere);
                                               //session.setAttribute("appConfig", DataInitialization.loadDefaults());
                                               session.setAttribute("userrole", usere.getPersonRoleTypes());
                                               session.setAttribute("username", e.getUsername());
                                               session.setAttribute("password", e.getPassword());
                    
                                               /*log.info("Logged in User role -> " + usere.getPersonRoleTypes());
                                               log.info("Logged in User -> " + e.getUsername());
                                               log.info("Logged in password -> " + e.getPassword());*/
                                           }
                
                                           ActivityLog alog = new ActivityLog();
                                           alog.setUser(e.getUsername());
                                           alog.setActivityLogType(ActivityLogType.INFO);
                                           alog.setEventName("LOGIN");
                                           alog.setEventDescription("logged in at "
                                                                            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMMM, yyyy 'at' h:mm a")));
                                           alog.save(alog);
                
                                           loginStatus = true;
                                           loginForm.close();
                
                                           if (!requestCache.resolveRedirectUrl().isEmpty() || !requestCache.resolveRedirectUrl().isBlank())
                                           {
                                               UI.getCurrent().navigate(requestCache.resolveRedirectUrl());
                                           }
                                           else
                                           {
                                               UI.getCurrent().navigate(StartPage.class);
                                           }
                
                                       }
                                       catch (AuthenticationException ex)
                                       {
                                           log.error("cause " + ex.getCause());
                                           log.error("message " + ex.getMessage());
                                           log.error("stack trace " + ex.getStackTrace());
                                           // show default error message
                                           // Note: You should not expose any detailed information here like "username is known but password is wrong"
                                           // as it weakens security.
                                           loginForm.setError(true);
                                       }
                                   });
        
        
    }
    
    @Override
    public void configurePage(InitialPageSettings settings)
    {
        String faviconhref = "frontend/images/other/icon-192x192.png";
        
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("rel", "apple-touch-icon");
        attributes.put("rel", "shortcut icon");
        settings.addLink(faviconhref, attributes);
        /*String faviconhref = "frontend/images/other/icon-192x192.png";
        
        {
            HashMap<String, String> attributes = new HashMap<>();
            attributes.put("rel", "apple-touch-icon");
            settings.addLink(faviconhref, attributes);
        }
        
        {
            HashMap<String, String> attributes = new HashMap<>();
            attributes.put("rel", "shortcut icon");
            settings.addLink(faviconhref, attributes);
        }*/
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        log.info("Before enter Login");
        if (loginStatus)
        {
            log.info("Cannot navigate to login page. Currently logged in -> " + LoginPage.getLoggedInUser());
            // beforeEnterEvent.rerouteTo(StartPage.class);
            //event.rerouteTo(StartPage.class);
            event.forwardTo(StartPage.class);
        }
        else
        {
            if (!event.getLocation().getQueryParameters().getParameters().getOrDefault("error", Collections.emptyList()).isEmpty())
            {
                loginForm.setError(true);
            }
        }
       /* if (MainFrame.getLoggedInUser() != null)
        {
            log.info("event.getNavigationTarget() = " + event.getNavigationTarget());
            // event.rerouteTo(CheckInCheckOutManager.class);
            // event.rerouteTo("start");
            if (event.getNavigationTarget().equals(LoginPage.class))
            {
                event.forwardTo(StartPage.class);
            }
            else
            {
                event.rerouteTo(event.getNavigationTarget().getName());
            }
        }
        else
        {
            if (!event.getLocation().getQueryParameters().getParameters().getOrDefault("error", Collections.emptyList()).isEmpty())
            {
                loginForm.setError(true);
            }
        }*/
        
        
    }
    
    public static String getLoggedInUser()
    {
        if (loginStatus)
        {
            return CustomNullChecker.stringSafe((String) VaadinSession.getCurrent().getAttribute("username"));
        }
        return null;
    }
    
    public static PersonRoleType getUserRoleType()
    {
        try
        {
            if (loginStatus)
            {
                PersonRoleType o = (PersonRoleType) VaadinSession.getCurrent().getAttribute("userrole");
                return o != null ? o : PersonRoleType.ROLE_USER;
            }
            return PersonRoleType.ROLE_USER;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return PersonRoleType.ROLE_USER;
        }
    }
}
