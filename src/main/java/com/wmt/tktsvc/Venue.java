package com.wmt.tktsvc;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Venue {

    List<Seat> seats;
    int numberOfRows;
    int numberOfSeatsPerRow;
    int holdForInSeconds=60; // Default Hold for 1 minute

    Map<Integer, SeatHold> seatHolds = new ConcurrentHashMap<>();

    public Venue(int rows, int seatsPerRow) {
        this.numberOfRows = rows;
        this.numberOfSeatsPerRow = seatsPerRow;
        createSeats(rows, seatsPerRow);
    }

    private void createSeats(int rows, int seatsPerRow) {
        Seat prevSeat=null;
        Seat currentSeat;
        seats = new ArrayList<>();
        for(int i=0; i < rows; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < seatsPerRow; j++) {
                    currentSeat = createSeat(prevSeat, i, j);
                    prevSeat = currentSeat;
                }
            }
            else
            {
                for (int j = seatsPerRow-1; j >= 0; j--) {
                    currentSeat = createSeat(prevSeat, i, j);
                    prevSeat = currentSeat;
                }
            }

        }
    }

    private Seat createSeat(Seat prevSeat, int row, int column) {
        Seat currentSeat;
        currentSeat = new Seat(row + 1, column + 1);
        seats.add(currentSeat);
        currentSeat.setPreviousSeat(prevSeat);
        if (prevSeat != null) {
            prevSeat.setNextSeat(currentSeat);
        }
        return currentSeat;
    }

    public List<SeatBlock> findAvailableBlocksOfSeats() {
        if (seats != null) {
            int numberOfSeatsAvailable = 0;
            Seat rootSeat = null;

            List<SeatBlock> seatBlocks = new ArrayList<>();
            for(int i=1; i <= numberOfRows; i++) {
                numberOfSeatsAvailable = 0;
                rootSeat=null;
                for (int j=1; j <= numberOfSeatsPerRow; j++) {
                    Seat aSeat = findSeat(i, j);
                    if (aSeat != null && aSeat.isAvailable()) {
                        if (rootSeat == null) {
                            rootSeat = aSeat;
                        }
                        numberOfSeatsAvailable++;
                    }
                    else if (numberOfSeatsAvailable > 0) {
                        seatBlocks.add(new SeatBlock(rootSeat, numberOfSeatsAvailable));
                        rootSeat = null;
                        numberOfSeatsAvailable = 0;
                    }
                }
                if (numberOfSeatsAvailable > 0) {
                    seatBlocks.add(new SeatBlock(rootSeat, numberOfSeatsAvailable));
                }
            }
            Collections.sort(seatBlocks, Collections.reverseOrder());
            return seatBlocks;

        }
        return null;
    }
    public SeatBlock findAvailableBlocksOfSeats(List<SeatBlock> availableBlocksOfSeats, int numberOfSeatsRequested) {
        int numberOfSeatsAvailable = 0;
        int bestDiff=availableBlocksOfSeats.get(0).getNumberOfSeats();
        SeatBlock bestBlock = availableBlocksOfSeats.get(0);
        // First available Perfect Fit
        for (SeatBlock seatBlock : availableBlocksOfSeats) {
            if (numberOfSeatsRequested - seatBlock.getNumberOfSeats() == 0) {
                return seatBlock;
            }
        }
        bestDiff = this.numberOfSeatsPerRow;
        // Best available fit
        for (SeatBlock seatBlock : availableBlocksOfSeats) {
            int difference = seatBlock.getNumberOfSeats() - numberOfSeatsRequested;
            if (difference < bestDiff) {
                bestDiff = difference;
                bestBlock = seatBlock;
            }
        }
        if (bestBlock.getNumberOfSeats() >= numberOfSeatsRequested)
        {
            return bestBlock;
        }
        bestDiff = this.numberOfSeatsPerRow;

        // Best available fit
        for (SeatBlock seatBlock : availableBlocksOfSeats) {
            int difference = numberOfSeatsRequested - seatBlock.getNumberOfSeats();
            if (difference < bestDiff) {
                bestDiff = difference;
                bestBlock = seatBlock;
            }
        }
        return bestBlock;
    }


    public int getNumberOfAvailableSeats() {
        int numberOfSeats =0;
        if (seats != null) {
            numberOfSeats = (int) seats.stream()
                    .filter(seat -> seat.isAvailable())
                    .count();
        }
        return numberOfSeats;
    }

    public boolean holdFirstAvailableSeats(int numberOfRequestedSeats)
            throws SeatNotAvailableException {
        if (getNumberOfAvailableSeats() > numberOfRequestedSeats)
        {
            Seat seat = seats.get(0);
            holdBlockOfSeats(seat, numberOfRequestedSeats);
            return true;
        }
        return false;
    }

    private void holdBlockOfSeats(Seat seat,  int numberOfRequestedSeats)
            throws SeatNotAvailableException {
        while (seat != null && numberOfRequestedSeats > 0) {
            if (seat.isAvailable()) {
                seat.holdSeat(this.holdForInSeconds);
                numberOfRequestedSeats--;
            }
            seat = seat.getNextSeat();
        }
    }


    public int holdSeats(int row, int column, int numberOfRequestedSeats)
            throws SeatNotAvailableException {
        int numberOfSeatsHeld=0;

        if (getNumberOfAvailableSeats() > numberOfRequestedSeats)
        {
            for (int j=column; j <=column+numberOfRequestedSeats; j++) {
                Seat seat = findSeat(row, j);
                if (seat != null && seat.isAvailable()) {
                    seat.holdSeat(this.holdForInSeconds);
                    numberOfSeatsHeld++;
                }
            }
        }
        else
        {
            throw new SeatNotAvailableException();
        }
        return numberOfSeatsHeld;
    }


    public void printSeats() {
        final String stageWord = " STAGE ";
        for (int i = 0; i < 3*numberOfSeatsPerRow; i++) {
            System.out.print('-');
        }
        int dashLength = (3*numberOfSeatsPerRow - stageWord.length()) / 2;
        System.out.println();
        for (int i = 0; i < dashLength; i++) {
            System.out.print('-');
        }
        System.out.print(stageWord);
        for (int i = 0; i <= dashLength; i++) {
            System.out.print('-');
        }
        System.out.println();
        for (int i = 0; i < 3*numberOfSeatsPerRow; i++) {
            System.out.print('-');
        }
        System.out.println();

        for (int row=0; row < numberOfRows; row++) {
            for (int col=0; col < numberOfSeatsPerRow; col++) {
                Seat seat = findSeat(row+1, col+1);
                if (seat != null) {
                    if (seat.isAvailable()) {
                        System.out.print(" A ");
                    }
                    else if (seat.isHeld())
                    {
                        System.out.print(" H ");
                    }
                    else if (seat.isBooked()) {
                        System.out.print(" R ");

                    }
                }

            }
            System.out.println();
        }
        for (int i = 0; i < 3*numberOfSeatsPerRow; i++) {
            System.out.print('-');
        }
        System.out.println();
    }

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

    public List<Seat> findBestSeats(int numberOfSeatsRequested) throws SeatNotAvailableException {
        List<Seat> masterList=new ArrayList<>();
        if (getNumberOfAvailableSeats() >= numberOfSeatsRequested) {
            while (numberOfSeatsRequested > 0) {
                List<SeatBlock> availableSeats = findAvailableBlocksOfSeats();
                SeatBlock seatBlock = findAvailableBlocksOfSeats(availableSeats, numberOfSeatsRequested);
                List<Seat> seatList;
                if (seatBlock != null) {
                    int requiredNumberOfSeatsInTheRow = seatBlock.getNumberOfSeats();
                    if (seatBlock.getNumberOfSeats() > numberOfSeatsRequested) {
                        requiredNumberOfSeatsInTheRow = numberOfSeatsRequested;
                    }
                    seatList = bookSeat(seatBlock.getRootSeat(), requiredNumberOfSeatsInTheRow);
                    numberOfSeatsRequested = numberOfSeatsRequested - seatBlock.getNumberOfSeats();
                    masterList = Stream.concat(masterList.stream(), seatList.stream())
                            .collect(Collectors.toList());
                } else {

                }
            }

            if (numberOfSeatsRequested > 0) {
                releaseHold(masterList);
            } else {
                return masterList;
            }
        }
        else {
            throw new SeatNotAvailableException();
        }
        return null;
    }

    private List<Seat> bookSeat(Seat rootSeat, int numberOfSeatsRequested) throws SeatNotAvailableException {
        List<Seat> returnList = new ArrayList<>();
        returnList.add(rootSeat);
        rootSeat.holdSeat(this.holdForInSeconds);
        for (int j=rootSeat.getSeatNo()+1; j<rootSeat.getSeatNo()+numberOfSeatsRequested; j++) {
            Seat seat = findSeat(rootSeat.getRowNo(), j);
            seat.holdSeat(this.holdForInSeconds);
            returnList.add(seat);
        }
        return returnList;
    }

    private void releaseHold(List<Seat> newList) {
        if (newList != null) {
            newList.forEach(seat -> seat.release());
        }
    }

    public void releaseSeats(List<Seat> seats) {
        seats.forEach(seat -> seat.release());
    }

}
