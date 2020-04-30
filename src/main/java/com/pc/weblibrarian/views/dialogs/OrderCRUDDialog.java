package com.pc.weblibrarian.views.dialogs;

import com.pc.weblibrarian.customcomponents.CustomComboBox;
import com.pc.weblibrarian.customcomponents.CustomNotification;
import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.entity.LibraryItem;
import com.pc.weblibrarian.entity.LibraryUser;
import com.pc.weblibrarian.entity.OrderItem;
import com.pc.weblibrarian.enums.Currency;
import com.pc.weblibrarian.enums.PricingType;
import com.pc.weblibrarian.model.Pricing;
import com.pc.weblibrarian.views.BasicDialog;
import com.pc.weblibrarian.views.LoginPage;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.ReadOnlyHasValue;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;

import java.util.EnumSet;

@Slf4j
public class OrderCRUDDialog extends BasicDialog implements BeforeLeaveObserver
{
    private LibraryItem libraryItemClone = new LibraryItem();
    private GenericDataService gds = new GenericDataService(new LibraryItem());
    
    private double priceDouble;
    // TODO > Labels
    private H2 titleH3 = new H2();
    private H2 personH2 = new H2();
    
    // TODO > Buttons
    SmallButton save = new SmallButton("Order");
    // SmallButton edit = new SmallButton("Edit");
    SmallButton cancel = new SmallButton("Cancel");
    // SmallButton clear = new SmallButton("Clear");
    SmallButton delete = new SmallButton("Delete");
    // SmallButton clearImage = new SmallButton("Clear Image");
    
    Label fname = new Label("First Name");
    Label lname = new Label("Last Name");
    Label emailLabel = new Label("Username");
    ReadOnlyHasValue<String> email = new ReadOnlyHasValue<>(text -> emailLabel.setText(text));
    
    Label libraryItemName = new Label("Name");
    Label libraryItemType = new Label("Type");
    Label price = new Label("Price");
    Label quantityAvailable = new Label("Quantity Available");
    
    CustomComboBox<PricingType> pricingType = new CustomComboBox<PricingType>("Pricing Type", EnumSet.allOf(PricingType.class), null, "2", false);
    IntegerField quantityTF = new IntegerField("Quantity");
    
