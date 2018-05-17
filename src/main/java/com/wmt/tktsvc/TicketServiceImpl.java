package com.wmt.tktsvc;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wmt.tktsvc.excep.SeatNotAvailableException;


public class TicketServiceImpl implements TicketService {
    transient private Venue venue;
    transient private Map<String, SeatHold> customerMap = new ConcurrentHashMap<>();

    public TicketServiceImpl(Venue newVenue) {
        venue = newVenue;
    }

    public int numSeatsAvailable() {
        return venue.getNumberOfAvailableSeats();
    }

    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        SeatHold existingHold = customerMap.get(customerEmail);
        if (existingHold == null) {
            // Check whethere numSeats is less than available number of seats
            List<Seat> seats = null;
            try {
            seats = venue.findBestSeats(numSeats);
            } catch (SeatNotAvailableException e) {
                return null;
            }
            if (seats != null && !seats.isEmpty()) {
                if (seats.size() != numSeats) {
                    venue.releaseSeats(seats);
                }
                else
                {
                    SeatHold seatHold = new SeatHold(customerEmail, seats);
                    int seatHeldId = IdGenerator.generate(customerEmail);
                    seatHold.setSeatHoldId(seatHeldId);
                    customerMap.put(customerEmail, seatHold);
                    return seatHold;
                }
            }

        }
        else
        {
            System.out.println("There is already an existing hold/reservation for "+ customerEmail);
        }

        return null;
    }

    public String reserveSeats(int seatHoldId, String customerEmail) {
        SeatHold seatHold = customerMap.get(customerEmail);
        if (seatHold != null &&
                seatHold.isSeatsHeldByUser(customerEmail) &&
                seatHold.getSeatHoldId() == seatHoldId) {
            try {
                seatHold.reserveSeats();
                customerMap.remove(customerEmail);
                return generateConfId(seatHoldId);
            } catch (SeatNotAvailableException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String generateConfId(int seatHoldId) {
        return "R-"+ seatHoldId;
    }
}
