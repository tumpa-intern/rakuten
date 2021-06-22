package com.axisrooms.rakuten.manager;

import com.axisrooms.rakuten.bean.*;
import com.axisrooms.rakuten.enums.Occupancy;
import com.axisrooms.rakuten.generated.RatePlanInfo.RatePlanInfoResponse;
import com.axisrooms.rakuten.generated.productInfo.ProductInfoResponse;
import com.axisrooms.rakuten.generated.productInfo.RoomData;
import com.axisrooms.rakuten.generated.updateInventory.InventoryUpdate;
import com.axisrooms.rakuten.generated.updateInventory.InventoryUpdateDataList;
import com.axisrooms.rakuten.generated.updateInventory.InventoryUpdateResponse;
import com.axisrooms.rakuten.generated.updatePrice.*;
import com.axisrooms.rakuten.model.TransactionLog;
import com.axisrooms.rakuten.repository.AxisroomsOtaRepository;
import com.axisrooms.rakuten.request.InventoryRequest;
import com.axisrooms.rakuten.request.PriceRequest;
import com.axisrooms.rakuten.request.RestrictionRequest;
import com.axisrooms.rakuten.response.*;
import com.axisrooms.rakuten.util.MarshalUnmarshalUtils;
import com.axisrooms.rakuten.util.OccupancyNotSupportedException;
import com.axisrooms.rakuten.util.Utils;
import com.caucho.quercus.QuercusEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

/**
 * actual call to the external api in ota side will happen from here
 */
@Service
@Slf4j
public class RakutenOtaManager implements OTAManager {

    @Autowired(required = true)
    private RestTemplate restTemplate;
    @Autowired
    private AxisroomsOtaRepository repository;
    @Value("${getRoomsUrl}")
    private String getRoomsUrl;
    @Value("${getUpdateInvUrl}")
    private String getUpdateInvUrl;
    @Value("${rakuten-ota.communication.apitoken}")
    private String apiKey;
    @Value("${getProductInfoUrl}")
    private String getProductInfoUrl;
    @Value("${getUpdatePriceUrl}")
    private String getUpdatePriceUrl;



    public String getpage() throws IOException {

        java.io.ByteArrayOutputStream newConsole = new ByteArrayOutputStream();
        System.setOut(new PrintStream(newConsole));

        QuercusEngine engine = new QuercusEngine();
        engine.setOutputStream(System.out);
        engine.executeFile("src\\main\\java\\com\\axisrooms\\rakuten\\index.php");

        return newConsole.toString();
    }

    @Override
    public ProductInfoResponse getRoomList(String hotelId) throws Exception {
        ProductInfoResponse productInfoResponse = getProductInfo(hotelId);
        return productInfoResponse;
    }

    private ProductInfoResponse getProductInfo(String hotelId) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String ans = getpage();
        httpHeaders.set("Authorization", ans);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        String Url = getRoomsUrl + hotelId;
        ResponseEntity<String> response = restTemplate.exchange(Url, HttpMethod.GET, entity, String.class);
        String responseString = response.getBody().toString();
        log.info("Axisrooms ota getRooms Response=" + responseString);
        try {
            return MarshalUnmarshalUtils.deserialize(responseString, ProductInfoResponse.class);
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {
            return MarshalUnmarshalUtils.deserialize(responseString, ProductInfoResponse.class);
        }
    }

    @Override
    public RatePlanResponse getRatePlans(String hotelId, String roomId) throws Exception {
        return null;
    }

    @Override
    public InventoryResponse updateInventory(InventoryRequest inventoryRequest) throws Exception {
        TransactionLog transactionLog = new TransactionLog();
        Utils.addCommonData(inventoryRequest, transactionLog);
        InventoryResponse inventoryResponse = null;
        try {
            Utils.addCMRequest(inventoryRequest, transactionLog);
            InventoryUpdate inventoryUpdateRequest = buildInventoryUpdateRequest(inventoryRequest);
            String jsonString = MarshalUnmarshalUtils.serialize(inventoryUpdateRequest);
            log.info("update inventory request:: " + jsonString);
            Utils.addOTARequest(jsonString, transactionLog);
            InventoryUpdateResponse inventoryUpdateResponse = restTemplate
                    .postForObject(getUpdateInvUrl, inventoryUpdateRequest, InventoryUpdateResponse.class);
            log.info("Response for update inventory....." + MarshalUnmarshalUtils.serialize(inventoryUpdateResponse));
            Utils.addOTAResponse(MarshalUnmarshalUtils.serialize(inventoryUpdateResponse), transactionLog);
            inventoryResponse = buildInventoryResponse(inventoryUpdateResponse);
            Utils.addCMResponse(inventoryResponse, transactionLog);

        } catch (Throwable throwable) {
            Utils.addOTAResponse(throwable, transactionLog);
            throw throwable;
        } finally {
            repository.save(transactionLog);
        }
        return inventoryResponse;
    }

