package com.pc.weblibrarian.enums;

public enum ContentRating
{
    GENERAL("General"), MODERATE("Moderate"), ADULT("Adult");
    
    private final String variant;
    
    private ContentRating(String variant) {
        this.variant = variant;
    }
    
    public String getVariantName() {
        return this.variant;
    }
    
    
}
