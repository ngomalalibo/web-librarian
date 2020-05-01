package com.pc.weblibrarian.security;

import com.pc.weblibrarian.exceptions.CustomAccessDeniedException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterSecurity //extends GenericFilterBean
{
    // @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        System.out.println("Request URI = " + req.getRequestURI());
        if (isAccessGranted(req.getRequestURI(), req))
        {
            throw new CustomAccessDeniedException("Forbidden -> Access Control");
        }
        chain.doFilter(request, response);
        
    }
    
    public static boolean isAccessGranted(String uri, HttpServletRequest req)
    {
        List<String> allowedRoles = new ArrayList<>();
        Method enclosingMethod = req.getClass().getEnclosingMethod();
        if ("/login".equals(uri) || "/register".equals(uri) || "/emailvalidation".equals(uri))
        {
            return true;
        }
        
        if (!SecurityUtils.isUserLoggedIn())
        { //
            return false;
        }
        // Allow if no roles are required.
        Secured secured = AnnotationUtils.findAnnotation(enclosingMethod, Secured.class);
        PreAuthorize preAuthorize = AnnotationUtils.findAnnotation(enclosingMethod, PreAuthorize.class);
        SecuredByRole securedByRole = AnnotationUtils.findAnnotation(enclosingMethod, SecuredByRole.class);
        if (secured == null && preAuthorize == null && securedByRole == null)
        {
            return true; // grant access
        }
        if (secured != null)
        {
            allowedRoles = Arrays.asList(secured.value());
        }
        if (preAuthorize != null)
        {
            String values = preAuthorize.value();
            String[] dd;
            if (values.contains(","))
            {
                dd = values.split(",");
            }
            else
            {
                dd = values.split(" ");
            }
            dd = (String[]) Arrays.stream(dd).map(String::trim).toArray();
            allowedRoles = Arrays.asList(dd);
            allowedRoles.forEach(d -> System.out.println("Allowed Roles " + d));
        }
        if (securedByRole != null)
        {
            allowedRoles = Arrays.asList(securedByRole.value());
        }
        
        // lookup needed role in user roles
        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        
        return allowedRoles.containsAll(userAuthentication.getAuthorities());
        // return userAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(allowedRoles::contains);
    }
}
