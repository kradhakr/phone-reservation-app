package com.spring.phone.reservation.controller;

import com.spring.phone.reservation.dto.PhoneDTO;
import com.spring.phone.reservation.exception.PhoneAlreadyAvailableException;
import com.spring.phone.reservation.exception.PhoneAlreadyBookedException;
import com.spring.phone.reservation.model.Phone;
import com.spring.phone.reservation.service.impl.PhoneServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class PhoneControllerTest {
     @MockBean
     private PhoneServiceImpl phoneService;

    @Autowired
    private MockMvc mockMvc;
    private final String PHONE_API = "/api/phone/";

    @Test
    public void givenAvailablePhone_whenBookPhone_thenReturnsBookedPhone() throws Exception {
        Long phoneId = 1L;
        String user = "User 1";
        Phone phone = new Phone("Samsung", "Galaxy S9", false, user, LocalDateTime.now());
        when(phoneService.bookPhone(phoneId, user)).thenReturn(convertToDTO(phone));

        mockMvc.perform(post(PHONE_API + "{phoneId}/book", phoneId)
                        .param("bookedBy", user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.available", is(false)))
                .andExpect(jsonPath("$.brand").value("Samsung"))
                .andExpect(jsonPath("$.bookedBy", is(user)));
    }

    @Test
    public void givenAlreadyBookedPhone_whenBookPhone_thenThrowsPhoneAlreadyBooked() throws Exception {
        Long phoneId = 1L;
        String user = "User 2";
        doThrow(new PhoneAlreadyBookedException("Phone 'Samsung Galaxy S9' (ID: 1) is already booked by User 1"))
                .when(phoneService)
                .bookPhone(phoneId, user);

        mockMvc.perform(post(PHONE_API + "{phoneId}/book", phoneId)
                        .param("bookedBy", user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string("Phone 'Samsung Galaxy S9' (ID: 1) is already booked by User 1"));
    }

    @Test
    public void givenBookedPhone_whenReturnPhone_thenSetsPhoneAvailable() throws Exception {
        Long phoneId = 2L;
        Phone phone = new Phone("Samsung", "Galaxy S8", true, null, null);
        when(phoneService.returnPhone(phoneId)).thenReturn(convertToDTO(phone));

        mockMvc.perform(post(PHONE_API + "{phoneId}/return", phoneId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    public void givenAvailablePhone_whenReturnPhone_thenThrowsPhoneAlreadyAvailable() throws Exception {
        Long phoneId = 1L;
        doThrow(new PhoneAlreadyAvailableException("OnePlus 10' (ID: 400) is already available"))
                .when(phoneService)
                .returnPhone(phoneId);

        mockMvc.perform(post(PHONE_API + "{phoneId}/return", phoneId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string("OnePlus 10' (ID: 400) is already available"));
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
