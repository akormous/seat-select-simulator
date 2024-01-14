package io.akormous.seatselectsimulator.Seat;

import java.util.List;

public class SeatIdObject {
    List<Long> ids;

    public SeatIdObject() {}

    public SeatIdObject(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getIds() {
        return ids;
    }
}
