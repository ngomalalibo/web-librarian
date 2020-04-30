package com.pc.weblibrarian.entity;

import com.pc.weblibrarian.enums.AddressType;
import com.pc.weblibrarian.model.Country;
import com.pc.weblibrarian.model.State;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a single address.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Address extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private String city = "";
    private State state = new State("Lagos", "ng");
    private String zipCode = "";
    private Country country = new Country("Nigeria", "ng");
    private boolean primaryAddress;
    private String streetAddressLine = "";
    private AddressType addressType = AddressType.NONE;
    
    public Address(String city, State state, String zipCode, Country country, boolean primaryAddress, String streetAddressLine, AddressType addressType)
    {
        super.prepersist(this);
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.primaryAddress = primaryAddress;
        this.streetAddressLine = streetAddressLine;
        this.addressType = addressType;
    }
    
    public Address()
    {
        super();
    }
    
    
    public static final class Builder
    {
        private Address address;
        
        private Builder()
        {
            address = new Address();
        }
        
        public static Builder anAddress()
        {
            return new Builder();
        }
        
        public Builder setCity(String city)
        {
            address.setCity(city);
            return this;
        }
        
        public Builder setState(State state)
        {
            address.setState(state);
            return this;
        }
        
        public Builder setZipCode(String zipCode)
        {
            address.setZipCode(zipCode);
            return this;
        }
        
        public Builder setCountry(Country country)
        {
            address.setCountry(country);
            return this;
        }
        
        public Builder setPrimaryAddress(boolean primaryAddress)
        {
            address.setPrimaryAddress(primaryAddress);
            return this;
        }
        
        public Builder setStreetAddressLine(String streetAddressLine)
        {
            address.setStreetAddressLine(streetAddressLine);
            return this;
        }
        
        public Builder setAddressType(AddressType addressType)
        {
            address.setAddressType(addressType);
            return this;
        }
        
        public Address build()
        {
            return address;
        }
    }
}
