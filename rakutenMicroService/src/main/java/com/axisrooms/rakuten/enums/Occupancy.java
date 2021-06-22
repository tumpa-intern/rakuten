package com.axisrooms.rakuten.enums;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum Occupancy {

    SINGLE(1, "single",10,1,1),
    DOUBLE(2, "double",10,2,2),
    TWIN(18, "twin",10,2,21),
    TRIPLE(3, "triple",10,3,3),
    QUARDABLE(4, "quad",10,4,4),
    QUINTUPLE(20, "penta",10,5,5),
    HEXA(19, "hexa",10,6,6),
    HEPTA(21, "hepta",10,7,7),
    OCTA(22, "octa",10,8,8),
    NINE(26, "nine",10,9,9),
    TEN(27, "deca",10,10,10),
    ELEVEN(39, "eleven",10,11,11),
    TWELVE(40, "twelve",10,12,12),
    TWENTY(35, "twenty",10,20,20),
    THIRTY(36, "thirty",10,30,30),
    EXTRA_PERSON(10, "extraPerson",10,1,0),
    FULL_RATE(13, "fullRate",10,1,0),
    EA2(14, "ea2",10,2,0),
    EA3(15, "ea3",10,3,0),
    EC2(16, "ec2",10,2,0),
    EC3(17, "ec3",10,3,0),
    EXTRA_CHILD_BELOW_FIVE(23, "extraChildBelowFive",10,1,0),
    EXTRA_CHILD_ABOVE_FIVE(24, "extraChildAboveFive",10,1,0),
    EXTRA_INFANT(25, "extraInfant",10,1,0),
    SINGLE_WITH_CHILD(28, "singleWithChild",10,2,0),
    DOUBLE_WITH_CHILD(29, "doubleWithChild",10,3,0),
    DOUBLE_WITH_2_CHILD(30, "doubleWith2Child",10,4,0),
    TRIPLE_WITHOUT_CHILD(31, "tripleWithoutChild",10,3,0),
    TRIPLE_WITH_CHILD(32, "tripleWithChild",10,4,0),
    QUARD_WITHOUT_CHILD(33, "quardWithoutChild",10,4,0),
    QUARD_WITH_CHILD(34, "quardWithChild",10,5,0),
    CHILD_WITH_BED(37, "childWithBed",10,1,0),
    CHILD_WITHOUT_BED(38, "childWithoutBed",10,1,0),
    EXTRA_BED(12, "extraBed",10,1,0),
    EXTRA_ADULT(5, "extraAdult",10,1,0),
    EXTRA_CHILD(11, "extraChild",8,1,0),
    EXTRA_ADULT2(14, "extraAdult2",10,1,0),
    EXTRA_CHILD2(16, "extraChild2",8,1,0),
    EXTRA_ADULT3(15, "extraAdult3",10,1,0),
    EXTRA_CHILD3(17, "extraChild3",8,1,0);

    private static Map<Integer, Occupancy> occupancyMap = new HashMap<>();

    public static Map<Integer, String> occupancyMapAlloted = new HashMap<>();

    static {
        for (Occupancy entity : Occupancy.values()) {
            occupancyMap.put(entity.getId(), entity);
        }
    }

    static {
        for (Occupancy entity : Occupancy.values()) {
            occupancyMapAlloted.put(entity.getRoomCapacity(), entity.getName());
        }
    }

    private final int id;
    private final String name;
    private final int ageQualifyingCode;
    private final int capacity;
    @Getter
    private final int roomCapacity;
    private Occupancy(int occId, String occName, int occAgeQualifyingCode, int capacity,int roomCapacity) {
        id = occId;
        name = occName;
        ageQualifyingCode = occAgeQualifyingCode;
        this.capacity = capacity;
        this.roomCapacity=roomCapacity;
    }

    public static Occupancy valueOf(int id) {
        return occupancyMap.get(id);
    }

    public int getCapacity(){return capacity;}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAgeQualifyingCode() {
        return ageQualifyingCode;
    }

//    public int getroomCapacity() {
//        return roomCapacity;
//    }

    public static Map<Integer, Occupancy> getOccupancyMap() {
        return occupancyMap;
    }

    public static void setOccupancyMap(Map<Integer, Occupancy> occupancyMap) {
        Occupancy.occupancyMap = occupancyMap;
    }
}
