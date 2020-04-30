package com.pc.weblibrarian.session;

// import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@VaadinSessionScope
public class SessionService
{
    private String uid = UUID.randomUUID().toString();
    
    public String getSessionId()
    {
        return "session " + uid;
    }
}