    @Override
    public PriceResponse updatePrice(PriceRequest priceRequest) throws OccupancyNotSupportedException, Exception {
        TransactionLog transactionLog = new TransactionLog();
        Utils.addCommonData(priceRequest, transactionLog);
        RatePlanResponse ratePlanResponse = null;
        PriceResponse priceResponse=null;
        try {
            Utils.addCMRequest(priceRequest, transactionLog);
            UpdatePriceOTA updatePriceOTA = buildUpdatePriceRequests(priceRequest);
            String jsonString = MarshalUnmarshalUtils.serialize(updatePriceOTA);
            log.info("update price request:: " + jsonString);
            Utils.addOTARequest(jsonString, transactionLog);
            PriceUpdateResponse priceUpdateResponse = restTemplate.postForObject
                    (getUpdatePriceUrl, updatePriceOTA, PriceUpdateResponse.class);
            String responseJson = MarshalUnmarshalUtils.serialize(priceUpdateResponse);
            log.info("Response for update price....." + responseJson);
            Utils.addOTAResponse(responseJson, transactionLog);
            priceResponse = buildPriceResponse(priceUpdateResponse);
            Utils.addCMResponse(priceResponse, transactionLog);
        }catch (Throwable throwable) {
            Utils.addOTAResponse(throwable, transactionLog);
            throw throwable;
        } finally {
            repository.save(transactionLog);
        }
        return priceResponse;
    }

    @Override
    public InventoryResponse updateRestriction(RestrictionRequest restrictionRequest) throws Exception {
        return null;
    }

    private InventoryUpdate buildInventoryUpdateRequest(InventoryRequest inventoryRequest) {
        List<InventoryUpdateDataList> inventoryUpdateDataList = new ArrayList<>();
        InventoryUpdate inventoryUpdate = new InventoryUpdate();
//        inventoryUpdate.setRequestType("SaveSupplierHotel");
//        inventoryUpdate.setClientHotelCode(inventoryRequest.getHotelId());
//        inventoryUpdate.setToken(apiKey);
//        for (InventoryData eachInventoryData : inventoryRequest.getData()) {
//            InventoryUpdateDataList inventoryUpdateDataLst = new InventoryUpdateDataList();
//            inventoryUpdateDataList.add(inventoryUpdateDataLst);
//            inventoryUpdateDataLst.setRequestType("UpdateAllocation");
//            inventoryUpdate.setData(inventoryUpdateDataList);
//            List<InventoryUpdateData> updateDataList = new ArrayList<>();
//
//            for (Inventory eachInventory : eachInventoryData.getInventories()) {
//                InventoryUpdateData inventoryUpdateData = new InventoryUpdateData();
//                updateDataList.add(inventoryUpdateData);
//                inventoryUpdateDataLst.setData(updateDataList);
//                inventoryUpdateData.setAllocation(eachInventory.getInventory().toString());
//                inventoryUpdateData.setClientRoomCode(eachInventoryData.getRoomId());
//                inventoryUpdateData.setFrom(eachInventory.getStartDate().toString());
//                inventoryUpdateData.setTo(eachInventory.getEndDate().toString());
//
//            }
//        }
        return inventoryUpdate;
    }


    private InventoryResponse buildInventoryResponse(InventoryUpdateResponse inventoryUpdateResponse) {
        InventoryResponse inventoryResponse = new InventoryResponse();
        if (inventoryUpdateResponse.getStatus()) {
            inventoryResponse.setMessage("success");
            inventoryResponse.setHttpStatusCode(HttpStatus.OK.value());
        } else {
            inventoryResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            inventoryResponse.setMessage(inventoryUpdateResponse.getMessage());
        }
        return inventoryResponse;
    }

