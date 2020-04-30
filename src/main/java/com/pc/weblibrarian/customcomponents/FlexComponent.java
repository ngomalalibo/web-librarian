package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

public class FlexComponent extends FlexLayout
{
    public FlexComponent()
    {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.BETWEEN);
    }
    
    public FlexComponent(Component... components)
    {
        this();
        add(components);
    }
}
