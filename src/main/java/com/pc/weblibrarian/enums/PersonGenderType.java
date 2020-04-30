package com.pc.weblibrarian.enums;

public enum PersonGenderType
{
    MALE, FEMALE;

    public static String getDisplayText(PersonGenderType i)
    {
        switch (i)
        {
            case MALE:
                return "Male";
            case FEMALE:
                return "Female";
            default:
                return "";
        }
    }

    public String displayText()
    {
        return getDisplayText(this);
    }
}