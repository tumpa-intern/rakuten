package com.axisrooms.rakuten.controller;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.axisrooms.rakuten.generated.productInfo.ProductInfoResponse;
import com.axisrooms.rakuten.generated.productInfo.Room;
import com.axisrooms.rakuten.generated.productInfo.RoomDataResponse;
import com.axisrooms.rakuten.generated.productInfo.RoomInfoResponse;
import com.axisrooms.rakuten.manager.OTAManager;
import com.axisrooms.rakuten.request.InventoryRequest;
import com.axisrooms.rakuten.request.PriceRequest;
import com.axisrooms.rakuten.response.InventoryResponse;
import com.axisrooms.rakuten.response.PriceResponse;
import com.axisrooms.rakuten.response.RoomResponse;
import com.axisrooms.rakuten.util.Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Add all the api controllers, url mappings here
 */

@RequestMapping(value = "/v1/rakuten")
@Api(description = "Api to communicate with rakuten Microservice from Channel Manager")
@Slf4j
@RestController
public class Rakuten {

    // to ensure security.. exposing as it's for internal purpose only
    @Value("${microservice.communication.token}")
    private String acceptedToken;

    @Autowired
    private OTAManager otaManager;

    @GetMapping(path = "/{token}/getRooms/{hotelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Fetch rooms from rakuten ota given a hotelId.", response = RoomResponse.class)
    public ResponseEntity<?> getRooms(@PathVariable("token") String token, @PathVariable("hotelId") String hotelId) {
	ResponseEntity<?> responseEntity;
	try {
	    Utils.isValid(token, hotelId, acceptedToken);
	    ProductInfoResponse roomResponse = otaManager.getRoomList(hotelId);
	    responseEntity = new ResponseEntity<>(roomResponse, HttpStatus.OK);
	    RoomInfoResponse object = mapData(responseEntity, roomResponse);
	    return new ResponseEntity<>(object, HttpStatus.OK);
	} catch (SocketTimeoutException e) {
	    log.error("Encountered exception while getting rooms", e);
	    PriceResponse priceResponse = new PriceResponse();
	    priceResponse.setMessage(e.getMessage() + " ==> Please try again after  sometime.");
	    priceResponse.setHttpStatusCode(HttpStatus.GATEWAY_TIMEOUT.value());
	    responseEntity = new ResponseEntity<>(priceResponse, HttpStatus.GATEWAY_TIMEOUT);
	    return responseEntity;
	} catch (Throwable throwable) {
	    log.error("Encountered exception while getting rooms", throwable);
	    RoomResponse roomResponse = new RoomResponse();
	    roomResponse.setMessage(throwable.getMessage());
	    roomResponse.setHttpStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
	    responseEntity = new ResponseEntity<>(roomResponse, HttpStatus.SERVICE_UNAVAILABLE);
	}
	return responseEntity;
    }

    private RoomInfoResponse mapData(ResponseEntity<?> responseEntity, ProductInfoResponse roomResponse) {
	// TODO Auto-generated method stub
	RoomInfoResponse object = new RoomInfoResponse();
	if (responseEntity.getStatusCode().name().equals("OK")) {

	    object.setMessage("Successful");
	    object.setStatus(String.valueOf(responseEntity.getStatusCodeValue()));
	    List<Room> list = roomResponse.getRooms();
	    List<RoomDataResponse> rooms = new ArrayList<>(list.size());
	    int count = 0;
	    String id = null, name = null;
	    for (Room obj : list) {
		id = obj.getTitle().getEnUS();
		RoomDataResponse objroom = new RoomDataResponse();
		objroom.setId(id);// id == null ? "null" :
		objroom.setName(id); // name == null ? "null" :
		rooms.add(objroom);
	    }

	    object.setData(rooms);
	}

	    object.setData(rooms);
	}

	return object;
    }

