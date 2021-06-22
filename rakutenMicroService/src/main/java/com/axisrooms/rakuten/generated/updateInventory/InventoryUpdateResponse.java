
package com.axisrooms.rakuten.generated.updateInventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryUpdateResponse {
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Status")
    private Boolean status;
}
