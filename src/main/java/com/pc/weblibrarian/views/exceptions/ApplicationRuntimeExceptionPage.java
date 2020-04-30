package com.pc.weblibrarian.views.exceptions;

import com.pc.weblibrarian.exceptions.CustomRuntimeException;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@Tag(Tag.DIV)
// @ParentLayout(MainFrame.class)
public class ApplicationRuntimeExceptionPage extends VerticalLayout implements HasErrorParameter<CustomRuntimeException> // this route/view is invoked when an Exception is thrown
{
    private static final long serialVersionUID = 1L;
    
    private H2 header = new H2();
    private Paragraph errorTag = new Paragraph();
    private Div explanation = new Div();
    
    public ApplicationRuntimeExceptionPage()
    {
        add(header);
        add(errorTag);
        add(explanation);
        getStyle().set("padding", "1em");
    }
    
    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<CustomRuntimeException> parameter)
    {
        header.setText("Oops! We Have an Internal Server Error.");
        errorTag.setText("ERR:" + UUID.randomUUID().toString());
        log.error("ERR:" + UUID.randomUUID().toString(), parameter.getCaughtException());
        explanation.setText(parameter.getCustomMessage());
        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
}
