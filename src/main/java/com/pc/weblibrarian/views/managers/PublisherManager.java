package com.pc.weblibrarian.views.managers;

import com.pc.weblibrarian.customcomponents.CustomNotification;
import com.pc.weblibrarian.customcomponents.CustomSearchTextField;
import com.pc.weblibrarian.customcomponents.Fragment;
import com.pc.weblibrarian.customcomponents.SmallButton;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.PublisherDP;
import com.pc.weblibrarian.entity.Publisher;
import com.pc.weblibrarian.security.SecuredByRole;
import com.pc.weblibrarian.views.BasicDialog;
import com.pc.weblibrarian.views.MainFrame;
import com.pc.weblibrarian.views.dialogs.PublisherCRUDDialog;
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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

@SecuredByRole(value = {"USER", "ADMIN"})
@Route(value = "publisher", layout = MainFrame.class)
// @PreserveOnRefresh
@UIScope
public class PublisherManager extends Fragment
{
    private static final boolean superadmin = true;
    private Publisher publisherClone;
    
    private PublisherDP dp = new PublisherDP();
    ConfigurableFilterDataProvider<Publisher, Void, String> configurableFilterDataProvider = dp.withConfigurableFilter();
    
    Grid<Publisher> grid = new Grid<>(Publisher.class, false);
    
    int total;
    
    public PublisherManager()
    {
        super();
        total = configurableFilterDataProvider.size(new Query<>());
        
        Span totalcountspan = new Span(total + " Total");
        totalcountspan.addClassNames("secondary-text", "font-size-xs");
        configurableFilterDataProvider.addDataProviderListener(lst -> totalcountspan.setText(total + " Total"));
        
        CustomSearchTextField searchbox = new CustomSearchTextField("Search", ValueChangeMode.EAGER, new Icon("lumo", "search"), false/*not needed here with TextFieldClearButton in use*/, "Search By Name", "small");
        searchbox.addValueChangeListener(event ->
                                         {
                                             configurableFilterDataProvider.setFilter(event.getValue());
                                             configurableFilterDataProvider.refreshAll();
            
                                             total = configurableFilterDataProvider.size(new Query<>());
                                             totalcountspan.setText(total + " Total");
                                         });
        
        grid.setDataProvider(configurableFilterDataProvider);
        grid.setMultiSort(true);
        grid.setColumnReorderingAllowed(true);
        
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        GridSingleSelectionModel<Publisher> singleSelect = (GridSingleSelectionModel<Publisher>) grid.getSelectionModel();
        singleSelect.asSingleSelect();
        
        grid.setItemDetailsRenderer(
                new ComponentRenderer<>(publisher ->
                                                new VerticalLayout()));
        
        grid.addColumn("publisherName").setHeader("Publisher Name").setResizable(true);
        grid.addColumn(d -> d.getContactPerson()).setHeader("Contact Person").setResizable(true);
        grid.addColumn("publisherWebsite").setHeader("Publisher Website").setResizable(true);
        grid.addColumn("wikiLink").setHeader("Wikilink").setResizable(true);
        grid.addColumn("addressText").setHeader("Address").setResizable(true);
        
        grid.addComponentColumn(publisher ->
                                {
            
                                    try
                                    {
                                        publisherClone = (Publisher) publisher.clone();
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
                                                              new PublisherCRUDDialog(publisher, superadmin ? BasicDialog.BeanAction.EDIT : BasicDialog.BeanAction.VIEW, (a) ->
                                                              {
                                                                  // grid.getDataProvider().refreshItem(a);
                                                                  configurableFilterDataProvider.refreshItem(a);
                                                                  grid.getDataProvider().refreshAll();
                                                                  UI.getCurrent().getPage().reload();
                                                                  return true;
                                                              }, BasicDialog.DialogSize.MID).open();
                                                          });
                                    Button delete = new Button(new IronIcon("lumo", "cross"));
                                    delete.getElement().setAttribute("theme", "small icon error");
                                    delete.addClickListener(click ->
                                                            {
                                                                // new CustomNotification("Delete Clicked", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                
                                                                String publisherName = publisher.getPublisherName();
                                                                new ConfirmDialog("Delete Publisher",
                                                                                  String.format("Are you sure you want to delete %s?", publisherName), "Yes " + BasicDialog.BeanAction.DELETE.displayText(),
                                                                                  (e) ->
                                                                                  {
                                                                                      publisher.delete(Connection.DB_PUBLISHER, publisher);
                                                                                      System.out.printf("Deleted Publisher: %s\n", publisherName);
                                                                                      System.out.println("Publisher deleted successfully");
                                                                                      new CustomNotification("Publisher deleted successfully", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                    
                                                                                      grid.getDataProvider().refreshItem(publisher);
                                                                                      grid.getDataProvider().refreshAll();
                    
                                                                                      e.getSource().close();
                                                                                  }, "Do not delete", (e) ->
                                                                                  {
                                                                                      new CustomNotification("Publisher not deleted", "success", true, 1000, Notification.Position.TOP_CENTER).open();
                    
                                                                                      System.out.println("Publisher not deleted");
                                                                                      e.getSource().close();
                                                                                  }).open();
                                                            });
            
                                    fl.add(edit, delete);
                                    return fl;
                                }).setFlexGrow(0).setWidth("100px").setResizable(true);
        addHeaderComponent(new H1("Fragment Main Header"));
        addHeaderComponent(new H3("Authors Manager"));
        
        
        // TODO> search fo icons and themes using Variant, VaadinIcon.ADD_DOCK API
        SmallButton addentity = new SmallButton("New Publisher").theme("primary");
//        addentity.setIcon(new Icon("lumo", "add"));
        addentity.setIcon(new Icon(VaadinIcon.ADD_DOCK));
        //addentity.setVisible(MainFrame.getUserRoleType().isSuperAdmin());
        
        addentity.addClickListener(clk -> new PublisherCRUDDialog(new Publisher(),
                                                                  BasicDialog.BeanAction.NEW,
                                                                  (a) ->
                                                                  {
                                                                      //refresh data
                                                                      //refreshDataTable(a);
                                                                      configurableFilterDataProvider.refreshItem(a);
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
