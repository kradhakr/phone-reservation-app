package com.spring.phone.reservation.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String brand;
    private String model;
    private boolean available = true;
    private LocalDateTime bookedAt;
    private String bookedBy;

    public Phone (String brand, String model) {
        this.brand = brand;
        this.model = model;
    }

    public Phone (String brand, String model, boolean available, String bookedBy, LocalDateTime bookedAt) {
        this.brand = brand;
        this.model = model;
        this.available = available;
        this.bookedBy = bookedBy;
        this.bookedAt = bookedAt;
    }

    public Phone () {
    }

}
