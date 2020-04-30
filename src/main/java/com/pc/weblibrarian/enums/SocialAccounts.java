package com.pc.weblibrarian.enums;

public enum SocialAccounts
{
    INSTAGRAM, TWITTER, FACEBOOK;
    
    public static String getDisplayText(SocialAccounts s)
    {
        switch (s)
        {
            case INSTAGRAM:
                return "Instagram";
            case TWITTER:
                return "Twitter";
            case FACEBOOK:
                return "Facebook";
            default:
                return "";
        }
    }
    
    public String displayText()
    {
        return getDisplayText(this);
    }
}
