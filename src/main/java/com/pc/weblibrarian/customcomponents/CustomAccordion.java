package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class CustomAccordion extends Accordion
{
    public CustomAccordion()
    {
        VerticalLayout personalInformationLayout =
                new VerticalLayout();
        personalInformationLayout.add(
                new TextField("Name"),
                new TextField("Phone"),
                new TextField("Email")
        );
        add("Personal Information",
            personalInformationLayout);
        VerticalLayout billingAddressLayout =
                new VerticalLayout();
        billingAddressLayout.add(
                new TextField("Address"),
                new TextField("City"),
                new TextField("State"),
                new TextField("Zip Code")
        );
        add("Billing Address",
            billingAddressLayout);
        VerticalLayout paymenLayout =
                new VerticalLayout();
        paymenLayout.add(
                new Span("Not yet implemented")
        );
        AccordionPanel billingAddressPanel =
                add("Payment", paymenLayout);
        billingAddressPanel.setEnabled(false);
    }
}
