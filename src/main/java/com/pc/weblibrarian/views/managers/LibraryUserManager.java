package com.pc.weblibrarian.views.managers;

import com.pc.weblibrarian.customcomponents.CustomNotification;
import com.pc.weblibrarian.customcomponents.CustomSearchTextField;
import com.pc.weblibrarian.customcomponents.Fragment;
import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.LibraryUsersDP;
import com.pc.weblibrarian.entity.LibraryUser;
import com.pc.weblibrarian.security.SecuredByRole;
import com.pc.weblibrarian.views.BasicDialog;
import com.pc.weblibrarian.views.MainFrame;
import com.pc.weblibrarian.views.dialogs.LibraryUserCRUDDIalog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

@SecuredByRole(value = {"USER"})
@Route(value = "libraryuser", layout = MainFrame.class)
@UIScope
// @PreserveOnRefresh
public class LibraryUserManager extends Fragment
{
    private static final boolean superadmin = true;
    
    LibraryUsersDP dp = new LibraryUsersDP();
    ConfigurableFilterDataProvider<LibraryUser, Void, String> fdp = dp.withConfigurableFilter();
    
    Grid<LibraryUser> grid = new Grid<>(LibraryUser.class, false);
    
    int total;
    
    public LibraryUserManager()
    {
        super();
        total = fdp.size(new Query<>());
        Span totalcountspan = new Span(total + " Total");
        totalcountspan.addClassNames("secondary-text", "font-size-xs");
        fdp.addDataProviderListener(lst -> totalcountspan.setText(total + " Total"));
        
        CustomSearchTextField searchbox = new CustomSearchTextField("Search", ValueChangeMode.EAGER, new Icon("lumo", "search"), false/*not needed here with TextFieldClearButton in use*/, "Search By Name/Title", "small");
        searchbox.addValueChangeListener(event ->
                                         {
                                             fdp.setFilter(event.getValue());
                                             fdp.refreshAll();
            
                                             total = fdp.size(new Query<>());
                                             totalcountspan.setText(total + " Total");
                                         });
        grid.setDataProvider(fdp);
        grid.setMultiSort(true);
        grid.setColumnReorderingAllowed(true);
        
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        GridSingleSelectionModel<LibraryUser> singleSelect = (GridSingleSelectionModel<LibraryUser>) grid.getSelectionModel();
        singleSelect.asSingleSelect();
        
        grid.setItemDetailsRenderer(
                new ComponentRenderer<>(libraryUser ->
                                        {
                                            VerticalLayout layout = new VerticalLayout();
                    
                                            return layout;
                                        }));
        
        grid.addColumn(LibraryUser::getUserEmail).setHeader("Email");
        grid.addColumn(d -> d.getPerson().getFullName()).setHeader("Name");
        grid.addColumn(LibraryUser::getPersonRoleTypes).setHeader("Role");
        grid.addColumn(LibraryUser::getAccessToConsumable).setHeader("Access to Consumable");
        grid.addColumn(LibraryUser::getAccessToRentable).setHeader("Access to Rentable");
        grid.addColumn(LibraryUser::getAccountBanned).setHeader("Account Banned");
        grid.addColumn(LibraryUser::getAccountLocked).setHeader("Account Locked");
        grid.addColumn(LibraryUser::getIsAdmin).setHeader("Admin");
        grid.addColumn(LibraryUser::getLastLoginDateTime).setHeader("Last Login");
        grid.addColumn(LibraryUser::getMaximumCheckoutItems).setHeader("Maximum Check out");
        
        grid.addComponentColumn(libraryUser ->
                                {
                                    Div fl = new Div();
                                    fl.getStyle().set("textAlign", "right");
            
                                    Button edit = new Button(new IronIcon("lumo", "edit"));
                                    edit.getElement().setAttribute("theme", "small icon");
                                    edit.getStyle().set("marginRight", "0.4em");
                                    edit.addClickListener(click ->
                                                          {
                                                              //System.out.println("libraryItem author = " + libraryItem.getPublication().getAuthor().getPerson().getFullName());
                                                              new LibraryUserCRUDDIalog(libraryUser, superadmin ? BasicDialog.BeanAction.EDIT : BasicDialog.BeanAction.VIEW,
                                                                                        (a) ->
                                                                                        {
                                                                                            // grid.getDataProvider().refreshItem(a);
                                                                                            fdp.refreshItem(a);
                                                                                            grid.getDataProvider().refreshAll();
                                                                                            UI.getCurrent().getPage().reload();
                                                                                            return true;
                                                                                        }, BasicDialog.DialogSize.LARGE).open();
                                                          });
                                    Button delete = new Button(new IronIcon("lumo", "cross"));
                                    delete.getElement().setAttribute("theme", "small icon error");
                                    delete.addClickListener(click ->
                                                            {
                                                                new CustomNotification("Delete Clicked", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                
                                                                String deleteProperty = libraryUser.getPerson().getFullName();
                                                                new ConfirmDialog("Delete library user",
                                                                                  String.format("Are you sure you want to delete %s?", deleteProperty), "Yes " + BasicDialog.BeanAction.DELETE.displayText(),
                                                                                  (e) ->
                                                                                  {
                                                                                      libraryUser.delete(Connection.DB_USER, libraryUser);
                                                                                      System.out.printf("Deleted library user: %s\n", deleteProperty);
                                                                                      System.out.println("Library user deleted successfully");
                                                                                      new CustomNotification("Library user deleted successfully", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                    
                                                                                      grid.getDataProvider().refreshItem(libraryUser);
                                                                                      grid.getDataProvider().refreshAll();
                    
                                                                                      e.getSource().close();
                                                                                  }, "Do not delete", (e) ->
                                                                                  {
                                                                                      new CustomNotification("User not deleted", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                                                                                      System.out.println("User not deleted");
                                                                                      e.getSource().close();
                                                                                  }).open();
                                                            });
            
                                    fl.add(edit, delete);
                                    return fl;
                                }).setFlexGrow(0).setWidth("100px").setResizable(true);
        addHeaderComponent(new H1("Fragment Main Header"));
        addHeaderComponent(new H3("Library Users"));
        
        SmallButton addentity = new SmallButton("New Library User").theme("primary");
//        addentity.setIcon(new Icon("lumo", "add"));
        addentity.setIcon(new Icon(VaadinIcon.ADD_DOCK));
        //addentity.setVisible(MainFrame.getUserRoleType().isSuperAdmin());
        
        addentity.addClickListener(clk -> new LibraryUserCRUDDIalog(new LibraryUser(),
                                                                    BasicDialog.BeanAction.NEW,
                                                                    (a) ->
                                                                    {
                                                                        //refresh data
                                                                        //refreshDataTable(a);
                                                                        fdp.refreshItem(a);
                                                                        grid.getDataProvider().refreshItem(a);
                                                                        grid.getDataProvider().refreshAll();
                                                                        UI.getCurrent().getPage().reload();
                                                                        return true;
                                                                    }, BasicDialog.DialogSize.LARGE).open());
        appendHeaderBComponent(new HeaderControls(searchbox, addentity));
        addFooterComponent(totalcountspan);
        
        addToolbarComponent(new Paragraph("You can sort, edit and delete records on this grid."));
        
        addContent(grid);
        addFooterComponent(new H6("Fragment Footer"));
        
        
    }
}
