package com.pc.weblibrarian.enums;

public enum CurrencyTypeAndSymbol
{
    USD, GBP, NGN, EUR;

    public static String getName(CurrencyTypeAndSymbol i)
    {
        switch (i)
        {
            case USD:
                return "US Dollar";
            case GBP:
                return "Pound sterling";
            case NGN:
                return "Naira";
            case EUR:
                return "Euro";
            default:
                return "";
        }
    }

    public static String getDisplaySymbol(CurrencyTypeAndSymbol i)
    {
        switch (i)
        {
            case USD:
                return "$";
            case GBP:
                return "£";
            case NGN:
                return "₦";
            case EUR:
                return "€";
            default:
                return "#";
        }
    }
}