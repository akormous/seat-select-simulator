package io.akormous.seatselectsimulator.Seat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {
    private static final Integer seatCount = 70;
    SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getAll() {
        return seatRepository.findAll();
    }

    public String buildJSONString(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode bodyObject = objectMapper.createObjectNode();
        bodyObject.put("message", message.toString());
        return bodyObject.toString();
    }

    public ResponseEntity<Object> createSeat(Seat seat) {
        seatRepository.save(seat);
        return new ResponseEntity<>(buildJSONString("Created successfully"), HttpStatus.OK);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<Object> bookSeats(List<Long> seatIds) {
        List<Long> bookedSeatIds = new ArrayList<>();
        for(Long seatId : seatIds) {
            if(seatRepository.getReferenceById(seatId).getReserved()) {
                bookedSeatIds.add(seatId);
            }
        }
        if(bookedSeatIds.size() != 0) {
            return new ResponseEntity<>(buildJSONString("Seats " + bookedSeatIds.toString() + " already booked"), HttpStatus.CONFLICT);
        }
        else {
            for(Long seatId : seatIds) {
                Seat seatToReserve = seatRepository.getReferenceById(seatId);
                seatToReserve.setReserved(true);
                seatRepository.save(seatToReserve);
            }
        }
        return new ResponseEntity<>(buildJSONString("Seats booked successfully"), HttpStatus.OK);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<Object> resetAll() {
        for(long id = 1; id <= seatCount; id++) {
            if(seatRepository.findById(id).isPresent()) {
                Seat s = seatRepository.getReferenceById(id);
                s.setReserved(false);
                seatRepository.save(s);
            }
            else {
                this.createSeat(new Seat(false));
            }
        }
        return new ResponseEntity<>(buildJSONString("Reset done for all seats"), HttpStatus.OK);
    }

}
