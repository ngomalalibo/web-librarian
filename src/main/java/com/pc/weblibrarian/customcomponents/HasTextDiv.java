package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.html.Div;

public class HasTextDiv extends Div implements HasText
{
    @Override
    public void setText(String text)
    {
        this.setText(text);
    }
    
    @Override
    public String getText()
    {
        return this.getText();
    }
}
