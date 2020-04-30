package com.pc.weblibrarian.views.dialogs;

import com.pc.weblibrarian.customcomponents.*;
import com.pc.weblibrarian.dataService.DataInitialization;
import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.dataproviders.PersonDP;
import com.pc.weblibrarian.entity.AppConfiguration;
import com.pc.weblibrarian.entity.LibraryUser;
import com.pc.weblibrarian.entity.Person;
import com.pc.weblibrarian.entity.UserVerification;
import com.pc.weblibrarian.enums.PersonRoleType;
import com.pc.weblibrarian.utils.BinderPersonConverter;
import com.pc.weblibrarian.utils.UniqueUserValidator;
import com.pc.weblibrarian.views.BasicDialog;
import com.pc.weblibrarian.views.LoginPage;
import com.pc.weblibrarian.views.users.NewUserPage;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;

import java.util.EnumSet;
import java.util.stream.Collectors;

public class LibraryUserCRUDDIalog extends BasicDialog implements BeforeLeaveObserver
{
    
    private LibraryUser libraryUserClone = new LibraryUser();
    private GenericDataService gds = new GenericDataService(new UserVerification());
    
    // TODO > Data Provider
    PersonDP dp = new PersonDP();
    ConfigurableFilterDataProvider<Person, Void, String> fdp = dp.withConfigurableFilter();
    
    // TODO > Components
    CustomTextField personSearch = new CustomTextField("", ValueChangeMode.EAGER, new Icon(VaadinIcon.SEARCH), true, "Search person", "small");
    
    
    CustomTextField usernameTF = new CustomTextField("Username", ValueChangeMode.LAZY, null, true, "", "small");
    PasswordField passwordPF = new PasswordField("Password");
    CustomComboBox<PersonRoleType> roleCB = new CustomComboBox<>("Role", EnumSet.allOf(PersonRoleType.class), null, "2", false);
    Checkbox accessToConsumable = new Checkbox("Access to Consumable", false);
    Checkbox accessToRentable = new Checkbox("Access to Rentable", false);
    Checkbox accountBanned = new Checkbox("Is Account Banned", false);
    Checkbox isAdmin = new Checkbox("Is Admin ", false);
    NumberField maxCheckOut = new NumberField("Max. Check Out");
    
    
    private ListBox<Person> personLB = new ListBox<>();
    
    // TODO > Layouts
    FormLayout userFL = new FormLayout();
    VerticalLayout libraryUserVL = new VerticalLayout(userFL);
    HorizontalLayout buttonsHL = new HorizontalLayout();
    
    
    // TODO > Labels
    private H2 titleH3 = new H2();
    private H2 personH2 = new H2();
    
    private Binder<LibraryUser> binder = new Binder<>(LibraryUser.class, true/* scans nested objects to bind as well*/);
    
    // TODO > Buttons
    SmallButton save = new SmallButton("Save");
    SmallButton edit = new SmallButton("Edit");
    SmallButton cancel = new SmallButton("Cancel");
    SmallButton clear = new SmallButton("Clear");
    SmallButton delete = new SmallButton("Delete");
    SmallButton clearImage = new SmallButton("Clear Image");
    
