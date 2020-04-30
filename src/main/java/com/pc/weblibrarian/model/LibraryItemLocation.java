package com.pc.weblibrarian.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class LibraryItemLocation implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    private String floorNumber;
    private String aisleNumber;
    private String sectionNumber;
    
    public LibraryItemLocation(String floorNumber, String aisleNumber, String sectionNumber)
    {
        this.floorNumber = floorNumber;
        this.aisleNumber = aisleNumber;
        this.sectionNumber = sectionNumber;
    }
    
    public LibraryItemLocation()
    {
        super();
    }
}
