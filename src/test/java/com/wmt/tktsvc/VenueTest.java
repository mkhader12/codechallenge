package com.wmt.tktsvc;


import java.util.List;

import com.wmt.tktsvc.excep.SeatNotAvailableException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class VenueTest {

    @Test
    public void fillSeats() {
        Venue venue = new Venue(5,10,60);
        assertEquals(50, venue.getNumberOfAvailableSeats());
    }

    @Test
    void findBestSeatInTheRowWhenSeatsAvailable() throws SeatNotAvailableException {
        Venue venue = new Venue(5,10,60);
        List<Seat> availableSeats = venue.findBestSeats(6);
        assertNotNull(availableSeats);
        assertEquals(6, availableSeats.size());
        assertEquals(6, availableSeats.stream().filter(seat -> seat.isHeld()).count());
    }

    @Test
    void findBestSeatInTheRowWhenSeatsNotAvailable() throws SeatNotAvailableException {
        Venue venue = new Venue(5,10,60);
        venue.findBestSeats(25);
        List<Seat> availableSeats = venue.findBestSeats(6);
        assertNotNull(availableSeats);
        List<SeatBlock> blockOfSeats = venue.findAvailableBlocksOfSeat();
        assertNotNull(blockOfSeats);
        System.out.println();
        venue.printVenueSeatLayout();

    }



}
