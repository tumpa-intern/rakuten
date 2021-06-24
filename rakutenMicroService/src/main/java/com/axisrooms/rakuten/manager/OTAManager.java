package com.axisrooms.rakuten.manager;

import com.axisrooms.rakuten.generated.productInfo.ProductInfoResponse;
import com.axisrooms.rakuten.request.InventoryRequest;
import com.axisrooms.rakuten.request.PriceRequest;
import com.axisrooms.rakuten.request.RestrictionRequest;
import com.axisrooms.rakuten.response.InventoryResponse;
import com.axisrooms.rakuten.response.PriceResponse;
import com.axisrooms.rakuten.response.RatePlanResponse;
import com.axisrooms.rakuten.util.OccupancyNotSupportedException;
import org.springframework.stereotype.Service;

@Service
public interface OTAManager {
   ProductInfoResponse getRoomList(String hotelId) throws Exception;

    RatePlanResponse getRatePlans(String hotelId, String roomId) throws Exception;

    InventoryResponse updateInventory(InventoryRequest inventoryRequest) throws Exception;

    PriceResponse updatePrice(PriceRequest priceRequest) throws OccupancyNotSupportedException, Exception;

    InventoryResponse updateRestriction(RestrictionRequest restrictionRequest) throws Exception;
}
