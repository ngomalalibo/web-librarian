package com.pc.weblibrarian.controllers;

import com.pc.weblibrarian.views.LoginPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class UserController extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler
{
    @GetMapping(value = "/loginThymeleaf")
    public String getLoginPage(Model model)
    {
        return "loginThymeleaf";
    }
    
    @RequestMapping("/logout")
    public String getLogout()
    {
        log.info("logout controller in controller");
        log.info("logout controller in controller");
        log.info("logout controller in controller");
        log.info("logout controller in controller");
        log.info("logout controller in controller");
        log.info("logout controller in controller");
        log.info("logout controller in controller");
        log.info("logout controller in controller");
        log.info("logout controller in controller");
        log.info("logout controller in controller");
        log.info("logout controller in controller");
        log.info("logout controller in controller");
        VaadinSession session = VaadinSession.getCurrent();
        log.info("logging out of session -> " + session);
        if (session != null)
        {
            session.setAttribute("libraryuser", null);
            session.setAttribute("userrole", null);
            session.setAttribute("username", null);
            session.setAttribute("password", null);
            UI.getCurrent().navigate(LoginPage.class);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        LoginPage.loginStatus = false;
        
        // UI.getCurrent().navigate(LoginPage.class);
        
        /*VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().getSession().close();
        UI.getCurrent().setPollInterval(3000);
        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(null);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HttpServletRequest request = (HttpServletRequest) VaadinService.getCurrentRequest();
        new SecurityContextLogoutHandler().logout(request, null, auth);*/
        return "redirect:/springauthor";
    }
    
    
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        log.info("logout controller in LogoutHandler");
       /* VaadinSession session = VaadinSession.getCurrent();
        System.out.println("session = " + session);
        if (session != null)
        {
            session.setAttribute("libraryuser", null);
            session.setAttribute("userrole", null);
            session.setAttribute("username", null);
            session.setAttribute("password", null);
        
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().getSession().close();
            UI.getCurrent().setPollInterval(3000);
        }*/
        //SecurityContextHolder.clearContext();
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // HttpServletRequest request = (HttpServletRequest) VaadinService.getCurrentRequest();
        // HttpServletResponse response = (HttpServletResponse) VaadinService.getCurrentResponse();
        // new SecurityContextLogoutHandler().logout(request, response, auth);
        
        
        LoginPage.loginStatus = false;
        
        super.onLogoutSuccess(request, response, authentication);
    }
    
   /* public static void logOut()
    {
        log.info("logOut()");
        VaadinSession session = VaadinSession.getCurrent();
        session.setAttribute("userrole", null);
        session.setAttribute("username", null);
        UI.getCurrent().navigate(LoginPage.class);
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().getSession().close();
        UI.getCurrent().setPollInterval(3000);
//		SecurityContextHolder.clearContext();
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		HttpServletRequest request = (HttpServletRequest) VaadinService.getCurrentRequest();
//		new SecurityContextLogoutHandler().logout(request, null, auth);
    }*/
    
}
