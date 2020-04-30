package com.pc.weblibrarian.dataService;

import lombok.Data;

@Data
public class AuthorFilter
{
    private String filterText;
    
    public AuthorFilter(String filterText)
    {
        this.filterText = filterText;
    }
    
}