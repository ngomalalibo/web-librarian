package com.pc.weblibrarian.views.users;

import com.pc.weblibrarian.customcomponents.FlexMe;
import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.dataService.DataInitialization;
import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.entity.AppConfiguration;
import com.pc.weblibrarian.entity.LibraryUser;
import com.pc.weblibrarian.entity.UserVerification;
import com.pc.weblibrarian.templates.ActionableEmail;
import com.pc.weblibrarian.utils.CustomNullChecker;
import com.pc.weblibrarian.utils.SendMail;
import com.pc.weblibrarian.utils.TokenGenerator;
import com.pc.weblibrarian.views.LoginPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import elemental.json.Json;
import elemental.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

// @Tag("login-page")
@Slf4j
@Theme(value = Lumo.class, variant = Lumo.DARK)
@Route(value = "passwordreset", absolute = true)
@PageTitle("Password Reset")
public class PasswordResetPage extends Div implements HasUrlParameter<String>
{
    
    private static final long serialVersionUID = 1L;
    
    private static GenericDataService gds = new GenericDataService(new LibraryUser());
    private static GenericDataService gdsUserV = new GenericDataService(new UserVerification());
    
    public PasswordResetPage()
    {
        super();
    }
    
    public void buildUI(JsonObject jsonObject)
    {
        addClassName("login-page");
        
        final Binder<LibraryUser> binder = new Binder<>(LibraryUser.class);
        
        AppConfiguration config = DataInitialization.loadDefaults();
        
        Span organizationname = new Span("Library Management");
        Span spanheader = new Span("Password Recovery");
        TextField username = new TextField("Username");
        PasswordField passwordA = new PasswordField("New Password");
        PasswordField passwordB = new PasswordField("Re-enter New Password");
        
        
        // organizationname.addClassName("organizationname");
        add(organizationname);
        spanheader.addClassName("spanheader");
        
        String userId = jsonObject.getString("userId");
        String newemail = jsonObject.getString("newEmail");
        
        username.setId("username");
        username.setReadOnly(true);
        username.setValue(newemail);
        
        binder.forField(passwordA)//
              .asRequired("Please enter a new password")
              .withValidator(new StringLengthValidator("Please enter a valid password",
                                                       config.getMinimumPasswordLength(), 30))
              .bind("passwordA");
        
        binder.forField(passwordB)//
              .asRequired("Please re-enter the new password")
              .withValidator(new StringLengthValidator("Please enter a valid password",
                                                       config.getMinimumPasswordLength(), 30))
              .withValidator(new Validator<String>()
              {
                  private static final long serialVersionUID = 1L;
            
                  @Override
                  public ValidationResult apply(String value, ValueContext context)
                  {
                      if (passwordA.getValue().equals(value))
                      {
                          return ValidationResult.ok();
                      }
                      else
                      {
                          return ValidationResult.error("Passwords must match");
                      }
                  }
              }).bind("passwordB");
        
        SmallButton loginbtn = new SmallButton("Save").theme("primary");
        loginbtn.addClickListener(clk ->
                                  {
                                      BinderValidationStatus<LibraryUser> val = binder.validate();
                                      if (val.isOk())
                                      {
                                          try
                                          {
                                              LibraryUser bean = new LibraryUser();
                                              binder.writeBeanIfValid(bean);
                                              if (setNewUserPassword(bean))
                                              {
                                                  new Notification("Reset Successful. You can now login.", 8000).open();
                                                  UI.getCurrent().navigate(LoginPage.class);
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
        
        FormLayout loginform = new FormLayout(new Div(spanheader), username, passwordA, passwordB, buttonbar);
        loginform.setClassName("loginform");
        
        add(loginform);
        
        Span versionnumber = new Span(getClass().getPackage().getImplementationVersion());
        versionnumber.addClassName("versionnumber");
        add(versionnumber);
        
        Span licenseto = new Span("Licensed To: " + config.getOrganizationName());
        licenseto.addClassName("licenseto");
        add(licenseto);
    }
    
    private void buildExpiredLinkPage()
    {
        VerticalLayout me = new VerticalLayout();
        me.add(new H2("Oops! This page has expired."));
        me.add(new RouterLink("Return to login", LoginPage.class));
        add(me);
    }
    
    @Override
    public void setParameter(BeforeEvent event, String parameter)
    {
        
        if (parameter != null)
        {
            try
            {
                JsonObject obj = Json.parse(TokenGenerator.decString(parameter));
                LocalDate createddate = LocalDate.parse(obj.getString("time"), DateTimeFormatter.ISO_DATE);
                boolean expired = Math.abs(Period.between(createddate, LocalDate.now()).getDays()) > 1;
                String userId = obj.getString("userId");
                String newemail = obj.getString("newEmail");
                boolean canrecover = checkUserVerficationValid(newemail);
                if (expired || !canrecover)
                {
                    buildExpiredLinkPage();
                }
                else
                {
                    buildUI(obj);
                }
            }
            catch (Exception e)
            {
                log.error("error ", e);
            }
        }
        
    }
    
    public static boolean resetUserPassword_SEND(String userEmail)
    {
        LibraryUser user = (LibraryUser) gds.getRecordByEntityProperty("userEmail", userEmail);
        if (user != null)
        {
            String token = TokenGenerator.createToken(user);
            
            UserVerification passrec = (UserVerification) gdsUserV.getRecordByEntityProperty("userEmail", userEmail);
            if (passrec == null)
            {
                passrec = new UserVerification();
            }
            passrec.setUserEmail(userEmail);
            passrec.setToken(token);
            passrec.setCreatedBy(user.getUserEmail());
            passrec.save(passrec);
            
            // send user email
            try
            {
                String urllink = UI.getCurrent().getRouter().getUrl(PasswordResetPage.class, token);
                log.info("resetUserPassword -> " + userEmail + " -> " + urllink);
                
                ActionableEmail alre = new ActionableEmail();
                alre.setSubject("Reset Password");
                alre.setPersonName(CustomNullChecker.stringSafe(user.getFirstName() + " " + user.getLastName()));
                alre.setToAddresses(userEmail);
                alre.setFromAddresses("ngomalalibo@gmail.com");
                
                alre.setLine1("We have received a request your reset the password for ##emailaddress##.");
                alre.setLine2("Please use the link below to reset your password");
                
                alre.setButtonText("Reset Password");
                alre.setButtonLink(urllink);
                alre.setLine3("If you did not request this password request, you can ignore this email.");
                SendMail sendMail = new SendMail();
                alre.setMessage(sendMail.getTemplate(alre));
                
                
                return SendMail.sendMailSSL(alre);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                log.error("Failed to send Password Reset Email");
            }
            
        }
        return false;
    }
    
    public boolean canRecoverPassword(String userId, String userEmail)
    {
        log.info("canRecoverPassword -> " + userId + " , " + userEmail);
        
        LibraryUser user = (LibraryUser) gds.getRecordByEntityProperty("userEmail", userEmail);
        
        if (user == null || user.getAccountBanned())
        {
            return false;
        }
        
        // UserVerification recovery = MDB.findUserVerification(userId, userEmail);
        UserVerification recovery = (UserVerification) gdsUserV.getRecordByEntityProperty("userEmail", userEmail);
        
        if (recovery == null)
        {
            return false;
        }
        return true;
    }
    
    public static boolean checkUserVerficationValid(String userEmail)
    {
        boolean valid = false;
        log.info("checkUserVerficationValid -> " + userEmail);
        // LibraryUser user = MDB.findUserById(userId);
        LibraryUser user = (LibraryUser) gds.getRecordByEntityProperty("userEmail", userEmail);
        if (user == null || user.getAccountBanned())
        {
        }
        else
        {
            UserVerification recovery = (UserVerification) gdsUserV.getRecordByEntityProperty("userEmail", userEmail);
            if (recovery != null && !recovery.isExpired())
            {
                valid = true;
            }
        }
        return valid;
    }
    
    public static void expireUserVerfication(String userEmail)
    {
        log.info("expireUserVerfication -> " + userEmail);
        UserVerification recovery = (UserVerification) gdsUserV.getRecordByEntityProperty("userEmail", userEmail);
        recovery.setExpired(true);
        recovery.setEmailAddressVerifiedDate(LocalDateTime.now());
        recovery.replaceEntity(recovery, recovery);
    }
    
    public boolean setNewUserPassword(LibraryUser bean)
    {
        boolean d = false;
        try
        {
            if (bean != null)
            {
                log.info("setNewUserPassword -> " + bean.getUserEmail());
                // LibraryUser usere = MDB.findUserById(userId);
                LibraryUser usere = (LibraryUser) gds.getRecordByEntityProperty("userEmail", bean.getUserEmail());
                usere.setLoginAttempts(0);
                usere.setAccountLocked(false);
                usere.createNewPassword(bean.getPassword());
                usere.save(usere);
                expireUserVerfication(bean.getUserEmail());
                d = true;
            }
            return d;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
}
