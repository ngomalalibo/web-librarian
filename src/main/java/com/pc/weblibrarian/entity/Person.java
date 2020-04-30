package com.pc.weblibrarian.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.dataproviders.GetObjectByID;
import com.pc.weblibrarian.enums.PersonGenderType;
import com.pc.weblibrarian.enums.PersonTitleType;
import com.pc.weblibrarian.model.ImageModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class Person extends PersistingBaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private PersonTitleType title;
    private PersonGenderType gender;
    
    @NotNull(message = "`firstName` field is mandatory")
    @Size(min = 3, message = "`name` must be at least 3 characters long")
    private String firstName;
    
    @NotNull(message = "`lastName` field is mandatory")
    @Size(min = 3, message = "`name` must be at least 3 characters long")
    private String lastName;
    private String aka;
    private String emailAddress;
    private String phoneNumber;
    private String website;
    private ImageModel image = new ImageModel(); // bytearray.toString()
    private LocalDate dateOfBirth;
    
    @BsonProperty("addressId")
    @JsonProperty("addressId")
    private List<String> addressIds = new ArrayList<>();
    
    @BsonIgnore
    // @JsonIgnore
    private List<Address> addressObjects = new ArrayList<>();
    
    
    public Person()
    {
        super();
        /*title = PersonTitleType.MR;
        gender = PersonGenderType.MALE;
        firstName = "";
        lastName = "";
        aka = "";
        emailAddress = "";
        phoneNumber = "";
        website = "";
        dateOfBirth = LocalDate.of(1990, 01, 01);
        this.setAddressObjects(this.getAddressIds().stream().filter(d -> this.addressIds.size() > 0).filter(d -> !Objects.equals(d, "")).map(c -> GetObjectByID.getObjectById(c, Connection.addresses)).collect(Collectors.toList()));*/
        // addressIds.add("");
    }
    
    public Person(PersonTitleType title, PersonGenderType gender, String firstName, String lastName, String aka, String emailAddress, String phoneNumber, String website, ImageModel image, LocalDate dateOfBirth, List<String> addressIds)
    {
        super.prepersist(this);
        this.title = title;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.aka = aka;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.image = image;
        this.dateOfBirth = dateOfBirth;
        this.addressIds = addressIds;
        this.setAddressObjects(this.getAddressIds().stream().filter(d -> this.addressIds.size() > 0).filter(d -> !Objects.equals(d, "")).map(c -> GetObjectByID.getObjectById(c, Connection.addresses)).collect(Collectors.toList()));
    }
    
    @BsonIgnore
    public String getFullName()
    {
        return this.firstName + " " + this.lastName;
    }
    
    /*Causes recursion by creating a person object within the same object class
    
    public static Person getInstance()
    {
        Country country = new Country("Nigeria", "ng");
        State state = new State("Lagos", "ng");
        Address ikeja = new Address("Ikeja", state, "100001", country, true, "No 1 Valley Close", AddressType.NONE);
        
        ImageModel image;
        // TODO> This line causes a maximum serialization error
        Person person = new Person();
        try
        {
            image = ImageUtil.convertPathToImageModel(ImageUtil.FILE_PATH + "Asset 3.png");
//                                              System.out.println("image = " + Arrays.toString(image.getImageByteArray()));

            person = new Person(PersonTitleType.MR,
                                PersonGenderType.MALE,
                                "Nebu",
                                "Wazobia",
                                "Nebu",
                                "nebu@wazobia.com",
                                "080-3456-7890",
                                Faker.instance().internet().url(),
                                image,
                                LocalDate.now(),
                                Collections.singletonList(ikeja.getUuid()));
            person.setAddressObjects(Collections.singletonList(ikeja));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return person;
    }*/
    
    
}
