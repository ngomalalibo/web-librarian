package com.pc.weblibrarian.views.dialogs;

import com.pc.weblibrarian.entity.Address;
import com.pc.weblibrarian.enums.AddressType;
import com.pc.weblibrarian.model.Country;
import com.pc.weblibrarian.model.State;
import com.pc.weblibrarian.utils.GetCountriesAndStates;
import com.pc.weblibrarian.views.BasicDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.EnumSet;

public class AddressDialog extends BasicDialog
{
    public AddressDialog(BeanAction actiontype)
    {
        super(actiontype, DialogSize.MID);
        
        Binder<Address> binder = new Binder<>(Address.class);
        
        TextField city = new TextField("City");
        binder.forField(city).bind("city");
        
        TextField zipcode = new TextField("Zipcode");
        binder.forField(zipcode).bind("zipCode");
        
        ComboBox<Country> countries = new ComboBox<>();
        countries.setItems(GetCountriesAndStates.getCountries());
        countries.setItemLabelGenerator(Country::getName);
        binder.forField(countries).bind("country");
        
        ComboBox<State> state = new ComboBox("State");
        binder.forField(state).bind("state");
        
        countries.addValueChangeListener(a ->
                                         {
                                             state.setItems(GetCountriesAndStates.getStates(countries.getValue().getCode()));
                                             state.setItemLabelGenerator(State::getRegion);
                                         });
        
        Checkbox primaryAddress = new Checkbox("Primary Address", false);
        binder.forField(primaryAddress).bind("primaryAddress");
        
        TextArea streetAddress = new TextArea("Street Address");
        streetAddress.getStyle().set("maxHeight", "150px");
        streetAddress.setPlaceholder("Address here ...");
        binder.forField(streetAddress).bind("streetAddressLine");
        
        RadioButtonGroup<AddressType> addressType = new RadioButtonGroup<>();
        addressType.setItems(EnumSet.allOf(AddressType.class));
        //addressType.setItems(AddressType.BILLINGADDRESS, AddressType.MAILINGADDRESS, AddressType.NONE);
        addressType.setLabel("Address Type");
        binder.forField(addressType).bind(Address::getAddressType, Address::setAddressType);
        
        HorizontalLayout buttonGroup = new HorizontalLayout();
        
        Button cancel = new Button("Cancel", new Icon(VaadinIcon.EXIT));
        cancel.setIconAfterText(false);
        
        Button save = new Button("Save", new Icon(VaadinIcon.DISC));
        cancel.setIconAfterText(false);
        
        buttonGroup.add(cancel, save);
        
        setContent(city, zipcode, countries, state, streetAddress, addressType, buttonGroup, primaryAddress);
        
    }
}
