package com.wmt.tktsvc;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TicketServiceTest {

    public static final int NUM_OF_ROWS = 20;
    public static final int NUM_OF_SEATS_PER_ROW = 30;

    @Test
    public void findNumberOfSeats() {
        Venue aNewVenue= new Venue(NUM_OF_ROWS, NUM_OF_SEATS_PER_ROW,60);
        TicketService ticketService = new TicketServiceImpl(aNewVenue);
        assertEquals(NUM_OF_ROWS*NUM_OF_SEATS_PER_ROW, ticketService.numSeatsAvailable());
    }

    @Test
    public void findNumberOfSeatsAfterHolding() {

    }
    @Test
    public void findNumberOfSeatsAfterReserving() {

    }

    @Test
    public void findAndHoldSeatForCustomer() {

    }

    @Test
    public void reserveAndCommitHeldSeats() {

    }

}
