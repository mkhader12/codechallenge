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
        Venue aNewVenue= new Venue(NUM_OF_ROWS, NUM_OF_SEATS_PER_ROW,60);
        TicketService ticketService = new TicketServiceImpl(aNewVenue);
        final int numSeats = 5;
        ticketService.findAndHoldSeats(numSeats,"cust@email.com");
        final int totalSeats = NUM_OF_ROWS * NUM_OF_SEATS_PER_ROW;
        assertEquals(totalSeats-numSeats, ticketService.numSeatsAvailable());
    }
    @Test
    public void findNumberOfSeatsAfterReserving() {
        Venue aNewVenue= new Venue(NUM_OF_ROWS, NUM_OF_SEATS_PER_ROW,60);
        TicketService ticketService = new TicketServiceImpl(aNewVenue);
        final int numSeats = 5;
        final String customerEmail = "cust@email.com";
        SeatHold seatHold = ticketService.findAndHoldSeats(numSeats, customerEmail);
        ticketService.reserveSeats(seatHold.getSeatHoldId(),customerEmail);
        final int totalSeats = NUM_OF_ROWS * NUM_OF_SEATS_PER_ROW;
        assertEquals(totalSeats-numSeats, ticketService.numSeatsAvailable());
    }

    @Test
    public void findAndHoldSeatForCustomer() {

    }

    @Test
    public void reserveAndCommitHeldSeats() {

    }

}
