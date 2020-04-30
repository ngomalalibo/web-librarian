package com.pc.weblibrarian.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pc.weblibrarian.enums.PersonRoleType;
import com.pc.weblibrarian.security.PasswordEncoder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

//@Entity(value = MDB.DB_USER, noClassnameStored = true)
@Data
@EqualsAndHashCode(callSuper = false)
public class LibraryUser extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    
    //Includes Person and Address details
    private String personId;
    
    @BsonIgnore
    private Person person = new Person();
    
    @NotNull(message = "`userId` field is mandatory")
    @Email(message = "`userId` must be an well-formed email address")
    //@JsonIgnore
    private String userEmail;
    
    private String firstName;
    private String lastName;
    
    //@JsonIgnore
    @NotNull(message = "`password` field is mandatory")
    @Size(min = 8, message = "`password` must be at least 8 characters long")
    
    @JsonIgnore
    @BsonIgnore
    private String password;
    
    private String hashedPassword;
    
    private PersonRoleType personRoleTypes;
    
    private int loginAttempts = 0;
    private int maximumCheckoutItems;
    
    private Boolean accountLocked = true;
    private Boolean accountBanned = false;
    private Boolean accessToRentable = true;
    private Boolean accessToConsumable = true;
    private Boolean isAdmin = true;
    
    private LocalDateTime lastLoginDateTime;
    
    public void createNewPassword(String password)
    {
        this.setPassword(PasswordEncoder.getPasswordEncoder().encode(password));
    }
    
    public LibraryUser(@NotNull(message = "`userId` field is mandatory") @Email(message = "`userId` must be an well-formed email address") String userEmail, @NotNull(message = "`password` field is mandatory") @Size(min = 8, message = "`password` must be at least 8 characters long") String password, PersonRoleType personRoleTypes)
    {
        super.prepersist(this);
        this.userEmail = userEmail;
        this.password = password;
        this.personRoleTypes = personRoleTypes;
    }
    
    public LibraryUser()
    {
        super();
    }
    
    @JsonIgnore
    @BsonIgnore
    public boolean isEmpty()
    {
        return this.userEmail == null || "".equals(this.getUserEmail());
    }
    
}
