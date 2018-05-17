package com.wmt.tktsvc;


import java.time.Instant;

import com.wmt.tktsvc.excep.SeatNotAvailableException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class SeatTest {

    @Test
    public void holdSeat() throws SeatNotAvailableException {
        Seat seat = new Seat(1,1);
        assertTrue(seat.isAvailable());
        seat.holdSeat(600);
        assertNotNull(seat.getHoldUntil());
        assertTrue(seat.getHoldUntil().isAfter(Instant.now()));
        assertFalse(seat.isAvailable());
    }

    @Test
    public void reservSeat() {
        Seat seat = new Seat(1,1);
        assertTrue(seat.isAvailable());
        String custEmail="email1@email.com";
        try {
            seat.reserveSeat(123, custEmail);
            fail("Expected to throw SeatNotAvailableException");
        } catch (SeatNotAvailableException e) {

        }

        seat.setHeldBy(custEmail);

        try {
            seat.reserveSeat(123, custEmail);
        } catch (SeatNotAvailableException e) {
            fail("Not Expected to throw SeatNotAvailableException");
        }
        assertFalse(seat.isAvailable());
    }


}
