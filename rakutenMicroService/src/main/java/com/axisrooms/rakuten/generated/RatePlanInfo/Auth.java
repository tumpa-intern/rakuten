
package com.axisrooms.rakuten.generated.RatePlanInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Auth {
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Token")
    private String token;
}
