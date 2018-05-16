package com.wmt.tktsvc;

import java.time.Instant;

public class Seat {
    private int rowNo;
    private int seatNo;
    private int seatHoldId;
    private Instant holdUntil;
    private Seat nextSeat;
    private Seat previousSeat;
    private String heldBy;

    public Seat(int rowNumber, int seatNumber) {
        this.rowNo = rowNumber;
        this.seatNo = seatNumber;
    }

    public void holdSeat(long numberOfSeconds) throws SeatNotAvailableException {
        if (isAvailable()) {
            holdUntil = Instant.now().plusSeconds(numberOfSeconds);
        }
        else
        {
            throw new SeatNotAvailableException();
        }
    }

    public void reserveSeat(int seatHoldId, String customerEmail) throws SeatNotAvailableException {
        if (isHeld(customerEmail))  {
            this.seatHoldId = seatHoldId;
            this.holdUntil = null;
        }
        else
        {
            throw new SeatNotAvailableException();
        }
    }

    public int getRowNo() {
        return rowNo;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public boolean isAvailable() {
        boolean availabilty =  seatHoldId==0 && !isHeld();
        return availabilty;
    }

    public Instant getHoldUntil() {
        return holdUntil;
    }

    public void setPreviousSeat(Seat previousSeat) {
        this.previousSeat = previousSeat;
    }

    public void setNextSeat(Seat nextSeat) {
        this.nextSeat = nextSeat;
    }

    public Seat getNextSeat() {
        return nextSeat;
    }

    public boolean isHeld() {
        return (this.holdUntil != null &&  this.holdUntil.isAfter(Instant.now()));
    }

    public boolean isBooked() {
        return (seatHoldId > 0);
    }

    public void release() {
        seatHoldId = 0;
        holdUntil = null;
    }

    public String getSeatId() {
        String seatId = ""+ rowNo + getSeatPosition();
        return seatId;
    }

    public String getSeatPosition() {
        StringBuffer returnString = new StringBuffer();
        int iteration = getSeatNo()/26+1;
        int reminder = getSeatNo()%26;

        for (int i=0; i < iteration; i++) {
            returnString.append((char) ('A' + reminder - 1));
        }
        return returnString.toString();
    }

    public boolean isHeld(String customerEmail) {
        return (this.heldBy != null && this.heldBy.equals(customerEmail));
    }

    public void setHeldBy(String heldBy) {
        this.heldBy = heldBy;
    }
}
