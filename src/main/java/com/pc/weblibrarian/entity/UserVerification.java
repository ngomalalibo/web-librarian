package com.pc.weblibrarian.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

//@Entity(value = MDB.DB_USERVERIFICATION, noClassnameStored = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class UserVerification extends PersistingBaseEntity
{
    
    private static final long serialVersionUID = 1L;
    
    private String token;
    private String userEmail;
    private String verificationType;
    private boolean expired;
    
    private LocalDateTime emailAddressVerifiedDate;
    private LocalDateTime phoneNumberVerifiedDate;
    
    
    public UserVerification()
    {
        super();
    }
}
