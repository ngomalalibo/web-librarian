package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.textfield.Autocapitalize;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;

public class CustomTextArea extends TextArea
{
    public CustomTextArea(String label, ValueChangeMode vcm, Icon icon, boolean clearBtn, String placeHolder, String theme)
    {
        super(label);
        addClassNames("btn-mr");
        getStyle().set("minHeight", "150px");
        setValueChangeMode(vcm);
        setPrefixComponent(icon);
        //setSuffixComponent(new HTMLComponents.TextFieldClearButton(this));
        setClearButtonVisible(clearBtn);
        setPlaceholder(placeHolder);
        setAutocapitalize(Autocapitalize.SENTENCES);
        setAutocomplete(Autocomplete.ON);
        
        setAutofocus(true);
        setAutoselect(true);
        
        // Following 2 theme lines should do the same thing
        addThemeName(theme);
        getElement().setAttribute("theme", theme);
    }
}