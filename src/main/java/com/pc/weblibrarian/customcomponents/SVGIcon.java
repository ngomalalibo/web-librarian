package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.html.Image;

public class SVGIcon extends Image
{
    
    private final String prefx = "images/svg/";
    
    public SVGIcon()
    {
    
    }
    
    public SVGIcon(String icon)
    {
        setIcon(icon);
    }
    
    public SVGIcon(String icon, String size)
    {
        setIcon(icon);
        setWidth(size);
    }
    
    public void setIcon(String icon)
    {
        setSrc(prefx + icon);
    }
}
