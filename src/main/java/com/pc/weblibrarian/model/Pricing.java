package com.pc.weblibrarian.model;

import com.pc.weblibrarian.enums.Currency;
import com.pc.weblibrarian.enums.PricingType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class Pricing implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private PricingType priceType;
    private double unitCost;
    // private String unitCostCurrency;
    private Currency currency;
    
    public Pricing()
    {
        super();
    }
    
    public Pricing(double cost, Currency currency, PricingType type)
    {
        setUnitCost(cost);
        setPriceType(type);
        setCurrency(currency);
    }
    
    
}
