package com.axisrooms.rakuten.generated.productInfo;

import java.util.List;

import com.axisrooms.rakuten.response.Description;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Room {

    public String propertyCode;
    public String roomTypeCode;
    public String roomCategory;
    public boolean isRoomOpened;
    public Title title;
    public Brief brief;
    public Description description;
    public int maxRollaways;
    public int maxCribs;
    public int minAdultOccupancy;
    public int maxAdultOccupancy;
    public int minChildOccupancy;
    public int maxChildOccupancy;
    public Object quantity;
    public Object viewCode;
    public Object nonSmoking;
    public Object floor;
    public String roomGender;
    public boolean isSharing;
    public Object size;
    public List<BedRoom> bedRooms;
    public Object livingRooms;
    public Object bathRooms;
    public Object equipments;
    public Object amenities;
    public Object images;
}
