package com.pc.weblibrarian.backend;

import com.pc.weblibrarian.entity.PersistingBaseEntity;

import java.util.List;

public interface Persistable
{
    
    public <T extends PersistingBaseEntity> void prepersist(T t);
    
    public <T extends PersistingBaseEntity> PersistingBaseEntity save(T t);
    
    public <T extends PersistingBaseEntity> boolean delete(String collectionName, T t);
    
    public <T extends PersistingBaseEntity> boolean deleteMany(List<T> t, String collectionName);
}
