
package com.axisrooms.rakuten.generated.RatePlanInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RatePlanInfoResponse {
    @JsonProperty("Data")
    private RatePlanData[] data;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Message")
    private String message;
}
