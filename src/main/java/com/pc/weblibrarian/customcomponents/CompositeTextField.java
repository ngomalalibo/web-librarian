package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Label;

public class CompositeTextField extends Composite<Div>
{
    private Label label;
    private Input input;
    
    public CompositeTextField(String labelText, String value)
    {
        label = new Label();
        label.setText(labelText);
        input = new Input();
        input.setValue(value);
        getContent().add(label, input);
    }
}
