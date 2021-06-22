package com.axisrooms.rakuten.request;

import com.axisrooms.rakuten.generated.updateInventory.Avail;
import com.axisrooms.rakuten.generated.updateInventory.Meta;
import com.axisrooms.rakuten.request.validation.ValidInventoryRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@ValidInventoryRequest
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryRequest {
    public Meta meta;
    public List<Avail> avails;
}
