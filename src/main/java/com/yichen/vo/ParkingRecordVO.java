package com.yichen.vo;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
@Data
public class ParkingRecordVO {
    private long id;
    private String plateNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Duration parkingTime;
    private Boolean status;
}
