package com.axisrooms.rakuten.generated.RatePlanInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RatePlanData {
    @JsonProperty("RoomCode")
    private String roomCode;
    @JsonProperty("HotelName")
    private String hotelName;
    @JsonProperty("ClientPlanId")
    private String clientPlanId;
    @JsonProperty("PlanId")
    private String planId;
    @JsonProperty("HotelCode")
    private String hotelCode;
    @JsonProperty("ClientPlanName")
    private String clientPlanName;
    @JsonProperty("ClientHotelCode")
    private String clientHotelCode;
    @JsonProperty("RoomName")
    private String roomName;
    @JsonProperty("ClientRoomCode")
    private String clientRoomCode;
    @JsonProperty("BaseCurrency")
    private String baseCurrency;
}
