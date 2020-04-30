package com.pc.weblibrarian.views.dialogs;

import com.pc.weblibrarian.customcomponents.CustomNotification;
import com.pc.weblibrarian.customcomponents.CustomTextField;
import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.PersonDP;
import com.pc.weblibrarian.entity.Author;
import com.pc.weblibrarian.entity.Person;
import com.pc.weblibrarian.utils.BinderPersonConverter;
import com.pc.weblibrarian.utils.UniqueAuthorValidator;
import com.pc.weblibrarian.views.BasicDialog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Emphasis;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.stream.Collectors;

// TODO > Data providers
// TODO > Local Variables
// TODO > Layouts
// TODO > Labels
// TODO > Input Fields
// TODO > Data Components. Grid, List
// TODO > Positioning
// TODO > Buttons
// TODO > Binder
// TODO > Component Placement
// TODO >  Event Handlers
// TODO > Label configurations

// @Controller
public class AuthorsCRUDDialog extends BasicDialog
{
    // TODO > Data providers
//        PersonListBoxDP dp = new PersonListBoxDP();
    
    PersonDP dp = new PersonDP();
    ConfigurableFilterDataProvider<Person, Void, String> fdp = dp.withConfigurableFilter();
    
    // TODO > Local Variables
    final Person[] p = {new Person()};
    
    // TODO > Layouts
    FormLayout formLayout = new FormLayout();
    HorizontalLayout newPersonHL = new HorizontalLayout();
    HorizontalLayout personAuthorHL = new HorizontalLayout();
    HorizontalLayout buttonsHL = new HorizontalLayout();
    VerticalLayout personVL = new VerticalLayout();
    VerticalLayout authorVL = new VerticalLayout();
    
    // TODO > Labels
    private Label titleH3 = new Label();
    private Label personH2 = new Label();
    
    // TODO > Input Fields
    CustomTextField personSearch = new CustomTextField("", ValueChangeMode.EAGER, new Icon(VaadinIcon.SEARCH), true, "Search Person", "small");
    CustomTextField wikiLinkTF = new CustomTextField("Wikilink", ValueChangeMode.EAGER, null, false, "Wikilink", "medium");
    CustomTextField biograghyTF = new CustomTextField("Biography", ValueChangeMode.EAGER, null, false, "Biography", "medium");
    ListBox<Person> personsLB = new ListBox<>();
    
    Author beann = null;
    
