package com.axisrooms.rakuten.generated.productInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Meta {

    public String reqId;
    public Object reqTime;
    public String path;
    public String version;
}
