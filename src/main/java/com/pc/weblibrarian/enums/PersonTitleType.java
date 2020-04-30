package com.pc.weblibrarian.enums;

public enum PersonTitleType
{
    MR, MRS, MASTER, MISS, SIR, LORD, LADY, ESQ, HON, PASTOR, MINISTER;

    public static String getDisplayText(PersonTitleType i)
    {
        switch (i)
        {
            case MR:
                return "Mr";
            case MRS:
                return "Mrs";
            case MASTER:
                return "Mastr";
            case MISS:
                return "Miss";
            case SIR:
                return "Sir";
            case LORD:
                return "Lord";
            case LADY:
                return "Lady";
            case ESQ:
                return "Esquire";
            case HON:
                return "Hon";
            case PASTOR:
                return "Pastor";
            case MINISTER:
                return "Minister";
            default:
                return "";
        }
    }

    public String displayText()
    {
        return getDisplayText(this);
    }
}