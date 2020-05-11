package com.pc.weblibrarian.views;

import com.pc.weblibrarian.security.SecuredByRole;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
// import com.vaadin.flow.spring.annotation.SpringComponent;

// @Component
@UIScope
@SecuredByRole(value = {"USER"})
@Route(value = "start", layout = MainFrame.class)
public class StartPage extends VerticalLayout implements BeforeEnterObserver
{
    
    public StartPage()
    {
        super();
        getStyle().set("padding", "1em");
        VerticalLayout vl = new VerticalLayout();
        vl.getStyle().set("padding-bottom", "50px");
        vl.add(new H3("Web Librarian is also a content management system for notes, articles, quotes and a trusted journal"));
        
        add(vl);
        add(new H3("With Weblibrarian you can do the following: "));
        
        UnorderedList featuresUL = new UnorderedList(new ListItem("Manage Library Publications and Media"));
        featuresUL.add(new ListItem("Manage Library Transactions"));
        featuresUL.add(new ListItem("Manage Library Users"));
        featuresUL.add(new ListItem("Manage Library Resources"));
        featuresUL.add(new ListItem("Publish articles, notes and quotes online via the CMS"));
        add(featuresUL);
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent)
    {
    
    }
}
