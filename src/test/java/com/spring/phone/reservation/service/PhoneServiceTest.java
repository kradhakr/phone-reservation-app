package com.spring.phone.reservation.service;

import com.spring.phone.reservation.dto.PhoneDTO;
import com.spring.phone.reservation.exception.PhoneAlreadyAvailableException;
import com.spring.phone.reservation.exception.PhoneAlreadyBookedException;
import com.spring.phone.reservation.exception.ResourceNotFoundException;
import com.spring.phone.reservation.model.Phone;
import com.spring.phone.reservation.repository.PhoneRepository;
import com.spring.phone.reservation.service.impl.PhoneServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PhoneServiceTest {
    @Mock
    private PhoneRepository phoneRepository;

    @InjectMocks
    private PhoneServiceImpl phoneService;


    @Test
    public void givenAvailablePhone_whenBookPhone_thenReturnsBookedPhone() throws PhoneAlreadyBookedException, ResourceNotFoundException {
        Long phoneId = 100L;
        Phone phone = new Phone("Apple", "iPhone 10");
        when(phoneRepository.findById(phoneId)).thenReturn(Optional.of(phone));

        PhoneDTO bookedPhone = phoneService.bookPhone(phoneId, "User A");

        assertThat(bookedPhone).isNotNull();
        assertThat(bookedPhone.isAvailable()).isFalse();
        assertThat(bookedPhone.getBookedBy()).isEqualTo("User A");
        verify(phoneRepository, times(1)).save(phone);
    }

    @Test
    public void givenAlreadyBookedPhone_whenBookPhone_thenThrowsPhoneAlreadyBooked() throws PhoneAlreadyBookedException, ResourceNotFoundException {
        Long phoneId = 200L;
        Phone phone = new Phone("Nokia", "4400", false, "User C", LocalDateTime.now());
        when(phoneRepository.findById(phoneId)).thenReturn(Optional.of(phone));

        assertThatThrownBy(() -> phoneService.bookPhone(phoneId, "User D"))
                .isInstanceOf(PhoneAlreadyBookedException.class)
                .hasMessageContaining("Phone 'Nokia 4400' (ID: 200) is already booked by User C");
    }

    @Test
    public void givenBookedPhone_whenReturnPhone_thenSetsPhoneAvailable() throws ResourceNotFoundException, PhoneAlreadyAvailableException {
        Long phoneId = 300L;
        Phone phone = new Phone("Nokia", "4000", false, "User B", LocalDateTime.now());
        when(phoneRepository.findById(phoneId)).thenReturn(Optional.of(phone));

        PhoneDTO returnedPhone = phoneService.returnPhone(phoneId);

        assertThat(returnedPhone).isNotNull();
        assertThat(returnedPhone.isAvailable()).isTrue();
        assertThat(returnedPhone.getBookedBy()).isNull();
        assertThat(returnedPhone.getBookedAt()).isNull();
        verify(phoneRepository, times(1)).save(phone);
    }

    @Test
    public void givenAvailablePhone_whenReturnPhone_thenThrowsPhoneAlreadyAvailable() throws ResourceNotFoundException, PhoneAlreadyAvailableException {
        Long phoneId = 400L;
        Phone phone = new Phone("OnePlus", "10");
        when(phoneRepository.findById(phoneId)).thenReturn(Optional.of(phone));

        assertThatThrownBy(() -> phoneService.returnPhone(phoneId))
                .isInstanceOf(PhoneAlreadyAvailableException.class)
                .hasMessageContaining("OnePlus 10' (ID: 400) is already available");
    }
}
