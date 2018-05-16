package com.wmt.tktsvc;


import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


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


    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setSeatsHeld(List<Seat> seatsHeld) {
        this.seats = seatsHeld;
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
            long seatsAvailable =  (seats.stream().filter(seat -> seat.isHeld(customerEmail)).count());
            if (seatsAvailable > 0)
                return true;
        }
        return false;
    }

    public void reserveSeats() throws SeatNotAvailableException {
        AtomicBoolean returnValue = new AtomicBoolean(false);
        if (seats != null) {
            for (Seat seat : seats) {
                seat.reserveSeat(this.seatHoldId, customerEmail);
            }
        }
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void addSeats(List<Seat> inSeats) {
        if (seats == null) {
            seats = inSeats;
        }
        else
        {
            seats.addAll(inSeats);
        }
    }
}
