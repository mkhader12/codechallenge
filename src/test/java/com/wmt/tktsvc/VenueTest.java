package com.wmt.tktsvc;


import java.util.List;

import com.wmt.tktsvc.excep.SeatNotAvailableException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class VenueTest {

    @Test
    public void fillSeats() {
        Venue venue = new Venue(5,10,60);
        assertEquals(50, venue.getNumberOfAvailableSeats());
    }

    @Test
    public void findBestSeatInTheRowWhenSeatsAvailable() throws SeatNotAvailableException {
        Venue venue = new Venue(5,10,60);
        List<Seat> availableSeats = venue.findBestSeats(6);
        assertNotNull(availableSeats);
        assertEquals(6, availableSeats.size());
        assertEquals(6, availableSeats.stream().filter(seat -> seat.isHeld()).count());
    }

    @Test
    public void findBestSeatInTheRowWhenSeatsNotAvailable() throws SeatNotAvailableException {
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
