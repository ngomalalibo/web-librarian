package com.pc.weblibrarian.enums;

public enum VideoRating
{
    UNKNOWN, UNRATED, G, PG, PG13, R, NC17;

    public static String getDisplayText(VideoRating i)
    {
        switch (i)
        {
            case UNRATED:
                return "Unrated";
            case G:
                return "G";
            case PG:
                return "PG";
            case PG13:
                return "PG-13";
            case R:
                return "R";
            case NC17:
                return "NC-17";
            default:
                return "Unknown";
        }
    }

    public String displayText()
    {
        return getDisplayText(this);
    }
}