//    @GetMapping(
//            path = "/{token}/getRatePlans/{hotelId}/{roomId}",
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ApiOperation(
//            value = "Fetch Rateplans (Price Configurations) from rakuten given a hotelId and roomId.",
//            response = RatePlanResponse.class
//    )
//    public ResponseEntity<?> getRatePlans(@PathVariable("token") String token, @PathVariable("hotelId") String hotelId,
//                                          @PathVariable("roomId") String roomId) {
//        ResponseEntity<?> responseEntity;
//        try {
//            Utils.isValid(token, hotelId, acceptedToken);
//            Preconditions.checkArgument(!StringUtils.isEmpty(roomId), "RoomId cannot be null or empty");
//            RatePlanResponse ratePlanResponse = otaManager.getRatePlans(hotelId, roomId);
//            responseEntity = new ResponseEntity<>(ratePlanResponse, HttpStatus.OK);
//        } catch (SocketTimeoutException e) {
//            log.error("Encountered exception while getting rooms", e);
//            PriceResponse priceResponse = new PriceResponse();
//            priceResponse.setMessage(e.getMessage()+" ==> Please try again after sometime.");
//            priceResponse.setHttpStatusCode(HttpStatus.GATEWAY_TIMEOUT.value());
//            responseEntity = new ResponseEntity<>(priceResponse, HttpStatus.GATEWAY_TIMEOUT);
//            return responseEntity;
//        }catch (Throwable throwable) {
//            log.error("Encountered exception while getting ratePlans", throwable);
//            RatePlanResponse ratePlanResponse = new RatePlanResponse();
//            ratePlanResponse.setMessage(throwable.getMessage());
//            ratePlanResponse.setHttpStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
//            responseEntity = new ResponseEntity<>(ratePlanResponse, HttpStatus.SERVICE_UNAVAILABLE);
//        }
//        return responseEntity;
//    }

    @PostMapping(path = "/updateInventory", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Updates inventory to rakuten ota", response = InventoryResponse.class)
    public ResponseEntity<?> updateInventory(@Valid @RequestBody InventoryRequest inventoryRequest) {
	log.info("inside update inventory api of rakuten ota Request Received is ====>" + inventoryRequest);
	InventoryResponse response;
	ResponseEntity<?> responseEntity;
	try {
	    response = otaManager.updateInventory(inventoryRequest);
	    responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
	    return responseEntity;
	    // TO DO
	} catch (SocketTimeoutException e) {
	    log.error("Encountered exception while getting rooms", e);
	    PriceResponse priceResponse = new PriceResponse();
	    priceResponse.setMessage(e.getMessage() + " ==> Please try again after  sometime.");
	    priceResponse.setHttpStatusCode(HttpStatus.GATEWAY_TIMEOUT.value());
	    responseEntity = new ResponseEntity<>(priceResponse, HttpStatus.GATEWAY_TIMEOUT);
	    return responseEntity;
	} catch (Throwable throwable) {
	    log.error("Encountered exception while update inventory", throwable);
	    InventoryResponse inventoryResponse = new InventoryResponse();
	    inventoryResponse.setMessage(throwable.getMessage());
	    inventoryResponse.setHttpStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
	    responseEntity = new ResponseEntity<>(inventoryResponse, HttpStatus.SERVICE_UNAVAILABLE);
	}
	return responseEntity;
    }

    @PostMapping(path = "/updatePrice", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update rates to rakuten ota", response = PriceResponse.class)
    public ResponseEntity<?> updatePrice(@Valid @RequestBody PriceRequest priceRequest) {
	ResponseEntity<?> responseEntity;
	log.info("Update price received request ====>" + priceRequest);
	try {
	    PriceResponse priceResponse = otaManager.updatePrice(priceRequest);
	    responseEntity = new ResponseEntity<>(priceResponse, HttpStatus.OK);
	} catch (SocketTimeoutException e) {
	    log.error("Encountered exception while getting rooms", e);
	    PriceResponse priceResponse = new PriceResponse();
	    priceResponse.setMessage(e.getMessage() + " ==> Please try again after  sometime.");
	    priceResponse.setHttpStatusCode(HttpStatus.GATEWAY_TIMEOUT.value());
	    responseEntity = new ResponseEntity<>(priceResponse, HttpStatus.GATEWAY_TIMEOUT);
	    return responseEntity;
	} catch (Throwable throwable) {
	    log.error("Encountered exception while update prices", throwable);
	    PriceResponse priceResponse = new PriceResponse();
	    priceResponse.setMessage(throwable.getMessage());
	    priceResponse.setHttpStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
	    responseEntity = new ResponseEntity<>(priceResponse, HttpStatus.SERVICE_UNAVAILABLE);
	}
	return responseEntity;
    }

}
