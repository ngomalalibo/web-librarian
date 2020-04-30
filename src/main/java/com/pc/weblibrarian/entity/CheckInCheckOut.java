package com.pc.weblibrarian.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//@Entity(value = MDB.DB_CHECKINCHECKOUT, noClassnameStored = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class CheckInCheckOut extends PersistingBaseEntity
{

    private static final long serialVersionUID = 1L;
    
    public CheckInCheckOut()
    {
        super();
    }
    
    private String userId;
    private String adminId;
    
    private String libraryItemId;
    private String checkOutItemCondition;
    private String checkInItemCondition;
    private String dueDateExtendedBy;

    private LocalDate checkOutDate;
    
    private LocalDate expectedCheckInDate;
    private LocalDate actualCheckInDate;
    private LocalDate itemLostDate;
    private LocalDate dueDateExtendedDate;

    private List<String> notes = new ArrayList<>();
    
    public CheckInCheckOut(String userId, String adminId, String libraryItemId, String checkOutItemCondition, String checkInItemCondition, String dueDateExtendedBy, LocalDate checkOutDate, LocalDate expectedCheckInDate, LocalDate actualCheckInDate, LocalDate itemLostDate, LocalDate dueDateExtendedDate, List<String> notes)
    {
        super.prepersist(this);
        this.userId = userId;
        this.adminId = adminId;
        this.libraryItemId = libraryItemId;
        this.checkOutItemCondition = checkOutItemCondition;
        this.checkInItemCondition = checkInItemCondition;
        this.dueDateExtendedBy = dueDateExtendedBy;
        this.checkOutDate = checkOutDate;
        this.expectedCheckInDate = expectedCheckInDate;
        this.actualCheckInDate = actualCheckInDate;
        this.itemLostDate = itemLostDate;
        this.dueDateExtendedDate = dueDateExtendedDate;
        this.notes = notes;
    }
}
