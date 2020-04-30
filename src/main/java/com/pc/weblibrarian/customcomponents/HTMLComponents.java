package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.textfield.TextField;

public class HTMLComponents
{
    public static final String G_W_SM = "4rem";
    public static final String G_W_DATE = "7rem";
    public static final String G_W_PHONE = "10rem";
    public static final String G_W_EMAIL = "14rem";
    public static final String G_W_MONEY = "115px";
    public static final String G_W_MONEY_LG = "150px";
    public static String editButtonWidth = "64px";
    
    public static Span gridH(String text)
    {
        Span d = new Span(text.toUpperCase());
        d.addClassName("gridhead");
        return d;
    }
    
    public static String GLXC(String binding, String cssclass)
    {
        return String.format("<div class='%s'>[[item.%s]]</div>", cssclass, binding);
    }
    
    public static String GLABXC(String a, String b, String ca, String cb)
    {
        return String.format("<div class='%s'>[[item.%s]]</div><div class='%s'>[[item.%s]]</div>", ca, a, cb, b);
    }
    
    public static SmallButton getEditButton()
    {
        Icon icon = new Icon("lumo", "create");
        icon.setSize("var(--lumo-icon-size-s)");
        icon.setColor("var(--lumo-contrast-70pct)");
        SmallButton edit = new SmallButton(icon);
        edit.addClassName("edit-control");
        edit.getElement().setAttribute("on-click", "edit");
        return edit;
    }
    
    public static class TextFieldClearButton extends Button
    {
        public TextFieldClearButton(TextField searchbox)
        {
            getElement().setAttribute("theme", "tertiary-inline");
            setIcon(new Icon("lumo", "cross"));
            addClickListener(c -> searchbox.clear());
        }
    }
    
    public static ContextMenu getContextMenu()
    {
        ContextMenu contextMenu = new ContextMenu();
        Component target =  null/*createTargetComponent()*/;
        contextMenu.setTarget(target);
        Label message = new Label("-");
        contextMenu.addItem("First menu item",
                            e -> message.setText("Clicked on " +
                                                         "the first item"));
        contextMenu.addItem("Second menu item",
                            e -> message.setText("Clicked on " +
                                                         "the second item"));
// The created MenuItem component can be saved
// for later use
        MenuItem item = contextMenu.addItem("Disabled " +
                                                    "menu item",
                                            e -> message.setText("This cannot happen"));
        item.setEnabled(false);
        
        return contextMenu;
    }
    
    public static class BlockQuote extends Div
    {
        
        public BlockQuote(Component... component) {
            addClassName("blockquoute");
            add(component);
        }
        
        public BlockQuote color(String color) {
            getStyle().set("borderLeftColor", color);
            return this;
        }
        
    }
    
}
