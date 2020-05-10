package com.pc.weblibrarian.views.exceptions;

import com.pc.weblibrarian.views.MainFrame;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

//anytime this RouteNotFoundError is thrown vaadin responds with this message. RouteNotFoundError implements HasErrorParameter
@UIScope
@Component
@ParentLayout(MainFrame.class)
public class RouteExceptionPage extends RouteNotFoundError
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter)
    {
        getElement().appendChild(new H2("Custom Error Handler.").getElement());
        getElement().appendChild(new H4("Sorry, this page does not exist.").getElement());
        getElement().getStyle().set("padding", "1em");
        return HttpServletResponse.SC_NOT_FOUND;
    }
    
}
