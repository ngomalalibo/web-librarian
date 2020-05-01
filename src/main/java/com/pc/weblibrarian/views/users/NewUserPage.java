package com.pc.weblibrarian.views.users;

import com.pc.weblibrarian.customcomponents.CustomNotification;
import com.pc.weblibrarian.customcomponents.CustomTextField;
import com.pc.weblibrarian.customcomponents.FlexMe;
import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.dataService.DataInitialization;
import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.entity.ActivityLog;
import com.pc.weblibrarian.entity.AppConfiguration;
import com.pc.weblibrarian.entity.LibraryUser;
import com.pc.weblibrarian.entity.UserVerification;
import com.pc.weblibrarian.enums.ActivityLogType;
import com.pc.weblibrarian.enums.PersonRoleType;
import com.pc.weblibrarian.security.PasswordEncoder;
import com.pc.weblibrarian.templates.ActionableEmail;
import com.pc.weblibrarian.utils.CustomNullChecker;
import com.pc.weblibrarian.utils.SendMail;
import com.pc.weblibrarian.utils.TokenGenerator;
import com.pc.weblibrarian.views.LoginPage;
import com.pc.weblibrarian.views.StartPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "register", absolute = true)
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class NewUserPage extends VerticalLayout implements PageConfigurator, BeforeEnterObserver
{
    final Binder<LibraryUser> binder = new Binder<>(LibraryUser.class, true);
    
    private static final String SERVER = "localhost:8080/";
    
    private static GenericDataService gds = new GenericDataService(new LibraryUser());
    private static GenericDataService gdsUserV = new GenericDataService(new UserVerification());
    
    private Label feedback = new Label();
    
    private AppConfiguration config = DataInitialization.loadDefaults();
    private BCryptPasswordEncoder bcryptPassEncoder = PasswordEncoder.getPasswordEncoder();
    
    public NewUserPage()
    {
        super();
        setSizeFull();
        addClassNames("login-page", "text-white");
        // getElement().setProperty("color", "white !important");
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        
        VerticalLayout componentVL = new VerticalLayout();
        componentVL.setJustifyContentMode(JustifyContentMode.CENTER);
        componentVL.setAlignItems(Alignment.CENTER);
        componentVL.addClassName("text-white");
        
        setSizeFull();
        
        Div containerDiv = new Div();
        containerDiv.add(new H1("Library Management"));
        containerDiv.getElement().setProperty("color", "white");
        // containerDiv.addClassNames("text-white");
        add(containerDiv);
        
        
        CustomTextField fnameTF = new CustomTextField("First Name", ValueChangeMode.LAZY, null, false, "", TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
        // fnameTF.addClassName("text-white");
        CustomTextField lnameTF = new CustomTextField("Last Name", ValueChangeMode.LAZY, null, false, "", TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
        // lnameTF.addClassName("text-white");
        CustomTextField username = new CustomTextField("Username", ValueChangeMode.LAZY, null, false, "", TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
        // username.addClassName("text-white");
        PasswordField password = new PasswordField("Password", "");
        // password.addClassName("text-white");
        PasswordField confirmPassword = new PasswordField("Confirm Password", "");
        // confirmPassword.addClassName("text-white");
        password.setValueChangeMode(ValueChangeMode.LAZY);
        confirmPassword.setValueChangeMode(ValueChangeMode.EAGER);
        confirmPassword.addValueChangeListener(d ->
                                               {
            
                                                   if (!confirmPassword.getValue().equals(password.getValue()))
                                                   {
                                                       feedback.setClassName("alert-danger");
                                                       feedback.setText("Passwords do not match");
                                                   }
                                                   else
                                                   {
                                                       feedback.setClassName("alert-light");
                                                       feedback.setText("");
                                                   }
                                               });
        
        SmallButton registerButton = new SmallButton("Register").theme("primary");
        registerButton.addClassName("align-self-stretch");
        
        registerButton.addClickListener(clk ->
                                        {
                                            if (!register())
                                            {
                                                binder.setBean(new LibraryUser());
                                            }
            
                                        });
        
        FlexMe buttonbar = new FlexMe(registerButton);
        // buttonbar.addClassName("buttonbar");
        Anchor backToLogin = new Anchor("/login", "Back to Login");
        
        
        componentVL.add(new H3("User Registration"), feedback, fnameTF, lnameTF, username, password, confirmPassword, buttonbar, backToLogin);
        add(componentVL);
        
        /*fnameTF.addClassNames("login-view", "logintextfields");
        lnameTF.addClassNames("login-view", "logintextfields");
        username.addClassNames("login-view", "logintextfields");
        
        password.addClassNames("login-view", "logintextfields");
        confirmPassword.addClassNames("login-view", "logintextfields");*/
        
        binder.forField(fnameTF).asRequired().bind("firstName");
        binder.forField(lnameTF).asRequired().bind("lastName");
        binder.forField(username).withValidator(new EmailValidator("Please use a valid email address")).asRequired().bind("userEmail");
        binder.forField(password).withValidator(new StringLengthValidator("The password is not valid", config.getMinimumPasswordLength(), 30))
              .asRequired("Please enter your password").bind("password");
        
        /*Div loginform = new Div(new Div(spanheader));
        loginform.add(fname, new Div(fnameTF));
        loginform.add(lname, new Div(lnameTF));
        loginform.add(usernamelbl, new Div(username));
        loginform.add(passwordlbl, new Div(password));
        loginform.add(confirmPasswordlbl, new Div(confirmPassword));*/
        
        
    }
    
    private LibraryUser bean = new LibraryUser();
    
    private boolean register()
    {
        BinderValidationStatus<LibraryUser> val = binder.validate();
        if (val.isOk())
        {
            try
            {
                binder.writeBean(bean);
                
                System.out.println("Fullname = " + bean.getFirstName() + " " + bean.getLastName());
                
                bean.setAccessToConsumable(true);
                bean.setAccessToRentable(true);
                bean.setAccountBanned(false);
                bean.setAccountLocked(false);
                bean.setIsAdmin(false);
                bean.setLoginAttempts(0);
                bean.setMaximumCheckoutItems(config.getMaxCheckoutItemsPerUser());
                bean.setCreatedBy(bean.getUserEmail());
                // bean.setPersonRoleTypes(Collections.singleton(PersonRoleType.ROLE_USER));
                bean.setPersonRoleTypes(PersonRoleType.ROLE_USER);
                bean.setHashedPassword(bcryptPassEncoder.encode(bean.getPassword()));
                bean.save(bean);
                
                ActivityLog alog = new ActivityLog(bean.getUserEmail(), "Saved(" + bean.getClass().getSimpleName() + " with " + bean.getUuid() + ") ",
                                                   "user registration" + bean.getUserEmail() + " at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMMM, yyyy 'at' h:mm a")), ActivityLogType.INFO, bean.getClass().getSimpleName());
                
                alog.save(alog);
                
                if (verifyUserEmail_SEND(bean))
                {
                    feedback.addClassName("text-success");
                    feedback.setText("Thank you for registering... Please check your email for confirmation and account activation");
                    return true;
                }
                else
                {
                    feedback.addClassName("text-danger");
                    feedback.setText("Registration failed... Please contact the administrator");
                }
                //UI.getCurrent().navigate("login");
            }
            catch (BadCredentialsException e1)
            {
                log.error("Bad Credentials", e1);
                feedback.addClassName("text-danger");
                feedback.setText(e1.getMessage());
            }
            catch (Exception e1)
            {
                log.error("Application Error", e1);
                feedback.addClassName("text-danger");
                feedback.setText("Application Error");
            }
        }
        else
        {
            String text = val.getValidationErrors().stream().map(e -> e.getErrorMessage())
                             .collect(Collectors.joining(", "));
            feedback.setText(text);
        }
        return false;
    }
    
    @Override
    public void configurePage(InitialPageSettings settings)
    {
        
        String faviconhref = "frontend/images/other/icon-192x192.png";
        
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("rel", "apple-touch-icon");
        attributes.put("rel", "shortcut icon");
        settings.addLink(faviconhref, attributes);
        
        /*{
            HashMap<String, String> attributes = new HashMap<>();
            attributes.put("rel", "apple-touch-icon");
            settings.addLink(faviconhref, attributes);
        }
        
        {
            HashMap<String, String> attributes = new HashMap<>();
            attributes.put("rel", "shortcut icon");
            settings.addLink(faviconhref, attributes);
        }*/
    }
    
    public static boolean verifyUserEmail_SEND(LibraryUser user)
    {
        
        
        if (user != null)
        {
            String token = TokenGenerator.createToken(user);
            UserVerification passrec = (UserVerification) gdsUserV.getRecordByEntityProperty("userEmail", user.getUserEmail());
            if (passrec == null)
            {
                passrec = new UserVerification();
            }
            // passrec.setUserId(user.getUuid());
            passrec.setUserEmail(user.getUserEmail());
            passrec.setVerificationType("email");
            passrec.setToken(token);
            passrec.setCreatedBy(user.getUserEmail());
            passrec.save(passrec);
            
            // send user email
            try
            {
                String urllink = SERVER + UI.getCurrent().getRouter().getUrl(UserEmailValidationPage.class, token);
                log.info("createUserToken -> " + user.getUserEmail() + " -> " + urllink);
                
                ActionableEmail alre = new ActionableEmail();
                alre.setSubject("Web Librarian - Confirm Email Address");
                alre.setPersonName(CustomNullChecker.stringSafe(user.getFirstName() + " " + user.getLastName()));
                alre.setToAddresses(user.getUserEmail());
                alre.setLine1("You have now been added as a user on Weblibrarian.");
                alre.setLine2(
                        "You will need to verify your email ##emailaddress##, before you can checkout any items from the "
                                + user.getOrganization());
                alre.setLine3("Welcome to Web Librarian");
                alre.setButtonText("Verify Email");
                alre.setButtonLink(urllink);
                
                SendMail sendMail = new SendMail();
                alre.setMessage(sendMail.getTemplate(alre));
                
                return SendMail.sendMailSSL(alre);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                log.error("Failed to send Confirm Email Address Email");
                new CustomNotification("Failed to send Confirm Email Address Email", "btn-outline-danger", true, 3000, Notification.Position.TOP_CENTER).open();
            }
        }
        return false;
    }
    
    public boolean createUserToken(String userId, String userEmail)
    {
        // LibraryUser user = gds.findUserById(userId);
        LibraryUser user = (LibraryUser) gds.getRecordByEntityProperty("userEmail", userEmail);
        if (user != null)
        {
            String token = TokenGenerator.createToken(user);
            
            // UserVerification passrec = MDB.findUserVerification(userId, userEmail);
            UserVerification passrec = (UserVerification) gdsUserV.getRecordByEntityProperty("userEmail", userEmail);
            if (passrec == null)
            {
                passrec = new UserVerification();
            }
            passrec.setUserEmail(userEmail);
            passrec.setVerificationType("password");
            passrec.setToken(token);
            passrec.setCreatedBy(user.getUserEmail());
            passrec.save(passrec);
            
            // send user email
            try
            {
                String urllink = UI.getCurrent().getRouter().getUrl(PasswordResetPage.class, token);
                
                ActionableEmail alre = new ActionableEmail();
                alre.setSubject("Confirm Email Address");
                alre.setPersonName(CustomNullChecker.stringSafe(user.getFirstName() + " " + user.getLastName()));
                alre.setToAddresses(userEmail);
                alre.setFromAddresses("ngomalalibo@gmail.com");
                
                
                alre.setLine1("You have now been added as a user on Weblibrarian.");
                alre.setLine2("You will need to create a password for your login id: ##emailaddress##. Please use the link below to reset your password:");
                alre.setLine3("Welcome to Web Librarian");
                alre.setButtonText("Reset Password");
                alre.setButtonLink(urllink);
                
                SendMail sendMail = new SendMail();
                alre.setMessage(sendMail.getTemplate(alre));
                
                
                return SendMail.sendMailSSL(alre);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                log.error("Failed to send Confirm Email Address Email");
            }
            
        }
        return false;
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent)
    {
        if (LoginPage.loginStatus)
        {
            // beforeEnterEvent.rerouteTo(StartPage.class);
            beforeEnterEvent.forwardTo(StartPage.class);
        }
    }
}
