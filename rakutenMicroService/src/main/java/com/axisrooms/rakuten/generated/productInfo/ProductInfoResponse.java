package com.axisrooms.rakuten.generated.productInfo;


import com.axisrooms.rakuten.bean.RatePlan;
import lombok.Data;

import java.util.List;

@Data
public class ProductInfoResponse {
    public Meta meta;
    public Property property;
    public List<Room> rooms;
    public List<RatePlan> ratePlans;
}



