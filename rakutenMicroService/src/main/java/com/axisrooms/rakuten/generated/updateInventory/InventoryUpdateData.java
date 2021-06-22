package com.axisrooms.rakuten.generated.updateInventory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryUpdateData {
    @JsonProperty("To")
    private String to;
    @JsonProperty("ClientRoomCode")
    private String clientRoomCode;
    @JsonProperty("From")
    private String from;
    @JsonProperty("Allocation")
    private String allocation;
}