    private PriceResponse buildPriceResponse(PriceUpdateResponse priceUpdateResponse) {
        PriceResponse priceResponse = new PriceResponse();
        if (priceUpdateResponse.getStatus()) {
            priceResponse.setMessage("success");
            priceResponse.setHttpStatusCode(HttpStatus.OK.value());
        } else {
            priceResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            priceResponse.setMessage(priceUpdateResponse.getMessage());
        }
        return priceResponse;
    }

    private RatePlanResponse buildRatePlansResponse(RatePlanInfoResponse ratePlanInfoResponse, String roomId, RoomData roomData) {
        int adultOccupancy = roomData.getAllowedAdult();
        int childOccupancy = roomData.getAllowedChild();
        int guestOccupancy = roomData.getAllowedGuest();
        RatePlanResponse ratePlanResponse = new RatePlanResponse();
        if (Objects.nonNull(ratePlanInfoResponse)) {
            if (ratePlanInfoResponse.getMessage().equalsIgnoreCase("success")) {
                ratePlanResponse.setHttpStatusCode(HttpStatus.OK.value());
                ratePlanResponse.setMessage("success");
                List<RatePlanDescription> ratePlanDescriptions = new ArrayList<>();
                ratePlanResponse.setRatePlanDescriptions(ratePlanDescriptions);
                RatePlanDescription ratePlanDescription = new RatePlanDescription();
                ratePlanDescriptions.add(ratePlanDescription);
                ratePlanDescription.setRoomId(roomId);
                List<RatePlanConfiguration> ratePlanConfigurations = new ArrayList<>();
                ratePlanDescription.setConfigurations(ratePlanConfigurations);
                List<String> occupancies = new ArrayList<>();
                for (com.axisrooms.rakuten.generated.RatePlanInfo.RatePlanData datum : ratePlanInfoResponse.getData()) {
                    ratePlanDescription.setCurrency(datum.getBaseCurrency());
                    ratePlanDescription.setOccupancies(occupancies);
                    if (datum.getClientRoomCode().equals(roomId)) {
                        RatePlanConfiguration ratePlanConfiguration = new RatePlanConfiguration();
                        ratePlanConfigurations.add(ratePlanConfiguration);
                        if (datum.getPlanId() != null) {
                            ratePlanConfiguration.setRatePlanId(datum.getPlanId());
                        }
                        if (datum.getClientPlanName() != null) {
                            ratePlanConfiguration.setRatePlanName(datum.getClientPlanName());
                        }
                        while (adultOccupancy > 0) {
                            occupancies.add(Occupancy.occupancyMapAlloted.get(adultOccupancy));
                            --adultOccupancy;
                        }
                        if (childOccupancy > 0) {
                            occupancies.add("extraChild");
                            childOccupancy = 0;
                        }
                        if (guestOccupancy > 0) {
                            occupancies.add("extraAdult");
                            guestOccupancy = 0;
                        }
                    }
                }
            } else {
                ratePlanResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                ratePlanResponse.setMessage("External api call failed " + ratePlanInfoResponse.getMessage());
            }
        } else {
            ratePlanResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            ratePlanResponse.setMessage("Marshalling error occurred");
        }
        return ratePlanResponse;
    }

