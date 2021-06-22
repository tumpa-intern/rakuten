
package com.axisrooms.rakuten.generated.RatePlanInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@lombok.Data
public class Datum {
    private String CommissionPerc;
    private List<String> Occupancy;
    private String RatePlanName;
    private String RateplanId;
    private String TaxPerc;
    private Validity Validity;
}
