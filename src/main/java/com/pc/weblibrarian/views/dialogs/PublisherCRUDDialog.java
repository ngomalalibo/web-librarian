package com.pc.weblibrarian.views.dialogs;

import com.pc.weblibrarian.customcomponents.CustomNotification;
import com.pc.weblibrarian.customcomponents.CustomTextArea;
import com.pc.weblibrarian.customcomponents.CustomTextField;
import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.entity.Publisher;
import com.pc.weblibrarian.views.BasicDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextAreaVariant;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.stream.Collectors;

public class PublisherCRUDDialog extends BasicDialog
{
    FormLayout formLayout = new FormLayout();
    VerticalLayout publisherVL = new VerticalLayout(formLayout);
    HorizontalLayout newPublisherHL = new HorizontalLayout(publisherVL);
    // HorizontalLayout personAuthorHL = new HorizontalLayout();
    HorizontalLayout buttonsHL = new HorizontalLayout();
    // VerticalLayout authorVL = new VerticalLayout();
    
    // TODO > Labels
    private H2 titleH3 = new H2();
    
    
    private Publisher publisherClone = new Publisher();
    
    public PublisherCRUDDialog(Publisher bean, BeanAction beanAction, BasicDialog.Action<Publisher> action, DialogSize dialogSize)
    {
        super(beanAction, dialogSize);
        
        // formLayout.add(newPublisherHL, buttonsHL);
        
        CustomTextField publisherNameTF = new CustomTextField("Publisher Name", ValueChangeMode.LAZY, null, false, "Publisher name", TextFieldVariant.LUMO_SMALL.getVariantName());
        CustomTextField websiteTF = new CustomTextField("Publisher Website", ValueChangeMode.LAZY, null, false, "Publisher website", TextFieldVariant.LUMO_SMALL.getVariantName());
        CustomTextField wikilinkTF = new CustomTextField("Wikilink", ValueChangeMode.LAZY, null, false, "Wikilink", TextFieldVariant.LUMO_SMALL.getVariantName());
        CustomTextField contactPerson = new CustomTextField("Contact Person", ValueChangeMode.LAZY, null, false, "", TextAreaVariant.LUMO_SMALL.getVariantName());
        CustomTextField copyRight = new CustomTextField("Copyright", ValueChangeMode.LAZY, null, false, "", TextAreaVariant.LUMO_SMALL.getVariantName());
        CustomTextArea address = new CustomTextArea("Address", ValueChangeMode.LAZY, null, false, "", "small");
        
        publisherVL.setSizeFull();
        
        SmallButton save = new SmallButton("Save");
        save.theme("primary");
        
        SmallButton edit = new SmallButton("Edit");
        edit.theme("primary");
        
        SmallButton cancel = new SmallButton("Cancel");
        SmallButton clear = new SmallButton("Clear");
        SmallButton delete = new SmallButton("Delete");
        delete.theme("error");
        
        formLayout.add(publisherNameTF, websiteTF, wikilinkTF, contactPerson, copyRight, address);
        
        if (beanAction == BeanAction.VIEW)
        {
            setTitle(new Label("Publisher Details"));
            save.setVisible(false);
            edit.setVisible(false);
            clear.setVisible(false);
            delete.setVisible(false);
            
        }
        if (beanAction == BeanAction.NEW)
        {
            // image.setVisible(false);
            buttonsHL.add(clear);
            buttonsHL.add(save);
            titleH3 = new H2("New Publisher");
            setTitle(titleH3);
        }
        if (beanAction == BeanAction.EDIT)
        {
            buttonsHL.add(clear);
            buttonsHL.add(edit);
            titleH3 = new H2("Edit Publisher");
            setTitle(titleH3);
        }
        if (beanAction == BeanAction.DELETE)
        {
            buttonsHL.add(delete);
        }
        
        if (beanAction != BeanAction.NEW)
        {
            try
            {
                publisherClone = (Publisher) bean.clone();
            }
            catch (CloneNotSupportedException e)
            {
                e.printStackTrace();
            }
        }
        
        buttonsHL.add(save, cancel);
        addFooterComponent(buttonsHL);
        setContent(newPublisherHL);
        
        binder.forField(address).bind("addressText");
        binder.forField(contactPerson).bind("contactPerson");
        binder.forField(publisherNameTF).bind("publisherName");
        binder.forField(websiteTF).bind("publisherWebsite");
        binder.forField(wikilinkTF).bind("wikiLink");
    
        binder.addStatusChangeListener(s ->
                                       {
                                           if (publisherNameTF.getValue() == null || websiteTF.getValue().equals(""))
                                           {
                                               save.setEnabled(false);
                                               edit.setEnabled(false);
                                               //new CustomNotification("continue completing form details", "error", true, 1000, Notification.Position.TOP_CENTER).open();
            
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
                                          binder.writeBean(bean);
                    
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
        
        edit.addClickListener(e ->
                              {
                                  try
                                  {
                                      synchronized (e)
                                      {
                                          binder.writeBean(bean);
                    
                                          bean.setUuid(publisherClone.getUuid());
                                          Publisher publisher = bean.replaceEntity(publisherClone, bean);
                    
                                          if (publisher.equals(bean))
                                          {
                                              if (action.action(bean))
                                              {
                                                  close();
                                                  new CustomNotification(bean.getClass().getSimpleName() + " updated successfully", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                                              }
                                              else
                                              {
                                                  new CustomNotification(bean.getClass().getSimpleName() + " not updated", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                                              }
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
    }
    
    // TODO > Binder (readbean manually copies object to ui fields. bean>ui, writebean manually copies ui data to object. ui>bean)
    private Binder<Publisher> binder = new Binder<>(Publisher.class, true/* scans nested objects to bind as well*/);
    
}