    private UpdatePriceOTA buildUpdatePriceRequests(PriceRequest priceRequest)
            throws OccupancyNotSupportedException {
        UpdatePriceOTA updatePriceOTA = new UpdatePriceOTA();
        List<UpdateData> updateDataList=new ArrayList<>();
        updatePriceOTA.setClientHotelCode(priceRequest.getHotelId());
        updatePriceOTA.setRequestType("SaveSupplierHotel");
        updatePriceOTA.setToken(apiKey);
        for (PriceData eachPriceData : priceRequest.getData()) {
            for (RoomDetail roomDetail : eachPriceData.getRoomDetails()) {
                List<Data> dataList = new ArrayList<>();
                UpdateData updateData = new UpdateData();
                for (RatePlanDetail ratePlanDetail : roomDetail.getRatePlanDetails()) {
                    try {
                        for (com.axisrooms.rakuten.bean.Rate eachRate : ratePlanDetail.getRates()) {                         //multiple apis here

                            updateData.setRequestType("Price_Cancellation");
                            Data data = new Data();
                            data.setClientRoomCode(roomDetail.getRoomId());
                            data.setContractCode("");
                            data.setFrom(eachRate.getStartDate().toString());
                            data.setTo(eachRate.getEndDate().toString());
                            RoomAvailablityDetail roomAvailablityDetail = new RoomAvailablityDetail();
                            PriceDetail priceDetail = new PriceDetail();
//                            priceDetail.setBt("B2C");
                            priceDetail.setPlanId(ratePlanDetail.getRatePlanId());
                            priceDetail.setGst("0");
                            priceDetail.setOnePaxRack("0");
                            priceDetail.setTwoPaxOccupancy("0");

                            Map<String, String> priceMap = eachRate.getPrices();
                            for (String occupancy : priceMap.keySet()) {
                                if (occupancy.equalsIgnoreCase("single")) {
                                    priceDetail.setOnePaxOccupancy(priceMap.get(occupancy));
                                } else if (occupancy.equalsIgnoreCase("double")) {
                                    priceDetail.setTwoPaxOccupancy(priceMap.get(occupancy));
                                } else if (occupancy.equalsIgnoreCase("triple")) {
                                    priceDetail.setThreePaxOccupancy(priceMap.get(occupancy));
                                } else if (occupancy.equalsIgnoreCase("quad")) {
                                    priceDetail.setFourPaxOccupancy(priceMap.get(occupancy));
                                } else if (occupancy.equalsIgnoreCase("extrachild")) {
                                    priceDetail.setChildRate(priceMap.get(occupancy));
                                    priceDetail.setChildWithBedRate(priceMap.get(occupancy));
                                } else if (occupancy.equalsIgnoreCase("extraadult")) {
                                    priceDetail.setExtraAdultRate(priceMap.get(occupancy));
                                } else {
                                    throw new OccupancyNotSupportedException("Occupancy not supported");
                                }
                            }
                            priceDetail.setExtraBedRate("0");
//                            priceDetail.setCommissionAmount("0");
                            roomAvailablityDetail.setPriceDetail(priceDetail);
                            RoomCancellation roomCancellation = new RoomCancellation();
                            roomCancellation.setPolicyDescription("write Cancellation policy description here.");
                            data.setRoomAvailablityDetail(roomAvailablityDetail);
                            data.setRoomCancellation(roomCancellation);
                            dataList.add(data);
                            updateData.setData(dataList);
                        }
                        updateDataList.add(updateData);
                        updatePriceOTA.setUpdateData(updateDataList);
                    } catch (OccupancyNotSupportedException e) {
                        log.error("Exception while preparing price update api input:: " + e.getMessage());
                        throw e;
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw e;
                    }
                }
            }

        }
        return updatePriceOTA;
    }
}

//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Value("${getRoomsUrl}")
//    private String getRoomsUrl;

//    @Value("${getUpdateInvUrl}")
//    private String getUpdateInvUrl;
//
//    @Value("${getUpdatePriceUrl}")
//    private String getUpdatePriceUrl;
//
//    @Value("${getProductInfoUrl}")
//    private String getProductInfoUrl;
//
//    @Value("${rakuten-ota.communication.userName}")
//    private String username;
//
//    @Value("${rakuten-ota.communication.password}")
//    private String password;
//
//    @Value("${rakuten-ota.communication.apitoken}")
//    private String apiKey;
//
//    @Autowired
//    private AxisroomsOtaRepository repository;
//
//    @Override
//    public RoomResponse getRoomList(String hotelId) throws Exception {
//        ProductInfoRequest productInfoRequest = buildProductInfoRequest(hotelId);
//        ProductInfoResponse productInfoResponse = getProductInfo(productInfoRequest);
//        RoomResponse roomResponse = buildRoomResponse(productInfoResponse);
//        return roomResponse;
//    }
//
//    @Override
//    public RatePlanResponse getRatePlans(String hotelId, String roomId) throws Exception {
//        ProductInfoRequest productInfoRequest = buildProductInfoRequest(hotelId);
//        ProductInfoResponse productInfoResponse = getProductInfo(productInfoRequest);
//        RoomData roomData = Arrays.stream(productInfoResponse.getData()).filter(x -> x.getClientRoomCode().equals(roomId)).findFirst().get();
//        RatePlanInfoRequest ratePlanInfoRequest = buildRatePlanInfoRequest(hotelId, roomId);
//        RatePlanInfoResponse ratePlanInfoResponse = getRatePlanInfo(ratePlanInfoRequest);
//        RatePlanResponse ratePlanResponse = buildRatePlansResponse(ratePlanInfoResponse, roomId,roomData);
//        return ratePlanResponse;
//    }

