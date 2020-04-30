package com.pc.weblibrarian.enums;

public enum BindingType
{
    PAPERBACK, HARDCOVER, SPIRAL, OTHER;

    public static String getDisplayText(BindingType i)
    {
        switch (i)
        {
            case PAPERBACK:
                return "Paperback";
            case HARDCOVER:
                return "Hardcover";
            case OTHER:
                return "Other";
            case SPIRAL:
                return "Spiral";
            default:
                return "Unknown";
        }
    }

    public String displayText()
    {
        return getDisplayText(this);
    }
}
