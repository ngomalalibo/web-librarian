package com.pc.weblibrarian.views.managers;

import com.pc.weblibrarian.customcomponents.CustomNotification;
import com.pc.weblibrarian.customcomponents.CustomSearchTextField;
import com.pc.weblibrarian.customcomponents.Fragment;
import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.PublicationDP;
import com.pc.weblibrarian.entity.LibraryItem;
import com.pc.weblibrarian.security.SecuredByRole;
import com.pc.weblibrarian.views.BasicDialog;
import com.pc.weblibrarian.views.MainFrame;
import com.pc.weblibrarian.views.dialogs.LibraryItemCRUDDialog;
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
import org.springframework.security.access.prepost.PreAuthorize;

// @PreAuthorize(value = "hasAnyRole('ADMIN')")
@SecuredByRole(value = {"USER"})
@Route(value = "publicationItem", layout = MainFrame.class)
@PreserveOnRefresh
public class PublicationManager extends Fragment
{
    private static final boolean superadmin = true;
    
    private PublicationDP dp = new PublicationDP();
    private ConfigurableFilterDataProvider<LibraryItem, Void, String> fdp = dp.withConfigurableFilter();
    
    Grid<LibraryItem> grid = new Grid<>(LibraryItem.class, false);
    
    LibraryItem li = new LibraryItem();
    
    int total;
    
    public PublicationManager()
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
        grid.addThemeNames("no-border", "no-row-borders", "row-stripes");
        
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        GridSingleSelectionModel<LibraryItem> singleSelect = (GridSingleSelectionModel<LibraryItem>) grid.getSelectionModel();
        singleSelect.asSingleSelect();
        
        grid.setItemDetailsRenderer(
                new ComponentRenderer<>(libraryItem ->
                                        {
                                            VerticalLayout layout = new VerticalLayout();
                    
                                            layout.add(new Label(libraryItem.getLibraryItemName()));
                                            layout.add(new Label(libraryItem.getLibraryItemType().getLibraryItemType()));
                    
                                            return layout;
                                        }));
        
        grid.addColumn(LibraryItem::getLibraryItemName).setHeader("Name/Title").setResizable(true);
        grid.addColumn(d -> d.getPublication().getAuthor().getPerson().getFullName()).setHeader("Author");
        grid.addColumn(LibraryItem::getLibraryItemType).setHeader("Type").setResizable(true);
        grid.addColumn(LibraryItem::getQuantityAvailable).setHeader("Quantity Available").setResizable(true);
        grid.addColumn(LibraryItem::getMaximumCheckoutCopies).setHeader("Checkout Quantity").setResizable(true);
        grid.addColumn(LibraryItem::getLibraryItemLocation).setHeader("Location").setResizable(true);
        grid.addColumn(LibraryItem::getContentRating).setHeader("Content Rating").setResizable(true);
        
        
        grid.addColumn(d -> d.getPublication().getGenre()).setHeader("Genre");
        grid.addColumn(d -> d.getPublication().getISBN_13()).setHeader("ISBN");
        grid.addColumn(d -> d.getPublication().getTitle()).setHeader("Title");
        grid.addColumn(d -> d.getPublication().getPubLanguage()).setHeader("Language");
        grid.addColumn(d -> d.getPublication().getReleaseYear()).setHeader("Release year");
        grid.addColumn(d -> d.getPublication().getPublicationType()).setHeader("Publication Type");
        grid.addColumn(d -> d.getPublication().getBindingType()).setHeader("Binding Type");
        
        grid.addComponentColumn(libraryItem ->
                                {
            
                                    try
                                    {
                                        li = (LibraryItem) libraryItem.clone();
                                    }
                                    catch (CloneNotSupportedException e)
                                    {
                                        e.getMessage();
                                    }
                                    Div fl = new Div();
                                    fl.getStyle().set("textAlign", "right");
            
                                    Button edit = new Button(new IronIcon("lumo", "edit"));
                                    edit.getElement().setAttribute("theme", "small icon");
                                    edit.getStyle().set("marginRight", "0.4em");
                                    edit.addClickListener(click ->
                                                          {
                                                              //System.out.println("libraryItem author = " + libraryItem.getPublication().getAuthor().getPerson().getFullName());
                                                              new LibraryItemCRUDDialog(libraryItem, superadmin ? BasicDialog.BeanAction.EDIT : BasicDialog.BeanAction.VIEW,
                                                                                        (a) ->
                                                                                        {
                                                                                            // grid.getDataProvider().refreshItem(a);
                                                                                            fdp.refreshItem(a);
                                                                                            grid.getDataProvider().refreshAll();
                                                                                            UI.getCurrent().getPage().reload();
                                                                                            return true;
                                                                                        }, BasicDialog.DialogSize.XXLARGE).open();
                                                          });
                                    Button delete = new Button(new IronIcon("lumo", "cross"));
                                    delete.getElement().setAttribute("theme", "small icon error");
                                    delete.addClickListener(click ->
                                                            {
                                                                new CustomNotification("Delete Clicked", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                
                                                                String deleteProperty = libraryItem.getLibraryItemName();
                                                                new ConfirmDialog("Delete library item",
                                                                                  String.format("Are you sure you want to delete %s?", deleteProperty), "Yes " + BasicDialog.BeanAction.DELETE.displayText(),
                                                                                  (e) ->
                                                                                  {
                                                                                      libraryItem.delete(Connection.DB_LIBRARYITEM, libraryItem);
                                                                                      System.out.printf("Deleted library item: %s\n", deleteProperty);
                                                                                      System.out.println("Library item deleted successfully");
                                                                                      new CustomNotification("Library item deleted successfully", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                    
                                                                                      grid.getDataProvider().refreshItem(libraryItem);
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
        addHeaderComponent(new H3("Publications"));
        
        SmallButton addentity = new SmallButton("New Library Item").theme("primary");
//        addentity.setIcon(new Icon("lumo", "add"));
        addentity.setIcon(new Icon(VaadinIcon.ADD_DOCK));
        //addentity.setVisible(MainFrame.getUserRoleType().isSuperAdmin());
        
        addentity.addClickListener(clk -> new LibraryItemCRUDDialog(new LibraryItem(),
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
                                                                    }, BasicDialog.DialogSize.XXLARGE).open());
        
        appendHeaderBComponent(new HeaderControls(searchbox, addentity));
        addFooterComponent(totalcountspan);
        
        addToolbarComponent(new Paragraph("You can sort, edit and delete records on this grid."));
        
        addContent(grid);
        addFooterComponent(new H6("Fragment Footer"));
    }
}
