package com.pc.weblibrarian.model;

import lombok.Data;

@Data
public class State
{
    private String region;
    private String country;
    
    public State(String region, String country)
    {
        super();
        this.region = region;
        this.country = country;
    }
    
    public State()
    {
        super();
    }
}
