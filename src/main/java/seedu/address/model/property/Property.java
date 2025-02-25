package seedu.address.model.property;

import java.util.ArrayList;
import java.util.HashSet;

import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/*
 * A sample property class
 * */
public class Property{
    private String name;
    private String postalCode;
    private String houseNumber;
    private String unitNumber;
    private HashSet<Tag> tags = new HashSet<>();
    private ArrayList<Person> owners = new ArrayList<>();

    public Property(String postalCode, String houseNumber, String unitNumber){
        this.postalCode = postalCode;
        this.houseNumber = houseNumber;
        this.unitNumber = unitNumber;
    }

    public void setName(String name){
        this.name = name;

    }
    public String getName(){
        return name;
    }

    public String getPostalCode(){
        return postalCode;
    }

    public String getHouseNumber(){
        return houseNumber;
    }

    public String getUnitNumber(){
        return unitNumber;
    }

    public HashSet<Tag> getTags(){
        return tags;
    }

    public ArrayList<Person> getOwners(){
        return owners;
    }
}