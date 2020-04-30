package com.pc.weblibrarian.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pc.weblibrarian.enums.PricingType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import javax.persistence.SequenceGenerator;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderItem extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private int quantity;
    private String libraryItemId;
    PricingType transactionType;
    private double price;
    private boolean checked;
    private String userEmail;
    
    @SequenceGenerator(name = "orderSerialNumber")
    private int orderSerialNumber;
    
    @BsonIgnore
    @JsonIgnore
    private LibraryItem libraryItem;
    
    public OrderItem(int quantity, String libraryItemId, PricingType transactionType, double price, boolean checked, String userEmail)
    {
        this.quantity = quantity;
        this.libraryItemId = libraryItemId;
        this.transactionType = transactionType;
        this.price = price;
        this.checked = checked;
        this.userEmail = userEmail;
        // this.orderSerialNumber = orderSerialNumber;
    }
    
    public OrderItem()
    {
    }
}
