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
import com.pc.weblibrarian.security.PasswordEncoder;
import com.pc.weblibrarian.utils.BinderPersonConverter;
import com.pc.weblibrarian.utils.CustomNullChecker;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ReadOnlyHasValue;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.EnumSet;
import java.util.stream.Collectors;


public class LibraryUserCRUDDIalog extends BasicDialog implements BeforeLeaveObserver
{
    
    // TODO > Beans >
    private LibraryUser libraryUserClone = new LibraryUser();
    private GenericDataService gds = new GenericDataService(new UserVerification());
    
    private BCryptPasswordEncoder bcryptPassEncoder = PasswordEncoder.getPasswordEncoder();
    
    
    // TODO > Data Provider
    PersonDP dp = new PersonDP();
    ConfigurableFilterDataProvider<Person, Void, String> fdp = dp.withConfigurableFilter();
    private final AppConfiguration config = DataInitialization.loadDefaults();
    
    
    // TODO > Components
    CustomTextField personSearch = new CustomTextField("", ValueChangeMode.EAGER, new Icon(VaadinIcon.SEARCH), true, "Search person", "small");
    Label fname = new Label("First Name");
    Label lname = new Label("Last Name");
    Label emailLabel = new Label("Username");
    
    ReadOnlyHasValue<String> email = new ReadOnlyHasValue<>(text -> emailLabel.setText(text));
    
    
    // CustomTextField usernameTF = new CustomTextField("Username", ValueChangeMode.LAZY, null, true, "", "small");
    PasswordField passwordPF = new PasswordField("Password");
    SmallButton changePasswordLink = new SmallButton("Change Password");
    
    CustomComboBox<PersonRoleType> roleCB = new CustomComboBox<>("Role", EnumSet.allOf(PersonRoleType.class), null, "2", false);
    Checkbox accessToConsumable = new Checkbox("Access to Consumable", false);
    Checkbox accessToRentable = new Checkbox("Access to Rentable", false);
    Checkbox accountBanned = new Checkbox("Is Account Banned", false);
    Checkbox accountLocked = new Checkbox("Is Account Locked", false);
    // Checkbox isAdmin = new Checkbox("Is Admin ", false);
    IntegerField maxCheckOut = new IntegerField("Max. Check Out");
    
    PasswordField oldPasswordPF = new PasswordField("Old Password");
    PasswordField newPasswordPF = new PasswordField("New Password");
    PasswordField confirmPasswordPF = new PasswordField("Confirm Password");
    SmallButton changePasswordBtn = new SmallButton("Change Password");
    
    private ListBox<Person> personLB = new ListBox<>();
    
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
    SmallButton newPersonBtn = new SmallButton("Add Person");
    
