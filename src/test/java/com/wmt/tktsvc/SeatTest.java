package com.wmt.tktsvc;


import java.time.Instant;

import com.wmt.tktsvc.excep.SeatNotAvailableException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SeatTest {

    @Test
    public void holdSeat() throws SeatNotAvailableException {
        Seat seat = new Seat(1,1);
        seat.holdSeat(600);
        assertNotNull(seat.getHoldUntil());
        assertTrue(seat.getHoldUntil().isAfter(Instant.now()));
        assertFalse(seat.isAvailable());
    }
//
//    @Test
//    public void reserveSeat() throws SeatNotAvailableException {
//        Seat seat = new Seat(1,1);
//        seat.reserveSeat(123);
//        assertNull(seat.getHoldUntil());
//        assertFalse(seat.isAvailable());
//    }


}
