package com.pc.weblibrarian.views;

import com.pc.weblibrarian.customcomponents.IconText;
import com.pc.weblibrarian.customcomponents.NavigationBar;
import com.pc.weblibrarian.views.managers.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*@JsModule("./styles/bootstrap.js")
@JsModule("./styles/jquery-1.11.2.min.js")
@JsModule("./styles/jquery-3.4.1.min.js")
@JsModule("./styles/popper.min.js")
@JsModule("./styles/modernizr-2.8.3-respond-1.4.2.min.js")*/
@UIScope
@org.springframework.stereotype.Component
@Push
@Slf4j
@JsModule("./styles/shared-styles.js") // . referenceces the webapp directory in main directory
@CssImport("./styles/css/all.css")
@CssImport("./styles/css/maincss.css")
@CssImport("./styles/css/animate.css")
@CssImport("./styles/css/bootstrap.min.css")
@CssImport("./styles/css/publicpage.css")
@PWA(name = "Web Librarian CMS", shortName = "LibraryCMS", offlineResources = {"./styles/offline.css", "./styles/offline.png"}, enableInstallPrompt = true)
// @RoutePrefix(value = "", absolute = true)
// @PreAuthorize(value = "hasRole('USER')")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainFrame extends AppLayout implements PageConfigurator, BeforeEnterObserver
{
    
    private NavigationBar sidenavbar;
    
    public MainFrame()
    {
        super();
        UI.getCurrent().getPage().addJavaScript("./styles/jquery-3.4.1.min.js");
        UI.getCurrent().getPage().addJavaScript("./styles/jquery-1.11.2.min.js");
        UI.getCurrent().getPage().addJavaScript("./styles/popper.min.js");
        UI.getCurrent().getPage().addJavaScript("./styles/modernizr-2.8.3-respond-1.4.2.min.js");
        UI.getCurrent().getPage().addJavaScript("./styles/bootstrap.js");
        
        getElement().setAttribute("theme", "dark");
        
        addToDrawer(navigationLinks());
    }
    
    private void createHeader()
    {
        H1 logo = new H1("Web Librarian and Content Management System");
        logo.addClassName("logo");
        
        Anchor logout = new Anchor("/logout", "Log out");
        logout.addClassNames("card-link", "mr-5");
        
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);
        
        header.expand(logo);
        header.addClassName("ml-5");
        
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");
        header.setSizeFull();
        header.setMinHeight("150px");
        
        addToNavbar(header);
    }
    
    private Component navigationLinks()
    {
        
        sidenavbar = new NavigationBar();
        sidenavbar.addClassNames("sidenav");
        sidenavbar.setHeightFull();
        sidenavbar.getElement().setAttribute("theme", "dark");
        sidenavbar.getStyle().set("paddingLeft", "20px");
        sidenavbar.setWidth("200px");
        
        Span catalog = new Span("CATALOG");
        catalog.setClassName("hideonsmall");
        
        Div dashboardTxt = new Div(catalog);
        dashboardTxt.addClassNames("vnav-sectionheader", "navpad");
        
        sidenavbar.addSection(dashboardTxt);
        sidenavbar.addSection(routerLink("Publications", new PublicationManager(), "paperclip.svg", HighlightConditions.sameLocation()));
        sidenavbar.addSection(routerLink("Media", new MediaManager(), "music.svg", HighlightConditions.sameLocation()));
        sidenavbar.addSection(routerLink("Requests", new OrderManager(), "activity.svg", HighlightConditions.sameLocation()));
        //check in out waiting list should be here, Transaction handling
        //sidenavbar.addSection(routerLink("View/Place Orders", new PlaceOrder(), "music.svg", HighlightConditions.sameLocation()));
//        sidenavbar.addSection(routerLink("Publications", StartPage.class, "book.svg", HighlightConditions.sameLocation()));
//        sidenavbar.addSection(routerLink("Audio / Video", AuthorManager.class, "film-light.svg", HighlightConditions.never()));
//        sidenavbar.addSection(routerLink("Authors", AuthorManager.class, "users.svg", HighlightConditions.never()));
//        sidenavbar.addSection(routerLink("Publishers", AuthorManager.class, "book.svg", HighlightConditions.never()));
        
        Span admin = new Span("ADMINISTRATION");
        admin.setClassName("hideonsmall");
        
        Div administrationtxt = new Div(admin);
        administrationtxt.addClassNames("vnav-sectionheader", "navpad");
        
        if (LoginPage.getUserRoleType().isSuperAdmin() || LoginPage.getUserRoleType().isAdmin())
        {
            sidenavbar.addSection(routerLink("Configuration", new ConfigurationManager(), "settings-light.svg", HighlightConditions.sameLocation()));
//        sidenavbar.addSection(routerLink("Activity Log", AuthorManager.class, "activity-light.svg", HighlightConditions.never()));
        }
        
        
        sidenavbar.addSection(administrationtxt);
        // if (getUserRoleType() == PersonRoleType.ROLE_SUPERADMIN)
        if (LoginPage.getUserRoleType().isSuperAdmin() || LoginPage.getUserRoleType().isAdmin())
        {
            sidenavbar.addSection(routerLink("Authors", new AuthorManager(), "list-light.svg", HighlightConditions.sameLocation()));
            sidenavbar.addSection(routerLink("People", new PersonManager(), "clock-light.svg", HighlightConditions.sameLocation()));
            sidenavbar.addSection(routerLink("Publishers", new PublisherManager(), "list.svg", HighlightConditions.sameLocation()));
            sidenavbar.addSection(routerLink("Articles", new ArticleManager(), "book-open.svg", HighlightConditions.sameLocation()));
            sidenavbar.addSection(routerLink("User Accounts", new LibraryUserManager(), "dollar-sign-light.svg", HighlightConditions.sameLocation()));
//        sidenavbar.addSection(routerLink("Inventory", AuthorManager.class, "layers-light.svg", HighlightConditions.never()));
//        sidenavbar.addSection(routerLink("Accounts", AuthorManager.class, "dollar-sign-light.svg", HighlightConditions.never()));
//        sidenavbar.addSection(routerLink("MailBox", AuthorManager.class, "mail-light.svg", HighlightConditions.never()));
        }
        
        /*Span susers = new Span("USERS");
        susers.setClassName("hideonsmall");
        
        Div usertxt = new Div(susers);
        usertxt.addClassNames("vnav-sectionheader", "navpad");
        sidenavbar.addSection(usertxt);*/
        
        Div blockspacer = new Div();
        blockspacer.addClassName("blockspacer");
        sidenavbar.addSection(blockspacer);

//        sidenavbar.addSection(routerLink("Configuration", AuthorManager.class, "settings-light.svg", HighlightConditions.never()));

//        Span username = new Span(getAdminUser());
        Span username = new Span(LoginPage.getLoggedInUser());
        Div userinfo = new Div(username);
        username.addClassNames("username-disp", "hideonsmall");
        
        sidenavbar.addFooter(userinfo);
        
        // sidenavbar.addSection(routerLink("Signout", new LoginPage(), "log-out-light.svg", HighlightConditions.sameLocation()));
        createHeader();
//
        sidenavbar.addFooter(userinfo);
//        sidenavbar.addFooter(routerLink("Sign Out", StartPage.class, "log-out-light.svg", HighlightConditions.never()));
        
        /*Router router = UI.getCurrent().getRouter();
        List<RouteData> routes = router.getRoutes();
        
        Details routedetails = new Details();
        routedetails.setSummaryText("Small");
        routedetails.setSummaryText("Route Details");
        routedetails.addThemeVariants(DetailsVariant.SMALL);
        routedetails.setOpened(true);
        routes.forEach(s ->
                       {
                           if (!s.getUrl().equals("passwordreset") && !s.getUrl().equals("emailvalidation"))
                           {
                               ListItem r = new ListItem(new RouterLink(s.getUrl(), s.getNavigationTarget()));
                               r.addClassNames("list-group-item-info");
                               routedetails.addContent(r);
                           }
                       });
        
        sidenavbar.addFooter(routedetails);*/
        return sidenavbar;
    }
    
    public <E extends Component> RouterLink routerLink(String text, Component target, String icon, HighlightCondition<RouterLink> hl)
    {
        // setContent(target);
        RouterLink link = new RouterLink("", target.getClass());
        link.add(new IconText(icon, text));
        link.setHighlightCondition(hl);
        link.addClassName("routerlink");
        
        return link;
        
    }
    
    @Override
    public void configurePage(InitialPageSettings settings)
    {
        
        // String faviconhref = "frontend/images/other/icon-192x192.png";
        String faviconhref = "images/other/icon-192x192.png";
        
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("rel", "apple-touch-icon");
        attributes.put("rel", "shortcut icon");
        settings.addLink(faviconhref, attributes);
    
        
        /*{
            HashMap<String, String> attributes = new HashMap<>();
            attributes.put("rel", "apple-touch-icon");
            settings.addLink(faviconhref, attributes);
        }
        
        {
            HashMap<String, String> attributes = new HashMap<>();
            attributes.put("rel", "shortcut icon");
            settings.addLink(faviconhref, attributes);
        }*/
    }
    
    
    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        /*if (LoginPage.loginStatus)
        {
            VaadinSession.getCurrent().setAttribute("originaltarget", event.getNavigationTarget());
            
        }
        else
        {
            event.forwardTo(LoginPage.class);
        }*/
    }
    
    public static void main(String[] args)
    {
        Router router = UI.getCurrent().getRouter();
        List<RouteData> routes = router.getRoutes();
        
        routes.forEach(d ->
                       {
                           System.out.println("url " + d.getUrl());
                           System.out.println("route " + d.toString());
                       });
    }
    
    public Button backgroundHeavyLifting()
    {
        return new Button("Click me", e ->
        {
            Notification.show("Running job on the background");
            final UI ui = UI.getCurrent(); //
            ExecutorService executor = Executors.newSingleThreadExecutor(); //
            
            executor.submit(() ->
                            {
                                ///doHeavyStuff(); //
                                ui.access(() ->
                                          { //
                                              Notification.show("Calculation done"); //
                                          });
                            });
        });
    }
}
