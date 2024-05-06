package com.spring.phone.reservation.controller;

import com.spring.phone.reservation.dto.PhoneDTO;
import com.spring.phone.reservation.exception.PhoneAlreadyAvailableException;
import com.spring.phone.reservation.exception.PhoneAlreadyBookedException;
import com.spring.phone.reservation.exception.ResourceNotFoundException;
import com.spring.phone.reservation.service.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phone")
public class PhoneController {
    private static final Logger logger = LoggerFactory.getLogger(PhoneController.class);
    @Autowired
    private PhoneService phoneService;

    @GetMapping("/")
    public List<PhoneDTO> getAllPhones() {
        return phoneService.findAllPhones();
    }

    @GetMapping("/{id}")
    public PhoneDTO getPhoneById(@PathVariable(value = "id") Long phoneId)
            throws ResourceNotFoundException {
        return phoneService.findById(phoneId);
    }

    @PostMapping("/{id}/book")
    public ResponseEntity<?> bookPhone(@PathVariable(value = "id") long phoneId, @RequestParam String bookedBy) throws ResourceNotFoundException, PhoneAlreadyBookedException {
        logger.info("Received request to book phone with ID {} for user {}", phoneId, bookedBy);
        try {
            PhoneDTO phoneDTO =  phoneService.bookPhone(phoneId, bookedBy);
            logger.info("Successfully booked phone with ID {} for user {}", phoneId, bookedBy);
            return ResponseEntity.ok(phoneDTO);
        } catch (PhoneAlreadyBookedException e) {
            logger.error("Failed to book phone with ID {}: {}", phoneId, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            logger.error("Unexpected error while booking phone with ID {}: {}", phoneId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while booking phone with ID {}: {}", phoneId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<?> returnPhone(@PathVariable(value = "id") long phoneId) throws ResourceNotFoundException, PhoneAlreadyAvailableException {
        logger.info("Received request to return phone with ID {}", phoneId);
        try {
            PhoneDTO phoneDTO =  phoneService.returnPhone(phoneId);
            logger.info("Successfully returned phone with ID {} for user {}", phoneId);
            return ResponseEntity.ok(phoneDTO);
        } catch (PhoneAlreadyAvailableException e) {
            logger.error("Failed to return phone with ID {}: {}", phoneId, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            logger.error("Unexpected error while booking phone with ID {}: {}", phoneId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while booking phone with ID {}: {}", phoneId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/{id}")
    public String deletePhone(@PathVariable(value = "id") Long phoneId) throws ResourceNotFoundException {
        PhoneDTO phoneDTO = phoneService.findById(phoneId);
        phoneService.delete(phoneDTO.getId());
        return "Deleted phone with id  :: "+phoneId;
    }
}

