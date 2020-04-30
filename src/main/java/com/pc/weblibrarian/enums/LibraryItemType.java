package com.pc.weblibrarian.enums;

import com.pc.weblibrarian.entity.Media;
import com.pc.weblibrarian.entity.PersistingBaseEntity;
import com.pc.weblibrarian.entity.Publication;

public enum LibraryItemType
{
    PUBLICATION("Publication"),
    MEDIA("Media");
    
    public static String getDisplayText(LibraryItemType i)
    {
        switch (i)
        {
            case PUBLICATION:
                return "Publication";
            case MEDIA:
                return "Media";
            default:
                return "";
        }
    }
    
    private String libraryItemType;
    
    public String getLibraryItemType()
    {
        return this.libraryItemType;
    }
    
    LibraryItemType(String libraryItemType)
    {
        this.libraryItemType = libraryItemType;
    }
    
    public static PersistingBaseEntity getLibraryItemClass(LibraryItemType lit)
    {
        if (lit == LibraryItemType.MEDIA)
        {
            return new Media();
        }
        else
        {
            return new Publication();
        }
    }
}