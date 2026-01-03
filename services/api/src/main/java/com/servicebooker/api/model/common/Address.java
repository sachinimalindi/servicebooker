package com.servicebooker.api.model.common;


import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String line1;
    private String line2;
    private String suburb;
    private String state;
    private String postcode;
    private String country;

    protected Address() {}
    public Address(String line1, String line2, String suburb, String state, String postcode, String country) {
        this.line1 = line1;
        this.line2 = line2;
        this.suburb = suburb;
        this.state = state;
        this.postcode = postcode;
        this.country = country;
    }

    public String getLine1() { return line1; }
    public String getLine2() { return line2; }
    public String getSuburb() { return suburb; }
    public String getState() { return state; }
    public String getPostcode() { return postcode; }
    public String getCountry() { return country; }
}
