package com.pc.weblibrarian.enums;

public enum PricingType
{
    BUY_NEW("Buy New"), BUY_USED("Buy Used"), RENT("Rent");
    
    private String value;
    
    public String getValue()
    {
        return value;
    }
    
    PricingType(String value)
    {
        this.value = value;
    }
    
    public static String getDisplayText(PricingType i)
    {
        switch (i)
        {
            case BUY_NEW:
                return "Buy New";
            case BUY_USED:
                return "Buy Used";
            case RENT:
                return "Rent";
            default:
                return "";
        }
    }
    
    public String displayText()
    {
        return getDisplayText(this);
    }
}
