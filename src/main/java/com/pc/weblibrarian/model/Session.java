package com.pc.weblibrarian.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@EqualsAndHashCode(callSuper = false)
public class Session
{
    
    @BsonProperty(value = "user_id")
    private String userId;
    
    private String jwt;
    
    public Session()
    {
        super();
    }
}
