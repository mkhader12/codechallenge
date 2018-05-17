package com.wmt.tktsvc;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.wmt.tktsvc.excep.SeatNotAvailableException;


/**
 * @author mkhader
 * @implNote Venue class is used as a venue where the seats are held
 *              and the reservation functions are performed
 */
public class Venue {

    transient private final VenuePrintMap venueMap = new VenuePrintMap(this);
    transient private List<Seat> seats;
    transient private int numberOfRows;
    transient private int numberOfSeatsPerRow;
    transient private int holdForInSeconds=300; // Default Hold for 5 minutes

    Map<Integer, SeatHold> seatHolds = new ConcurrentHashMap<>();

    /**
     * The constructor set number of rows and seats per row and
     *  the number of seconds to hold a seat. This initializes the seat creation
     *
     * @param rows
     * @param seatsPerRow
     * @param seatHoldInseconds
     */
    public Venue(int rows, int seatsPerRow, int seatHoldInseconds) {
        this.numberOfRows = rows;
        this.numberOfSeatsPerRow = seatsPerRow;
        this.holdForInSeconds = seatHoldInseconds;
        createSeats(rows, seatsPerRow);
    }

    /**
     * Creates seat objects into the list of seats
     *
     * @param rows
     * @param seatsPerRow
     */
    private void createSeats(int rows, int seatsPerRow) {
        Seat currentSeat;
        seats = new ArrayList<>();
        for(int i=0; i < rows; i++) {
            for (int j = 0; j < seatsPerRow; j++) {
                currentSeat = new Seat(i + 1, j + 1);
                seats.add(currentSeat);
            }
        }
    }

    /**
     * This methods traverse thru seats in all rows and looks for available seats and returns
     *  list of available seats either in the same row or various rows.
     *
     * @param numberOfSeatsRequested
     * @return
     * @throws SeatNotAvailableException
     */
    public List<Seat> findBestSeats(int numberOfSeatsRequested) throws SeatNotAvailableException {
        List<Seat> bestSeatsAvailable =new ArrayList<>();
        if (getNumberOfAvailableSeats() >= numberOfSeatsRequested) {
            // Loop until the number of seats found
            while (numberOfSeatsRequested > 0) {

                //
                // Get the best available block of seat for the number of seats requested.
                //  The block may have all seats required or else partial seats
                //
                SeatBlock seatBlock = getBestBlockOfSeats(numberOfSeatsRequested);

                List<Seat> availableSeats;
                if (seatBlock != null) {
                    // Check to see how many seats available in the block
                    int numberOfSeatsAvailable = seatBlock.getNumberOfSeats();
                    if (numberOfSeatsAvailable > numberOfSeatsRequested) {
                        numberOfSeatsAvailable = numberOfSeatsRequested;
                    }
                    //
                    // Book the number of seats available in the block
                    availableSeats = holdSeats(seatBlock.getRootSeat(), numberOfSeatsAvailable);
                    //
                    // Check to see if more seats are needed
                    numberOfSeatsRequested = numberOfSeatsRequested - seatBlock.getNumberOfSeats();
                    //
                    // Keep collecting the seat objects available in to a master list
                    bestSeatsAvailable = Stream.concat(bestSeatsAvailable.stream(), availableSeats.stream())
                            .collect(Collectors.toList());
                }
            }

            // If there is no enough seats available
            //   release previously held seats as part of the iteration
            if (numberOfSeatsRequested > 0) {
                releaseHold(bestSeatsAvailable);
            }
            else {
                // We got all the seats
                return bestSeatsAvailable;
            }
        }
        else {
            throw new SeatNotAvailableException();
        }
        return null;
    }

    /**
     *
     * @param numberOfSeatsRequested
     * @return
     */
    public SeatBlock getBestBlockOfSeats(int numberOfSeatsRequested) {
        List<SeatBlock> availableBlocksOfSeats = findAvailableBlocksOfSeat();
        SeatBlock bestBlock = availableBlocksOfSeats.get(0);
        //
        // First search for perfect fit
        //
        for (SeatBlock seatBlock : availableBlocksOfSeats) {
            if (numberOfSeatsRequested - seatBlock.getNumberOfSeats() == 0) {
                return seatBlock;
            }
        }
        //
        // Next look for maximum seats to cover
        //
        int bestDiff = this.numberOfSeatsPerRow;
        //
        // Best available slots to fit in
        //  Iterate over the seat blocks and find that can best fit
        //      the number of seats requested
        //
        for (SeatBlock seatBlock : availableBlocksOfSeats) {
            int difference = seatBlock.getNumberOfSeats() - numberOfSeatsRequested;
            if (difference < bestDiff) {
                bestDiff = difference;
                bestBlock = seatBlock;
            }
        }
        if (bestBlock.getNumberOfSeats() >= numberOfSeatsRequested)
        {
            // The block contains enough seats so return it
            return bestBlock;
        }

        //
        //  The worst case fit
        //   Now we are getting in to fragmented search
        //
        bestDiff = this.numberOfSeatsPerRow;
        for (SeatBlock seatBlock : availableBlocksOfSeats) {
            int difference = numberOfSeatsRequested - seatBlock.getNumberOfSeats();
            if (difference < bestDiff) {
                bestDiff = difference;
                bestBlock = seatBlock;
            }
        }
        return bestBlock;
    }


