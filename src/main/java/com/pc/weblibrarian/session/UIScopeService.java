package com.pc.weblibrarian.session;

// import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;


import java.util.UUID;

@Component
@UIScope
public class UIScopeService
{
    private String uid = UUID.randomUUID().toString();
    
    public String getUIScopeId()
    {
        return "ui " + uid;
    }
}