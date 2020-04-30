package com.pc.weblibrarian.views.dialogs;

import com.pc.weblibrarian.customcomponents.*;
import com.pc.weblibrarian.dataproviders.AuthorDP;
import com.pc.weblibrarian.entity.*;
import com.pc.weblibrarian.enums.*;
import com.pc.weblibrarian.model.*;
import com.pc.weblibrarian.utils.BinderAuthorConverter;
import com.pc.weblibrarian.utils.GetLanguages;
import com.pc.weblibrarian.utils.ImageUtil;
import com.pc.weblibrarian.utils.PublicationGenres;
import com.pc.weblibrarian.views.BasicDialog;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextAreaVariant;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.StreamResource;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class LibraryItemCRUDDialog extends BasicDialog
{
    // Fields are under
    
    //LibraryItem initializingBean = new LibraryItem();
    
    // TODO > Beans
    private LibraryItem beann;
    private LibraryItemLocation lil = new LibraryItemLocation();
    private ShippingInformation shipping = new ShippingInformation();
    private Pricing pricing = new Pricing();
    
    private Publication publicationClone = new Publication();
    final Address address = new Address("city", new State("Lagos", "ng"), "100001", new Country("Nigeria", "ng"), true, "street", AddressType.MAILINGADDRESS);
    final Person person = new Person(PersonTitleType.MR, PersonGenderType.MALE, "Publisher", "Name", "AKA", "publisher@mail.com", "080382749272", "website", null, LocalDate.now(), List.of(address.getUuid()));
    final Author author = new Author("authwiki", "authbio", person.getUuid());
    final Publisher publisher = new Publisher("Macmillan", "www", "CR", "wiki", address.getUuid(), person.getUuid());
    private Publication publication = new Publication("Edition", "Art", "1234567890987", "Title-Name", Locale.getDefault().getDisplayLanguage(), "Desc", "Rev", "2019", null, LocalDate.now(),
                                                      222, PublicationType.BOOK, ReleaseCycle.ANNUALLY, BindingType.PAPERBACK, author.getUuid(), publisher.getUuid(), LocalDate.now());
    
    private MetaData metadata = new MetaData("season", "episode", "director", "specificFeatures", "castCrew", "Duration", 4.5F);
    private Media media = new Media(LocalDate.now(), metadata, MediaRating.CLEAN, VideoRating.PG, MediaAspectRatio.ASPECT4_3, MediaGenres.getVideoGenres().get(0), MediaType.AUDIO_BOOK, LocalDate.now(), "location", "label", "ASIN", "CP", publisher.getUuid());
    private Media mediaClone = new Media();
    private Author authorClone = new Author();
    
    
    public LibraryItemCRUDDialog(LibraryItem bean, BeanAction beanAction, BasicDialog.Action<LibraryItem> action, DialogSize dialogSize)
    {
        //  TODO > Styling
        super(beanAction, dialogSize);
        
        titleH3.addClassName("mr-2");
        personH2.addClassName("ml-5");
        
        authorLB.setDataProvider(fdp);
        authorLB.getElement().getStyle().set("size", "4px");
        authorLB.getElement().getStyle().set("border", "1px solid ivory");
        authorLB.setMaxHeight("150px");
        authorLB.setHeight("150px");
        authorLB.setWidthFull();
        
        personH2.addClassName("font-size-s");
        personH2.addClassName("text-primary");
        
        maxCheckOutCopiesNF.addThemeName(TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
        quantityAvailable.addThemeName(TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
        
        imageButton.theme(ButtonVariant.LUMO_SMALL.getVariantName());
        
        image.setMaxWidth("200px");
        image.setMaxHeight("300px");
        image.setSizeFull();
        image.setVisible(false);
        
        save.theme("primary");
        save.setEnabled(true);
        
        edit.theme("primary");
        edit.setEnabled(true);
        
        delete.theme("error");
        delete.theme(ButtonVariant.LUMO_ERROR.getVariantName());
        
        clearImage.addClassNames("btn-sm", "btn", "btn-outline-primary");
        
        // TODO > Positioning
        libraryItemVL.setSizeFull();
        itemTypeVL.setSizeFull();
        libraryItemTF.setSizeFull();
        
        HorizontalLayout locationHL = new HorizontalLayout(floorNumberTF, aisleNumberTF, sectionNumberTF);
        Hr line = new Hr();
        Hr line1 = new Hr();
        
        weightNF.addClassName("mx-2");
        weightUnit.addClassName("mx-2");
        dimensionsTF.addClassName("mx-2");
        Section shippingInformation = new Section(weightNF, weightUnit, dimensionsTF);
        
        pricingType.addClassName("mx-2");
        currency.addClassName("mx-2");
        unitCost.addClassName("mx-2");
        Section pricingInfo = new Section(pricingType, currency, unitCost);
        
        HorizontalLayout sectionHL = new HorizontalLayout(maxCheckOutCopiesNF, quantityAvailable, contentRating, discontinueDate);
        
        HorizontalLayout imageAndItemType = new HorizontalLayout(libraryItemType, image, clearImage);
        
        libraryItemFL.add(imageAndItemType, libraryItemTF, sectionHL, line);
        VerticalLayout authorVL = new VerticalLayout(authorSearch, authorLB);
        libraryItemVL.add(libraryItemFL, locationHL, line1, shippingInformation, pricingInfo, authorVL);
        
        // TODO > beanAction component display setup
        
        if (beanAction == BeanAction.VIEW)
        {
            setTitle(new Label("Library Item Details"));
            save.setVisible(false);
            edit.setVisible(false);
            clear.setVisible(false);
            delete.setVisible(false);
            
            authorLB.setEnabled(false);
        }
        if (beanAction == BeanAction.NEW)
        {
            // image.setVisible(false);
            buttonsHL.add(clear);
            buttonsHL.add(save);
            titleH3 = new H2("New Library Item ");
            setTitle(titleH3);
        }
        if (beanAction == BeanAction.EDIT)
        {
            buttonsHL.add(clear);
            buttonsHL.add(edit);
            titleH3 = new H2("Edit Library Item ");
            setTitle(titleH3);
        }
        if (beanAction == BeanAction.DELETE)
        {
            buttonsHL.add(delete);
        }
        buttonsHL.add(cancel);
        addFooterComponent(buttonsHL);
        setContent(containerHL);
        
        FormLayout publicationForm = getPublicationForm();
        FormLayout mediaForm = getMediaForm();
        
        // TODO > Binders
        binder.forField(libraryItemTF).asRequired("enter name/title").bind(LibraryItem::getLibraryItemName, LibraryItem::setLibraryItemName);
        binder.forField(libraryItemType).asRequired("choose item type").bind(LibraryItem::getLibraryItemType, LibraryItem::setLibraryItemType);
        binder.forField(maxCheckOutCopiesNF).withNullRepresentation(0).bind(LibraryItem::getMaximumCheckoutCopies, LibraryItem::setMaximumCheckoutCopies);
        binder.forField(quantityAvailable).withNullRepresentation(0).bind(LibraryItem::getQuantityAvailable, LibraryItem::setQuantityAvailable);
        binder.forField(contentRating).bind("contentRating");
        binder.forField(discontinueDate).bind("discontinueDate");
        
        binder.addStatusChangeListener(s ->
                                       {
                                           if (authorLB.getValue() == null || libraryItemTF.getValue().equals(""))
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
        
        // TODO > Configure Data Components. Form, Grid, List, Radio Button, ComboBox, Renderers
        
        libraryItemType.setItems(EnumSet.allOf(LibraryItemType.class));
        libraryItemType.addValueChangeListener(f ->
                                               {
                                                   if (f.getValue() == LibraryItemType.PUBLICATION)
                                                   {
                                                       itemTypeVL.removeAll();
                                                       itemTypeVL.add(publicationForm);
                                                   }
                                                   else
                                                   {
                                                       itemTypeVL.removeAll();
                                                       itemTypeVL.add(mediaForm);
                                                   }
                                               });
        
        authorLB.setRenderer(new ComponentRenderer<Emphasis, Author>(a -> new Emphasis(a.getPerson().getTitle() + ", " + a.getPerson().getFullName())));
        
        
        // TODO > Initializing beans for testing and beanAction data setup. Commented after testing
        if (beanAction.equals(BeanAction.NEW))
        {
            
            bean.setContentRating(ContentRating.GENERAL);
            bean.setCurrencyTypeAndSymbol(CurrencyTypeAndSymbol.NGN);
            bean.setDiscontinueDate(LocalDate.now());
            bean.setLibraryItemLocation(new LibraryItemLocation("1", "2", "3"));
            bean.setLibraryItemName("Name");
            bean.setLibraryItemType(LibraryItemType.MEDIA);
            bean.setMaximumCheckoutCopies(1);
            bean.setPricingInformation(List.of(new Pricing(5000D, Currency.NGN, PricingType.BUY_NEW)));
            
            bean.setMedia(media);
            bean.setLibraryItemTypeId(media.getUuid());
            
            bean.setQuantityAvailable(3);
            bean.setQuantityUpdateDate(LocalDateTime.now());
            bean.setShippingInformation(new ShippingInformation(10.5, WeightUnit.KG, "5x5"));
            
            libraryItemType.setValue(LibraryItemType.MEDIA);
            
            floorNumberTF.setValue(bean.getLibraryItemLocation().getFloorNumber());
            aisleNumberTF.setValue(bean.getLibraryItemLocation().getAisleNumber());
            sectionNumberTF.setValue(bean.getLibraryItemLocation().getSectionNumber());
            
            weightNF.setValue(bean.getShippingInformation().getWeight());
            weightUnit.setValue(bean.getShippingInformation().getWeightUnit());
            dimensionsTF.setValue(bean.getShippingInformation().getDimensions());
            
            pricingType.setValue(bean.getPricingInformation().get(0).getPriceType());
            unitCost.setValue(bean.getPricingInformation().get(0).getUnitCost());
            currency.setValue(bean.getPricingInformation().get(0).getCurrency());
            
            mediaBinder.readBean(bean.getMedia());
            metaDataBinder.readBean(metadata);
            binder.readBean(bean);
            
            save.setEnabled(true);
            
            /*bean.setContentRating(ContentRating.GENERAL);
            bean.setCurrencyTypeAndSymbol(CurrencyTypeAndSymbol.NGN);
            bean.setDiscontinueDate(LocalDate.now());
            bean.setLibraryItemLocation(new LibraryItemLocation("1", "2", "3"));
            bean.setLibraryItemName("Name");
            bean.setLibraryItemType(LibraryItemType.PUBLICATION);
            bean.setMaximumCheckoutCopies(1);
            bean.setPricingInformation(List.of(new Pricing(5000D, Currency.NGN, PricingType.BUY_NEW)));
            
            bean.setPublication(publication);
            bean.setLibraryItemTypeId(publication.getUuid());
            
            bean.setQuantityAvailable(3);
            bean.setQuantityUpdateDate(LocalDateTime.now());
            bean.setShippingInformation(new ShippingInformation(10.5, WeightUnit.KG, "5x5"));
            
            try
            {
                initializingBean = (LibraryItem) bean.clone();
            }
            catch (CloneNotSupportedException e)
            {
                e.printStackTrace();
            }
            libraryItemType.setValue(LibraryItemType.PUBLICATION);
            
            floorNumberTF.setValue(bean.getLibraryItemLocation().getFloorNumber());
            aisleNumberTF.setValue(bean.getLibraryItemLocation().getAisleNumber());
            sectionNumberTF.setValue(bean.getLibraryItemLocation().getSectionNumber());
            
            weightNF.setValue(bean.getShippingInformation().getWeight());
            weightUnit.setValue(bean.getShippingInformation().getWeightUnit());
            dimensionsTF.setValue(bean.getShippingInformation().getDimensions());
            
            pricingType.setValue(bean.getPricingInformation().get(0).getPriceType());
            unitCost.setValue(bean.getPricingInformation().get(0).getUnitCost());
            currency.setValue(bean.getPricingInformation().get(0).getCurrency());
            
            pubBinder.readBean(bean.getPublication());
            binder.readBean(bean);*/
        }
        else //if (beanAction != BeanAction.NEW)
        {
            // TODO > Clone beans and embeddedObjects
            try
            {
                beann = (LibraryItem) bean.clone();
                
                if (bean.getLibraryItemType() != null)
                {
                    libraryItemType.setValue(bean.getLibraryItemType());
                    if (bean.getLibraryItemType().equals(LibraryItemType.PUBLICATION))
                    {
                        // if (!Objects.isNull(bean.getPublication()))
                        if (bean.getPublication() != null)
                        {
                            publicationClone = (Publication) bean.getPublication().clone();
                            authorClone = (Author) bean.getPublication().getAuthor().clone();
                            bean.setLibraryItemTypeId(bean.getPublication().getUuid());
                            
                            //System.out.println("bean.getPublication() = " + bean.getPublication());
                            
                            editionTF.setValue(bean.getPublication().getEdition());
                            publicationGenreCB.setValue(bean.getPublication().getGenre());
                            ISBNTF.setValue(bean.getPublication().getISBN_13());
                            languageCB.setValue(bean.getPublication().getPubLanguage());
                            descriptionTA.setValue(bean.getPublication().getDescription());
                            reviewTA.setValue(bean.getPublication().getReviewText());
                            releaseYearTF.setValue(bean.getPublication().getReleaseYear());
                            publishedDate.setValue(bean.getPublication().getFirstPublishedDate());
                            noOfPages.setValue(bean.getPublication().getNumberOfPages());
                            publicationType.setValue(bean.getPublication().getPublicationType());
                            releaseCycle.setValue(bean.getPublication().getReleaseCycle());
                            bindingType.setValue(bean.getPublication().getBindingType());
                            authorLB.setValue(bean.getPublication().getAuthor());
                        }
                    }
                    else
                    {
                        if (bean.getMedia() != null)
                        {
                            mediaClone = (Media) bean.getMedia().clone();
                            authorClone = (Author) bean.getMedia().getAuthor().clone();
                            bean.setLibraryItemTypeId(bean.getMedia().getUuid());
                            mediaBinder.readBean(bean.getMedia());
                            metaDataBinder.readBean(bean.getMedia().getMetaData());
                            Author objectById = bean.getMedia().getAuthor();
                            System.out.println("author = " + objectById);
                            authorLB.setValue(objectById);
                        }
                    }
                }
                binder.readBean(bean);
                
                // get item location
                if (!Objects.isNull(bean.getLibraryItemLocation()))
                {
                    floorNumberTF.setValue(bean.getLibraryItemLocation().getFloorNumber());
                    aisleNumberTF.setValue(bean.getLibraryItemLocation().getAisleNumber());
                    sectionNumberTF.setValue(bean.getLibraryItemLocation().getSectionNumber());
                }
                // get shipping information
                if (!Objects.isNull(bean.getShippingInformation()))
                {
                    weightNF.setValue(bean.getShippingInformation().getWeight());
                    weightUnit.setValue(bean.getShippingInformation().getWeightUnit());
                    dimensionsTF.setValue(bean.getShippingInformation().getDimensions());
                }
                //get pricing info
                if (!Objects.isNull(bean.getPricingInformation()) && bean.getPricingInformation().size() > 0)
                {
                    libraryItemVL.addComponentAtIndex(5, configurePricingGrid(pricingGridPro, bean));
                    pricingType.setValue(bean.getPricingInformation().get(0).getPriceType());
                    unitCost.setValue(bean.getPricingInformation().get(0).getUnitCost());
                    currency.setValue(bean.getPricingInformation().get(0).getCurrency());
                }
                
                //get image
                if (!Objects.isNull(bean.getPublication().getImage()) || !Objects.isNull(bean.getMedia().getImage()))
                {
                    image.setVisible(true);
                    
                    imageModel = bean.getLibraryItemType().equals(LibraryItemType.PUBLICATION) ? bean.getPublication().getImage() : bean.getMedia().getImage();
                    if (imageModel.getImageByteArray().length != 0 && imageModel != null && !imageModel.getFileName().equals(""))
                    {
                        displayImage(imageModel);
                    }
                }
                
                
            }
            catch (CloneNotSupportedException e)
            {
                e.getMessage();
            }
            //binder.readBean(bean);
        }
        
        // libraryItemVL.addComponentAtIndex(5, configurePricingGrid(pricingGridPro, bean));
        // binder.readBean(bean);
        // itemTypeVL.add(publicationForm);
        
        /*if (beanAction != BeanAction.VIEW)
        {
            if (beanAction != BeanAction.DELETE)
            {
                if (beanAction == BeanAction.NEW)
                {
                    buttonsHL.add(save);
                    titleH3 = new H2("New Library Item ");
                    setTitle(titleH3);
                }
                if (beanAction == BeanAction.EDIT)
                {
                    buttonsHL.add(edit);
                    titleH3 = new H2("Edit Library Item ");
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
            setTitle(new Label("Library Item Details"));
            save.setVisible(false);
            edit.setVisible(false);
            clear.setVisible(false);
            delete.setVisible(false);
            
            authorLB.setEnabled(false);
        }*/
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
                    
                                          //gather location, shipping and pricing info
                                          lil = new LibraryItemLocation(floorNumberTF.getValue(), aisleNumberTF.getValue(), sectionNumberTF.getValue());
                                          shipping = new ShippingInformation(weightNF.getValue(), (WeightUnit) weightUnit.getValue(), dimensionsTF.getValue());
                                          pricing = new Pricing(unitCost.getValue().doubleValue(), (Currency) currency.getValue(), (PricingType) pricingType.getValue());
                                          bean.setLibraryItemLocation(lil);
                                          bean.setShippingInformation(shipping);
                                          bean.setPricingInformation(List.of(pricing));
                    
                                          //gather item type info
                                          if (libraryItemType.getValue() == null)
                                          {
                                              new CustomNotification("Select item type", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                                              throw new RuntimeException("");
                                          }
                                          else
                                          {
                                              if (libraryItemType.getValue().equals(LibraryItemType.PUBLICATION))
                                              {
                                                  pubBinder.writeBean(publication);
                                                  System.out.println("Language " + publication.getPubLanguage());
                                                  if (authorLB.getValue() != null)
                                                  {
                                                      publication.setAuthorId(authorLB.getValue().getUuid());
                                                  }
                                                  bean.setLibraryItemTypeId(publication.getUuid());
                                                  bean.setPublication(publication);
                                                  publication.save(publication);
                                              }
                                              else
                                              {
                                                  mediaBinder.writeBean(media);
                                                  metaDataBinder.writeBean(metadata);
                                                  media.setMetaData(metadata);
                                                  bean.setLibraryItemTypeId(media.getUuid());
                            
                                                  if (authorLB.getValue() != null)
                                                  {
                                                      media.setAuthorId(authorLB.getValue().getUuid());
                                                  }
                                                  bean.setMedia(media);
                                                  media.save(media);
                                              }
                                          }
                                      
                                      
                                      /*if (!ObjectUtils.isEmpty(editionTF.getValue()) &&
                                              !ObjectUtils.isEmpty(publicationGenreCB.getValue()) &&
                                              !ObjectUtils.isEmpty(ISBNTF.getValue()) &&
                                              !ObjectUtils.isEmpty(languageCB.getValue()) &&
                                              !ObjectUtils.isEmpty(descriptionTA.getValue()) &&
                                              !ObjectUtils.isEmpty(reviewTA.getValue()) &&
                                              !ObjectUtils.isEmpty(releaseYearTF))
                                      {
                                          pubBinder.writeBean(publication);
                                          bean.setPublication(publication);
                                      }
                                      else
                                      {
                                          new CustomNotification("Please provide publication details", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                                      }*/
                    
                                          // address.save(address);
                                          // person.save(person);
                                          // author.save(author);
                                          // publisher.save(publisher);
                    
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
                    
                                          lil = new LibraryItemLocation(floorNumberTF.getValue(), aisleNumberTF.getValue(), sectionNumberTF.getValue());
                                          shipping = new ShippingInformation(weightNF.getValue(), (WeightUnit) weightUnit.getValue(), dimensionsTF.getValue());
                                          pricing = new Pricing(unitCost.getValue().doubleValue(), (Currency) currency.getValue(), (PricingType) pricingType.getValue());
                                          bean.setLibraryItemLocation(lil);
                                          bean.setShippingInformation(shipping);
                                          bean.setPricingInformation(List.of(pricing));
                    
                                          //gather item type info
                                          if (libraryItemType.getValue() == null)
                                          {
                                              new CustomNotification("Select item type", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                                              throw new RuntimeException("");
                                          }
                                          else
                                          {
                                              if (libraryItemType.getValue().equals(LibraryItemType.PUBLICATION))
                                              {
                                                  pubBinder.writeBean(bean.getPublication());
                                                  if (authorLB.getValue() != null)
                                                  {
                                                      publication.setAuthorId(authorLB.getValue().getUuid());
                                                  }
                                                  bean.setLibraryItemTypeId(publicationClone.getUuid());
                                                  this.publication.setUuid(publicationClone.getUuid());
                                                  this.publication.setPublisherId(publicationClone.getPublisherId());
                                                  bean.setPublication(publication);
                                                  publicationClone = this.publication.replaceEntity(publicationClone, this.publication);
                                              }
                                              else
                                              {
                                                  mediaBinder.writeBean(bean.getMedia());
                                                  if (authorLB.getValue() != null)
                                                  {
                                                      media.setAuthorId(authorLB.getValue().getUuid());
                                                  }
                            
                                                  metaDataBinder.writeBean(metadata);
                                                  media.setMetaData(metadata);
                                                  media.setPublisherId(mediaClone.getPublisherId());
                                                  bean.setLibraryItemTypeId(mediaClone.getUuid());
                                                  this.media.setUuid(mediaClone.getUuid());
                                                  bean.setMedia(this.media);
                                                  mediaClone = this.media.replaceEntity(mediaClone, this.media);
                                              }
                                          }
                    
                                          LibraryItem a = bean.replaceEntity(beann, bean);
                    
                                          if (a.equals(bean) && (publicationClone.equals(bean.getPublication()) || mediaClone.equals(bean.getMedia())))
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
        
        authorSearch.addValueChangeListener(vc ->
                                            {
                                                fdp.setFilter(vc.getValue());
                                                fdp.refreshAll();
                                            });
        authorLB.addValueChangeListener(vc ->
                                        {
                                            Author author = vc.getValue();
                                            String person = author.getPerson().getTitle() + " "
                                                    + author.getPerson().getFullName() + ", Email: "
                                                    + author.getPerson().getEmailAddress() + ", Phone: "
                                                    + author.getPerson().getPhoneNumber();
                                            personH2.setText(person);
                                            setTitle(titleH3, personH2);
                                        });
        
        imageButton.addClickListener(a ->
                                     {
                                         new UploadImageDialog("bean.getUuid()", (bbb) ->
                                         {
                                             StreamResource rResource = new StreamResource(bbb.getFileName(), () -> new ByteArrayInputStream(bbb.getImageByteArray()));
                                             image.setSrc(rResource);
                
                                             image.setVisible(true);
                
                                             if (bean.getLibraryItemType().equals(LibraryItemType.PUBLICATION))
                                             {
                                                 bean.getPublication().setImage(bbb);
                                                 displayImage(imageModel);
                                                 return new Binary(BsonBinarySubType.BINARY, bean.getPublication().getImage().getImageByteArray());
                                             }
                                             else
                                             {
                                                 bean.getMedia().setImage(bbb);
                                                 displayImage(imageModel);
                                                 return new Binary(BsonBinarySubType.BINARY, bean.getMedia().getImage().getImageByteArray());
                                             }
                
                
                                             // bean.getPublication().setImage(bbb);
                
                
                                         }, true).open();
                                     });
        
        clearImage.addClickListener(a ->
                                    {
                                        image.setSrc("");
                                        bean.getPublication().setImage(new ImageModel());
                                        image.setVisible(false);
                                        new CustomNotification("Image cleared", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                                    });
        
    }
    
    private void displayImage(final ImageModel imageModel1)
    {
        //display image
        try
        {
            if (imageModel1.getImageByteArray().length == 0 || imageModel1.getImageByteArray() == null)
            {//set Dummy image
                ImageModel dummyImage = ImageUtil.convertPathToImageModel(ImageUtil.IMAGE_FILE);
                resource = new StreamResource(imageModel1.getFileName(), () -> new ByteArrayInputStream(dummyImage.getImageByteArray()));
            }
            
            else
            {//set provided image
                resource = new StreamResource(imageModel1.getFileName(), () -> new ByteArrayInputStream(imageModel1.getImageByteArray()));
            }
            image = new Image(resource, imageModel1.getFileName());
            image.setClassName("img-fluid");
            image.setTitle("Cover");
            image.setVisible(true);
            
        }
        catch (IOException e)
        {
            new Notification("Cannot access file " + e.getMessage(), 2000, Notification.Position.MIDDLE).open();
        }
    }
    
    private FormLayout getPublicationForm()
    {
        FormLayout publicationFL = new FormLayout();
        
        pubBinder.forField(libraryItemTF).withNullRepresentation("").bind("title");
        pubBinder.forField(editionTF).withNullRepresentation("").bind("edition");
        pubBinder.forField(publicationGenreCB).bind("genre");
        pubBinder.forField(ISBNTF).withNullRepresentation("").bind("ISBN_13");
        // pubBinder.forField(languageCB).withNullRepresentation(new Locale("en").getDisplayLanguage()).bind("pubLanguage");
        pubBinder.forField(languageCB).bind("pubLanguage");
        /*if (bean.getPublication().getLanguage() == null || !beanAction.equals(BeanAction.NEW))
        {
            languageCB.setValue(new Locale("en").getDisplayLanguage());
        }*/
        //pubBinder.forField(languageCB).withNullRepresentation(new Locale("en").getDisplayLanguage()).bind("ISBN_13");
        pubBinder.forField(descriptionTA).bind("description");
        pubBinder.forField(reviewTA).bind("reviewText");
        pubBinder.forField(releaseYearTF).bind("releaseYear");
        pubBinder.forField(publishedDate).bind("firstPublishedDate");
        pubBinder.forField(noOfPages).bind("numberOfPages");
        pubBinder.forField(publicationType).bind("publicationType");
        pubBinder.forField(releaseCycle).bind("releaseCycle");
        pubBinder.forField(bindingType).bind("bindingType");
        pubBinder.forField(authorLB).withConverter(new BinderAuthorConverter()).bind("authorId");
        
        
        publicationFL.add(editionTF, publicationGenreCB, ISBNTF, languageCB, descriptionTA, reviewTA, releaseYearTF, imageButton, publishedDate, noOfPages, publicationType, releaseCycle, bindingType);
        return publicationFL;
    }
    
    private FormLayout getMediaForm()
    {
        FormLayout mediaFL = new FormLayout();
        
        mediaBinder.forField(releaseDate).bind("releaseDate");
        mediaBinder.forField(mediaRatingCB).bind("mediaRating");
        mediaBinder.forField(videoRatingCB).bind("videoRating");
        mediaBinder.forField(mediaGenreCB).bind("mediaGenres");
        mediaBinder.forField(mediaTypeCB).bind("mediaType");
        mediaBinder.forField(labelTF).bind("label");
        mediaBinder.forField(asinTF).bind("ASIN");
        mediaBinder.forField(copyRightTF).bind("copyright");
        mediaBinder.forField(aspectRationCB).bind("mediaAspectRatio");
        //mediaBinder.forField(authorLB).withConverter(new BinderAuthorConverter()).bind("authorId");
        
        metaDataBinder.forField(castAndCrewTA).bind("castAndCrew");
        metaDataBinder.forField(directorTF).bind("director");
        metaDataBinder.forField(episodeTF).bind("episode");
        metaDataBinder.forField(mediaDurationTF).bind("mediaDurationMinutes");
        metaDataBinder.forField(seasonTF).bind("season");
        metaDataBinder.forField(specialFeaturesTF).bind("specialFeatures");
        metaDataBinder.forField(userRating).bind("userRating");
        
        
        Section metaDataSection = new Section(new Div(castAndCrewTA), directorTF, episodeTF, mediaDurationTF, seasonTF, specialFeaturesTF, userRating);
        mediaFL.add(releaseDate, mediaRatingCB, videoRatingCB, mediaGenreCB, mediaTypeCB, labelTF, asinTF, copyRightTF, aspectRationCB, metaDataSection);
        
        return mediaFL;
    }
    
    
    public GridPro<Pricing> configurePricingGrid(GridPro<Pricing> pricingGridPro, LibraryItem bean)
    {
        pricingGridPro.setItems(bean.getPricingInformation());
        pricingGridPro.setThemeName(GridVariant.LUMO_ROW_STRIPES.getVariantName());
        pricingGridPro.setSingleCellEdit(true);
        pricingGridPro.setEnterNextRow(true);
        pricingGridPro.setSelectionMode(Grid.SelectionMode.SINGLE);
        pricingGridPro.setSizeFull();
        pricingGridPro.addEditColumn(Pricing::getCurrency).select(Pricing::setCurrency, Currency.class).setHeader("Currency (editable)");
        pricingGridPro.addEditColumn(Pricing::getPriceType).select(Pricing::setPriceType, PricingType.class).setHeader("Pricing Type (editable)");
        
        Input customInput = new Input();
        pricingGridPro.addEditColumn(Pricing::getUnitCost).custom(customInput, (item, newValue) -> item.setUnitCost(Double.parseDouble(newValue)))
                      .setHeader("Unit Cost").setWidth("100px");
        
        pricingGridPro.addItemPropertyChangedListener(item ->
                                                      {
                                                          beann.getPricingInformation().forEach(d ->
                                                                                                {
                                                                                                    if (d.getPriceType().equals(item.getItem().getPriceType()))
                                                                                                    {
                                                                                                        d = item.getItem();
                                                                                                    }
                                                                                                });
            
                                                      });
        
        return pricingGridPro;
    }
    
    // TODO > Data Provider
    AuthorDP dp = new AuthorDP();
    ConfigurableFilterDataProvider<Author, Void, String> fdp = dp.withConfigurableFilter();
    
    // TODO > Layouts
    VerticalLayout libraryItemFL = new VerticalLayout();
    VerticalLayout libraryItemVL = new VerticalLayout();
    VerticalLayout itemTypeVL = new VerticalLayout();
    HorizontalLayout containerHL = new HorizontalLayout(libraryItemVL, itemTypeVL); //goes into content
    HorizontalLayout buttonsHL = new HorizontalLayout(); //goes into footer
    
    // TODO > Labels
    private H2 titleH3 = new H2();
    private H2 personH2 = new H2();
    
    
    // TODO > Components
    CustomTextField authorSearch = new CustomTextField("", ValueChangeMode.EAGER, new Icon(VaadinIcon.SEARCH), true, "Search Author", "small");
    CustomTextField libraryItemTF = new CustomTextField("Name/Title", ValueChangeMode.LAZY, null, false, "Title/Name", TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    
    IntegerField maxCheckOutCopiesNF = new IntegerField("Max check out copies", ""); //CustomNumberField("Max check out copies", "Max check out copies", true, 0, 20, true, true);
    IntegerField quantityAvailable = new IntegerField("Quantity available", ""); //CustomNumberField("Quantity available", "Quantity available", true, 0, 100, true, true);
    CustomComboBox<ContentRating> contentRating = new CustomComboBox<>("Content Rating", EnumSet.allOf(ContentRating.class), ContentRating::getVariantName, "2", false);
    DatePicker discontinueDate = new DatePicker("Discontinue date");
    CustomTextField floorNumberTF = new CustomTextField("Floor Number", ValueChangeMode.LAZY, null, false, "", TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    CustomTextField aisleNumberTF = new CustomTextField("Aisle Number", ValueChangeMode.LAZY, null, false, "", TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    CustomTextField sectionNumberTF = new CustomTextField("Section Number", ValueChangeMode.LAZY, null, false, "", TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    CustomNumberField weightNF = new CustomNumberField("Weight", "Weight", true, 0, 100, true, true);
    CustomComboBox<WeightUnit> weightUnit = new CustomComboBox<>("Weight", EnumSet.allOf(WeightUnit.class), WeightUnit::getUnit, "2", false);
    CustomTextField dimensionsTF = new CustomTextField("Dimensions", ValueChangeMode.LAZY, null, false, "Dimensions", TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    CustomComboBox<PricingType> pricingType = new CustomComboBox<>("Pricing Type", EnumSet.allOf(PricingType.class), PricingType::getValue, "2", false);
    NumberField unitCost = new NumberField("Unit Cost", "Unit Cost");
    CustomComboBox<Currency> currency = new CustomComboBox<>("Currency", EnumSet.allOf(Currency.class), Currency::getCurrency, "2", false);
    
    RadioButtonGroup<LibraryItemType> libraryItemType = new RadioButtonGroup<>();
    
    private ListBox<Author> authorLB = new ListBox<>();
    
    private GridPro<Pricing> pricingGridPro = new GridPro<>();
    
    
    //Publication
    private CustomTextField editionTF = new CustomTextField("Edition", ValueChangeMode.LAZY, null, false, "1st Edition", "medium");
    private CustomComboBox<String> publicationGenreCB = new CustomComboBox<String>("Genre", PublicationGenres.getPublicationGenres(), null, "2", false);
    private CustomTextField ISBNTF = new CustomTextField("ISBN", ValueChangeMode.LAZY, null, false, "eg 978-3-16-148410-0", "medium");
    private CustomComboBox<String> languageCB = new CustomComboBox<String>("Language", GetLanguages.getLanguages(), null, "2", false);
    private CustomTextArea descriptionTA = new CustomTextArea("Description", ValueChangeMode.LAZY, null, false, " Description", TextAreaVariant.LUMO_SMALL.getVariantName());
    private CustomTextArea reviewTA = new CustomTextArea("Review", ValueChangeMode.LAZY, null, false, " Review", TextAreaVariant.LUMO_SMALL.getVariantName());
    private CustomTextField releaseYearTF = new CustomTextField("Release Year", ValueChangeMode.LAZY, null, false, "", "medium");
    private SmallButton imageButton = new SmallButton("Image");
    
    private DatePicker publishedDate = new DatePicker("Published Date", LocalDate.now());
    private IntegerField noOfPages = new IntegerField("No of pages");
    private CustomComboBox<PublicationType> publicationType = new CustomComboBox<PublicationType>("Publication Type", EnumSet.allOf(PublicationType.class), null, "2", false);
    private CustomComboBox<ReleaseCycle> releaseCycle = new CustomComboBox<ReleaseCycle>("Release Cycle", EnumSet.allOf(ReleaseCycle.class), null, "2", false);
    private CustomComboBox<BindingType> bindingType = new CustomComboBox<BindingType>("Binding Type", EnumSet.allOf(BindingType.class), null, "2", false);
    
    //Media
    private DatePicker releaseDate = new DatePicker("Release Date", LocalDate.now());
    private CustomComboBox<MediaRating> mediaRatingCB = new CustomComboBox<>("Media Rating", EnumSet.allOf(MediaRating.class), null, "2", false);
    private CustomComboBox<VideoRating> videoRatingCB = new CustomComboBox<>("Video Rating", EnumSet.allOf(VideoRating.class), null, "2", false);
    private CustomComboBox<String> mediaGenreCB = new CustomComboBox<>("Media Genre", MediaGenres.getVideoGenres(), null, "2", false);
    private CustomComboBox<MediaType> mediaTypeCB = new CustomComboBox<>("Media Type", EnumSet.allOf(MediaType.class), null, "2", false);
    private CustomTextField labelTF = new CustomTextField("Label", ValueChangeMode.LAZY, null, false, "", TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    private CustomTextField asinTF = new CustomTextField("ASIN", ValueChangeMode.LAZY, null, false, "", TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    private CustomTextField copyRightTF = new CustomTextField("Copyright Info", ValueChangeMode.LAZY, null, false, "", TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    
    //Metadata Form
    private CustomComboBox<MediaAspectRatio> aspectRationCB = new CustomComboBox<>("Aspect Ratio", EnumSet.allOf(MediaAspectRatio.class), null, "2", false);
    private CustomTextArea castAndCrewTA = new CustomTextArea("Cast and Crew", ValueChangeMode.LAZY, null, false, "", TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    private CustomTextField directorTF = new CustomTextField("Director", ValueChangeMode.LAZY, null, false, "", TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    private CustomTextField episodeTF = new CustomTextField("Episode", ValueChangeMode.LAZY, null, false, "", TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    private CustomTextField mediaDurationTF = new CustomTextField("Media Duration", ValueChangeMode.LAZY, null, false, "", TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    private CustomTextField seasonTF = new CustomTextField("Season", ValueChangeMode.LAZY, null, false, "", TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    private CustomTextField specialFeaturesTF = new CustomTextField("Special Features", ValueChangeMode.LAZY, null, false, "", TextAreaVariant.MATERIAL_ALWAYS_FLOAT_LABEL.getVariantName());
    private NumberField userRating = new NumberField("User rating");
    
    
    // TODO > Buttons
    SmallButton save = new SmallButton("Save");
    SmallButton edit = new SmallButton("Edit");
    SmallButton cancel = new SmallButton("Cancel");
    SmallButton clear = new SmallButton("Clear");
    SmallButton delete = new SmallButton("Delete");
    SmallButton clearImage = new SmallButton("Clear Image");
    
    
    // TODO > Binder (readbean manually copies object to ui fields. bean>ui, writebean manually copies ui data to object. ui>bean)
    private Binder<LibraryItem> binder = new Binder<>(LibraryItem.class, true/* scans nested objects to bind as well*/);
    private Binder<Publication> pubBinder = new Binder<>(Publication.class, true/* scans nested objects to bind as well*/);
    private Binder<Media> mediaBinder = new Binder<>(Media.class, true/* scans nested objects to bind as well*/);
    private Binder<MetaData> metaDataBinder = new Binder<>(MetaData.class, true/* scans nested objects to bind as well*/);
    
    private Image image = new Image();
    private ImageModel imageModel = new ImageModel();
    
    private StreamResource resource;
    
    
}
