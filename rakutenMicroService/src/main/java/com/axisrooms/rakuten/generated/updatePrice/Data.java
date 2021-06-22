
package com.axisrooms.rakuten.generated.updatePrice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@lombok.Data
public class Data {
    @JsonProperty("ContractCode")
    private String contractCode;
    @JsonProperty("RoomAvailablityDetail")
    private RoomAvailablityDetail roomAvailablityDetail;
    @JsonProperty("To")
    private String to;
    @JsonProperty("RoomCancellation")
    private RoomCancellation roomCancellation;
    @JsonProperty("ClientRoomCode")
    private String clientRoomCode;
    @JsonProperty("From")
    private String from;
}
