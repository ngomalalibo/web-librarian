package com.pc.weblibrarian.enums;

public enum ReleaseCycle
{
    DAILY, WEEKLY, BI_WEEKLY, MONTHLY, QUARTERLY, BI_ANNUALLY, ANNUALLY, OTHER;

    public static String getDisplayText(ReleaseCycle i)
    {
        switch (i)
        {
            case DAILY:
                return "Daily";
            case WEEKLY:
                return "Weekly";
            case BI_WEEKLY:
                return "Bi-Weekly";
            case MONTHLY:
                return "Monthly";
            case QUARTERLY:
                return "Quarterly";
            case BI_ANNUALLY:
                return "Bi-Annually";
            case ANNUALLY:
                return "Annually";
            case OTHER:
                return "Other";
            default:
                return "Unknown";
        }
    }

    public String displayText()
    {
        return getDisplayText(this);
    }
}
