package com.axisrooms.rakuten.generated.updatePrice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@lombok.Data
public class UpdateData {
    @JsonProperty("Data")
    private List<Data> data;
    @JsonProperty("RequestType")
    private String requestType;
}
