package com.pc.weblibrarian.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class Note extends PersistingBaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String note;
    
    public Note(String note)
    {
        super.prepersist(this);
        this.note = note;
    }
    
    public Note()
    {
        super();
    }
}
