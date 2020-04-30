package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

public class FlexMe extends FlexLayout
{
    
    private static final long serialVersionUID = 1L;
    
    public FlexMe()
    {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.BETWEEN);
    }
    
    public FlexMe(Component... components)
    {
        this();
        add(components);
    }
}
