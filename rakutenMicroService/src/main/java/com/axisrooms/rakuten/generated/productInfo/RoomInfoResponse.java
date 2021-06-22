
package com.axisrooms.rakuten.generated.productInfo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RoomInfoResponse {

    @JsonProperty("message")
    private String message;
    @JsonProperty("status")
    private String status;
    @JsonProperty("data")
    private List<RoomDataResponse> data;

}
