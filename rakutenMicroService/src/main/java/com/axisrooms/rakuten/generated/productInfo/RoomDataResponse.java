package com.axisrooms.rakuten.generated.productInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RoomDataResponse {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;

}
