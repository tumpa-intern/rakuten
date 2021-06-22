package com.axisrooms.rakuten.generated.updatePrice;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@lombok.Data
public class RoomAvailablityDetail {
    @JsonProperty("PriceDetail")
    private PriceDetail priceDetail;
}