    // TODO > Layouts
    HorizontalLayout buttonsHL = new HorizontalLayout();
    HorizontalLayout passwordChangeHL = new HorizontalLayout(passwordPF, changePasswordLink);
    VerticalLayout changePasswordVL = new VerticalLayout();
    FormLayout userFL = new FormLayout();
    VerticalLayout libraryUserVL = new VerticalLayout(fname, lname, userFL, passwordChangeHL, changePasswordVL);
    HorizontalLayout personHL = new HorizontalLayout(personSearch, newPersonBtn);
    VerticalLayout personVL = new VerticalLayout(personHL, personLB);
    
    
    public LibraryUserCRUDDIalog(LibraryUser bean, BeanAction beanAction, BasicDialog.Action<LibraryUser> action, DialogSize dialogSize)
    {
        //  TODO > Styling (inside constructor)
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
        
        changePasswordBtn.addClassNames("btn", "btn-primary");
        
        
        // TODO > Positioning
        
        changePasswordVL.add(oldPasswordPF, newPasswordPF, confirmPasswordPF, changePasswordBtn);
        passwordChangeHL.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        changePasswordVL.setVisible(false);
        
        
        // TODO > beanAction component display setup
        if (beanAction == BeanAction.VIEW)
        {
            setTitle(new Label("Library User Details"));
            save.setVisible(false);
            edit.setVisible(false);
            clear.setVisible(false);
            delete.setVisible(false);
            
            personLB.setEnabled(false);
        }
        /*if (beanAction == BeanAction.NEW)
        {
            bean.setMaximumCheckoutItems(config.getMaxCheckoutItemsPerUser());
            
            // image.setVisible(false);
            buttonsHL.add(clear);
            buttonsHL.add(save);
            titleH3 = new H2("New Library User ");
            setTitle(titleH3);
            
        }*/
        if (beanAction == BeanAction.EDIT)
        {
            buttonsHL.add(clear);
            buttonsHL.add(edit);
            titleH3 = new H2("Edit Library User ");
            setTitle(titleH3);
            // usernameTF.setReadOnly(true);
        }
        if (beanAction == BeanAction.DELETE)
        {
            buttonsHL.add(delete);
        }
        buttonsHL.add(cancel);
        addFooterComponent(buttonsHL);
        
        
        userFL.add(emailLabel, passwordPF, roleCB, accessToConsumable, accessToRentable, accountBanned, accountLocked, maxCheckOut, personVL);
        
        setContent(libraryUserVL);
        
        boolean myaccount = CustomNullChecker.stringSafe(LoginPage.getLoggedInUser()).equalsIgnoreCase(bean.getUserEmail());
        
        boolean createdbyme = CustomNullChecker.stringSafe(LoginPage.getLoggedInUser()).equalsIgnoreCase(bean.getCreatedBy());
        
        PersonRoleType myrole = LoginPage.getUserRoleType();
        
        
        // MultiSelectListBox<PersonRoleType> roleTypeLB = new MultiSelectListBox<>();
        if (myaccount)
        {
            setTitle("My Contact Information");
            roleCB.setValue(bean.getPersonRoleTypes());
            roleCB.setReadOnly(true);
            
            if (!bean.getPersonRoleTypes().isSuperAdmin() || !bean.getPersonRoleTypes().isAdmin())
            {
                accessToConsumable.setEnabled(false);
                accessToRentable.setEnabled(false);
                accountBanned.setEnabled(false);
                roleCB.setEnabled(false);
                accountLocked.setEnabled(false);
                maxCheckOut.setEnabled(false);
            }
            else
            {
                accessToConsumable.setEnabled(true);
                accessToRentable.setEnabled(true);
                accountBanned.setEnabled(true);
                roleCB.setEnabled(true);
                accountLocked.setEnabled(true);
                maxCheckOut.setEnabled(true);
            }
        }
        else //not my account
        {
            if (bean.getPersonRoleTypes().isSuperAdmin() || bean.getPersonRoleTypes().isAdmin()) // superadmin
            {
                accessToConsumable.setEnabled(true);
                accessToRentable.setEnabled(true);
                accountBanned.setEnabled(true);
                roleCB.setEnabled(true);
                accountLocked.setEnabled(true);
                maxCheckOut.setEnabled(true);
                if (createdbyme)
                {
                    binder.forField(roleCB).asRequired("Please choose a user role").bind("personRoleType");
                    
                    roleCB.setReadOnly(false);
                }
                else
                {
                    roleCB.setReadOnly(true);
                }
            }
            else
            {
                binder.forField(roleCB).asRequired("Please choose a user role").bind("personRoleTypes");
                accessToConsumable.setEnabled(false);
                accessToRentable.setEnabled(false);
                accountBanned.setEnabled(false);
                roleCB.setEnabled(false);
                accountLocked.setEnabled(false);
                maxCheckOut.setEnabled(false);
            }
        }
        
        
        UserVerification usv = (UserVerification) gds.getRecordByEntityProperty("userEmail", bean.getUserEmail());
        
        if (usv == null)
        {
            usv = new UserVerification();
            usv.setOrganization(bean.getOrganization());
            usv.save(usv);
        }
        
        
        if (!beanAction.equals(BeanAction.NEW) && usv.getEmailAddressVerifiedDate() == null)
        {
            fname.setText(bean.getFirstName());
            lname.setText(bean.getLastName());
            passwordPF.setValue(VaadinSession.getCurrent().getAttribute("password").toString());
            passwordPF.setEnabled(false);
            
            email.setValue(bean.getUserEmail());
            
            Span span = new Span("Email Address Not Verified.");
            span.getStyle().set("fontWeight", "600").set("color", "aliceblue").set("fontSize", "0.7em")
            /* .set("marginLeft", "1em").set("lineHeight", "1em")*/;
            
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
            getErrorDiv().setVisible(true);
            
            // TODO > Clone beans and embeddedObjects
            
            try
            {
                libraryUserClone = (LibraryUser) bean.clone();
                
                
                if (bean.getPerson() != null)
                {
                    personLB.setValue(bean.getPerson());
                }
                else
                {
                    personLB.setEnabled(true);
                }
                binder.readBean(bean);
            }
            catch (CloneNotSupportedException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            fname.setText(bean.getFirstName());
            lname.setText(bean.getLastName());
            passwordPF.setValue(VaadinSession.getCurrent().getAttribute("password").toString());
            passwordPF.setEnabled(false);
            
            email.setValue(bean.getUserEmail());
            
            
            // TODO > Clone beans and embeddedObjects
            
            try
            {
                libraryUserClone = (LibraryUser) bean.clone();
                if (bean.getPerson() != null)
                {
                    personLB.setValue(bean.getPerson());
                }
                else
                {
                    personLB.setEnabled(true);
                }
                binder.readBean(bean);
            }
            catch (CloneNotSupportedException e)
            {
                e.printStackTrace();
            }
            getErrorDiv().setVisible(false);
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
            
            maxCheckOut.setEnabled(!myrole.isSuperAdmin());
            if (myrole.isSuperAdmin())
            {
                if (beanAction != BeanAction.DELETE && beanAction != BeanAction.VIEW)
                {
                    // admin actions for new and edit
                }
            }
            
            /*if (beanAction != BeanAction.VIEW)
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
                
                *//*addTerminalComponent(new SmallButton("Save").theme("primary").onclick(() ->
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
                                                                                      }));*//*
            }*/
        }
        
        
        // TODO > Binders
        
        newPasswordPF.setValueChangeMode(ValueChangeMode.LAZY);
        confirmPasswordPF.setValueChangeMode(ValueChangeMode.EAGER);
        oldPasswordPF.setValueChangeMode(ValueChangeMode.EAGER);
        
        binder.forField(email).bind(LibraryUser::getUserEmail, null);
        // binder.forField(roleTypeLB).bind("personRoleType"); //bound already above
        // binder.forField(passwordPF).bind("password");
        binder.forField(accessToConsumable).bind("accessToConsumable");
        binder.forField(accessToRentable).bind("accessToRentable");
        binder.forField(accountBanned).bind("accountBanned");
        binder.forField(accountLocked).bind("accountLocked");
        binder.forField(maxCheckOut).bind("maximumCheckoutItems");
        Binder.BindingBuilder<LibraryUser, String> libraryUserStringBindingBuilder = binder.forField(personLB).withConverter(new BinderPersonConverter());
        if (!beanAction.equals(BeanAction.NEW))
        {
            libraryUserStringBindingBuilder.withValidator(new UniqueUserValidator());
        }
        libraryUserStringBindingBuilder.bind("personId");
        
        newPersonBtn.addClickListener(event ->
                                      {
                                          //throw new CustomFileNotFoundException("Testing exception handling ");
                                          /*Country country = new Country("Nigeria", "ng");
                                          State state = new State("Lagos", "ng");
                                          Address ikeja = new Address("Ikeja", state, "100001", country, true, "No 1 Valley Close", AddressType.NONE);
                                          //ikeja = (Address) ikeja.save(ikeja);
                                          System.out.println("ikeja.getUuid() = " + ikeja.getUuid());
            
                                          ImageModel image;
                                          PersonCommand personCommand = new PersonCommand();
                                          try
                                          {
                                              image = ImageUtil.convertPathToImageModel(ImageUtil.FILE_PATH + "Asset 3.png");
//                                              System.out.println("image = " + Arrays.toString(image.getImageByteArray()));
                                              Person person = new Person(PersonTitleType.MR,
                                                                         PersonGenderType.MALE,
                                                                         "Nebu",
                                                                         "Wazobia",
                                                                         "Nebu",
                                                                         "nebu@wazobia.com",
                                                                         "080-3456-7890",
                                                                         Faker.instance().internet().url(),
                                                                         image,
                                                                         LocalDate.now(),
                                                                         Collections.singletonList(ikeja.getUuid()));
                                              personCommand.setPerson(person);
                                              personCommand.setAddresses(Collections.singletonList(ikeja));
                                          }
                                          catch (IOException e)
                                          {
                                              e.printStackTrace();
                                          }*/
                                          Person person = new Person();
                                          person.setFirstName(bean.getFirstName());
                                          person.setLastName(bean.getLastName());
                                          person.setEmailAddress(bean.getUserEmail());
                                          new PersonCRUDDialog(person, BasicDialog.BeanAction.NEW, d ->
                                          {
                                              //UI.getCurrent().getPage().reload();
                                              return true;
                                          }).open();
                                      });
        
        binder.addStatusChangeListener(s ->
                                       {
                                           setDialogChangeStatus(true);
                                           save.setEnabled(true);
                                           edit.setEnabled(true);
                                           getErrorDiv().setVisible(false);
                                           getErrorDiv().setText("");
                                       }
        );
        
        // TODO > Initializing beans for testing and beanAction data setup. Commented after testing
        
        // TODO > Binder (readbean manually copies object to ui fields. bean>ui, writebean manually copies ui data to object. ui>bean)
        
        
        // TODO > Configure Data Components. Form, Grid, List, Radio Button, ComboBox, Renderers
        personLB.setRenderer(new ComponentRenderer<Emphasis, Person>(a -> new Emphasis(a.getTitle() + ", " + a.getFullName())));
        
        
        // TODO >  Event Handlers
        
        changePasswordLink.addClickListener(d ->
                                            {
                                                if (changePasswordVL.isVisible())
                                                {
                                                    changePasswordVL.setVisible(false);
                                                }
                                                else
                                                {
                                                    changePasswordVL.setVisible(true);
                                                }
                                            });
        
        
        oldPasswordPF.addValueChangeListener(s ->
                                             {
                                                 if (PasswordEncoder.getPasswordEncoder().matches(s.getValue(), bean.getHashedPassword()))
                                                 {
                                                     newPasswordPF.setEnabled(true);
                                                     confirmPasswordPF.setEnabled(true);
                                                 }
                                                 else
                                                 {
                                                     newPasswordPF.setValue("");
                                                     confirmPasswordPF.setValue("");
                                                     newPasswordPF.setEnabled(false);
                                                     confirmPasswordPF.setEnabled(false);
                                                 }
                                             });
        confirmPasswordPF.addValueChangeListener(d ->
                                                 {
                                                     if (!confirmPasswordPF.getValue().equals(newPasswordPF.getValue()))
                                                     {
                                                         changePasswordBtn.setEnabled(false);
                                                         new CustomNotification("Passwords do not match", "alert-danger", true, 3000, Notification.Position.TOP_CENTER).open();
                                                     }
                                                     else
                                                     {
                                                         changePasswordBtn.setEnabled(true);
                                                     }
                                                 });
        changePasswordBtn.addClickListener(click ->
                                           {
                                               bean.setPassword(confirmPasswordPF.getValue());
                                               bean.setHashedPassword(bcryptPassEncoder.encode(bean.getPassword()));
                                               VaadinSession.getCurrent().setAttribute("password", bean.getPassword());
            
                                               bean.replaceEntity(bean, bean);
                                               new CustomNotification("Password Changed", "alert-success", true, 3000, Notification.Position.TOP_CENTER).open();
            
                                               changePasswordVL.setVisible(false);
                                           });
        
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
                                          if (personLB.getValue() != null)
                                          {
                                              binder.writeBean(bean);/*needed for validationException to be caught otherwise remove*/
                                              bean.setPerson(personLB.getValue());
                                              bean.setFirstName(bean.getPerson().getFirstName());
                                              bean.setLastName(bean.getPerson().getLastName());
                                              bean.setPassword(VaadinSession.getCurrent().getAttribute("password").toString());
                                              bean.setHashedPassword(bcryptPassEncoder.encode(VaadinSession.getCurrent().getAttribute("password").toString()));
                                              bean.setPersonId(bean.getPerson().getUuid());
                        
                                              bean.save(bean);
                                              System.out.println(bean.getClass().getSimpleName() + " saved successfully");
                        
                                              new CustomNotification(bean.getClass().getSimpleName() + " saved successfully", "alert-success", true, 1000, Notification.Position.TOP_CENTER).open();
                                              // execute the method (refresh data of calling interface) passed as parameter to the dialog here and if the return value is true close the dialog
                                              if (action.action(bean))
                                              {
                                                  close();
                                              }
                                          }
                                          else
                                          {
                                              new CustomNotification("Select person", "alert alert-danger", true, 3000, Notification.Position.TOP_CENTER).open();
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
                                          if (personLB.getValue() != null)
                                          {
                                              binder.writeBean(bean);/*needed for validationException to be caught otherwise remove*/
                                              bean.setPerson(personLB.getValue());
                                              bean.setFirstName(bean.getPerson().getFirstName());
                                              bean.setLastName(bean.getPerson().getLastName());
                                              bean.setPassword(VaadinSession.getCurrent().getAttribute("password").toString());
                                              bean.setHashedPassword(bcryptPassEncoder.encode(VaadinSession.getCurrent().getAttribute("password").toString()));
                                              bean.setPersonId(bean.getPerson().getUuid());
                        
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
                                          }
                                          else
                                          {
                                              new CustomNotification("Select person", "alert-danger", true, 3000, Notification.Position.TOP_CENTER).open();
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
        
        personSearch.addValueChangeListener(vc ->
                                            {
                                                fdp.setFilter(vc.getValue());
                                                fdp.refreshAll();
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
            
                                            fname.setText(person.getFirstName());
                                            lname.setText(person.getLastName());
                                        });
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
