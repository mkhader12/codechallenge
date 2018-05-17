package com.wmt.tktsvc;


public class VenuePrintMap {
    private final Venue venue;

    public VenuePrintMap(Venue venue) {
        this.venue = venue;
    }

    /**
     * Print Venue Layout Method and helper methods
     */
    public void printVenueSeatLayout() {
        printStageHeader();
        printSeatStatus();
        printDashes(3 * venue.getNumberOfSeatsPerRow());
        System.out.println();
    }

    void printSeatStatus() {
        for (int row = 0; row < venue.getNumberOfRows(); row++) {
            for (int col = 0; col < venue.getNumberOfSeatsPerRow(); col++) {
                Seat seat = venue.findSeat(row + 1, col + 1);
                if (seat != null) {
                    if (seat.isAvailable()) {
                        System.out.print(" A ");
                    } else if (seat.isHeld()) {
                        System.out.print(" H ");
                    } else if (seat.isReserved()) {
                        System.out.print(" R ");

                    }
                }

            }
            System.out.println();
        }
    }

    void printStageHeader() {
        final String stageWord = " STAGE  ";
        printDashes(3 * venue.getNumberOfSeatsPerRow());
        int dashLength = (3 * venue.getNumberOfSeatsPerRow() - stageWord.length()) / 2;
        System.out.println();
        printDashes(dashLength);
        System.out.print(stageWord);
        printDashes(dashLength);
        System.out.println();
        printDashes(3 * venue.getNumberOfSeatsPerRow());
        System.out.println();
    }

    void printDashes(int dashLength) {
        for (int i = 0; i < dashLength; i++) {
            System.out.print('-');
        }
    }
}