package com.axisrooms.rakuten.generated.updatePrice;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@lombok.Data
public class PriceDetail {
    @JsonProperty("ChildWithBedRate")
    private String childWithBedRate;
    @JsonProperty("TwoPaxOccupancy")
    private String twoPaxOccupancy;
    @JsonProperty("OnePaxOccupancy")
    private String onePaxOccupancy;
    @JsonProperty("ThreePaxOccupancy")
    private String threePaxOccupancy;
    @JsonProperty("FourPaxOccupancy")
    private String fourPaxOccupancy;
    @JsonProperty("ExtraBedRate")
    private String extraBedRate;
    @JsonProperty("PlanId")
    private String planId;
    @JsonProperty("ExtraAdultRate")
    private String extraAdultRate;
    @JsonProperty("TwoPaxRack")
    private String twoPaxRack;
    @JsonProperty("BT")
    private String bt;
    @JsonProperty("CommissionAmount")
    private String commissionAmount;
    @JsonProperty("ChildRate")
    private String childRate;
    @JsonProperty("OnePaxRack")
    private String onePaxRack;
    @JsonProperty("GST")
    private String gst;
}
