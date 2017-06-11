package com.contract.entities.provider;

import com.contract.entities.provider.Contact;
import com.contract.entities.provider.Location;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julbarra on 5/31/17.
 */
@Document(collection = "providers")
public class Provider {

    @Id
    private String id;

    private String name;

    private Contact contact;

    private Location location;

    private Curriculum curriculum;

    private List<ProvideServices> provideServices;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Curriculum getCurriculum() {
        return curriculum != null ? curriculum : new Curriculum();
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    public List<ProvideServices> getProvideServices() {
        return provideServices != null ? provideServices : new ArrayList<>();
    }

    public void setProvideServices(List<ProvideServices> provideServices) {
        this.provideServices = provideServices;
    }
}