    public AuthorsCRUDDialog(Author bean, BasicDialog.BeanAction beanAction, BasicDialog.Action<Author> action, DialogSize dialogSize)
    {
        super(beanAction, dialogSize);
        
        titleH3.addClassName("mr-2");
        personH2.addClassName("ml-5");
        
        // TODO > Data Components. Grid, List
        personsLB.setDataProvider(fdp);
        personsLB.getElement().getStyle().set("size", "4px");
        personsLB.getElement().getStyle().set("border", "1px solid ivory");
        personsLB.setMaxHeight("150px");
        personsLB.setHeight("150px");
        personsLB.setWidthFull();
        
        personsLB.setRenderer(new ComponentRenderer<Emphasis, Person>(pc -> new Emphasis(pc.getTitle() + " " + pc.getFirstName() + " " + pc.getLastName())));
        
        personH2.addClassName("font-size-s");
        personH2.addClassName("text-primary");
        
        // TODO > Positioning
        personVL.setSizeFull();
        authorVL.setSizeFull();
        
        // TODO > Buttons
        SmallButton newPersonBtn = new SmallButton("Add Person");
        SmallButton save = new SmallButton("Save");
        save.theme("primary");
        save.setEnabled(false);
        
        SmallButton edit = new SmallButton("Edit");
        edit.theme("primary");
        edit.setEnabled(false);
        
        SmallButton cancel = new SmallButton("Cancel");
        SmallButton clear = new SmallButton("Clear");
        SmallButton delete = new SmallButton("Delete");
        delete.theme("error");
        delete.theme(ButtonVariant.LUMO_ERROR.getVariantName());
        
        // TODO > Binder (readbean manually copies object to ui fields. bean>ui, writebean manually copies ui data to object. ui>bean)
        Binder<Author> binder = new Binder<>(Author.class, true/* scans nested objects to bind as well*/);
        
        try
        {
            beann = (Author) bean.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.getMessage();
        }
        
        binder.setReadOnly(beanAction == BeanAction.DELETE || beanAction == BeanAction.VIEW);
        binder.forField(wikiLinkTF).asRequired().bind(Author::getWikiLink, Author::setWikiLink);
        binder.forField(biograghyTF).asRequired().bind(Author::getBiography, Author::setBiography);
        
        Binder.BindingBuilder<Author, String> binderBuilder = binder.forField(personsLB).withConverter(new BinderPersonConverter());
        if (beanAction == BeanAction.NEW)
        {
            binderBuilder.withValidator(new UniqueAuthorValidator());
        }
        
        binderBuilder.bind(Author::getPersonId, Author::setPersonId);
        try
        {
            if (beanAction != BeanAction.NEW)
            {
                personsLB.setValue(bean.getPerson());
                binder.readBean(bean);
            }
        }
        catch (IllegalArgumentException e)
        {
            e.getMessage();
        }
        
        //bind methods first argument is a ValueProvider of class AuthorCommand and type string
        binder.addStatusChangeListener(event ->
                                       {
                                           if (personsLB.getValue() != null)
                                           {
                                               if (wikiLinkTF.getValue().equals("") || biograghyTF.getValue().equals(""))
                                               {
                                                   /*getErrorDiv().setVisible(true);
                                                   getErrorDiv().getStyle().set("align", "center");
                                                   getErrorDiv().setText("Completing form details");*/
                                                   save.setEnabled(false);
                                                   edit.setEnabled(false);
                                                   new CustomNotification("Completing form details", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                    
                                               }
                                               else
                                               {
                                                   getErrorDiv().setVisible(false);
                                                   getErrorDiv().setText("");
                                                   save.setEnabled(true);
                                                   edit.setEnabled(true);
                                                   new CustomNotification("Form is looking good", "primary", true, 1000, Notification.Position.TOP_CENTER).open();
                                               }
                                           }
                                           else
                                           {
                                               //new CustomNotification("Select person", "primary", true, 3000, Notification.Position.TOP_CENTER).open();
                                               new CustomNotification("Select an author", "btn-outline-danger", false, 3000, Notification.Position.TOP_CENTER).open();
                                           }
                                       });
        
        if (beanAction != BeanAction.VIEW)
        {
            if (beanAction != BeanAction.DELETE)
            {
                if (beanAction == BeanAction.NEW)
                {
                    buttonsHL.add(save);
                    titleH3 = new Label("New Author ");
                    setTitle(titleH3);
                }
                if (beanAction == BeanAction.EDIT)
                {
                    buttonsHL.add(edit);
                    titleH3 = new Label("Edit Author ");
                    setTitle(titleH3);
                }
            }
            if ((beanAction == BeanAction.NEW || beanAction == BeanAction.EDIT))
            {
                buttonsHL.add(clear);
//                addFooterComponent(clear);
            }
            
            if (beanAction == BeanAction.DELETE)
            {
                buttonsHL.add(delete);
//                addFooterComponent(delete);
            }
        }
        else
        {
            setTitle(new Label("Author Details"));
            save.setVisible(false);
            edit.setVisible(false);
            clear.setVisible(false);
            delete.setVisible(false);
            
            personsLB.setEnabled(false);
        }
        
        
        // TODO > Component Placement
        newPersonHL.add(personSearch, newPersonBtn);
        personVL.add(newPersonHL, personsLB);
        authorVL.add(wikiLinkTF, biograghyTF);
        buttonsHL.add(cancel);
        addFooterComponent(buttonsHL);
        personAuthorHL.add(personVL, authorVL);
        formLayout.add(personAuthorHL);
        setContent(formLayout);
        
        // TODO >  Event Handlers
        cancel.addClickListener(event ->
                                {
                                    this.close();
                                });
        
        delete.addClickListener(event ->
                                {
                                    new CustomNotification("Delete Clicked", "", true, 1000, Notification.Position.TOP_CENTER).open();
                                    String fullname = p[0].getFirstName() + " " + p[0].getLastName();
            
                                    new ConfirmDialog("Delete Author",
                                                      String.format("Are you sure you want to delete %s?", fullname), "Yes " + BasicDialog.BeanAction.DELETE.displayText(),
                                                      (e) ->
                                                      {
                                                          bean.delete(Connection.DB_AUTHOR, bean);
                                                          System.out.printf("Deleted Person: %s\n", fullname);
                                                          System.out.println("Author deleted successfully");
                                                          new CustomNotification("Author deleted successfully", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                
                                                          e.getSource().close();
                                                      }, "Do not delete", (e) ->
                                                      {
                                                          new CustomNotification("Author not deleted", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                                                          e.getSource().close();
                                                      }).open();
                                });
        clear.addClickListener(e ->
                               {
                                   //readbean manually copies object to ui fields
                                   //writebean manually copies ui data to object
//                                   binder.readBean(bean);
                                   binder.readBean(bean);
                                   new CustomNotification("Form Cleared", "info", true, 1000, Notification.Position.TOP_CENTER).open();
                               });
        save.addClickListener((e) ->
                              {
                                  try
                                  {
                                      binder.writeBean(bean);/*needed for validationException to be caught otherwise remove*/
                                      System.out.println("Valid Bean: " + binder.getBean());
                                      //copies values from the fields to the object passed as parameter
                                      bean.save(bean);
                                      System.out.println(bean.getClass().getSimpleName() + " saved successfully");
                                      new CustomNotification(bean.getClass().getSimpleName() + " saved successfully", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                
                                      // execute the method (refresh data of calling interface) passed as parameter to the dialog here and if the return value is true close the dialog
                                      if (action.action(bean))
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
                              });
        edit.addClickListener((e) ->
                              {
                                  try
                                  {
                                      binder.writeBean(bean);
                
                                      // bean.setWikiLink(bean.getWikiLink());
                                      // bean.setBiography(bean.getBiography());
                
                                      Author a = bean.replaceEntity(beann, bean);
                                      Div message;
                                      if (a.equals(bean))
                                      {
                                          System.out.println(bean.getClass().getSimpleName() + " updated successfully");
                                          new CustomNotification(bean.getClass().getSimpleName() + " updated successfully", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                    
                                      }
                                      else
                                      {
                                          new CustomNotification(bean.getClass().getSimpleName() + " not updated", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                    
                                      }
                                      // execute the method (refresh data of calling interface) passed as parameter to the dialog here and if the return value is true close the dialog
                                      if (action.action(bean))
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
        personSearch.addValueChangeListener(vc ->
                                            {
                                                fdp.setFilter(vc.getValue());
                                                fdp.refreshAll();
                                            });
        
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
                                          new PersonCRUDDialog(new Person(), BasicDialog.BeanAction.NEW, d ->
                                          {
                                              UI.getCurrent().getPage().reload();
                                              return true;
                                          }).open();
                                      });
        personsLB.addValueChangeListener(vc ->
                                         {
                                             p[0] = vc.getValue();
                                             String person = p[0].getTitle() + " "
                                                     + p[0].getFirstName() + " "
                                                     + p[0].getLastName() + ", Email: "
                                                     + p[0].getEmailAddress() + ", Phone: "
                                                     + p[0].getPhoneNumber();
                                             personH2.setText(person);
                                             setTitle(titleH3, personH2);
                                             /*  > commented notification
                                             Div message = new Div(new Span("Person Selected: " + person));
                                             message.setClassName("info");
                                             new CustomNotification(message).open();*/
            
                                         });
        
        // TODO > Label configurations
        
        //binder.readBean(bean);
        //binder.setStatusLabel(infoBelowContent);
        /*binder.addStatusChangeListener(fired ->
                                       {
                                           getErrorDivContent().setVisible(true);
                                       });*/
        //binder.setStatusLabel(getErrorLabel());
        
        /*SerializableBiConsumer<Emphasis, String> consumer = (emphasis, person) ->
        {
            PersonCommand pc = PersonDataService.getPersonCommandById(person);
            emphasis.setText(pc.getTitle() + " " + pc.getFirstName() + " " + pc.getLastName());
        };*/
        
        //the second parameter provides the initialization/content for the first argument
        //the first parameter is the container for the contents of the contents from the second parameter
        //when you see an object of first argument return second argument
//        personsLB.setRenderer(new ComponentRenderer<>(Emphasis::new, consumer));
        
    
    }
}
