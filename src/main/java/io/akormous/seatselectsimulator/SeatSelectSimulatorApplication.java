package io.akormous.seatselectsimulator;

import io.akormous.seatselectsimulator.Seat.Seat;
import io.akormous.seatselectsimulator.Seat.SeatIdObject;
import io.akormous.seatselectsimulator.Seat.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SpringBootApplication
public class SeatSelectSimulatorApplication {

	@Autowired
	private SeatService seatService;


	public static void main(String[] args) {
		SpringApplication.run(SeatSelectSimulatorApplication.class, args);
	}


	@Bean
	public Function<String, List<Seat>> getAllSeats() {
		return message -> {
			try {
				System.out.println("Getting all seats");
				if(seatService.getAll().size() == 0) {
					seatService.resetAll();
				}
				return seatService.getAll();
			}
			catch(Exception e) {
				System.out.println("Error occurred while getting all seats");
				return new ArrayList<>();
			}
		};
	}


	@Bean
	public Function<SeatIdObject,ResponseEntity<Object>> bookSeats() {
		return seatIds -> {
			// given seat ids, book them
			try {
				System.out.println("Booking seats with ids " + seatIds.getIds().toString());
				return seatService.bookSeats(seatIds.getIds());
			}
			catch(Exception e) {
				System.out.println("Error occurred while booking seats with ids " + seatIds.getIds().toString());
				return new ResponseEntity<>(seatService.buildJSONString("Error occurred while booking seats with seat ID: " + seatIds.getIds().toString()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		};
	}

	@Bean
	public Function<String,ResponseEntity<Object>> reset() {
		return message -> {
			try {
				System.out.println("Resetting all seats");
				return seatService.resetAll();
			}
			catch(Exception e) {
				System.out.println("Error occurred while resetting all seats");
				return new ResponseEntity<>(seatService.buildJSONString("Error occurred while resetting all seats"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		};
	}
}
