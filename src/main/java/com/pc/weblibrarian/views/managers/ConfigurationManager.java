package com.pc.weblibrarian.views.managers;

import com.pc.weblibrarian.customcomponents.Fragment;
import com.pc.weblibrarian.customcomponents.HTMLComponents;
import com.pc.weblibrarian.customcomponents.NumberTextField;
import com.pc.weblibrarian.dataService.DataInitialization;
import com.pc.weblibrarian.entity.AppConfiguration;
import com.pc.weblibrarian.security.SecuredByRole;
import com.pc.weblibrarian.views.MainFrame;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

@SecuredByRole(value = "ADMIN")
// @Secured(value = "ADMIN")
// @PreAuthorize(value = "hasAnyRole('ADMIN')") // Does not work with vaadin views
@Route(value = "appConfig", layout = MainFrame.class)
@PreserveOnRefresh
public class ConfigurationManager extends Fragment implements BeforeEnterObserver
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        /*if (MainFrame.getUserRoleType() != PersonRoleType.ROLE_SUPERADMIN)
        {
            event.rerouteTo("authors");
        }*/
    }
    
    Binder<AppConfiguration> binder = new Binder<>(AppConfiguration.class);
    
    private final Div formarea = new Div();
    
    public ConfigurationManager()
    {
        super();
        setHeaderText("Configuration");
        
        DataInitialization.loadDefaults();
        AppConfiguration config = DataInitialization.loadDefaults();
        
        binder.setBean(config);
        
        formarea.getStyle().set("margin", "1em");
        
        String welcometext = "On this page you can customize the behavior of the Web Librarian application,"
                + " including options for setting check-in and check-out behaviours. Only a user with the "
                + "Super Admin role will be able to access or modify application configurations.";
        
        HTMLComponents.BlockQuote welcomeqoute = new HTMLComponents.BlockQuote(new H6("Welcome to Configuration"), new Span(welcometext));
        welcomeqoute.getStyle().set("maxWidth", "55em");
        formarea.add(welcomeqoute);
        
        addContent(formarea);
        
        buildPage();
        
        binder.addValueChangeListener(lst ->
                                      {
                                          if (lst.isFromClient())
                                          {
                                              if (binder.validate().isOk())
                                              {
                                                  log.info("config.save()");
                                                  config.save(config);
                                              }
                                          }
                                      });
    }
    
    private void buildPage()
    {
        
        formarea.add(new H5("Organization"));
        {
            TextField organizationName = new TextField();
            organizationName.setPlaceholder("Organization Name");
            organizationName.setWidth("50%");
            organizationName.getElement().setAttribute("clear-button-visible", "");
            binder.forField(organizationName).asRequired("Please set the organization name")
                  .withValidator(new StringLengthValidator("Please use a valid name", 1, 30))
                  .bind("organizationName");
            
            TextField adminEmail = new TextField();
            adminEmail.setWidth("50%");
            binder.forField(adminEmail).asRequired("Please set an admin email")
                  .withValidator(new EmailValidator("Please use a valid email address"))
                  .bind("adminEmail");
            
            Checkbox copyOrganizationAdminOnAllEmails = new Checkbox("Copy Company Admin on Outgoing Email");
            binder.forField(copyOrganizationAdminOnAllEmails).bind("copyOrganizationAdminOnAllEmails");
            
            MyCustomLayout layout0 = new MyCustomLayout();
            layout0.addItemWithLabel("Organization", organizationName);
            formarea.add(layout0);
            
            MyCustomLayout layout1 = new MyCustomLayout();
            layout1.addItemWithLabel("Admin Email", adminEmail, copyOrganizationAdminOnAllEmails);
            formarea.add(layout1);
            
        }
        
        formarea.add(new H5("Check-In/Out"));
        
        {
            FormLayout f1 = new FormLayout();
            f1.setResponsiveSteps(new FormLayout.ResponsiveStep("0em", 1));
            
            NumberTextField maxDaysToEditCheckOut = new NumberTextField();
            maxDaysToEditCheckOut.setSuffixComponent(new Span("days"));
            maxDaysToEditCheckOut.centerAlign();
            binder.forField(maxDaysToEditCheckOut).asRequired("This configuration must be set")
                  .bind("maxCheckoutExtendedDays");
            f1.addFormItem(maxDaysToEditCheckOut, "Maximum days for checkout edits");
            
            NumberTextField maxCheckoutItemsPerUser = new NumberTextField("Max Check-out per user");
            maxCheckoutItemsPerUser.setSuffixComponent(new Span("items"));
            maxCheckoutItemsPerUser.centerAlign();
            binder.forField(maxCheckoutItemsPerUser).asRequired("This configuration must be set")
                  .bind("maxCheckoutItemsPerUser");
            f1.add(maxCheckoutItemsPerUser);
            
            NumberTextField maxCheckoutDays = new NumberTextField("Check-out Maximum days");
            maxCheckoutDays.setSuffixComponent(new Span("days"));
            maxCheckoutDays.centerAlign();
            binder.forField(maxCheckoutDays).asRequired("This configuration must be set")
                  .bind("maxCheckoutDays");
            f1.add(maxCheckoutDays);
            
            NumberTextField checkoutRemiderDays = new NumberTextField("Days Till first reminder");
            checkoutRemiderDays.setSuffixComponent(new Span("days"));
            checkoutRemiderDays.centerAlign();
            binder.forField(checkoutRemiderDays).asRequired("This configuration must be set")
                  .bind("checkoutReminderDays");
            f1.add(checkoutRemiderDays);
            formarea.add(f1);
            
        }
        
        {
            VerticalLayout f2 = new VerticalLayout();
            Checkbox allowAdminDeleteCheckOut = new Checkbox("Allow Administators to Delete check-out entries");
            binder.forField(allowAdminDeleteCheckOut).bind("allowAdminDeleteCheckOut");
            f2.add(allowAdminDeleteCheckOut);
            
            Checkbox notifyAdminAtReminder = new Checkbox("Notify Administators for check-In reminder");
            binder.forField(notifyAdminAtReminder).bind("notifyAdminAtReminder");
            f2.add(notifyAdminAtReminder);
            
            Checkbox notifyUserAtCheckout = new Checkbox("Notify (Email) Users At check-out");
            binder.forField(notifyUserAtCheckout).bind("notifyUserAtCheckout");
            f2.add(notifyUserAtCheckout);
            formarea.add(f2);
        }
        
        formarea.add(new H5("User Registration"));
        
        {
//			VerticalLayout f2 = new VerticalLayout();
//			NumberTextField minimumPasswordLength = new NumberTextField("Minimum Password Length");
//			minimumPasswordLength.addPlusMinus();
//			binder.forField(minimumPasswordLength).asRequired().bind(AppConfiguration._minimumPasswordLength);
//			f2.add(minimumPasswordLength);

//			NumberTextField maximumPasswordAttempts = new NumberTextField("Maximum Password Attempts");
//			maximumPasswordAttempts.addPlusMinus();
//			binder.forField(maximumPasswordAttempts).asRequired().bind(AppConfiguration._maximumPasswordAttempts);
//			f2.add(maximumPasswordAttempts);
//			formarea.add(f2);
        }
        
        {
            VerticalLayout f2 = new VerticalLayout();
            Checkbox requireUserAddress = new Checkbox("Require Address for New Users");
            binder.forField(requireUserAddress).bind("requireUserAddress");
            f2.add(requireUserAddress);
            
            Checkbox enforceEmailValidation = new Checkbox("Require Email Validation Before Checkout");
            binder.forField(enforceEmailValidation).bind("emailValidatedBeforeCheckoutAllowed");
            f2.add(enforceEmailValidation);
            formarea.add(f2);
        }
    }
    
    public static class MyCustomLayout extends Composite<FormLayout>
    {
        
        private static final long serialVersionUID = 1L;
        
        public void addItemWithLabel(String label, Component... items)
        {
            getContent().setResponsiveSteps(new FormLayout.ResponsiveStep("0em", 1));
            Div itemWrapper = new Div();
            // Wrap the given items into a single div
            for (Component c : items)
            {
                itemWrapper.add(new Div(c));
            }
            // getContent() returns a wrapped FormLayout
            getContent().addFormItem(itemWrapper, label);
        }
    }
}
