package com.pc.weblibrarian.dataService;

import com.pc.weblibrarian.entity.Person;

import java.util.List;

public class PersonDataServiceImplementation implements DataServiceInterface
{
    @Override
    public List<Person> getData(String id, String filter, List sortOrders, GenericDataService genericDataService)
    {
        System.out.println("getData in PersonDataServiceImplementation");
        return (List<Person>) genericDataService.getEntityByTwoFilterSearch(sortOrders, "firstName", "lastName", filter);
        
        //entityByTwoFilterSearch.forEach(d -> System.out.println("Full name " + d.getFullName()));
        
    }
}