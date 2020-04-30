package com.pc.weblibrarian.dataService;

import com.pc.weblibrarian.dataproviders.SortProperties;
import com.pc.weblibrarian.entity.PersistingBaseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DataServiceInterface<B extends PersistingBaseEntity, S extends PersistingBaseEntity>
{
    public List<B> getData(String id, String filter, List<SortProperties> sortOrders, GenericDataService dataService);
}
