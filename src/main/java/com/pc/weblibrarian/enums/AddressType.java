package com.pc.weblibrarian.enums;

import java.io.Serializable;

public enum AddressType implements Serializable
{
    MAILINGADDRESS("Mailing Address"), BILLINGADDRESS("Billing Address"), NONE("None");
    
    private final String value;
    
    AddressType(String v)
    {
        value = v;
    }
    
    public String value()
    {
        return value;
    }
    
    public static String getDisplayText(AddressType i)
    {
        switch (i)
        {
            case MAILINGADDRESS:
                return "Mailing Address";
            case BILLINGADDRESS:
                return "Billing Address";
            default:
                return "None";
        }
    }
    
    public String displayText()
    {
        return getDisplayText(this);
    }
    
    public static AddressType fromValue(String v)
    {
        for (AddressType c : AddressType.values())
        {
            if (c.value.equals(v))
            {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}