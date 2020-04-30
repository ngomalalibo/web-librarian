package com.pc.weblibrarian.model;

import lombok.Data;

@Data
public class Country
{
    private String name;
    private String code;
    
    public Country(String name, String code)
    {
        super();
        this.name = name;
        this.code = code;
    }
    
    public Country()
    {
        super();
    }
}
