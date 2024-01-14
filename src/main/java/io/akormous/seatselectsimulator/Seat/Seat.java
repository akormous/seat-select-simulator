package io.akormous.seatselectsimulator.Seat;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean isReserved;

    public Seat() {}

    public Seat(Boolean isReserved) {
        this.isReserved = isReserved;
    }

    public Long getId() {
        return id;
    }

    public Boolean getReserved() {
        return isReserved;
    }

    public void setReserved(Boolean reserved) {
        isReserved = reserved;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", isReserved=" + isReserved +
                '}';
    }
}
