package com.axisrooms.rakuten.generated.productInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RoomData {
    @JsonProperty("RoomId")
    private String roomId;
    @JsonProperty("RoomName")
    private String roomName;
    @JsonProperty("ClientRoomCode")
    private String clientRoomCode;
    @JsonProperty("Allocation")
    private String allocation;
    @JsonProperty("TotalAllowedPax")
    private int totalAllowedPax;
    @JsonProperty("AllowedAdult")
    private int allowedAdult;
    @JsonProperty("AllowedChild")
    private int allowedChild;
    @JsonProperty("AllowedGuest")
    private int allowedGuest;
}
