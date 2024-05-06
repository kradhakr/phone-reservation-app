package com.spring.phone.reservation.service.impl;

import com.spring.phone.reservation.dto.PhoneDTO;
import com.spring.phone.reservation.exception.PhoneAlreadyAvailableException;
import com.spring.phone.reservation.exception.PhoneAlreadyBookedException;
import com.spring.phone.reservation.exception.ResourceNotFoundException;
import com.spring.phone.reservation.model.*;
import com.spring.phone.reservation.repository.PhoneRepository;
import com.spring.phone.reservation.service.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhoneServiceImpl implements PhoneService {
    private static final Logger logger = LoggerFactory.getLogger(PhoneServiceImpl.class);
    @Autowired
    private PhoneRepository phoneRepository;

    @Override
    public List<PhoneDTO> findAllPhones() {
        List<Phone> phoneList = phoneRepository.findAll();
        return phoneList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public PhoneDTO findById(Long phoneId) throws ResourceNotFoundException {
        logger.debug("Get phone with ID {} ", phoneId);
        Optional<Phone> phone = phoneRepository.findById(phoneId);
        return phone.map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Phone not found :: " + phoneId));
    }

    @Override
    public PhoneDTO bookPhone(Long phoneId, String bookedBy) throws ResourceNotFoundException, PhoneAlreadyBookedException {
        logger.debug("Booking phone with ID {} for user {}", phoneId, bookedBy);
        Optional<Phone> phone = Optional.ofNullable(phoneRepository.findById(phoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Phone not found :: " + phoneId)));

        Phone bookedPhone = (phone != null && phone.isPresent()) ? phone.get() : null;
        if (!bookedPhone.isAvailable()) {
            logger.warn("Phone ID {} is already booked by {}", phoneId, bookedPhone.getBookedBy());
            throw new PhoneAlreadyBookedException(String.format("Phone '%s %s' (ID: %d) is already booked by %s", bookedPhone.getBrand(), bookedPhone.getModel(), phoneId, bookedPhone.getBookedBy()));
        }
        bookedPhone.setAvailable(false);
        bookedPhone.setBookedBy(bookedBy);
        bookedPhone.setBookedAt(LocalDateTime.now());
        phoneRepository.save(bookedPhone);
        logger.info("Phone with ID {} successfully booked for user {}", phoneId, bookedBy);

        return convertToDTO(bookedPhone);
    }

    @Override
    public PhoneDTO returnPhone(Long phoneId) throws ResourceNotFoundException, PhoneAlreadyAvailableException {
        logger.debug("Returning phone with ID {} for user {}", phoneId);
        Optional<Phone> phone = Optional.ofNullable(phoneRepository.findById(phoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Phone not found :: " + phoneId)));

        Phone returnedPhone = (phone != null && phone.isPresent()) ? phone.get() : null;
        if (returnedPhone.isAvailable()) {
            logger.warn("Phone ID {} is already available", phoneId);
            throw new PhoneAlreadyAvailableException(String.format("Phone '%s %s' (ID: %d) is already available", returnedPhone.getBrand(), returnedPhone.getModel(), phoneId));
        }
        returnedPhone.setAvailable(true);
        returnedPhone.setBookedBy(null);
        returnedPhone.setBookedAt(null);
        phoneRepository.save(returnedPhone);
        logger.info("Phone with ID {} successfully returned for user {}", phoneId);

        return convertToDTO(returnedPhone);
    }

    @Override
    public void delete(Long phoneId)
    {
        logger.debug("Delete phone with ID {} ", phoneId);
        phoneRepository.deleteById(phoneId);
    }

    private Phone convertToEntity(PhoneDTO phoneDTO) {
        Phone phone = new Phone();
        phone.setId(phoneDTO.getId());
        phone.setModel(phoneDTO.getModel());
        phone.setBrand(phoneDTO.getBrand());
        phone.setAvailable(phoneDTO.isAvailable());
        phone.setBookedAt(phoneDTO.getBookedAt());
        phone.setBookedBy(phoneDTO.getBookedBy());
        return phone;
    }

    private PhoneDTO convertToDTO(Phone phone) {
        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setId(phone.getId());
        phoneDTO.setModel(phone.getModel());
        phoneDTO.setBrand(phone.getBrand());
        phoneDTO.setAvailable(phone.isAvailable());
        phoneDTO.setBookedAt(phone.getBookedAt());
        phoneDTO.setBookedBy(phone.getBookedBy());
        return phoneDTO;
    }
}