    public LibraryUserCRUDDIalog(LibraryUser bean, BeanAction beanAction, BasicDialog.Action<LibraryUser> action, DialogSize dialogSize)
    {
        super(beanAction, dialogSize);
        
        titleH3.addClassName("mr-2");
        personH2.addClassName("ml-5");
        
        personLB.setDataProvider(fdp);
        personLB.getElement().getStyle().set("size", "4px");
        personLB.getElement().getStyle().set("border", "1px solid ivory");
        personLB.setMaxHeight("150px");
        personLB.setHeight("150px");
        personLB.setWidthFull();
        
        personH2.addClassName("font-size-s");
        personH2.addClassName("text-primary");
        
        save.theme("primary");
        save.setEnabled(true);
        
        edit.theme("primary");
        edit.setEnabled(true);
        
        delete.theme("error");
        delete.theme(ButtonVariant.LUMO_ERROR.getVariantName());
        
        clearImage.addClassNames("btn-sm", "btn", "btn-outline-primary");
        
        maxCheckOut.setMaxWidth("10");
        
        if (beanAction == BeanAction.VIEW)
        {
            setTitle(new Label("Library User Details"));
            save.setVisible(false);
            edit.setVisible(false);
            clear.setVisible(false);
            delete.setVisible(false);
            
            personLB.setEnabled(false);
        }
        if (beanAction == BeanAction.NEW)
        {
            AppConfiguration config = DataInitialization.loadDefaults();
            bean.setMaximumCheckoutItems(config.getMaxCheckoutItemsPerUser());
            
            // image.setVisible(false);
            buttonsHL.add(clear);
            buttonsHL.add(save);
            titleH3 = new H2("New Library User ");
            setTitle(titleH3);
            
        }
        if (beanAction == BeanAction.EDIT)
        {
            buttonsHL.add(clear);
            buttonsHL.add(edit);
            titleH3 = new H2("Edit Library User ");
            setTitle(titleH3);
            usernameTF.setReadOnly(true);
        }
        if (beanAction == BeanAction.DELETE)
        {
            buttonsHL.add(delete);
        }
        buttonsHL.add(cancel);
        addFooterComponent(buttonsHL);
        userFL.add(usernameTF, passwordPF, roleCB, accessToConsumable, accessToRentable, accountBanned, isAdmin, maxCheckOut);
        
        setContent(libraryUserVL);
        
        boolean myaccount = LoginPage.getLoggedInUser().equalsIgnoreCase(bean.getUserEmail());
        
        boolean createdbyme = LoginPage.getLoggedInUser().equalsIgnoreCase(bean.getCreatedBy());
        
        if (myaccount)
        {
            setTitle("My Contact Information");
        }
        
        PersonRoleType myrole = LoginPage.getUserRoleType();
        
        // MultiSelectListBox<PersonRoleType> roleTypeMLB = new MultiSelectListBox<>();
        ListBox<PersonRoleType> roleTypeMLB = new ListBox<>();
        roleTypeMLB.setItems(EnumSet.allOf(PersonRoleType.class));
        
        roleTypeMLB.getElement().setAttribute("theme", "small");
        
        roleTypeMLB.setRenderer(new ComponentRenderer<Emphasis, PersonRoleType>(pc -> new Emphasis(pc.displayText())));
        if (myaccount)
        {
            roleTypeMLB.setValue(bean.getPersonRoleTypes());
            roleTypeMLB.setReadOnly(true);
        }
        else
        {
            if (bean.getPersonRoleTypes() == PersonRoleType.ROLE_SUPERADMIN)
            {
                if (createdbyme)
                {
                    binder.forField(roleTypeMLB).asRequired("Please choose a user role").bind("personRoleType");
                }
                else
                {
                    roleTypeMLB.setValue(bean.getPersonRoleTypes());
                    roleTypeMLB.setReadOnly(true);
                }
            }
            else
            {
                binder.forField(roleTypeMLB).asRequired("Please choose a user role").bind("personRoleType");
            }
        }
        addHeaderBComponent(roleTypeMLB);
        
        
        binder.forField(usernameTF).bind("userEmail");
        binder.forField(roleCB).bind("personRoleType");
        binder.forField(passwordPF).bind("password");
        binder.forField(accessToConsumable).bind("accessToConsumable");
        binder.forField(accessToRentable).bind("accessToRentable");
        binder.forField(accountBanned).bind("accountBanned");
        binder.forField(isAdmin).bind("isAdmin");
        binder.forField(maxCheckOut).bind("maxCheckOut");
        Binder.BindingBuilder<LibraryUser, String> libraryUserStringBindingBuilder = binder.forField(personLB).withConverter(new BinderPersonConverter());
        if (!beanAction.equals(BeanAction.NEW))
        {
            libraryUserStringBindingBuilder.withValidator(new UniqueUserValidator());
        }
        libraryUserStringBindingBuilder.bind("personId");
        
        binder.addStatusChangeListener(s ->
                                       {
                                           setDialogChangeStatus(true);
                                           if (usernameTF.getValue() == null || personLB.getValue() != null)
                                           {
                                               save.setEnabled(false);
                                               edit.setEnabled(false);
                                               //new CustomNotification("continue completing form details", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                
                                           }
                                           else
                                           {
                                               save.setEnabled(true);
                                               edit.setEnabled(true);
                                               getErrorDiv().setVisible(false);
                                               getErrorDiv().setText("");
                                               //new CustomNotification("Form is looking good", "primary", true, 1000, Notification.Position.TOP_CENTER).open();
                                           }
                                       }
        );
        personLB.setRenderer(new ComponentRenderer<Emphasis, Person>(a -> new Emphasis(a.getTitle() + ", " + a.getFullName())));
        
        AppConfiguration config = DataInitialization.loadDefaults();
        
        UserVerification usv = (UserVerification) gds.getRecordByEntityProperty("userEmail", bean.getUserEmail());
        
        if (usv == null)
        {
            usv = new UserVerification();
            usv.setOrganization(bean.getOrganization());
            usv.save(usv);
        }
        
        if (!beanAction.equals(BeanAction.NEW) && usv.getEmailAddressVerifiedDate() == null)
        {
            Span span = new Span("Email Address Not Verified.");
            span.getStyle().set("fontWeight", "600").set("color", "aliceblue").set("fontSize", "0.7em")
                .set("marginLeft", "1em").set("lineHeight", "1em");
            
            Div divy = new Div(new SVGIcon("alert-octagon-light.svg", "1em"), span);
            divy.getStyle().set("display", "flex").set("alignItems", "center");
            
            SmallButton adduserbtn = new SmallButton("Send Verification Email");
            adduserbtn.addClickListener(clk ->
                                        {
                                            NewUserPage.verifyUserEmail_SEND(bean);
                                        });
            adduserbtn.theme("primary");
            
            FlexMe flexm = new FlexMe(divy, adduserbtn);
            flexm.setWidth("100%");
            VerticalLayout vlay = new VerticalLayout(flexm);
            vlay.getStyle().set("background", "var(--lumo-shade-90pct)");
            
            setErrorDivContent(vlay);
            
            try
            {
                libraryUserClone = (LibraryUser) bean.clone();
                
                binder.readBean(bean);
                
                if (bean.getPerson() != null)
                {
                    personLB.setValue(bean.getPerson());
                }
            }
            catch (CloneNotSupportedException e)
            {
                e.printStackTrace();
            }
        }
        
        if (beanAction == BeanAction.EDIT)
        {
            boolean canedit = false;
            switch (myrole)
            {
                case ROLE_SUPERADMIN:
                
                case ROLE_ADMIN:
                    canedit = true;
                    break;
                
                default:
                    break;
            }
            binder.setReadOnly(!canedit);
            
            maxCheckOut.setReadOnly(!myrole.isSuperAdmin());
            if (myrole.isSuperAdmin())
            {
                if (beanAction != BeanAction.DELETE && beanAction != BeanAction.VIEW)
                {
                
                }
            }
            
            if (beanAction != BeanAction.VIEW)
            {
                
                SmallButton images = new SmallButton("Images").theme("contrast");
                images.addClickListener(a ->
                                        {
                                            new UploadImageDialog(bean.getUuid(), (bbb) ->
                                            {
                                                action.action(bean);
                                                return null;
                                            }, true).open();
                                        });
                addFooterComponent(images);
                
                addTerminalComponent(new SmallButton("Save").theme("primary").onclick(() ->
                                                                                      {
                                                                                          if (binder.validate().isOk())
                                                                                          {
                                                                                              if (binder.writeBeanIfValid(bean))
                                                                                              {
                                                                                                  bean.save(bean);
                                                                                                  if (action.action(bean))
                                                                                                  {
                                                                                                      close();
                                                                                                  }
                                                                                              }
                                                                                          }
                                                                                      }));
            }
            
            
            cancel.addClickListener(event ->
                                    {
                                        this.close();
                                    });
            
            clear.addClickListener(e ->
                                   {
                                       //readbean manually copies object to ui fields
                                       //writebean manually copies ui data to object
                                       binder.readBean(bean);
                                       new CustomNotification("Form Cleared", "info", true, 1000, Notification.Position.TOP_CENTER).open();
                                   });
            save.addClickListener((e) ->
                                  {
                                      try
                                      {
                                          synchronized (e)
                                          {
                                              binder.writeBean(bean);/*needed for validationException to be caught otherwise remove*/
                                              // System.out.println("Valid Bean: " + binder.getBean());
                        
                        
                                              bean.save(bean);
                                              System.out.println(bean.getClass().getSimpleName() + " saved successfully");
                        
                                              new CustomNotification(bean.getClass().getSimpleName() + " saved successfully", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                                              // execute the method (refresh data of calling interface) passed as parameter to the dialog here and if the return value is true close the dialog
                                              if (action.action(bean))
                                              {
                                                  close();
                                              }
                                          }
                    
                                      }
                                      catch (ValidationException ex)
                                      {
                                          String errors = ex.getValidationErrors().stream().map(ValidationResult::getErrorMessage).collect(Collectors.joining("\n"));
                                          System.out.println(errors);
                                          getErrorDiv().setVisible(true);
                                          getErrorDiv().setText(errors);
                                          new CustomNotification(errors, "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                                      }
                                  });
            edit.addClickListener((e) ->
                                  {
                                      synchronized (e)
                                      {
                                          try
                                          {
                                              binder.writeBean(bean);
                        
                                              LibraryUser a = bean.replaceEntity(libraryUserClone, bean);
                        
                                              if (a.equals(bean))
                                              {
                                                  if (action.action(bean))
                                                  {
                                                      close();
                                                      new CustomNotification(bean.getClass().getSimpleName() + " updated successfully", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                                                  }
                                              }
                                              else
                                              {
                                                  new CustomNotification(bean.getClass().getSimpleName() + " not updated", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                                              }
                                              // execute the method (refresh data of calling interface) passed as parameter to the dialog here and if the return value is true close the dialog
                        
                                          }
                                          catch (ValidationException ex)
                                          {
                                              String errors = ex.getValidationErrors().stream().map(ValidationResult::getErrorMessage).collect(Collectors.joining("\n"));
                                              System.out.println(errors);
                                              getErrorDiv().setVisible(true);
                                              getErrorDiv().setText(errors);
                                              new CustomNotification(errors, "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                                          }
                                          catch (Exception exc)
                                          {
                                              String errors = exc.getMessage();
                                              System.out.println(errors);
                                              exc.printStackTrace();
                                              getErrorDiv().setVisible(true);
                                              getErrorDiv().setText(errors);
                                              new CustomNotification(errors, "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                                          }
                                      }
                
                                  });
            personLB.addValueChangeListener(vc ->
                                            {
                                                Person person = vc.getValue();
                                                String personDet = person.getTitle() + " "
                                                        + person.getFullName() + ", Email: "
                                                        + person.getEmailAddress() + ", Phone: "
                                                        + person.getPhoneNumber();
                                                personH2.setText(personDet);
                                                setTitle(titleH3, personH2);
                                            });
            
        }
    }
    
    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent)
    {
        if (this.hasChanges())
        {
            BeforeLeaveEvent.ContinueNavigationAction action =
                    beforeLeaveEvent.postpone();
            if (getDialogChangeStatus())
            {
                new ConfirmDialog("Discard Changes",
                                  "Are you sure you want to leave this page", "Yes ",
                                  (e) ->
                                  {
                                      action.proceed();
                                  }, "No", (e) ->
                                  {
                                      e.getSource().close();
                                  }).open();
            }
        }
    }
    
    private boolean hasChanges()
    {
// no-op implementation
        return true;
    }
    
}
