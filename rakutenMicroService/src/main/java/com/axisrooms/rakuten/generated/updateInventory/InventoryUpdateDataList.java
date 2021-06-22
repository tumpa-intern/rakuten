package com.axisrooms.rakuten.generated.updateInventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class InventoryUpdateDataList {
    @JsonProperty("Data")
    private List<InventoryUpdateData> data;
    @JsonProperty("RequestType")
    private String requestType;
}
