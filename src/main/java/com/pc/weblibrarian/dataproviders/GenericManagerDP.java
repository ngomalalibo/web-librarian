package com.pc.weblibrarian.dataproviders;

import com.pc.weblibrarian.dataService.AddressDataServiceImplementation;
import com.pc.weblibrarian.dataService.DataServiceInterface;
import com.pc.weblibrarian.dataService.GenericDataService;
import com.pc.weblibrarian.dataService.PersonDataServiceImplementation;
import com.pc.weblibrarian.entity.PersistingBaseEntity;
import com.pc.weblibrarian.entity.Person;
import com.pc.weblibrarian.views.dialogs.PersonCRUDDialog;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Repository
public class GenericManagerDP<Bean extends PersistingBaseEntity> extends AbstractBackEndDataProvider<Bean, String>
{
    private GenericDataService genericDataService;
    public Bean bean;
    
    private DataServiceInterface dataServiceInterface;
    public static DataContext dataContext;
    
    public GenericManagerDP()
    {
        super();
        
    }
    
    /*public GenericManagerDP(Bean bbean)
    {
        super();
        bean = bbean;
        genericDataService = new GenericDataService(bean);
    }*/
    
    @Override
    protected Stream fetchFromBackEnd(Query<Bean, String> query)
    {
        List<SortProperties> sortProperties =  SortProperties.getSortProperties(query.getSortOrders());
        
        String filter = query.getFilter().orElse("");
        return fetch(sortProperties, filter);
    }
    
    @Override
    protected int sizeInBackEnd(Query<Bean, String> query)
    {
        String filter = Objects.requireNonNull(query.getFilter().orElse(""));
        return genericDataService.getEntityByKeyCount("_id", filter);
    }
    
    public Stream fetch(List<SortProperties> sortOrders, String filter)
    {
        if ((bean instanceof Person) && dataContext.equals(DataContext.EMBEDDED_OBJECT))
        {
            this.dataServiceInterface = new AddressDataServiceImplementation();
            this.genericDataService = PersonCRUDDialog.gds;
            System.out.println("After AddressDataServiceImplementation...");
        }
        else if ((bean instanceof Person) && dataContext.equals(DataContext.SEARCH))
        {
            this.dataServiceInterface = new PersonDataServiceImplementation();
            System.out.println("After PersonDataServiceImplementation");
        }
        
        List result;
        System.out.println("Fetching data");
        result = dataServiceInterface.getData("_id", filter, sortOrders, genericDataService);
        return result.stream();
        /*if ((bean instanceof Person) && dataContext.equals(DataContext.EMBEDDED_OBJECT))
        {
            this.dataServiceInterface = new AddressDataServiceImplementation();
            result = (List<Bean>) dataServiceInterface.getData("_id", filter, sortOrders);
        }
        else if ((bean instanceof Person) && dataContext.equals(DataContext.SEARCH))
        {
            this.dataServiceInterface = new PersonDataServiceImplementation();
            result = (List<Bean>) dataServiceInterface.getData("_id", filter, sortOrders);
        }*/
        
    }
}
