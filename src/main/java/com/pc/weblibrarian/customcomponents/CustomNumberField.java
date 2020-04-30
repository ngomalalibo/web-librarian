package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.textfield.NumberField;

public class CustomNumberField extends NumberField
{
    public CustomNumberField(String label, String placeHolder, boolean enabled, int min, int max, boolean hasControls, boolean autoCorrect)
    {
        super(label, placeHolder);
        
        setEnabled(enabled);
        setMin(min);
        setMax(max);
        setHasControls(hasControls);
        setAutocorrect(autoCorrect);
    }
}
