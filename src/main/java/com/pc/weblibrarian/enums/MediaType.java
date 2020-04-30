package com.pc.weblibrarian.enums;

/**
 * Media Categories
 */
public enum MediaType
{
    CASSETTE, VHS, CD, DVD, BLUE_RAY, E_BOOK, AUDIO_BOOK, VINYL;

    public static String getDisplayText(MediaType i)
    {
        switch (i)
        {
            case CASSETTE:
                return "Audio Cassette";
            case VHS:
                return "VHS Video";
            case CD:
                return "Audio CD";
            case DVD:
                return "DVD";
            case BLUE_RAY:
                return "Blueray Video";
            case E_BOOK:
                return "E-Book";
            case AUDIO_BOOK:
                return "Audio Book";
            case VINYL:
                return "Vinyl";
            default:
                return "";
        }
    }

    public String displayText()
    {
        return getDisplayText(this);
    }

    public boolean isVideoItem()
    {
        switch (this)
        {
            case CASSETTE:
            case AUDIO_BOOK:
            case VINYL:
            case CD:
            case E_BOOK:
                return false;
            case VHS:
            case DVD:
            case BLUE_RAY:
                return true;
            default:
                return false;
        }
    }
}