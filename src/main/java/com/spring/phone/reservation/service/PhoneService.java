package com.spring.phone.reservation.service;

import com.spring.phone.reservation.dto.PhoneDTO;
import com.spring.phone.reservation.exception.PhoneAlreadyAvailableException;
import com.spring.phone.reservation.exception.PhoneAlreadyBookedException;
import com.spring.phone.reservation.exception.ResourceNotFoundException;

import java.util.List;

public interface PhoneService {
    PhoneDTO bookPhone(Long phoneId, String bookedBy) throws ResourceNotFoundException, PhoneAlreadyBookedException;
    PhoneDTO returnPhone(Long phoneId) throws ResourceNotFoundException, PhoneAlreadyAvailableException;
    List<PhoneDTO> findAllPhones();
    PhoneDTO findById(Long phoneId) throws ResourceNotFoundException;
    void delete(Long phoneId);
}
