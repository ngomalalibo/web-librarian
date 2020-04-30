package com.pc.weblibrarian.views;

import com.pc.weblibrarian.entity.PersistingBaseEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.dom.Style;

public class BasicDialog extends Dialog
{
    private final Span header = new Span();
    private final Div headerB = new Div();
    private final Div headerflex = new Div(header, headerB);
    private final FlexLayout contentdiv = new FlexLayout();
    private final Div infoBelowContent = new Div();
    private final FlexLayout formComponentFlex = new FlexLayout();
    private final FlexLayout footdiv = new FlexLayout(formComponentFlex);
    private final Div parent = new Div();
    
    
    private boolean dialogChangeStatus;
    
    //AppConfiguration config = AppConfiguration.get();
    
    public boolean getDialogChangeStatus()
    {
        return dialogChangeStatus;
    }
    
    public void setDialogChangeStatus(boolean status)
    {
        this.dialogChangeStatus = status;
    }
    
    public BasicDialog(BeanAction actiontype, DialogSize dialogSize)
    {
        
        setSizeFull();

//        getElement().getStyle().set("overflow", "hidden");
//        getElement().getStyle().set("overflow", "no-display");
        //getElement().getStyle().set("overflow", "-moz-scrollbars-none");
        //getElement().getStyle().set("height", "100%");
        getElement().getStyle().set("scroll", "no");
        
        contentdiv.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        
        header.setClassName("dialogheader");
        
        //formComponentFlex.getStyle().set("flexGrow", "1");
        infoBelowContent.getElement().getStyle().set("border", "1px solid ivory");
        infoBelowContent.getElement().getStyle().set("color", "ivory");
        infoBelowContent.addClassName("text-center");
        infoBelowContent.setVisible(false);
        infoBelowContent.setSizeFull();
        infoBelowContent.setMaxHeight("30px");
        infoBelowContent.setMinHeight("30px");
        infoBelowContent.setHeight("30px");
        
        footdiv.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        
        header.setClassName("dialogheader");
        
        if (actiontype == BeanAction.VIEW)
        {
           /* SmallButton cancel = new SmallButton("Close").theme("tertiary contrast");
            cancel.addClassName("ml-2");
            cancel.addClickListener(e -> close());
            footdiv.add(cancel);*/
        }
        else if (actiontype == BeanAction.NONE)
        {
            footdiv.setVisible(false);
        }
        else if (actiontype == BeanAction.EDIT || actiontype == BeanAction.NEW)
        {
            /*SmallButton cancel = new SmallButton("Cancel").theme("tertiary error");
            cancel.addClassName("ml-2");
            cancel.addClickListener(e -> close());
            footdiv.add(cancel);*/
        }
        
        if (actiontype == BeanAction.EDIT || actiontype == BeanAction.NEW)
        {
            setCloseOnEsc(false);
            setCloseOnOutsideClick(false);
        }
        
        if (actiontype == BeanAction.DELETE)
        {
            header.getStyle().set("color", "var(--lumo-error-color)");
            headerflex.getStyle().set("backgroundColor", "var(--lumo-error-color)");
            footdiv.getStyle().set("background", "#0000");
        }
        else
        {
//			headerflex.getStyle().set("backgroundColor", "var(--lumo-primary-color)");
        }
        
        setSize(dialogSize);
        
        headerflex.setId("dialog-top");
        contentdiv.setId("dialog-mid");
        footdiv.setId("dialog-low");
        
        parent.add(headerflex, contentdiv, infoBelowContent, footdiv);
        
        add(parent);
    }
    
    public Style getBackgroundStyle()
    {
        return parent.getStyle();
    }
    
