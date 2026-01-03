package com.servicebooker.api.model.common;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Money {
    private BigDecimal amount;
    private String currency;

    protected Money() {}
    public Money(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
}
