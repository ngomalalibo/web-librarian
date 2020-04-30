package com.pc.weblibrarian.entity;

import com.pc.weblibrarian.enums.CurrencyTypeAndSymbol;
import lombok.Data;
import lombok.EqualsAndHashCode;

//@Entity(value = MDB.DB_ORGANIZATION, noClassnameStored = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class Organization extends PersistingBaseEntity
{
    
    private static final long serialVersionUID = 1L;
    
    private String theme;
    private String organizationWebsite;
    private String organizationName;
    private String organization;
    /*private String organizationDBA;
    private String organizationAdministratorId;*/
    private CurrencyTypeAndSymbol organizationBaseCurrency;
    
    private String contactPerson;
    
    public Organization(String theme, String organizationWebsite, String organizationName, String organization, CurrencyTypeAndSymbol organizationBaseCurrency, String contactPerson)
    {
        super.prepersist(this);
        this.theme = theme;
        this.organizationWebsite = organizationWebsite;
        this.organizationName = organizationName;
        this.organization = organization;
        this.organizationBaseCurrency = organizationBaseCurrency;
        this.contactPerson = contactPerson;
    }
    
    public Organization()
    {
        super();
    }
}
