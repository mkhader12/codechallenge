package com.wmt.tktsvc;


import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class VenueTest {

    @Test
    public void fillSeats() {
        Venue venue = new Venue(5,10);
        assertEquals(50, venue.getNumberOfAvailableSeats());
        venue.printSeats();
    }
//
//    @Test
//    public void reserveFewSeats() throws SeatNotAvailableException {
//        Venue venue = new Venue(5,10);
//        boolean reserved = venue.reserveFirstAvailableSeats(444, 12);
//        assertTrue(reserved);
//        assertEquals(38, venue.getNumberOfAvailableSeats());
//        venue.printSeats();
//        System.out.println("\n");
//        venue.reserveSeats(3,6,445, 5);
//        venue.printSeats();
//        System.out.println("\n");
//        venue.reserveSeats(2,1, 446, 2);
//        venue.printSeats();
//        List<SeatBlock> availableBlocks = venue.findAvailableBlocksOfSeats();
//        System.out.println("\n");
//
//    }


    @Test void holdFewSeats() throws SeatNotAvailableException {
        Venue venue = new Venue(5,10);
        boolean reserved = venue.holdFirstAvailableSeats( 12);
        assertTrue(reserved);
        assertEquals(38, venue.getNumberOfAvailableSeats());
        venue.printSeats();
        System.out.println("\n");
        venue.holdFirstAvailableSeats(6);
        venue.printSeats();
        System.out.println("\n");
        venue.holdSeats(2,1, 2);
        venue.printSeats();
        List<SeatBlock> availableBlocks = venue.findAvailableBlocksOfSeats();
        System.out.println("\n");
    }


    @Test
    void findBestSeatInTheRowWhenSeatsAvailable() throws SeatNotAvailableException {
        Venue venue = new Venue(5,10);
        venue.holdSeats(1,1, 5);
        List<Seat> availableSeats = venue.findBestSeats(6);
        assertNotNull(availableSeats);
        assertEquals(6, availableSeats.size());
        assertEquals(6, availableSeats.stream().filter(seat -> seat.isAvailable()).count());
    }

    @Test
    void findBestSeatInTheRowWhenSeatsNotAvailable() throws SeatNotAvailableException {
        Venue venue = new Venue(5,10);
        venue.holdSeats(1,1, 5);
        venue.holdSeats(2,2, 5);
        venue.holdSeats(3,4, 5);
        venue.holdSeats(4,3, 5);
        venue.holdSeats(5,5, 5);
        venue.printSeats();

        List<Seat> availableSeats = venue.findBestSeats(6);
        assertNotNull(availableSeats);
        List<SeatBlock> blockOfSeats = venue.findAvailableBlocksOfSeats();
        assertNotNull(blockOfSeats);
        System.out.println();
        venue.printSeats();

    }



}
