package com.axisrooms.rakuten.generated.updateInventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Avail {

    public String propertyCode;
    public String roomTypeCode;
    public String fromDate;
    public String toDate;
    public int bookingLimit;
    public int minLos;
    public int maxLos;

}
