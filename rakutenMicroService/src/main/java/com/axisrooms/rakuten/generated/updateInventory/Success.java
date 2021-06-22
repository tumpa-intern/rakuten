package com.axisrooms.rakuten.generated.updateInventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Success {

    public String propertyCode;
    public String roomTypeCode;
    public Object ratePlanCode;
    public Object ratePlanCategory;
    public String fromDate;
    public String toDate;
    public Object minLos;
    public Object maxLos;

}
