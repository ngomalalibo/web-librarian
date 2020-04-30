package com.pc.weblibrarian.enums;

public enum WeightUnit
{
    KG("Kilograms"), LB("Pounds");
    
    private String unit;
    
    public String getUnit()
    {
        return unit;
    }
    
    WeightUnit(String unit)
    {
        this.unit = unit;
    }
    
    public double convertToUnit(WeightUnit target)
    {
        if (this == KG && target == LB)
        {
            return 2.204622622;
        } else if (this == LB && target == KG)
        {
            return 0.45359237;
        } else
            return 1;
    }
}