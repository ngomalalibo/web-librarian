package com.pc.weblibrarian.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

//@Entity(value = MDB.DB_WAITLIST, noClassnameStored = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class WaitingList extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private String userId;
    private String entityId;
    private LocalDate noticeSentDate;
    private LocalDate fulfilledDate;
    
    private String checkinCheckOutId;
    
    public WaitingList(String userId, String entityId, LocalDate noticeSentDate, LocalDate fulfilledDate, String checkinCheckOutId)
    {
        super.prepersist(this);
        this.userId = userId;
        this.entityId = entityId;
        this.noticeSentDate = noticeSentDate;
        this.fulfilledDate = fulfilledDate;
        this.checkinCheckOutId = checkinCheckOutId;
    }
    
    public WaitingList()
    {
        super();
    }
}
