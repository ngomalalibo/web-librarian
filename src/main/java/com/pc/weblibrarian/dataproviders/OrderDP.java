package com.pc.weblibrarian.dataproviders;

import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.entity.OrderItem;
import com.pc.weblibrarian.views.LoginPage;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderDP extends AbstractBackEndDataProvider<OrderItem, String>
{
    
    GenericDataService gds = new GenericDataService(new OrderItem());
    
    public OrderDP()
    {
        super();
    }
    
    @Override
    protected Stream<OrderItem> fetchFromBackEnd(Query<OrderItem, String> query)
    {
        List<SortProperties> sortProperties = SortProperties.getSortProperties(query.getSortOrders());
        
        String filter = query.getFilter().orElse("");
        return fetch(sortProperties, filter).stream();
    }
    
    @Override
    protected int sizeInBackEnd(Query<OrderItem, String> query)
    {
        List<SortProperties> sortProperties = SortProperties.getSortProperties(query.getSortOrders());
        
        String filter = query.getFilter().orElse("");
        return fetch(sortProperties, filter).size();
    }
    
    public List<OrderItem> fetch(List<SortProperties> sortOrders, String filter)
    {
        if (LoginPage.loginStatus)
        {
            filter = VaadinSession.getCurrent().getAttribute("username").toString();
            List<OrderItem> orderItems = gds.getRecordsByEntityKey("userEmail", filter, sortOrders);
            if (orderItems != null)
            {
                orderItems.forEach(d -> d.setLibraryItem(GetObjectByID.getObjectById(d.getLibraryItemId(), Connection.libraryItem)));
                Predicate<OrderItem> pred = d -> !d.isChecked();
                return orderItems.stream().filter(pred).collect(Collectors.toList());
            }
        }
        
        
        return new ArrayList<>();
    }
    
    
}
