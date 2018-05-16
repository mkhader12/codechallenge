package com.wmt.tktsvc;


public class SeatBlock implements Comparable<SeatBlock> {
    public SeatBlock(Seat rootSeat, int numberOfSeats) {
        this.rootSeat = rootSeat;
        this.numberOfSeats = numberOfSeats;
    }

    private Seat rootSeat;
    private int numberOfSeats;


    @Override
    public int compareTo(SeatBlock o) {
        if (o != null) {
            return this.numberOfSeats - o.getNumberOfSeats();
        }
        return 0;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public Seat getRootSeat() {
        return rootSeat;
    }
}
