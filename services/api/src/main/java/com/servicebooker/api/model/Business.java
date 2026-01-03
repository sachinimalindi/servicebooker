package com.servicebooker.api.model;



import com.servicebooker.api.model.common.Address;
import com.servicebooker.api.model.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "businesses")
public class Business extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String timezone; // IANA id

    private String phone;
    private String email;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private boolean active = true;

    protected Business() {}

    public Business(String name, String timezone, String phone, String email, Address address) {
        this.name = name;
        this.timezone = timezone;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getName() { return name; }
    public String getTimezone() { return timezone; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public Address getAddress() { return address; }
    public boolean isActive() {return active; }

    public void updateProfile(String name, String timezone, String phone, String email, Address address, boolean active) {
        this.name = name;
        this.timezone = timezone;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.active = active;
    }
}
