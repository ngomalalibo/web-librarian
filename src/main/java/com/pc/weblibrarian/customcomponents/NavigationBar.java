package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasTheme;
import com.vaadin.flow.component.html.Div;

public class NavigationBar extends Div implements HasTheme
{
    Div a = new Div();
    Div b = new Div();
    Div c = new Div();
    
    public NavigationBar()
    {
        setHeightFull();
        setThemeName("dark");
        setClassName("sidenav");
        a.setClassName("vnav-headerbanner");
        b.setClassName("vnav-midsection");
        c.setClassName("vnav-footer");
        add(a, b, c);
    }
    
    public void addHeader(Component... component)
    {
        a.add(component);
    }
    
    public void addSection(Component... components)
    {
        b.add(components);
    }
    
    public void addFooter(Component... component)
    {
        c.add(component);
    }
}
