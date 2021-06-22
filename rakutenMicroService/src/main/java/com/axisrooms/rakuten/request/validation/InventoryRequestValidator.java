package com.axisrooms.rakuten.request.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.axisrooms.rakuten.request.InventoryRequest;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InventoryRequestValidator implements ConstraintValidator<ValidInventoryRequest, InventoryRequest> {

    @Value("${microservice.communication.token}")
    private String acceptedToken;

    @Override
    public void initialize(ValidInventoryRequest constraintAnnotation) {

    }

    @Override
    public boolean isValid(InventoryRequest request, ConstraintValidatorContext context) {
	// todo
	// preconditions validations
	boolean result = true;
//            try{
//                Preconditions.checkArgument(request != null, "Request cannot be null");
//                String token = request.getToken();
//                String hotelId = request.getHotelId();
//                Utils.isValid(token, hotelId, acceptedToken);
//                List<InventoryData> data = request.getData();
//                Preconditions.checkArgument(!CollectionUtils.isEmpty(data), "Data cannot be null or empty");
//                Preconditions.checkArgument(!StringUtils.isEmpty(request.getArcRequestId()), "ArcRequestId cannot be null or empty");
//                for (InventoryData inventoryData : data) {
//                    Preconditions.checkArgument(!StringUtils.isEmpty(inventoryData.getRoomId()), "RoomId cannot be null or empty");
//                    Preconditions.checkArgument(!CollectionUtils.isEmpty(inventoryData.getRatePlans()),"RatePlans cannot be null or empty for Merakey OTA");
//                    Preconditions.checkArgument(!CollectionUtils.isEmpty(inventoryData.getInventories()), "Inventory block cannot be null or empty");
//                    for (Inventory inventory : inventoryData.getInventories()) {
//                        Utils.validateDates(inventory.getStartDate(), inventory.getEndDate());
//                    }
//                }
//            }catch(Throwable throwable){
//                log.error(throwable.getMessage());
//                result = false;
//                context.buildConstraintViolationWithTemplate(throwable.getMessage()).addConstraintViolation();
//            }

	return result;
    }
}
