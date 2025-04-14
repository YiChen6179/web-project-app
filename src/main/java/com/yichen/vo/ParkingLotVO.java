package com.yichen.vo;

import lombok.Data;

@Data
public class ParkingLotVO {
    private long id;
    private String LotName;
    private String address;
    private Integer totalSpot;
    private Integer availableSpot;
}
