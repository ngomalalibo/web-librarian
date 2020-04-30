package com.pc.weblibrarian.enums;

public enum MediaAspectRatio
{
    UNKNOWN, ASPECT4_3, ASPECT16_9, ASPECT21_9;

    public static String getDisplayText(MediaAspectRatio i)
    {
        switch (i)
        {
            case ASPECT4_3:
                return "4:3";
            case ASPECT16_9:
                return "16:9 Widescreen";
            case ASPECT21_9:
                return "21:9 UltraWide";
            default:
                return "Unknown";
        }
    }

    public String displayText()
    {
        return getDisplayText(this);
    }
}
