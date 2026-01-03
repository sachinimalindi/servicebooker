package com.servicebooker.api.model;


import com.servicebooker.api.model.common.BaseEntity;
import com.servicebooker.api.model.common.DepositPolicy;
import com.servicebooker.api.model.common.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "services")
public class ServiceOffering extends BaseEntity {

    @Column(nullable = false)
    private UUID businessId;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private int durationMinutes;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column=@Column(name="price_amount", nullable=false)),
            @AttributeOverride(name="currency", column=@Column(name="price_currency", nullable=false))
    })
    private Money price;

    @Embedded
    private DepositPolicy depositPolicy;

    @Column(nullable = false)
    private boolean active = true;

    protected ServiceOffering() {}

    private ServiceOffering(UUID businessId, String name, String description, int durationMinutes, Money price, DepositPolicy depositPolicy) {
        this.businessId = businessId;
        this.name = name;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.depositPolicy = depositPolicy;
    }

    public static ServiceOffering createNew(UUID businessId, String name, String description, int durationMinutes, Money price, String depositType, BigDecimal depositValue) {
       return new ServiceOffering(businessId, name, description, durationMinutes, price,  DepositPolicy.toDepositPolicy(depositType, depositValue));
    }



    public UUID getBusinessId() { return businessId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getDurationMinutes() { return durationMinutes; }
    public Money getPrice() { return price; }
    public DepositPolicy getDepositPolicy() { return depositPolicy; }
    public boolean isActive() { return active; }
    public void deactivate() { this.active = false; }

    public void updateService(
            String name,
            String description,
            int durationMinutes,
            BigDecimal priceAmount,
            String priceCurrency,
            String depositType,
            BigDecimal depositValue,
            boolean active
    ){
        this.name= name;
                this.description= description;
                this.durationMinutes= durationMinutes;
                this.price = new Money(priceAmount, priceCurrency);
                this.depositPolicy = new DepositPolicy(DepositPolicy.DepositType.valueOf(depositType), depositValue);
                this.active = active;
    }

}
