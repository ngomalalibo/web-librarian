package com.pc.weblibrarian.enums;

/**
 * Publication Categories
 */
public enum PublicationType
{
    BOOK, MAGAZINE, JOURNAL, NEWS_PAPER, TEXT_BOOK, MANUSCRIPT, NONE;

    public static String getDisplayText(PublicationType i)
    {
        switch (i)
        {
            case BOOK:
                return "Book";
            case MAGAZINE:
                return "Magazine";
            case NEWS_PAPER:
                return "Newspaper";
            case TEXT_BOOK:
                return "Textbook";
            case MANUSCRIPT:
                return "Manuscript";
            case JOURNAL:
                return "Journal";
            default:
                return "Unknown";
        }
    }

    public static PublicationType reverse(String attr)
    {
        return BOOK;
    }

    public String displayText()
    {
        return getDisplayText(this);
    }

}