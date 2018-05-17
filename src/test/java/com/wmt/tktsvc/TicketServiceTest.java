package com.wmt.tktsvc;


import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class TicketServiceTest {

    public static final int NUM_OF_ROWS = 10;
    public static final int NUM_OF_SEATS_PER_ROW = 10;

    private TicketService ticketService;
    private Venue aNewVenue;

    @BeforeEach
    public void setup() {
        aNewVenue= new Venue(NUM_OF_ROWS, NUM_OF_SEATS_PER_ROW,60);
        ticketService = new TicketServiceImpl(aNewVenue);
    }

    @Test
    public void findNumberOfSeats() {
        assertEquals(NUM_OF_ROWS*NUM_OF_SEATS_PER_ROW, ticketService.numSeatsAvailable());
    }

    @Test
    public void findNumberOfSeatsAfterHolding() {
        final int numSeats = 5;
        ticketService.findAndHoldSeats(numSeats,"cust@email.com");
        final int totalSeats = NUM_OF_ROWS * NUM_OF_SEATS_PER_ROW;
        assertEquals(totalSeats-numSeats, ticketService.numSeatsAvailable());
    }
    @Test
    public void findNumberOfSeatsAfterReserving() {
        final int numSeats = 5;
        final String customerEmail = "cust@email.com";
        SeatHold seatHold = ticketService.findAndHoldSeats(numSeats, customerEmail);
        ticketService.reserveSeats(seatHold.getSeatHoldId(),customerEmail);
        final int totalSeats = NUM_OF_ROWS * NUM_OF_SEATS_PER_ROW;
        assertEquals(totalSeats-numSeats, ticketService.numSeatsAvailable());
    }

    @Test
    public void findAndHoldSeatForSameCustomerMultipleTimes() {
        int numSeats = 5;
        SeatHold seatHold = ticketService.findAndHoldSeats(numSeats,"cust@email.com");
        List<Seat> seatsHeld = seatHold.getSeats();
        assertNotNull(seatHold);
        assertEquals(numSeats, seatsHeld.size());
        seatHold = ticketService.findAndHoldSeats(numSeats,"cust@email.com");
        assertNull(seatHold);
    }


    @Test
    public void tryToHoldSeatInHousefullVenue() {
        int numSeats = 100;
        SeatHold seatHold = ticketService.findAndHoldSeats(numSeats,"cust@email.com");
        aNewVenue.printVenueSeatLayout();
        List<Seat> seatsHeld = seatHold.getSeats();
        assertNotNull(seatHold);
        assertEquals(numSeats, seatsHeld.size());
        seatHold = ticketService.findAndHoldSeats(1,"cust2@email.com");
        assertNull(seatHold);
    }

    @Test
    public void tryReserveWithoutHold() {
        String confId = ticketService.reserveSeats(123, "cust@email.com");
        assertNull(confId);
    }



}
