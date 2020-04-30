package com.pc.weblibrarian.views.users;

import com.pc.weblibrarian.utils.TokenGenerator;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import elemental.json.Json;
import elemental.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Viewport("user-scalable=no, initial-scale=1.0, shrink-to-fit=no")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@Route(value = "emailvalidation", absolute = true)
@PageTitle("Email Confirmation Page")
@Slf4j
public class UserEmailValidationPage extends VerticalLayout implements HasUrlParameter<String>
{
    
    private static final long serialVersionUID = 1L;
    
    public UserEmailValidationPage()
    {
        super();
        addClassNames("container-fluid", "text-white");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassNames("login-page");
        
        getElement().setProperty("color", "white !important");
        setSizeFull();
        
    }
    
    public void buildUI(JsonObject jsonObject)
    {
        add(new H2("Awesome!"));
        add(new Span("Thanks for confirming your email."));
        add(new Anchor("/login", "Return to login Page"));
    }
    
    private void buildExpiredLinkPage()
    {
        add(new H2("Oops! This page has expired."));
        add(new Anchor("/login", "Return to login page"));
    }
    
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter)
    {
        if (parameter != null)
        {
            try
            {
                log.info("verifying user in setParameter method");
                JsonObject obj = Json.parse(TokenGenerator.decString(parameter));
                log.info("returned parameter = " + obj);
                LocalDate createddate = LocalDate.parse(obj.getString("time"), DateTimeFormatter.ISO_DATE);
                String newemail = obj.getString("userEmail");
                boolean expired = Math.abs(Period.between(createddate, LocalDate.now()).getDays()) > 1;
                boolean canrecover = PasswordResetPage.checkUserVerficationValid(newemail);
                if (expired || !canrecover)
                {
                    buildExpiredLinkPage();
                }
                else
                {
                    buildUI(obj);
                    PasswordResetPage.expireUserVerfication(newemail);
                }
            }
            catch (Exception e)
            {
                log.error("error ", e);
                buildExpiredLinkPage();
            }
        }
        else
        {
            buildExpiredLinkPage();
        }
        
    }
    
}

