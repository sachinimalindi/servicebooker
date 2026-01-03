package com.servicebooker.api.model.common;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Locale;

@Embeddable
public class DepositPolicy {

    public enum DepositType { NONE, FIXED, PERCENT }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DepositType type = DepositType.NONE;

    // if FIXED: value = amount; if PERCENT: value = 0..100
    private BigDecimal value;

    protected DepositPolicy() {}


    public DepositPolicy(DepositType type, BigDecimal value) {
        this.type = (type == null) ? DepositType.NONE : type;

        // normalize/validate
        switch (this.type) {
            case NONE -> this.value = null;
            case FIXED -> {
                requireValue(value, "FIXED");
                if (value.signum() < 0) throw new IllegalArgumentException("FIXED deposit amount must be >= 0");
                this.value = value;
            }
            case PERCENT -> {
                requireValue(value, "PERCENT");
                if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(new BigDecimal("100")) > 0) {
                    throw new IllegalArgumentException("PERCENT deposit must be between 0 and 100");
                }
                this.value = value;
            }
        }
    }

    public static DepositPolicy toDepositPolicy(String type, BigDecimal value) {
        if (type == null || type.isBlank()) {
            return new DepositPolicy(DepositType.NONE, value);
        }

        final DepositPolicy.DepositType t;
        try {
            t = DepositPolicy.DepositType.valueOf(type.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid deposit type: " + type, ex);
        }

        return switch (t) {
            case NONE    -> new DepositPolicy(DepositType.NONE, value);
            case FIXED   -> new DepositPolicy(DepositType.FIXED, value);   // creates DepositPolicy, not enum
            case PERCENT -> new DepositPolicy(DepositType.PERCENT, value); // creates DepositPolicy, not enum
        };
    }

    private static void requireValue(BigDecimal value, String type) {
        if (value == null) {
            throw new IllegalArgumentException(type + " deposit requires a value");
        }
    }

    public DepositType getType() { return type; }
    public BigDecimal getValue() { return value; }
}
