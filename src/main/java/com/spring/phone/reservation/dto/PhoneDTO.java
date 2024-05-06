package com.spring.phone.reservation.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PhoneDTO {
    private Long id;
    private String brand;
    private String model;
    private boolean available;
    private LocalDateTime bookedAt;
    private String bookedBy;

}
