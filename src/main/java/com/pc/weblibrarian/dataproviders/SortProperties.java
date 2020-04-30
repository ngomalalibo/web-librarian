package com.pc.weblibrarian.dataproviders;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Data
public class SortProperties
{
    String propertyName;
    boolean ascending;
    
    public SortProperties(String propertyName, boolean ascending)
    {
        this.propertyName = propertyName;
        this.ascending = ascending;
    }
    
    public SortProperties()
    {
        super();
    }
    
    public static List<SortProperties> getSortProperties(List<QuerySortOrder> sortOrder)
    {
        AtomicReference<Boolean> sortAscending = new AtomicReference<>(Boolean.TRUE);
        AtomicReference<SortProperties> ps = new AtomicReference<>(new SortProperties());
        List<SortProperties> sortProperties = new LinkedList<>();
        sortOrder.forEach(qso ->
                          {
                              String sort = qso.getSorted();
                              sortAscending.set(qso.getDirection() == SortDirection.ASCENDING);
        
                              ps.set(new SortProperties());
                              ps.get().setAscending(sortAscending.get());
                              ps.get().setPropertyName(sort);
                              sortProperties.add(ps.get());
                          });
        
        return sortProperties;
    }
}