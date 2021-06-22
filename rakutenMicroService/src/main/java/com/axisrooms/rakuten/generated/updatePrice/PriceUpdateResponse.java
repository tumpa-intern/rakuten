package com.axisrooms.rakuten.generated.updatePrice;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceUpdateResponse {
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Status")
    private Boolean status;
}
