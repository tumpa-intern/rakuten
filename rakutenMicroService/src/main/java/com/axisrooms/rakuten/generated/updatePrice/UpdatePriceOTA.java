
package com.axisrooms.rakuten.generated.updatePrice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@lombok.Data
public class UpdatePriceOTA {
    @JsonProperty("ClientHotelCode")
    private String clientHotelCode;
    @JsonProperty("Token")
    private String token;
    @JsonProperty("RequestType")
    private String requestType;
    @JsonProperty("Data")
    private List<UpdateData> updateData;
}
