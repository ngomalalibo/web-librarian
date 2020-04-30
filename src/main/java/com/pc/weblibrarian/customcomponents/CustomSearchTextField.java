package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class CustomSearchTextField extends TextField
{
    public CustomSearchTextField(String label, ValueChangeMode vcm, Icon icon, boolean clearBtn, String placeHolder, String theme)
    {
        super(label);
        addClassNames("searchbox", "btn-mr");
        setValueChangeMode(vcm);
        setPrefixComponent(icon);
        setSuffixComponent(new HTMLComponents.TextFieldClearButton(this));
        setClearButtonVisible(clearBtn);
        setPlaceholder(placeHolder);
        
        // Following 2 theme lines should do the same thing
        addThemeName(theme);
        //getElement().setAttribute("theme", theme);
    }
}