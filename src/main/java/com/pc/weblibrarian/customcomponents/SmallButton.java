package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;

public class SmallButton extends Button
{
    public SmallButton() {
        setThemeName("small");
        addClassName("btnsizeb");
    }
    
    public SmallButton(Component icon) {
        setThemeName("small icon");
        setIcon(icon);
    }
    
    public SmallButton(String text) {
        this();
        setText(text);
    }
    
    public SmallButton theme(String theme) {
        addThemeName(theme);
        return this;
    }
    
    public SmallButton onclick(Click clk) {
        addClickListener(c -> clk.onclick());
        return this;
    }
    
    public interface Click {
        void onclick();
    }
}
