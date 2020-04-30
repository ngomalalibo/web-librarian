package com.pc.weblibrarian.templates;

import lombok.Data;

@Data
public class ActionableEmail
{
    private String message;
    private String buttonLink;
    private String toAddresses;
    private String fromAddresses;
    private String personName;
    private String subject;
    private String line1, line2, line3;
    private String buttonText;
    
}
