package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;

import java.util.Collection;

public class CustomComboBox<T> extends ComboBox
{
    public CustomComboBox(String label, Collection items, ItemLabelGenerator<T> itemLabel, String colspan, boolean clearBtn)
    {
        super(label, items);
        setClearButtonVisible(clearBtn);
        getElement().setAttribute("colspan", colspan);
        if (itemLabel != null)
        {
            setItemLabelGenerator(itemLabel);
        }
    }
}
