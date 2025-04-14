package com.yichen.po;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
@Data
public class ParkingRecord {
    private long id;
    private String plateNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Duration parkingTime;
    private Boolean status;
}
