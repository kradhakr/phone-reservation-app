package com.spring.phone.reservation;

import com.spring.phone.reservation.model.Phone;
import com.spring.phone.reservation.repository.PhoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PhoneReservationApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(PhoneReservationApplication.class);
	@Autowired
	PhoneRepository phoneRepository;

	public static void main(String[] args) {
		SpringApplication.run(PhoneReservationApplication.class, args);
	}
	@Override
	public void run(String... args) {
		Phone phone1 = new Phone("Samsung", "Galaxy S9");
		Phone phone2 = new Phone("Samsung", "Galaxy S8");
		Phone phone3 = new Phone("Samsung", "Galaxy S8");
		Phone phone4 = new Phone("Motorola", "Nexus 6");
		Phone phone5 = new Phone("Oneplus", "9");
		Phone phone6 = new Phone("Apple", "iPhone 13");
		Phone phone7 = new Phone("Apple", "iPhone 12");
		Phone phone8 = new Phone("Apple", "iPhone 11");
		Phone phone9 = new Phone("iPhone", "X");
		Phone phone10 = new Phone("Nokia", "3310");

		phoneRepository.save(phone1);
		phoneRepository.save(phone2);
		phoneRepository.save(phone3);
		phoneRepository.save(phone4);
		phoneRepository.save(phone5);
		phoneRepository.save(phone6);
		phoneRepository.save(phone7);
		phoneRepository.save(phone8);
		phoneRepository.save(phone9);
		phoneRepository.save(phone10);

		logger.info("Phone Application data loaded..");
	}
}
