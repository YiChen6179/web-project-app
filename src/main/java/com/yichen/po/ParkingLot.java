package com.yichen.po;

import lombok.Data;

@Data
public class ParkingLot {
    private long id;
    private String LotName;
    private String address;
    private Integer totalSpot;
    private Integer availableSpot;
}
