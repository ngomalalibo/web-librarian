package com.pc.weblibrarian.enums;

public enum Currency
{
    USD("US Dollars"), NGN("Naira"), BTC("Pounds");
    
    private String currency;
    
    public String getCurrency()
    {
        return currency;
    }
    
    Currency(String currency)
    {
        this.currency = currency;
    }
    
    String getOptionValue(int r)
    {
        if (r == 0)
        {
            return "USD";
        }
        else if (r == 1)
        {
            return "NGN";
        }
        else
        {
            return "BTC";
        }
    }
    
    int getValueOption(String r)
    {
        if ("USD".equals(r))
        {
            return 0;
        }
        else if ("NGN".equals(r))
        {
            return 1;
        }
        else
        {
            return 2;
        }
    }
    
    public static String convertDisplay(String raw)
    {
        if ("dollarBank".equals(raw))
        {
            return "Dollar Bank Transfer";
        }
        else if ("nairaBank".equals(raw))
        {
            return "Naira Bank Transfer";
        }
        else if ("payPal".equals(raw))
        {
            return "PayPal";
        }
        else
        {
            return "Other";
        }
    }
    
}
