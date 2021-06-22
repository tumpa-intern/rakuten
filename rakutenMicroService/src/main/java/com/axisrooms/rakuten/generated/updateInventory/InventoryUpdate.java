
package com.axisrooms.rakuten.generated.updateInventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@lombok.Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryUpdate {

        public Meta meta;
        public List<Success> successes;

}
