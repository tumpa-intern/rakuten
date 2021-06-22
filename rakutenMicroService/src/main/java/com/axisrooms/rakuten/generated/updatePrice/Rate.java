
package com.axisrooms.rakuten.generated.updatePrice;

import com.axisrooms.rakuten.util.Constants;
import com.axisrooms.rakuten.util.OccupancyNotSupportedException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Rate {
    //add more if required
    private Integer    Single;
    private Integer    Double;
    private Integer    Twin;
    private Integer    Triple;
    private Integer    Quardable;
    private Integer    Quintuple;
    private Integer    Hexa;
    private Integer    Hepta;
    private Integer    Octa;
    private Integer    Nine;
    private Integer    Ten;
    private Integer    Eleven;
    private Integer    Twelve;
    private Integer    Twenty;
    private Integer    Thirty;
    private Integer    fullRate;
    private Integer    ea2;
    private Integer    ea3;
    private Integer    ec2;
    private Integer    ec3;
    private Integer    doubleWith2Child;
    private Integer    singleWithChild;
    private Integer    doubleWithChild;
    private Integer    tripleWithoutChild;
    private Integer    tripleWithChild;
    private Integer    quardWithoutChild;
    private Integer    quardWithChild;
    private Integer    childWithBed;
    private Integer    childWithoutBed;
    private Integer    extraPerson;
    private Integer    extraBed;
    private Integer    extraAdult;
    private Integer    extraInfant;
    private Integer    extraChild;
    private Integer    extraChildBelowFive;
    private Integer    extraChildAboveFive;
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDate endDate;
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDate    startDate;

    public void setPriceByOccupancyName(String occupancyName, String givenPrice) throws OccupancyNotSupportedException {
        Double aDouble= java.lang.Double.parseDouble(givenPrice);
        int price = aDouble.intValue();
        switch (occupancyName.toLowerCase())
     {
        case "single": this.setSingle(price);
                        break;
        case "double": this.setDouble(price);
                        break;
        case "twin": this.setTwin(price);
                        break;
        case "triple": this.setTriple(price);
                        break;
        case "quad": this.setQuardable(price);
                        break;
        case "penta": this.setQuintuple(price);
                       break;
        case "hexa": this.setHexa(price);
                       break;
        case "hepta": this.setHepta(price);
                       break;
        case "octa": this.setOcta(price);
                       break;
        case "extraperson":  this.setExtraPerson(price);
                                break;
        case "extrabed":     this.setExtraBed(price);
                                break;
        case "extraadult":   this.setExtraAdult(price);
                                break;
        case "extrainfant":  this.setExtraInfant(price);
                                break;
        case "extrachild":   this.setExtraChild(price);
                                break;
        case "extrachildbelowfive":  this.setExtraChildBelowFive(price);
                                break;
        case "extrachildabovefive":  this.setExtraChildAboveFive(price);
                                break;
        case "nine":  this.setNine(price);
             break;
        case "ten":     this.setTen(price);
             break;
        case "eleven":   this.setEleven(price);
             break;
        case "twelve":  this.setTwelve(price);
             break;
        case "twenty":   this.setTwenty(price);
             break;
        case "thirty":  this.setThirty(price);
             break;
        case "fullrate":  this.setFullRate(price);
             break;
        case "ea2":  this.setEa2(price);
             break;
        case "ea3":     this.setEa3(price);
             break;
        case "ec2":   this.setEc2(price);
             break;
        case "ec3":  this.setEc3(price);
             break;
        case "singlewithchild":   this.setSingleWithChild(price);
             break;
        case "doublewithchild":  this.setDoubleWithChild(price);
             break;
        case "doublewith2child":  this.setDoubleWith2Child(price);
             break;
        case "triplewithoutchild": this.setTripleWithoutChild(price);
             break;
        case "triplewithchild":   this.setTripleWithChild(price);
             break;
        case "quardwithoutchild":  this.setQuardWithoutChild(price);
             break;
        case "quardwithchild":   this.setQuardWithChild(price);
             break;
        case "childwithbed":  this.setChildWithBed(price);
             break;
        case "childwithoutbed":  this.setChildWithoutBed(price);
             break;
        default:
            throw new OccupancyNotSupportedException(occupancyName+" occupancy is not supported");
     }
    }
}
