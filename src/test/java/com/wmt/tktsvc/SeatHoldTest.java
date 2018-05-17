package com.wmt.tktsvc;


import java.util.ArrayList;
import java.util.List;

import com.wmt.tktsvc.excep.SeatNotAvailableException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class SeatHoldTest {

    @Test
    public void seatHeldByCustomerTest() {
        List<Seat> seats = new ArrayList<>();
        seats.add(new Seat(1,1));

        final String custEmail = "myemail@email.com";
        SeatHold seatHold = new SeatHold(custEmail, seats);
        boolean result = seatHold.isSeatsHeldByUser(custEmail);
        assertTrue(result);
    }

    @Test
    public void seatNotHeldByCustomerTest() {
        List<Seat> seats = new ArrayList<>();
        seats.add(new Seat(1,1));

        final String custEmail = "myemail@email.com";
        SeatHold seatHold = new SeatHold(custEmail, seats);
        boolean result = seatHold.isSeatsHeldByUser("email2@email.com");
        assertFalse(result);
    }

    @Test
    public void reserveTestForRightCustomer() throws SeatNotAvailableException {
        List<Seat> seats = new ArrayList<>();
        final String custEmail = "myemail@email.com";

        final Seat newSeat = new Seat(1, 1);
        newSeat.holdSeat(10);
        seats.add(newSeat);

        SeatHold seatHold = new SeatHold(custEmail, seats);
        seatHold.setSeatHoldId(123);
        seatHold.reserveSeats();
        assertTrue(newSeat.isReserved());
    }

    @Test
    public void reserveTestForWrongCustomer()  {
        List<Seat> seats = new ArrayList<>();
        final Seat newSeat = new Seat(1, 1);
        seats.add(newSeat);

        final String custEmail = "myemail@email.com";
        SeatHold seatHold = new SeatHold(custEmail, seats);

        final String email2 = "email2@email.com";
        newSeat.setHeldBy(email2);
        try {
            seatHold.reserveSeats();
            fail("Expected SeatNotAvailableException");
        } catch (SeatNotAvailableException e) {
            // do nothing here it is expected
        }
        assertFalse(newSeat.isReserved());
    }


}
