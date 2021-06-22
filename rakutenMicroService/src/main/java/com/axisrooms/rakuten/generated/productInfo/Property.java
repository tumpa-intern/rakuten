package com.axisrooms.rakuten.generated.productInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Property {

    public Title title;
    public Address address;
    public Location location;
    public GuestPolicy guestPolicy;
    public String propertyCode;
    public boolean isPropertyOpened;
    public String propertyCategory;
}