//    @Override
//    public InventoryResponse updateInventory(InventoryRequest inventoryRequest) throws Exception {
//        TransactionLog transactionLog = new TransactionLog();
//        Utils.addCommonData(inventoryRequest, transactionLog);
//        InventoryResponse inventoryResponse = null;
//        try {
//            Utils.addCMRequest(inventoryRequest, transactionLog);
//            InventoryUpdate inventoryUpdateRequest = buildInventoryUpdateRequest(inventoryRequest);
//            String jsonString = MarshalUnmarshalUtils.serialize(inventoryUpdateRequest);
//            log.info("update inventory request:: " + jsonString);
//            Utils.addOTARequest(jsonString, transactionLog);
//            InventoryUpdateResponse inventoryUpdateResponse = restTemplate
//                        .postForObject(getUpdateInvUrl, inventoryUpdateRequest, InventoryUpdateResponse.class);
//            log.info("Response for update inventory....." + MarshalUnmarshalUtils.serialize(inventoryUpdateResponse));
//            Utils.addOTAResponse(MarshalUnmarshalUtils.serialize(inventoryUpdateResponse), transactionLog);
//            inventoryResponse = buildInventoryResponse(inventoryUpdateResponse);
//            Utils.addCMResponse(inventoryResponse, transactionLog);
//        } catch (Throwable throwable) {
//            Utils.addOTAResponse(throwable, transactionLog);
//            throw throwable;
//        } finally {
//            repository.save(transactionLog);
//        }
//        return inventoryResponse;
//    }
//
//    @Override
//    public PriceResponse updatePrice(PriceRequest priceRequest) throws OccupancyNotSupportedException, Exception {
//        TransactionLog transactionLog = new TransactionLog();
//        Utils.addCommonData(priceRequest, transactionLog);
//        RatePlanResponse ratePlanResponse = null;
//        PriceResponse priceResponse=null;
//        try {
//            Utils.addCMRequest(priceRequest, transactionLog);
//            UpdatePriceOTA updatePriceOTA = buildUpdatePriceRequests(priceRequest);
//                String jsonString = MarshalUnmarshalUtils.serialize(updatePriceOTA);
//                log.info("update price request:: " + jsonString);
//                Utils.addOTARequest(jsonString, transactionLog);
//                PriceUpdateResponse priceUpdateResponse = restTemplate.postForObject
//                        (getUpdatePriceUrl, updatePriceOTA, PriceUpdateResponse.class);
//                String responseJson = MarshalUnmarshalUtils.serialize(priceUpdateResponse);
//                log.info("Response for update price....." + responseJson);
//                Utils.addOTAResponse(responseJson, transactionLog);
//                priceResponse = buildPriceResponse(priceUpdateResponse);
//                Utils.addCMResponse(priceResponse, transactionLog);
//        }catch (Throwable throwable) {
//            Utils.addOTAResponse(throwable, transactionLog);
//            throw throwable;
//        } finally {
//            repository.save(transactionLog);
//        }
//        return priceResponse;
//    }
//
//    @Override
//    public InventoryResponse updateRestriction(RestrictionRequest restrictionRequest) throws Exception {
//        return null;
//    }
//
//    /*
//    Because common format has support for single room per request as of now
//     */
//    private InventoryUpdate buildInventoryUpdateRequest(InventoryRequest inventoryRequest) {
//        List<InventoryUpdateDataList> inventoryUpdateDataList=new ArrayList<>();
//        InventoryUpdate inventoryUpdate = new InventoryUpdate();
//        inventoryUpdate.setRequestType("SaveSupplierHotel");
//        inventoryUpdate.setClientHotelCode(inventoryRequest.getHotelId());
//        inventoryUpdate.setToken(apiKey);
//        for (InventoryData eachInventoryData : inventoryRequest.getData()) {
//            InventoryUpdateDataList inventoryUpdateDataLst = new InventoryUpdateDataList();
//            inventoryUpdateDataList.add(inventoryUpdateDataLst);
//            inventoryUpdateDataLst.setRequestType("UpdateAllocation");
//            inventoryUpdate.setData(inventoryUpdateDataList);
//            List<InventoryUpdateData> updateDataList=new ArrayList<>();
//
//            for (Inventory eachInventory : eachInventoryData.getInventories()) {
//                InventoryUpdateData inventoryUpdateData=new InventoryUpdateData();
//                updateDataList.add(inventoryUpdateData);
//                inventoryUpdateDataLst.setData(updateDataList);
//                inventoryUpdateData.setAllocation(eachInventory.getInventory().toString());
//                inventoryUpdateData.setClientRoomCode(eachInventoryData.getRoomId());
//                inventoryUpdateData.setFrom(eachInventory.getStartDate().toString());
//                inventoryUpdateData.setTo(eachInventory.getEndDate().toString());
//
//            }
//        }
//        return inventoryUpdate;
//    }
//
//    private InventoryResponse buildInventoryResponse(InventoryUpdateResponse inventoryUpdateResponse) {
//        InventoryResponse inventoryResponse = new InventoryResponse();
//        if (inventoryUpdateResponse.getStatus()) {
//            inventoryResponse.setMessage("success");
//            inventoryResponse.setHttpStatusCode(HttpStatus.OK.value());
//        } else {
//            inventoryResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            inventoryResponse.setMessage(inventoryUpdateResponse.getMessage());
//        }
//        return inventoryResponse;
//    }
//
//    private PriceResponse buildPriceResponse(PriceUpdateResponse priceUpdateResponse) {
//        PriceResponse priceResponse = new PriceResponse();
//        if (priceUpdateResponse.getStatus()) {
//            priceResponse.setMessage("success");
//            priceResponse.setHttpStatusCode(HttpStatus.OK.value());
//        } else {
//            priceResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            priceResponse.setMessage(priceUpdateResponse.getMessage());
//        }
//        return priceResponse;
//    }
//
//    private UpdatePriceOTA buildUpdatePriceRequests(PriceRequest priceRequest)
//            throws OccupancyNotSupportedException {
//        UpdatePriceOTA updatePriceOTA = new UpdatePriceOTA();
//        List<UpdateData> updateDataList=new ArrayList<>();
//        updatePriceOTA.setClientHotelCode(priceRequest.getHotelId());
//        updatePriceOTA.setRequestType("SaveSupplierHotel");
//        updatePriceOTA.setToken(apiKey);
//        for (PriceData eachPriceData : priceRequest.getData()) {
//            for (RoomDetail roomDetail : eachPriceData.getRoomDetails()) {
//                    List<Data> dataList = new ArrayList<>();
//                    UpdateData updateData = new UpdateData();
//                    for (RatePlanDetail ratePlanDetail : roomDetail.getRatePlanDetails()) {
//                        try {
//                            for (com.axisrooms.rakuten.bean.Rate eachRate : ratePlanDetail.getRates()) {                         //multiple apis here
//
//                                updateData.setRequestType("Price_Cancellation");
//                                Data data = new Data();
//                                data.setClientRoomCode(roomDetail.getRoomId());
//                                data.setContractCode("");
//                                data.setFrom(eachRate.getStartDate().toString());
//                                data.setTo(eachRate.getEndDate().toString());
//                                RoomAvailablityDetail roomAvailablityDetail = new RoomAvailablityDetail();
//                                PriceDetail priceDetail = new PriceDetail();
////                            priceDetail.setBt("B2C");
//                                priceDetail.setPlanId(ratePlanDetail.getRatePlanId());
//                                priceDetail.setGst("0");
//                                priceDetail.setOnePaxRack("0");
//                                priceDetail.setTwoPaxOccupancy("0");
//
//                                Map<String, String> priceMap = eachRate.getPrices();
//                                for (String occupancy : priceMap.keySet()) {
//                                    if (occupancy.equalsIgnoreCase("single")) {
//                                        priceDetail.setOnePaxOccupancy(priceMap.get(occupancy));
//                                    } else if (occupancy.equalsIgnoreCase("double")) {
//                                        priceDetail.setTwoPaxOccupancy(priceMap.get(occupancy));
//                                    } else if (occupancy.equalsIgnoreCase("triple")) {
//                                        priceDetail.setThreePaxOccupancy(priceMap.get(occupancy));
//                                    } else if (occupancy.equalsIgnoreCase("quad")) {
//                                        priceDetail.setFourPaxOccupancy(priceMap.get(occupancy));
//                                    } else if (occupancy.equalsIgnoreCase("extrachild")) {
//                                        priceDetail.setChildRate(priceMap.get(occupancy));
//                                        priceDetail.setChildWithBedRate(priceMap.get(occupancy));
//                                    } else if (occupancy.equalsIgnoreCase("extraadult")) {
//                                        priceDetail.setExtraAdultRate(priceMap.get(occupancy));
//                                    } else {
//                                        throw new OccupancyNotSupportedException("Occupancy not supported");
//                                    }
//                                }
//                                priceDetail.setExtraBedRate("0");
////                            priceDetail.setCommissionAmount("0");
//                                roomAvailablityDetail.setPriceDetail(priceDetail);
//                                RoomCancellation roomCancellation = new RoomCancellation();
//                                roomCancellation.setPolicyDescription("write Cancellation policy description here.");
//                                data.setRoomAvailablityDetail(roomAvailablityDetail);
//                                data.setRoomCancellation(roomCancellation);
//                                dataList.add(data);
//                                updateData.setData(dataList);
//                            }
//                            updateDataList.add(updateData);
//                            updatePriceOTA.setUpdateData(updateDataList);
//                        } catch (OccupancyNotSupportedException e) {
//                            log.error("Exception while preparing price update api input:: " + e.getMessage());
//                            throw e;
//                        } catch (Exception e) {
//                            log.error(e.getMessage());
//                            throw e;
//                        }
//                    }
//            }
//
//        }
//        return updatePriceOTA;
//    }
//
//    private RatePlanResponse buildRatePlanResponse(RateUpdateResponse rateUpdateResponse) {
//        RatePlanResponse ratePlanResponse = new RatePlanResponse();
//        if (rateUpdateResponse.getStatus().equalsIgnoreCase("success")) {
//            ratePlanResponse.setMessage("success");
//            ratePlanResponse.setHttpStatusCode(HttpStatus.OK.value());
//        } else {
//            ratePlanResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            ratePlanResponse.setMessage(ratePlanResponse.getMessage());
//        }
//        return ratePlanResponse;
//    }
//
//    private ProductInfoRequest buildProductInfoRequest(String hotelId) {
//        ProductInfoRequest productInfoRequest = new ProductInfoRequest();
//        com.axisrooms.rakuten.generated.productInfo.Auth auth = new com.axisrooms.rakuten.generated.productInfo.Auth();
//        productInfoRequest.setAuth(auth);
//        auth.setToken(apiKey);
//        auth.setType("RoomList");
//        productInfoRequest.setClientHotelCode(hotelId);
//        return productInfoRequest;
//    }
//
//    private ProductInfoResponse getProductInfo(ProductInfoRequest productInfoRequest) throws Exception {
//        log.info("Input request to fetch rooms: -> " + MarshalUnmarshalUtils.serialize(productInfoRequest));
//        ProductInfoResponse response =restTemplate.postForObject(getRoomsUrl, productInfoRequest, ProductInfoResponse.class);
//        log.info("Axisrooms ota getRooms Response=" + MarshalUnmarshalUtils.serialize(response));
//        return response;
//    }
//
//    public RakutenOtaManager() {
//        super();
//    }
//
//    /*
//        axisagent-commonOta conversion
//         */
//    private RoomResponse buildRoomResponse(ProductInfoResponse productInfoResponse) throws Exception {
//        RoomResponse roomResponse = new RoomResponse();
//        if (Objects.nonNull(productInfoResponse)) {
//            if (productInfoResponse.getMessage().equalsIgnoreCase("success")) {
//                Set<Description> descriptions = new HashSet<>();
//                for (RoomData datum : productInfoResponse.getData()) {
//                    Description description = new Description();
//                    descriptions.add(description);
//                    description.setId(datum.getClientRoomCode());
//                    description.setName(datum.getRoomName());
//                }
//                roomResponse.setDescriptions(descriptions);
//                roomResponse.setMessage(SUCCESS);
//                roomResponse.setHttpStatusCode(HttpStatus.OK.value());
//            } else {
//                roomResponse
//                        .setMessage("request to fetch rooms failed from ota::  " + productInfoResponse.getMessage());
//                roomResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            }
//        } else {
//            roomResponse.setMessage("Marshaling/Serialization Exception Occured.");
//            roomResponse.setHttpStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
//        }
//        return roomResponse;
//    }
//
//    private RatePlanInfoRequest buildRatePlanInfoRequest(String hotelId, String roomId) {
//        RatePlanInfoRequest ratePlanInfoRequest = new RatePlanInfoRequest();
//        com.axisrooms.rakuten.generated.RatePlanInfo.Auth auth = new com.axisrooms.rakuten.generated.RatePlanInfo.Auth();
//        ratePlanInfoRequest.setAuth(auth);
//        auth.setToken(apiKey);
//        auth.setType("RoomListWithMealPlan");
//        ratePlanInfoRequest.setClientHotelCode(hotelId);
//        return ratePlanInfoRequest;
//    }
//
//    private RatePlanInfoResponse getRatePlanInfo(RatePlanInfoRequest ratePlanInfoRequest) throws Exception {
//        log.info("Input request to fetch rate plans: -> " + MarshalUnmarshalUtils.serialize(ratePlanInfoRequest));
//        RatePlanInfoResponse response = restTemplate.postForObject(getProductInfoUrl, ratePlanInfoRequest, RatePlanInfoResponse.class); //use this for getRatePlans
//        log.info("Axisrooms ota get rate plans Response=" + MarshalUnmarshalUtils.serialize(response));
//        return response;
//    }
//
//    private RatePlanResponse buildRatePlansResponse(RatePlanInfoResponse ratePlanInfoResponse, String roomId,RoomData roomData){
//        int adultOccupancy=roomData.getAllowedAdult();
//        int childOccupancy=roomData.getAllowedChild();
//        int guestOccupancy=roomData.getAllowedGuest();
//        RatePlanResponse ratePlanResponse = new RatePlanResponse();
//        if (Objects.nonNull(ratePlanInfoResponse)) {
//            if (ratePlanInfoResponse.getMessage().equalsIgnoreCase("success")) {
//                ratePlanResponse.setHttpStatusCode(HttpStatus.OK.value());
//                ratePlanResponse.setMessage("success");
//                List<RatePlanDescription> ratePlanDescriptions = new ArrayList<>();
//                ratePlanResponse.setRatePlanDescriptions(ratePlanDescriptions);
//                RatePlanDescription ratePlanDescription = new RatePlanDescription();
//                ratePlanDescriptions.add(ratePlanDescription);
//                ratePlanDescription.setRoomId(roomId);
//                List<RatePlanConfiguration> ratePlanConfigurations = new ArrayList<>();
//                ratePlanDescription.setConfigurations(ratePlanConfigurations);
//                List<String> occupancies = new ArrayList<>();
//                for (com.axisrooms.rakuten.generated.RatePlanInfo.RatePlanData datum : ratePlanInfoResponse.getData()) {
//                    ratePlanDescription.setCurrency(datum.getBaseCurrency());
//                    ratePlanDescription.setOccupancies(occupancies);
//                    if(datum.getClientRoomCode().equals(roomId)) {
//                        RatePlanConfiguration ratePlanConfiguration = new RatePlanConfiguration();
//                        ratePlanConfigurations.add(ratePlanConfiguration);
//                        if (datum.getPlanId() != null) {
//                            ratePlanConfiguration.setRatePlanId(datum.getPlanId());
//                        }
//                        if (datum.getClientPlanName() != null) {
//                            ratePlanConfiguration.setRatePlanName(datum.getClientPlanName());
//                        }
//                        while (adultOccupancy > 0) {
//                            occupancies.add(Occupancy.occupancyMapAlloted.get(adultOccupancy));
//                            --adultOccupancy;
//                        }
//                        if(childOccupancy>0){
//                            occupancies.add("extraChild");
//                            childOccupancy=0;
//                        }
//                        if(guestOccupancy>0){
//                            occupancies.add("extraAdult");
//                            guestOccupancy=0;
//                        }
//                    }
//                }
//            } else {
//                ratePlanResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//                ratePlanResponse.setMessage("External api call failed " + ratePlanInfoResponse.getMessage());
//            }
//        } else {
//            ratePlanResponse.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            ratePlanResponse.setMessage("Marshalling error occurred");
//        }
//        return ratePlanResponse;
//    }