    protected void setSize(DialogSize size)
    {
        switch (size)
        {
            case XXLARGE:
                headerflex.getStyle().set("maxWidth", "1300px").set("width", "1300px");
                contentdiv.getStyle().set("maxWidth", "1300px").set("width", "1300px");
                infoBelowContent.getStyle().set("maxWidth", "1300px").set("width", "1300px");
                footdiv.getStyle().set("maxWidth", "1300px").set("width", "1300px");
                break;
            case XLARGE:
                headerflex.getStyle().set("maxWidth", "900px").set("width", "900px");
                contentdiv.getStyle().set("maxWidth", "900px").set("width", "900px");
                infoBelowContent.getStyle().set("maxWidth", "900px").set("width", "900px");
                footdiv.getStyle().set("maxWidth", "900px").set("width", "900px");
                break;
            case LARGE:
                headerflex.getStyle().set("maxWidth", "750px").set("width", "750px");
                contentdiv.getStyle().set("maxWidth", "750px").set("width", "750px");
                infoBelowContent.getStyle().set("maxWidth", "750px").set("width", "750px");
                footdiv.getStyle().set("maxWidth", "750px").set("width", "750px");
                break;
            case MID:
                headerflex.getStyle().set("maxWidth", "600px").set("width", "600px");
                contentdiv.getStyle().set("maxWidth", "600px").set("width", "600px");
                infoBelowContent.getStyle().set("maxWidth", "600px").set("width", "600px");
                footdiv.getStyle().set("maxWidth", "600px").set("width", "600px");
                
                break;
            default:
                headerflex.getStyle().set("maxWidth", "300px").set("width", "300px");
                contentdiv.getStyle().set("maxWidth", "300px").set("width", "300px");
                infoBelowContent.getStyle().set("maxWidth", "300px").set("width", "300px");
                footdiv.getStyle().set("maxWidth", "300px").set("width", "300px");
                break;
        }
    }
    
    public void setTitle(String text)
    {
        header.setText(text);
    }
    
    public void setTitle(Component... components)
    {
        header.add(components);
    }
    
    void setTitleB(String text)
    {
        Span headerB = new Span(text);
        headerB.addClassNames("secondary-text", "font-size-xs", "gridellipsis");
        addHeaderBComponent(headerB);
    }
    
    public void addHeaderBComponent(Component... c)
    {
        headerB.add(c);
    }
    
    public void setContent(Component... component)
    {
        contentdiv.removeAll();
        contentdiv.add(component);
    }
    
    public void addContent(Component... component)
    {
        contentdiv.add(component);
    }
    
    public void setErrorDivContent(Component component)
    {
        infoBelowContent.removeAll();
        infoBelowContent.add(component);
    }
    
    public void setErrorMessage(String message)
    {
        infoBelowContent.setText(message);
    }
    
    public Div getErrorDiv()
    {
        return infoBelowContent;
    }
    
    public void addFooterComponent(Component... components)
    {
        for (Component component : components)
        {
            Div dv = new Div(component);
            dv.addClassName("btn-mr");
            formComponentFlex.add(dv);
        }
    }
    
    public void addTerminalComponent(Component... components)
    {
        for (Component component : components)
        {
            Div dv = new Div(component);
            dv.addClassName("btn-ml");
            footdiv.add(dv);
        }
    }
    
    public enum BeanAction
    {
        NEW("New"), EDIT("Edit"), DELETE("Delete"), VIEW("View"), NONE("None");
        
        private final String value;
        
        BeanAction(String v)
        {
            value = v;
        }
        
        public String getValue()
        {
            return value;
        }
        
        public static String getDisplayText(BeanAction i)
        {
            switch (i)
            {
                case NEW:
                    return "New";
                case EDIT:
                    return "Edit";
                case DELETE:
                    return "Delete";
                case VIEW:
                    return "View";
                case NONE:
                    return "None";
                default:
                    return "Unknown";
            }
        }
        
        public String displayText()
        {
            return getDisplayText(this);
        }
        
        public static BeanAction fromValue(String v)
        {
            for (BeanAction c : BeanAction.values())
            {
                if (c.value.equals(v))
                {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }
        
    }
    
    public enum DialogSize
    {
        SMALL, MID, LARGE, XLARGE, XXLARGE
    }
    
    
    private boolean hasChanges()
    {
// no-op implementation
        return true;
    }
    
    public interface Action<T extends PersistingBaseEntity>
    {
        //boolean parameter may not be needed anymore
        boolean action(T bean);
    }
    
}

