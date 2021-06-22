package com.axisrooms.rakuten.generated.productInfo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BedRoom {

    public List<Bed> beds;
    public String name;
    public String size;
    public boolean isPrivate;

}
