package com.axisrooms.rakuten.request;

import com.axisrooms.rakuten.bean.PriceData;
import com.axisrooms.rakuten.request.validation.ValidPriceRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@ValidPriceRequest
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceRequest {
    private String          channelId;
    private String          token;
    private String          arcRequestId;
    private String          hotelId;
    private List<PriceData> data;
}
