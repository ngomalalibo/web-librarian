package com.pc.weblibrarian.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pc.weblibrarian.enums.ContentRating;
import com.pc.weblibrarian.enums.CurrencyTypeAndSymbol;
import com.pc.weblibrarian.enums.LibraryItemType;
import com.pc.weblibrarian.model.LibraryItemLocation;
import com.pc.weblibrarian.model.Pricing;
import com.pc.weblibrarian.model.ShippingInformation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class LibraryItem extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private String libraryItemName;
    
    //Type and Title
    private LibraryItemType libraryItemType;
    
    private String libraryItemTypeId;
    
    @BsonIgnore
    @JsonIgnore
    Media media = new Media();
    
    @BsonIgnore
    @JsonIgnore
    Publication publication = new Publication();
    
    private int maximumCheckoutCopies;
    private int quantityAvailable;
    
    private List<Comment> comments = new ArrayList<>();
    
    private LocalDate discontinueDate;
    private ShippingInformation shippingInformation;
    private LibraryItemLocation libraryItemLocation;
    private LocalDateTime quantityUpdateDate;
    
    @Value("${webLibrarian.contentRating}")
    private ContentRating contentRating;
    
    private List<Pricing> pricingInformation = new ArrayList<>();
    
    @Value("${webLibrarian.currencyType}")
    private CurrencyTypeAndSymbol currencyTypeAndSymbol;
    
    public LibraryItem(String libraryItemName, LibraryItemType libraryItemType, int maximumCheckoutCopies, int quantityAvailable, List<Comment> comments, LocalDate discontinueDate, ShippingInformation shippingInformation, LibraryItemLocation libraryItemLocation, LocalDateTime quantityUpdateDate, List<Pricing> pricingInformation)
    {
        super.prepersist(this);
        this.libraryItemName = libraryItemName;
        this.libraryItemType = libraryItemType;
        this.maximumCheckoutCopies = maximumCheckoutCopies;
        this.quantityAvailable = quantityAvailable;
        this.comments = comments;
        this.discontinueDate = discontinueDate;
        this.shippingInformation = shippingInformation;
        this.libraryItemLocation = libraryItemLocation;
        this.quantityUpdateDate = quantityUpdateDate;
        this.pricingInformation = pricingInformation;
    }
    
    public LibraryItem()
    {
        super();
    }
    
    /*public static void main(String[] args)
    {
        LibraryItem bean = new LibraryItem();
        Address address = new Address("city", new State("Lagos", "ng"), "100001", new Country("Nigeria", "ng"), true, "street", AddressType.MAILINGADDRESS);
        Person person = new Person(PersonTitleType.MR, PersonGenderType.MALE, "Publisher", "Name", "AKA", "publisher@mail.com", "080382749272", "website", null, LocalDate.now(), List.of(address.getUuid()));
        Author author = new Author("authwiki", "authbio", person.getUuid());
        Publisher publisher = new Publisher("Macmillan", "www", "CR", "wiki", address.getUuid(), person.getUuid());
    
    
        bean.setContentRating(ContentRating.GENERAL);
        bean.setCurrencyTypeAndSymbol(CurrencyTypeAndSymbol.NGN);
        bean.setDiscontinueDate(LocalDate.now());
        bean.setLibraryItemLocation(new LibraryItemLocation("1", "2", "3"));
        bean.setLibraryItemName("Name");
        bean.setLibraryItemType(LibraryItemType.PUBLICATION);
        bean.setMaximumCheckoutCopies(1);
        bean.setPricingInformation(List.of(new Pricing(5000D, Currency.NGN, PricingType.BUY_NEW)));
    
        Publication publication = new Publication("Edition", "Art", "1234567890987", "Title", "English", "Desc", "Rev", "2019", null, LocalDate.now(),
                                      222, PublicationType.BOOK, ReleaseCycle.ANNUALLY, BindingType.PAPERBACK, author.getUuid(), publisher.getUuid(), LocalDate.now());
        bean.setPublication(publication);
        bean.setLibraryItemTypeId(publication.getUuid());
    
        bean.setQuantityAvailable(3);
        bean.setQuantityUpdateDate(LocalDateTime.now());
        bean.setShippingInformation(new ShippingInformation(10.5, WeightUnit.KG, "5x5"));
        
        getLibraryItemEmbeddedObjects(bean);
    }*/
}
