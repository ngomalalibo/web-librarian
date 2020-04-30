package com.pc.weblibrarian.enums;

public enum MediaRating
{
    UNKNOWN, CLEAN, EXPLICIT;

    public static String getDisplayText(MediaRating i)
    {
        switch (i)
        {
            case CLEAN:
                return "Clean";
            case EXPLICIT:
                return "Explicit";
            default:
                return "Unknown";
        }
    }

    public String displayText()
    {
        return getDisplayText(this);
    }
}
