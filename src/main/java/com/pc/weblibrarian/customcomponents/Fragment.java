package com.pc.weblibrarian.customcomponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fragment extends FlexLayout
{
//    private static final long serialVersionUID = 1L;
    
    private Div titleA = new Div();
    private FlexLayout titleB = new FlexLayout();
    private FlexComponent headerflx = new FlexComponent(titleA, titleB);
    private FlexLayout midcontent = new FlexLayout();
    private Div toolbar = new Div();
    private Div footer = new Div();
    
    public static Logger log = LoggerFactory.getLogger(Fragment.class);
    
    public Fragment()
    {
        
        addClassNames("animated", "fadeIn", "faster");
        
        getStyle().set("flexGrow", "1").set("flexDirection", "column");
        //getStyle().set("flexDirection", "column");
        setSizeFull();
        titleA.setClassName("gridellipsis");
        titleA.getStyle().set("flexGrow", "1");
        titleB.getStyle().set("flexGrow", "2");
        titleB.setClassName("gridellipsis");
        titleB.setJustifyContentMode(JustifyContentMode.END);
        
        headerflx.addClassName("fragmenttitle");
        //headerflx.getStyle().set("flexDirection", "column");
        
        midcontent.getStyle().set("flexGrow", "1").set("flexDirection", "column");
        midcontent.setSizeFull();
        
        // add(toolbar, midcontent, footer);
        add(headerflx, toolbar, midcontent, footer);
    }
    
    public void setHeaderText(String title)
    {
        titleA.setText(title);
    }
    
    //sets the component
    public void addHeaderComponent(Component... component)
    {
        titleA.removeAll();
        titleA.add(component);
    }
    
    public void appendHeaderComponent(Component... component)
    {
        titleA.add(component);
    }
    
    public void addHeaderBComponent(Component... component)
    {
        titleB.removeAll();
        titleB.add(component);
    }
    
    public void appendHeaderBComponent(Component... component)
    {
        titleB.add(component);
    }
    
    public void addToolbarComponent(Component... component)
    {
        toolbar.add(component);
    }
    
    public void addContent(Component... component)
    {
        midcontent.add(component);
    }
    
    public void addFooterComponent(Component... component)
    {
        footer.add(component);
    }
    
    public static class HeaderControls extends FlexLayout
    {
        
        public HeaderControls()
        {
            getStyle().set("flexGrow", "1");
            setJustifyContentMode(JustifyContentMode.END);
            setAlignItems(Alignment.BASELINE);
        }
        
        public HeaderControls(Component... component)
        {
            this();
            add(component);
        }
        
    }
    
}
