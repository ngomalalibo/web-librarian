package com.pc.weblibrarian.views.exceptions;

import com.pc.weblibrarian.views.LoginPage;
import com.pc.weblibrarian.views.MainFrame;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletResponse;

// @ResponseStatus(HttpStatus.FORBIDDEN)
// @ResponseStatus(HttpStatus.FORBIDDEN) // Important here. This handleNotFound method is called each time the HttpStatus.NOT_FOUND status occurs
// @VaadinSessionScope

@Slf4j
@UIScope
@org.springframework.stereotype.Component
@SpringComponent(value = "forbidden")
@Route(value = "forbidden", layout = MainFrame.class)
@Tag("div")
@ParentLayout(MainFrame.class)
public class AccessDeniedExceptionHandler extends Component implements HasErrorParameter<AccessDeniedException>, RouterLayout
{
    
    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<AccessDeniedException> parameter)
    {
        // getElement().removeFromTree();
        getElement().removeAllChildren();
        getElement().appendChild(new H2("Custom Access Denied Error Handler.").getElement());
        getElement().appendChild(new H4("Tried to navigate to a view without correct access rights").getElement());
        getElement().getStyle().set("padding", "1em");
        
        return HttpServletResponse.SC_FORBIDDEN;
    }
    
    public AccessDeniedExceptionHandler()
    {
        super();
        if (LoginPage.loginStatus)
        {
            getElement().removeFromTree();
            getElement().removeAllChildren();
            getElement().appendChild(new H2("Custom Access Denied Error Handler.").getElement());
            getElement().appendChild(new H4("Tried to navigate to a view without correct access rights").getElement());
            getElement().getStyle().set("padding", "1em");
        }
        
    }
    
    /*@ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(Exception e)
    {
        log.info("handleAccessDeniedException in vaadin handler");
        getElement().appendChild(new H2("Custom Access Denied Error Handler.").getElement());
        getElement().appendChild(new H4("Tried to navigate to a view without correct access rights").getElement());
        getElement().getStyle().set("padding", "1em");
    }*/
}