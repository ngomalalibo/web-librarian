package com.pc.weblibrarian.views.exceptions;

import com.pc.weblibrarian.utils.CustomNullChecker;
import com.pc.weblibrarian.views.MainFrame;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@UIScope
@org.springframework.stereotype.Component
@SpringComponent(value = "nullpointer")
@Route(value = "nullpointer", layout = MainFrame.class)
@Tag("div")
public class NullPointerExceptionHandler extends Component implements HasErrorParameter<NullPointerException>
{
    private String customMessage;
    
    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NullPointerException> parameter)
    {
        // getElement().removeFromTree();
        customMessage = parameter.getCustomMessage();
        getElement().removeAllChildren();
        getElement().appendChild(new H2("Custom Null pointer exception Handler.").getElement());
        getElement().appendChild(new H4("No information to retrieve").getElement());
        getElement().appendChild(new Label("Additional Information: " + parameter.getCustomMessage()).getElement());
        getElement().getStyle().set("padding", "1em");
        
        return HttpServletResponse.SC_FORBIDDEN;
    }
    
    public NullPointerExceptionHandler()
    {
        super();
        getElement().removeFromTree();
        getElement().removeAllChildren();
        getElement().appendChild(new H2("Custom Null pointer exception Handler.").getElement());
        getElement().appendChild(new H4("No information to retrieve").getElement());
        getElement().appendChild(new Label("Additional Information: " + CustomNullChecker.stringSafe(customMessage)).getElement());
        getElement().getStyle().set("padding", "1em");
        
    }
}
