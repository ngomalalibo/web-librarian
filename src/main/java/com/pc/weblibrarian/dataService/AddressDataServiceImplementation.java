package com.pc.weblibrarian.dataService;

import com.pc.weblibrarian.entity.Address;
import com.pc.weblibrarian.entity.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddressDataServiceImplementation implements DataServiceInterface
{
    // private GenericDataService<Person, Address> genericDataService;
    
    @Override
    public List<Person> getData(String id, String filter, List sortOrders, GenericDataService genericDataService)
    {
        System.out.println("getData in AddressDataServiceImplementation");
        List<Person> people = new ArrayList<>();
        // Map<Person, List<Address>> entityJoin = genericDataService.getEntityJoin(filter, "_id", id, "Address", "firstName", "lastName");
        Map<Person, List<Address>> entityJoin = genericDataService.getRecordAndEmbeddedObjectList(filter, "_id", id, "Address", "firstName", "lastName");
        
        // entityJoin.values().stream().findFirst().ifPresent(addresses::set);
        entityJoin.keySet().stream().forEach(p ->
                                             {
                                                 p.setAddressObjects(entityJoin.get(p));
                                                 people.add(p);
                                             });
        return people;
    }
}