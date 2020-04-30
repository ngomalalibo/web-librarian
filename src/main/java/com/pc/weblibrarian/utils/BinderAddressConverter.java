package com.pc.weblibrarian.utils;

import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.GetObjectByID;
import com.pc.weblibrarian.entity.Address;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.util.Collections;
import java.util.List;

public class BinderAddressConverter implements Converter<Address, List<String>>
{
    
    @Override
    public Result<List<String>> convertToModel(Address address, ValueContext valueContext)
    {
        return Result.ok(Collections.singletonList(address.getUuid()));
    }
    
    @Override
    public Address convertToPresentation(List<String> strings, ValueContext valueContext)
    {
        return strings.stream().map(d -> GetObjectByID.getObjectById(d, Connection.addresses)).findFirst().get();
    }
    
}