    /**
     * This method returns seats availability in chucks of
     *  availability Seat block.
     *  The seat block contains a beginning seat object and
     *   the number of continuous seats available.
     *
     */
    public List<SeatBlock> findAvailableBlocksOfSeat() {
        if (seats != null) {
            List<SeatBlock> seatBlocks = new ArrayList<>();

            // Iterate thru seats
            //  and if the seats available store beginning seat object
            //    followed by the number of seats available.
            for(int i=1; i <= numberOfRows; i++) {
                int numberOfSeatsAvailable = 0;
                Seat beginSeat=null;
                for (int j=1; j <= numberOfSeatsPerRow; j++) {
                    Seat aSeat = findSeat(i, j);
                    if (aSeat != null && aSeat.isAvailable()) {
                        // Seats are available
                        // Set the beginning seat
                        //
                        if (beginSeat == null) {
                            beginSeat = aSeat;
                        }
                        numberOfSeatsAvailable++;
                    }
                    else if (numberOfSeatsAvailable > 0) {
                        //
                        // When the seats no longer available store the block
                        //
                        seatBlocks.add(new SeatBlock(beginSeat, numberOfSeatsAvailable));
                        beginSeat = null;
                        numberOfSeatsAvailable = 0;
                    }
                }
                // Keep adding the seat blocks in to a list
                if (numberOfSeatsAvailable > 0) {
                    seatBlocks.add(new SeatBlock(beginSeat, numberOfSeatsAvailable));
                }
            }
            // Reverse sort the seat blocks so that maximum available seats will be on top
            Collections.sort(seatBlocks, Collections.reverseOrder());
            return seatBlocks;

        }
        return null;
    }


    /**
     * This method returns number seats available
     * @return
     */
    public int getNumberOfAvailableSeats() {
        int numberOfSeats =0;
        if (seats != null) {
            numberOfSeats = (int) seats.stream()
                    .filter(seat -> seat.isAvailable())
                    .count();
        }
        return numberOfSeats;
    }

    /**
     *  Find the seat by row and column
     *
     * @param row
     * @param column
     * @return
     */
    public Seat findSeat(int row, int column) {
        if (seats != null) {
            for (Seat seat : seats) {
                if (seat.getRowNo() == row && seat.getSeatNo() == column) {
                    return seat;
                }
            }

        }
        return null;
    }

    /**
     *
     * @param beginSeat
     * @param numberOfSeatsRequested
     * @return
     * @throws SeatNotAvailableException
     */
    private List<Seat> holdSeats(Seat beginSeat, int numberOfSeatsRequested) throws SeatNotAvailableException {
        List<Seat> heldSeatList = new ArrayList<>();

        // Add the beginning seat
        heldSeatList.add(beginSeat);
        beginSeat.holdSeat(this.holdForInSeconds);

        // Hold all the other seats for the number of seats
        for (int j=beginSeat.getSeatNo()+1; j<beginSeat.getSeatNo()+numberOfSeatsRequested; j++) {
            Seat seat = findSeat(beginSeat.getRowNo(), j);
            seat.holdSeat(this.holdForInSeconds);
            heldSeatList.add(seat);
        }
        return heldSeatList;
    }

    private void releaseHold(List<Seat> newList) {
        if (newList != null) {
            newList.forEach(seat -> seat.release());
        }
    }

    public void releaseSeats(List<Seat> seats) {
        seats.forEach(seat -> seat.release());
    }


    /**
     *  Print Venue Layout Method and helper methods
     */
    public void printVenueSeatLayout() {
        venueMap.printVenueSeatLayout();
    }


    public int getNumberOfSeatsPerRow() {
        return numberOfSeatsPerRow;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }
}
