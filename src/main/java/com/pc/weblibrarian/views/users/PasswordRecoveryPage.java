package com.pc.weblibrarian.views.users;

import com.pc.weblibrarian.customcomponents.FlexMe;
import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.dataService.DataInitialization;
import com.pc.weblibrarian.entity.AppConfiguration;
import com.pc.weblibrarian.entity.LibraryUser;
import com.pc.weblibrarian.views.LoginPage;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.extern.slf4j.Slf4j;

@Theme(value = Lumo.class, variant = Lumo.DARK)
@Route(value = "passwordrecovery", absolute = true)
@PageTitle("Password Recovery")
@Tag("login-page")
@Slf4j
public class PasswordRecoveryPage extends Div
{
    
    private static final long serialVersionUID = 1L;
    
    
    public PasswordRecoveryPage()
    {
        super();
        addClassName("login-page");
        
        final Binder<LibraryUser> binder = new Binder<>(LibraryUser.class);
        
        AppConfiguration config = DataInitialization.loadDefaults();
        
        Span organizationname = new Span("Library Management");
        Image organizationlogo = new Image("", "organizationlogo");
        Span spanheader = new Span("Password Recovery");
        TextField username = new TextField("Username");
        SmallButton loginbtn = new SmallButton("Send Email").theme("primary");
        
        organizationname.addClassName("organizationname");
        add(organizationname);
        organizationlogo.addClassName("organizationlogo");
        spanheader.addClassName("spanheader");
        username.setId("username");
        username.getElement().setAttribute("placeholder", "username").setAttribute("autocomplete", "username");
        
        binder.forField(username).withValidator(new EmailValidator("Please enter a valid email address")).asRequired()
              .bind("username");
        
        loginbtn.addClickListener(clk ->
                                  {
                                      BinderValidationStatus<LibraryUser> val = binder.validate();
                                      if (val.isOk())
                                      {
                                          try
                                          {
                                              LibraryUser bean = new LibraryUser();
                                              binder.writeBeanIfValid(bean);
                                              String usr = bean.getUserEmail();
                                              log.info("username -> " + usr);
                    
                                              if (PasswordResetPage.resetUserPassword_SEND(usr))
                                              {
                                                  username.setReadOnly(true);
                                                  loginbtn.setEnabled(false);
                                                  new Notification("Please check your email for instructions.", 8000).open();
                                              }
                                          }
                                          catch (Exception e1)
                                          {
                                              log.error("Application Error", e1);
                                          }
                                      }
                                  });
        
        SmallButton gotologin = new SmallButton("Go to Login...").theme("contrast");
        gotologin.addClickListener(c -> UI.getCurrent().navigate(LoginPage.class));
        
        FlexMe buttonbar = new FlexMe(gotologin, loginbtn);
        buttonbar.addClassName("buttonbar");
        
        FormLayout loginform = new FormLayout(new Div(spanheader), username, buttonbar);
        loginform.setClassName("loginform");
        
        add(loginform);
        
        Span versionnumber = new Span(getClass().getPackage().getImplementationVersion());
        versionnumber.addClassName("versionnumber");
        add(versionnumber);
        
        Span licenseto = new Span("Licensed To: " + config.getOrganizationName());
        licenseto.addClassName("licenseto");
        add(licenseto);
    }
}

