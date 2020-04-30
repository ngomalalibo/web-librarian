package com.pc.weblibrarian.entity;

import com.pc.weblibrarian.enums.PricingType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderTransaction extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private String userId;
    private List<String> libraryItems;
    private List<OrderItem> orderItem;
    private String shippingInformation;
    
    PricingType pricingType;
    
    private String remark;
    
    private double discount;
    private double totalAmount;
    
    public OrderTransaction(String userId, List<String> libraryItems, List<OrderItem> orderItem, String shippingInformation, PricingType pricingType, String remark, double discount, double totalAmount)
    {
        super.prepersist(this);
        this.userId = userId;
        this.libraryItems = libraryItems;
        this.orderItem = orderItem;
        this.shippingInformation = shippingInformation;
        this.pricingType = pricingType;
        this.remark = remark;
        this.discount = discount;
        this.totalAmount = totalAmount;
    }
    
    public OrderTransaction()
    {
        super();
    }
}
