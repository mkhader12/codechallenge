package com.wmt.tktsvc;


import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.wmt.tktsvc.excep.SeatNotAvailableException;


public class SeatHold {

    private int seatHoldId;
    private List<Seat> seats;
    private String customerEmail;

    public SeatHold(String inCustomerEmail, List<Seat> inSeats) {
        this.customerEmail = inCustomerEmail;
        this.seats = inSeats;
        this.seats.forEach(seat -> {
            seat.setHeldBy(inCustomerEmail);
        });
    }

    public void setSeatHoldId(int seatHoldId) {
        this.seatHoldId = seatHoldId;
    }

    public List<Seat> getSeatsHeld() {
        return seats;
    }

    public int getSeatHoldId() {
        return seatHoldId;
    }

    public boolean isSeatsHeldByUser(String customerEmail) {
        if (seats != null) {
            return (seats.stream()
                    .filter(seat -> seat.isHeld(customerEmail))
                    .count() > 0);
        }
        return false;
    }

    public void reserveSeats() throws SeatNotAvailableException {
        if (seats != null) {
            for (Seat seat : seats) {
                seat.reserveSeat(this.seatHoldId, customerEmail);
            }
        }
    }

    public List<Seat> getSeats() {
        return seats;
    }
}