    // TODO > Layouts
    HorizontalLayout buttonsHL = new HorizontalLayout();
    FormLayout userInfoFL = new FormLayout(fname, lname, emailLabel);
    FormLayout libraryItemFL = new FormLayout(libraryItemName, libraryItemType, pricingType, price, quantityAvailable, quantityTF); // check quantity against maxCheckout quantity
    Section section = new Section(libraryItemFL);
    FormLayout orderFL = new FormLayout();
    VerticalLayout leftVL = new VerticalLayout(userInfoFL, section);
    // VerticalLayout rightVL = new VerticalLayout(orderFL);
    HorizontalLayout requestHL = new HorizontalLayout(leftVL/*, rightVL*/);
    
    
    public OrderCRUDDialog(LibraryItem bean, OrderItem orderBean, BeanAction beanAction, Action<LibraryItem> action, DialogSize dialogSize)
    {
        super(beanAction, dialogSize);
        
        save.addClickListener(d ->
                              {
                                  orderBean.setChecked(false);
                                  orderBean.setLibraryItemId(bean.getUuid());
                                  orderBean.setPrice(priceDouble);
                                  orderBean.setQuantity(quantityTF.getValue());
                                  if (LoginPage.loginStatus)
                                  {
                                      orderBean.setUserEmail(VaadinSession.getCurrent().getAttribute("username").toString());
                                  }
            
                                  OrderItem savedOrder = (OrderItem) orderBean.save(orderBean);
            
                                  if (savedOrder.equals(orderBean))
                                  {
                                      if (action.action(bean))
                                      {
                                          close();
                                          new CustomNotification(bean.getClass().getSimpleName() + " saved successfully", "alert-success", true, 1000, Notification.Position.TOP_CENTER).open();
                                      }
                                      else
                                      {
                                          new CustomNotification(bean.getClass().getSimpleName() + " not saved", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                                      }
                                  }
                              });
        
        /*edit.addClickListener(d ->
                              {
                                  if (orderBean != null)
                                  {
                
                                      orderBean.setChecked(false);
                                      // orderBean.setLibraryItemId(bean.getUuid());
                                      orderBean.setPrice(Double.parseDouble(price.getText()));
                                      orderBean.setQuantity(quantityTF.getValue());
                
                                      OrderItem savedOrder = orderBean.replaceEntity(orderBean, orderBean);
                
                                      if (savedOrder.equals(orderBean))
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
                              });*/
        
        
        cancel.addClickListener(event ->
                                {
                                    this.close();
                                });
        
        
        pricingType.addValueChangeListener(d ->
                                           {
            
                                               if (bean.getPricingInformation() != null && bean.getPricingInformation().size() > 0)
                                               {
                                                   log.info("inside pricing type listener 1");
                                                   if (bean.getPricingInformation().stream().map(Pricing::getPriceType).anyMatch(s -> s.equals(pricingType.getValue())))
                                                   {
                                                       log.info("inside pricing type listener 2");
                                                       bean.getPricingInformation().forEach(s ->
                                                                                            {
                                                                                                if (s.getPriceType() != null)
                                                                                                {
                                                                                                    if (s.getPriceType().equals(pricingType.getValue()))
                                                                                                    {
                                                                                                        priceDouble = s.getUnitCost();
                                                                                                        price.setText("Price: " + priceDouble);
                                                                                                    }
                                                                                                }
                                                                                                else
                                                                                                {
                                                                                                    priceDouble = 0;
                                                                                                    price.setText("Price: " + priceDouble);
                                                                                                }
                                                                                            });
                                                   }
                                                   else
                                                   {
                                                       priceDouble = 0;
                                                       price.setText("Price: " + priceDouble);
                                                       new CustomNotification("Price not available. kindly contact the librarian", "alert-info", true, 3000, Notification.Position.TOP_CENTER).open();
                                                   }
                                               }
                                               else
                                               {
                                                   priceDouble = 0;
                                                   price.setText("Price: " + priceDouble);
                                               }
                                           });
        
        quantityTF.setValueChangeMode(ValueChangeMode.LAZY);
        quantityTF.addValueChangeListener(f ->
                                          {
                                              if (f.getValue() == null)
                                              {
                                                  save.setEnabled(false);
                                                  // edit.setEnabled(false);
                                              }
                                              else
                                              {
                                                  save.setEnabled(true);
                                                  // edit.setEnabled(true);
                                              }
                                              if (bean.getQuantityAvailable() == 0)
                                              {
                                                  new CustomNotification("Item not available", "alert-info", true, 3000, Notification.Position.TOP_CENTER).open();
                                              }
                                              else
                                              {
                                                  if (quantityTF.getValue() != null)
                                                  {
                                                      if ((bean.getQuantityAvailable() - quantityTF.getValue()) < 0)
                                                      {
                                                          setErrorDivContent(new Label("Quantity not available"));
                                                          getErrorDiv().setVisible(true);
                                                      }
                                                      else
                                                      {
                                                          setErrorDivContent(new Label(""));
                                                          getErrorDiv().setVisible(false);
                                                      }
                                                  }
                                              }
                                          });
        
        
        buttonsHL.add(save, cancel);
        addFooterComponent(buttonsHL);
        setContent(requestHL);
        
        titleH3.addClassName("mr-2");
        personH2.addClassName("ml-5");
        
        save.theme("primary");
        save.setEnabled(false);
        
        // edit.theme("primary");
        // edit.setEnabled(false);
        
        delete.theme("error");
        delete.theme(ButtonVariant.LUMO_ERROR.getVariantName());
        
        
        if (beanAction == BeanAction.VIEW)
        {
            setTitle(new Label("Order Details"));
            save.setVisible(false);
            // edit.setVisible(false);
            delete.setVisible(false);
        }
        if (beanAction == BeanAction.NEW)
        {
            // image.setVisible(false);
            buttonsHL.add(save);
            titleH3 = new H2("New Order");
            setTitle(titleH3);
        }
        if (beanAction == BeanAction.EDIT)
        {
            // buttonsHL.add(edit);
            titleH3 = new H2("Edit Order");
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
                libraryItemClone = (LibraryItem) bean.clone();
                
                if (LoginPage.loginStatus)
                {
                    LibraryUser loggedInUser = (LibraryUser) VaadinSession.getCurrent().getAttribute("libraryuser");
                    fname.setText("First Name: " + loggedInUser.getFirstName());
                    lname.setText("Last Name: " + loggedInUser.getLastName());
                    emailLabel.setText("Email: " + VaadinSession.getCurrent().getAttribute("username").toString());
                    
                    libraryItemName.setText("Name: " + bean.getLibraryItemName());
                    libraryItemType.setText("Type: " + bean.getLibraryItemType());
                    priceDouble = bean.getPricingInformation().stream().filter(d -> d.getPriceType().equals(PricingType.RENT)).findFirst()
                                      .orElse(new Pricing(300D, Currency.NGN, PricingType.RENT)).getUnitCost();
                    price.setText(String.format("Price: %f", priceDouble));
                    quantityAvailable.setText("Quantity Available: " + bean.getQuantityAvailable());
                }
                
                
            }
            catch (CloneNotSupportedException e)
            {
                e.printStackTrace();
            }
        }
        
        
        /*binder.forField(address).bind("addressText");
        
        binder.addStatusChangeListener(s ->
                                       {
            
                                       });*/
        
        cancel.addClickListener(event ->
                                {
                                    this.close();
                                });
        
        
    }
    
    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent)
    {
    
    }
}
