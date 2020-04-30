package com.pc.weblibrarian.views.dialogs;

import com.pc.weblibrarian.customcomponents.CustomComboBox;
import com.pc.weblibrarian.customcomponents.CustomNotification;
import com.pc.weblibrarian.customcomponents.CustomTextField;
import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.dataproviders.GetObjectByID;
import com.pc.weblibrarian.entity.Address;
import com.pc.weblibrarian.entity.Person;
import com.pc.weblibrarian.enums.AddressType;
import com.pc.weblibrarian.enums.PersonGenderType;
import com.pc.weblibrarian.enums.PersonTitleType;
import com.pc.weblibrarian.model.Country;
import com.pc.weblibrarian.model.ImageModel;
import com.pc.weblibrarian.model.State;
import com.pc.weblibrarian.utils.GetCountriesAndStates;
import com.pc.weblibrarian.utils.ImageUtil;
import com.pc.weblibrarian.views.BasicDialog;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Emphasis;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.DateRangeValidator;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.StreamResource;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class PersonCRUDDialog extends BasicDialog
{
    private Address addressBean = new Address();
    private Address addressBean1;
    
    Person pcbean1;
    
    public static GenericDataService gds;
    
    
    Address a = new Address();
    
    // TODO > Local Variables
    public PersonCRUDDialog(Person pcbean, BasicDialog.BeanAction beanAction, BasicDialog.Action basicDialogAction)
    {
        super(beanAction, DialogSize.XLARGE);
        // TODO > Data providers
        //get addresses of person
        /*GenericDataService gds = new GenericDataService(pcbean);
        Map<Person, List<Address>> recordAndEmbeddedObjectList = gds.getRecordAndEmbeddedObjectList("Person", "Address", "_id", pcbean.getUuid(), "_id", "addresses");*/
        
        pcbean.setAddressObjects(pcbean.getAddressIds().stream().map(c -> GetObjectByID.getObjectById(c, Connection.addresses)).collect(Collectors.toList()));
        DataProvider addressDP = DataProvider.ofCollection(pcbean.getAddressObjects());
        ConfigurableFilterDataProvider<Address, Void, String> fdp = addressDP.withConfigurableFilter();
        
        /*// TODO > Labels
        if (beanAction == BeanAction.NEW)
        {
            setTitle(new Label("New Person"));
        }
        else
        {
            setTitle(new Label("Edit Person"));
        }*/
        
        // TODO > Layouts
        FormLayout addressFL = new FormLayout();
        FormLayout personDetailsFL = new FormLayout();
        HorizontalLayout outerLayoutHL = new HorizontalLayout(personDetailsFL, addressFL);
        setContent(outerLayoutHL);
        
        HorizontalLayout saveCancelHL = new HorizontalLayout();
        
        // TODO > Input Fields and Buttons (Form Components)
        AtomicReference<Image> image = new AtomicReference<>();
        
        //addresses = pcbean.getAddressObjects();
        addressBean1 = new Address();
        if (pcbean.getAddressIds().size() > 0 && beanAction != BeanAction.NEW)
        {
            addressBean = GetObjectByID.getObjectById(Objects.requireNonNull(pcbean.getAddressIds().get(0)), Connection.addresses);
        }
        
        try
        {
            pcbean1 = (Person) Objects.requireNonNull(pcbean).clone();
            if (addressBean != null)
            {
                addressBean1 = (Address) Objects.requireNonNull(addressBean).clone();
            }
        }
        catch (CloneNotSupportedException e)
        {
            e.getMessage();
        }
        
        AtomicReference<ImageModel> imageModel = new AtomicReference<>(pcbean.getImage());
        StreamResource resource;
        try
        {
            if (imageModel.get().getImageByteArray().length == 0 || imageModel.get().getImageByteArray() == null)
            {//set Dummy image
                imageModel.set(ImageUtil.convertPathToImageModel(ImageUtil.IMAGE_FILE));
                resource = new StreamResource(imageModel.get().getFileName(), () -> new ByteArrayInputStream(imageModel.get().getImageByteArray()));
            }
            
            else
            {//set provided image
                resource = new StreamResource(imageModel.get().getFileName(), () -> new ByteArrayInputStream(imageModel.get().getImageByteArray()));
            }
            image = new AtomicReference<>(new Image(resource, imageModel.get().getFileName()));
            image.get().setClassName("img-fluid");
            image.get().setTitle("Passport");
            
        }
        catch (IOException e)
        {
            new Notification("Cannot access file " + e.getMessage(), 2000, Notification.Position.MIDDLE).open();
        }
        
        CustomTextField fNameTF = new CustomTextField("First Name", ValueChangeMode.LAZY, null, false, "First Name", "medium");
        CustomTextField lNameTF = new CustomTextField("Last Name", ValueChangeMode.LAZY, null, false, "Last Name", "medium");
        CustomTextField akaTF = new CustomTextField("AKA", ValueChangeMode.LAZY, null, false, "Aka", "medium");
        CustomTextField emailTF = new CustomTextField("Email", ValueChangeMode.LAZY, null, false, "email@gmail.com", "medium");
        CustomTextField phoneTF = new CustomTextField("Phone", ValueChangeMode.LAZY, null, false, "123-4556-7889", "medium");
        CustomTextField websiteTF = new CustomTextField("Website", ValueChangeMode.LAZY, null, false, "www.website.com", "medium");
        CustomTextField zipCodeTF = new CustomTextField("Zipcode", ValueChangeMode.LAZY, null, false, "100001", "medium");
        DatePicker DOB = new DatePicker("Date of Birth");
        ListBox<Address> addressLB = new ListBox<>();
//        CustomUpload imageURLFU = new CustomUpload();
        
        Country country = new Country("Nigeria", "ng");
        State state = new State("Lagos", "ng");
        
        ComboBox<Country> countryCombo = new ComboBox<>();
        countryCombo.setItems(GetCountriesAndStates.getCountries());
        countryCombo.setLabel("Country");
        countryCombo.getStyle().set("colspan", "2");
        countryCombo.setClearButtonVisible(false);
        countryCombo.setValue(country);
        countryCombo.setOpened(false);
        
        String countryCode = (countryCombo.getValue()).getCode();

//        CustomComboBox<State> stateCombo = new CustomComboBox<State>("State", GetCountriesAndStates.getStates(countryCode), State::getRegion, "2", false);
        ComboBox<State> stateCombo = new ComboBox<>();
        stateCombo.setItems(GetCountriesAndStates.getStates(countryCode));
        stateCombo.setLabel("State");
        countryCombo.getStyle().set("colspan", "2");
        stateCombo.setClearButtonVisible(false);
        stateCombo.setValue(state);
        stateCombo.setAllowCustomValue(false);
        stateCombo.setOpened(false);
        
        stateCombo.setItemLabelGenerator(State::getRegion);
        countryCombo.setItemLabelGenerator(Country::getName);
        
        CustomComboBox<PersonTitleType> titleCombo = new CustomComboBox<>("Title", EnumSet.allOf(PersonTitleType.class), PersonTitleType::getDisplayText, "2", false);
        CustomTextField streetAddrTF = new CustomTextField("Street Address", ValueChangeMode.LAZY, null, false, "eg. No 1, John Lane", "medium");
        CustomTextField cityTF = new CustomTextField("City", ValueChangeMode.LAZY, null, false, "eg. London", "medium");
        
        CustomComboBox<AddressType> addressCombo = new CustomComboBox<>("Address  Type", EnumSet.allOf(AddressType.class), AddressType::getDisplayText, "2", false);
        Checkbox primaryAddress = new Checkbox("Primary Address", true);
        RadioButtonGroup<PersonGenderType> gender = new RadioButtonGroup<>();
        gender.setItems(PersonGenderType.values());
        gender.setValue(PersonGenderType.MALE);
        
        SmallButton save = new SmallButton("Save");
        save.theme("primary");
        
        SmallButton edit = new SmallButton("Edit");
        edit.theme("primary");
        
        SmallButton cancel = new SmallButton("Cancel");
        SmallButton clear = new SmallButton("Clear");
        SmallButton delete = new SmallButton("Delete");
        delete.theme("error");
        
        SmallButton imageButton = new SmallButton("Image");
//                imageButton.theme("contrast");
        imageButton.theme(ButtonVariant.LUMO_SMALL.getVariantName());
        
        // TODO > Binder, Validators, Converters (readbean manually copies object to ui fields. bean>ui, writebean manually copies ui data to object. ui>bean)
        Binder<Person> binder = new Binder<>(Person.class, true);
        Binder<Address> addressBinder = new Binder<>(Address.class, true);
        
        if (beanAction != BeanAction.NEW)
        {
            binder.setBean(pcbean);
        }
        
        binder.setReadOnly(beanAction == BeanAction.DELETE || beanAction == BeanAction.VIEW);
        addressBinder.setReadOnly(beanAction == BeanAction.DELETE || beanAction == BeanAction.VIEW);
        
        // TODO > Set binder (Confirm that initial bean values contain data for components that have data eg ComboBox before calling binder.setBean())
        //addressBinder.setBean(addressBean);
        
        binder.forField(titleCombo).bind("title");
        binder.forField(DOB).withValidator(new DateRangeValidator("Enter a reasonable birthdate", LocalDate.of(1850, 01, 01), LocalDate.of(2020, 01, 01)));
        binder.withValidator(name -> fNameTF.getValue().length() >= 3, "First name must contain at least three characters").forField(fNameTF).asRequired("First name cannot be blank").bind("firstName");
        binder.withValidator(name -> lNameTF.getValue().length() >= 3, "Last name must contain at least three characters").forField(lNameTF).asRequired("Last name cannot be blank").bind("lastName");
        binder.forField(gender).bind("gender");
        binder.forField(akaTF).bind("aka");
        binder.forField(emailTF).withValidator(new EmailValidator("Enter a valid email")).bind("emailAddress");
        binder.forField(phoneTF).bind("phoneNumber");
        binder.forField(DOB).bind("dateOfBirth");
        binder.forField(websiteTF).bind("website");
        
        
        /*Binder.BindingBuilder<Person, List<String>> binderBuilder = binder.forField(addressLB).withConverter(new BinderAddressConverter());
        binderBuilder.bind(null, null);*/
        
        addressBinder.forField(streetAddrTF).bind("streetAddressLine");
        addressBinder.forField(cityTF).bind("city");
        addressBinder.forField(stateCombo).bind("state");
        addressBinder.forField(countryCombo).bind("country");
        addressBinder.forField(zipCodeTF).bind("zipCode");
        addressBinder.forField(addressCombo).bind("addressType");
        addressBinder.forField(primaryAddress).bind("primaryAddress");
        
        binder.addStatusChangeListener(event ->
                                       {
            
                                           if (fNameTF.getValue().equals("") || lNameTF.getValue().equals(""))
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
            
                                       });
        
        // TODO> Display Context Setup, Field Positioning and Component Placement
        personDetailsFL.add(titleCombo, fNameTF, lNameTF, gender, akaTF, emailTF, phoneTF, websiteTF, DOB, imageButton);
        addressFL.add(image.get(), streetAddrTF, cityTF, countryCombo, stateCombo, addressCombo, zipCodeTF, primaryAddress, addressLB);
        
        if (fNameTF.getValue().equals("") || lNameTF.getValue().equals(""))
        {
            save.setEnabled(false);
            edit.setEnabled(false);
        }
        else
        {
            save.setEnabled(true);
            edit.setEnabled(true);
        }
        
        
        if (beanAction != BeanAction.VIEW)
        {
            addressLB.setEnabled(true);
            if (beanAction != BeanAction.DELETE)
            {
                if (beanAction == BeanAction.NEW)
                {
                    setTitle(new Label("New Person"));
                    saveCancelHL.add(save);
                    
                    save.setVisible(true);
                    edit.setVisible(false);
                    
                    addressLB.setEnabled(true);
                }
                if (beanAction == BeanAction.EDIT)
                {
                    setTitle(new Label("Edit Person"));
                    saveCancelHL.add(edit);
                    
                    save.setVisible(false);
                    edit.setVisible(true);
                    
                    addressLB.setEnabled(true);
                }
            }
            if ((beanAction == BeanAction.NEW || beanAction == BeanAction.EDIT))
            {
                saveCancelHL.add(clear);
                clear.setVisible(true);
                addressLB.setEnabled(true);
            }
            
            if (beanAction == BeanAction.DELETE)
            {
                saveCancelHL.add(delete);
                clear.setVisible(false);
                addressLB.setEnabled(false);
                
            }
        }
        else
        {
            setTitle(new Label("Person Details"));
            save.setVisible(false);
            edit.setVisible(false);
            clear.setVisible(false);
            delete.setVisible(false);
            
            addressLB.setEnabled(false);
        }
        
        saveCancelHL.add(save, cancel);
        addFooterComponent(saveCancelHL);
        
        
        // TODO > Data Components. Grid, List
        addressLB.setDataProvider(fdp);
        addressLB.getElement().getStyle().set("size", "4px");
        addressLB.getElement().getStyle().set("border", "1px solid ivory");
        addressLB.setMaxHeight("150px");
        addressLB.setHeight("150px");
        addressLB.setWidthFull();
        addressLB.setEnabled(true);
        
        addressLB.addValueChangeListener(event -> addressBinder.setBean(event.getValue()));
        
        // TODO >  Event Handlers
        countryCombo.addValueChangeListener(event ->
                                            {
                                                String selectedCountryCode = (countryCombo.getValue()).getCode();
                                                List<State> ss = GetCountriesAndStates.getStates(selectedCountryCode);
                                                stateCombo.setItems(ss);
                                                State state1 = ss.get(ss.size() % 2);
                                                stateCombo.setValue(state1);
                                            });
        save.addClickListener(event ->
                              {
                                  try
                                  {
                                      binder.writeBean(pcbean);
                                      addressBinder.writeBean(addressBean);
                                      pcbean.setAddressIds(List.of(addressBean.getUuid()));
                                      /*List<Address> addresses = addressLB.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
                
                                      addresses.forEach(f ->
                                                        {
                                                            if (addressBean.getUuid().equals(f.getUuid()))
                                                            {
                                                                index.set(addresses.indexOf(f));
                                                            }
                                                        });
                                      if (index.get() == -1)
                                      {
                                          addresses.add(addressBean);
                                      }
                                      else
                                      {
                                          addresses.set(index.get(), addressBean);
                                      }
                
                                      List<String> addressIds = addresses.stream().map(Address::getUuid).collect(Collectors.toList());
                                      pcbean.setAddressObjects(addresses);
                                      pcbean.setAddressIds(addressIds);*/
                
                                      addressBean.save(addressBean);
                                      pcbean.save(pcbean);
                                      new CustomNotification(pcbean.getClass().getSimpleName() + " updated successfully", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                
                                      // execute the method (refresh data of calling interface) passed as parameter to the dialog here and if the return value is true close the dialog
                                      if (basicDialogAction.action(pcbean))
                                      {
                                          close();
                                      }
                                  }
                                  catch (ValidationException e)
                                  {
                                      String errors = e.getValidationErrors().stream().map(ValidationResult::getErrorMessage).collect(Collectors.joining("\n"));
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
                              });
        edit.addClickListener((e) ->
                              {
                                  try
                                  {
                                      binder.writeBean(pcbean);
                                      addressBinder.writeBean(addressBean);
                
                                      List<String> addressIds = pcbean.getAddressIds();
                
                                      if (addressIds.size() > 0)
                                      {
                                          if (addressIds.contains(addressBean.getUuid()))
                                          {
                                              a = addressBean.replaceEntity(addressBean1, addressBean);
                                          }
                                          else
                                          {
                                              Address objectById = GetObjectByID.getObjectById(addressBean.getUuid(), Connection.addresses);
                                              if (objectById == null)
                                              {
                                                  a = (Address) addressBean.save(addressBean);
                                                  addressIds.add(a.getUuid());
                                              }
                                              else
                                              {
                                                  throw new Exception("Address already exists");
                                              }
                                          }
                                      }
                                      else
                                      {
                                          Address objectById = GetObjectByID.getObjectById(addressBean.getUuid(), Connection.addresses);
                                          if (objectById == null)
                                          {
                                              a = (Address) addressBean.save(addressBean);
                                              addressIds.add(a.getUuid());
                                          }
                                          else
                                          {
                                              throw new Exception("Address already exists");
                                          }
                                      }
                                      pcbean.setAddressIds(addressIds);
                
                                      //Address a = (Address) addressBean.replaceEntity(addressBean1, addressBean);
                                      Person pp = pcbean.replaceEntity(pcbean1, pcbean);
                
                                      Div message;
                                      if (a.equals(addressBean) && pp.equals(pcbean))
                                      {
                                          System.out.println(pcbean.getClass().getSimpleName() + " updated successfully");
                                          new CustomNotification(pcbean.getClass().getSimpleName() + " updated successfully", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                    
                    
                                      }
                                      else
                                      {
                                          new CustomNotification(pcbean.getClass().getSimpleName() + " not updated", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                    
                                      }
                                      // execute the method (refresh data of calling interface) passed as parameter to the dialog here and if the return value is true close the dialog
                                      if (basicDialogAction.action(pcbean))
                                      {
                                          close();
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
                                  catch (Exception exc)
                                  {
                                      String errors = exc.getMessage();
                                      System.out.println(errors);
                                      exc.printStackTrace();
                                      getErrorDiv().setVisible(true);
                                      getErrorDiv().setText(errors);
                                      new CustomNotification(errors, "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                                  }
                              });
        
        cancel.addClickListener(event ->
                                {
                                    this.close();
                                });
        clear.addClickListener(e ->
                               {
                                   //readbean manually copies object to ui fields
                                   //writebean manually copies ui data to object
//                                   binder.readBean(bean);
                                   binder.readBean(pcbean1);
                                   addressBinder.readBean(addressBean);
            
                                   new CustomNotification("Form Cleared", "info", true, 1000, Notification.Position.TOP_CENTER).open();
                               });
        
        AtomicReference<Image> finalImage = image;
        imageButton.addClickListener(a ->
                                     {
                                         new UploadImageDialog(pcbean.getUuid(), (bbb) ->
                                         {
                                             StreamResource rResource = new StreamResource(bbb.getFileName(), () -> new ByteArrayInputStream(bbb.getImageByteArray()));
                                             finalImage.get().setSrc(rResource);
                
                                             pcbean.setImage(bbb);
                
                                             return new Binary(BsonBinarySubType.BINARY, pcbean.getImage().getImageByteArray());
                
                                         }, true).open();
                                     });
        
        // TODO > Label configurations
        addressLB.setRenderer(new ComponentRenderer<HorizontalLayout, Address>(address ->
                                                                               {
                                                                                   HorizontalLayout hl = new HorizontalLayout();
                                                                                   if (address != null)
                                                                                   {
                                                                                       hl.add(new Emphasis(address.getStreetAddressLine() + ", " +
                                                                                                                   address.getCity() + ", " +
                                                                                                                   address.getZipCode() + ", " +
                                                                                                                   address.getState().getRegion() + ", " +
                                                                                                                   address.getCountry().getName()));
                                                                                   }
                                                                                   return hl;
                                                                               }));

//        addressLB.setRenderer(new ComponentRenderer<>(ps -> new Emphasis(ps.getStreetAddressLine() + " " + ps.getCity() + " " + ps.getCountry())));
    
    
    }
    
    
}
