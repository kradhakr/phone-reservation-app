package com.spring.phone.reservation.integration;

import com.spring.phone.reservation.model.Phone;
import com.spring.phone.reservation.repository.PhoneRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PhoneIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PhoneRepository phoneRepository;

    private final String PHONE_API = "/api/phone/";

    @Test
    public void givenAvailablePhone_whenBookPhone_thenReturnsBookedPhone() {
        Phone phone = new Phone("Samsung", "Galaxy S9");
        phoneRepository.save(phone);
        String url = this.PHONE_API + phone.getId() + "/book?bookedBy=Test User A";

        ResponseEntity<Phone> response = restTemplate.postForEntity(url, null, Phone.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        Phone bookedPhone = response.getBody();
        assertThat(bookedPhone).isNotNull();
        assertThat(bookedPhone.isAvailable()).isFalse();
        assertThat(bookedPhone.getBookedBy()).isEqualTo("Test User A");
        assertThat(bookedPhone.getBookedAt()).isNotNull();
    }

    @Test
    public void givenBookedPhone_whenReturnPhone_thenSetsPhoneAvailable() {
        Phone phone = new Phone("Nokia", "3000", false, "Test User B", LocalDateTime.now());
        phoneRepository.save(phone);
        String url = this.PHONE_API + phone.getId() + "/return";

        ResponseEntity<Phone> response = restTemplate.postForEntity(url, null, Phone.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        Phone returnedPhone = response.getBody();
        assertThat(returnedPhone).isNotNull();
        assertThat(returnedPhone.isAvailable()).isTrue();
        assertThat(returnedPhone.getBookedBy()).isNull();
        assertThat(returnedPhone.getBookedAt()).isNull();
    }

    @Test
    public void givenAlreadyBookedPhone_whenBookPhone_thenThrowsPhoneAlreadyBooked() {
        Phone phone = new Phone("Samsung", "Galaxy S10", false, "Test User C", LocalDateTime.now());
        phoneRepository.save(phone);
        String url = this.PHONE_API + phone.getId() + "/book?bookedBy=Test User D";

        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(409);
        assertThat(response.getBody()).isEqualTo("Phone 'Samsung Galaxy S10' (ID: " + phone.getId() + ") is already booked by Test User C");
    }

    @Test
    public void givenAvailablePhone_whenReturnPhone_thenThrowsPhoneAlreadyAvailable() {
        Phone phone = new Phone("Apple", "iPhone 14");
        phoneRepository.save(phone);
        String url = this.PHONE_API + phone.getId() + "/return";

        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(409);
        assertThat(response.getBody()).isEqualTo("Phone 'Apple iPhone 14' (ID: " + phone.getId() + ") is already available");
    }

    @Test
    public void givenDoesNotExistPhone_whenBookPhone_thenThrowsPhoneNotFound() {
        String url = this.PHONE_API + "0/book?bookedBy=Test User E";

        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isEqualTo("Phone not found :: 0");
    }

    @Test
    public void givenDoesNotExistPhone_whenReturnPhone_thenThrowsPhoneNotFound() {
        String url = this.PHONE_API + "1201/return";

        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isEqualTo("Phone not found :: 1201");
    }
}
