package com.pc.weblibrarian.model;

import com.pc.weblibrarian.enums.WeightUnit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class ShippingInformation implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private double weight;
    private WeightUnit weightUnit;
    private String dimensions;
    
    public double getWeight(WeightUnit targetWeightUnit)
    {
        if (getWeightUnit().equals(targetWeightUnit))
        {
            return getWeight();
        }
        else
        {
            return getWeight() * getWeightUnit().convertToUnit(targetWeightUnit);
        }
    }
    
    public void setWeight(double weight, WeightUnit weightUnit)
    {
        setWeight(weight);
        setWeightUnit(weightUnit);
    }
    
    public ShippingInformation(double weight, WeightUnit weightUnit, String dimensions)
    {
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.dimensions = dimensions;
    }
    
    public ShippingInformation()
    {
        super();
    }
}
