package com.servicebooker.api.model;



import com.servicebooker.api.model.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity
@Table(name = "customers",
        uniqueConstraints = @UniqueConstraint(name="uk_customer_business_email", columnNames = {"businessId","email"}))
public class Customer extends BaseEntity {

    @Column(nullable = false)
    private UUID businessId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    private String phone;

    protected Customer() {}

    public Customer(UUID businessId, String fullName, String email, String phone) {
        this.businessId = businessId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    public UUID getBusinessId() { return businessId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    public void updateCustomer( String fullName, String phone) {
        this.fullName = fullName;
        this.phone = phone;
    }
}
