package com.pc.weblibrarian.views.managers;

import com.pc.weblibrarian.customcomponents.CustomNotification;
import com.pc.weblibrarian.customcomponents.Fragment;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.GetObjectByID;
import com.pc.weblibrarian.dataproviders.OrderDP;
import com.pc.weblibrarian.entity.OrderItem;
import com.pc.weblibrarian.security.SecuredByRole;
import com.pc.weblibrarian.views.BasicDialog;
import com.pc.weblibrarian.views.MainFrame;
import com.pc.weblibrarian.views.dialogs.TransactionCRUDDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

@SecuredByRole(value = {"USER"})
@Route(value = "orders", layout = MainFrame.class)
@PreserveOnRefresh
public class OrderManager extends Fragment
{
    private OrderItem orderItemClone;
    private OrderDP dp = new OrderDP();
    private ConfigurableFilterDataProvider<OrderItem, Void, String> fdp = dp.withConfigurableFilter();
    
    Grid<OrderItem> grid = new Grid<>(OrderItem.class, false);
    
    OrderItem orderItem = new OrderItem();
    
    int total;
    
    public OrderManager()
    {
        super();
        total = fdp.size(new Query<>());
        
        Span totalcountspan = new Span(total + " Total");
        totalcountspan.addClassNames("secondary-text", "font-size-xs");
        fdp.addDataProviderListener(lst -> totalcountspan.setText(total + " Total"));
        
        grid.setDataProvider(fdp);
        grid.setMultiSort(true);
        grid.setColumnReorderingAllowed(true);
        grid.addThemeNames("no-border", "no-row-borders", "row-stripes");
        
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        GridSingleSelectionModel<OrderItem> singleSelect = (GridSingleSelectionModel<OrderItem>) grid.getSelectionModel();
        singleSelect.asSingleSelect();
        
        grid.setItemDetailsRenderer(
                new ComponentRenderer<>(orderItem ->
                                        {
                                            VerticalLayout layout = new VerticalLayout();
                    
                                            layout.add(new Label(GetObjectByID.getObjectById(orderItem.getLibraryItemId(), Connection.libraryItem).getLibraryItemName()));
                                            layout.add(new Label(GetObjectByID.getObjectById(orderItem.getLibraryItemId(), Connection.libraryItem).getLibraryItemType().getLibraryItemType()));
                    
                                            return layout;
                                        }));
        grid.addColumn(OrderItem::getOrderSerialNumber).setHeader("Order No");
        grid.addColumn(d -> d.getLibraryItem().getLibraryItemName()).setHeader("Name/Title").setResizable(true);
        grid.addColumn(OrderItem::getPrice).setHeader("Price").setResizable(true);
        grid.addColumn(OrderItem::getTransactionType).setHeader("Transaction Type").setResizable(true);
        grid.addColumn(OrderItem::getQuantity).setHeader("Quantity").setResizable(true);
    
        grid.addComponentColumn(orderItem ->
                                {
                                    try
                                    {
                                        orderItemClone = (OrderItem) orderItem.clone();
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
                                                              new TransactionCRUDDialog(orderItem, new OrderItem(), BasicDialog.BeanAction.EDIT,
                                                                                  a ->
                                                                                  {
                                                                                      fdp.refreshItem(a);
                                                                                      grid.getDataProvider().refreshAll();
                                                                                      return true;
                                                                                  },
                                                                                        BasicDialog.DialogSize.LARGE).open();
                                                          });
                                    Button delete = new Button(new IronIcon("lumo", "cross"));
                                    delete.getElement().setAttribute("theme", "small icon error");
                                    delete.addClickListener(click ->
                                                            {
                                                                new CustomNotification("Delete Clicked", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
            
                                                                String deleteProperty = orderItem.getLibraryItem().getLibraryItemName();
                                                                new ConfirmDialog("Delete library item",
                                                                                  String.format("Are you sure you want to delete %s?", deleteProperty), "Yes " + BasicDialog.BeanAction.DELETE.displayText(),
                                                                                  (e) ->
                                                                                  {
                                                                                      orderItem.delete(Connection.DB_LIBRARYITEM, orderItem);
                                                                                      System.out.printf("Deleted library item: %s\n", deleteProperty);
                                                                                      System.out.println("Library item deleted successfully");
                                                                                      new CustomNotification("Library item deleted successfully", "btn-outline-danger", true, 1000, Notification.Position.TOP_CENTER).open();
                
                                                                                      grid.getDataProvider().refreshItem(orderItem);
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
        
    }
    public interface Action<OrderItem>
    {
        //boolean parameter may not be needed anymore
        boolean action(OrderItem bean);
    }
}
