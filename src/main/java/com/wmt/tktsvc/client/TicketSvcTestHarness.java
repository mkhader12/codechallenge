package com.wmt.tktsvc.client;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wmt.tktsvc.Seat;
import com.wmt.tktsvc.SeatHold;
import com.wmt.tktsvc.TicketService;
import com.wmt.tktsvc.TicketServiceImpl;
import com.wmt.tktsvc.Venue;


public class TicketSvcTestHarness {
    private static TicketService ticketService;
    private static Venue venue;

    public static void main(String[] args) {

        final int numberOfRows = 10;
        final int numberOfSeatPerRow = 10;
        final int seatHoldInseconds = 180;
        venue=new Venue(numberOfRows, numberOfSeatPerRow, seatHoldInseconds);

        ticketService =  new TicketServiceImpl(venue);
        performUserFunctions();
    }

    private static void performUserFunctions() {
        TicketSvcTestHarness svcClient = new TicketSvcTestHarness();

        String userSelection = "";

        while (!userSelection.equals("Q")) {
            svcClient.printMenu();
            userSelection = svcClient.readInput("Enter your input : ");
            switch (userSelection) {
                case "V":
                    svcClient.viewSeatMap();
                    break;
                case "N":
                    svcClient.showNumberOfSeatsAvailable();
                    break;
                case "H":
                    svcClient.holdSeat();
                    break;
                case "R":
                    svcClient.reserveSeat();
                    break;
                case "Q":
                    svcClient.showGoodByeMsg();
                    break;
                default:
                    svcClient.showInvalidSelection();
                    break;
            }
        }
    }

    public void printMenu() {
        System.out.println("\n----- Ticketing System -----\n");
        System.out.println("\t'V' - View Seating Chart");
        System.out.println("\t'N' - Number of seats available");
        System.out.println("\t'H' - Hold Seat");
        System.out.println("\t'R' - Reserve Seat");
        System.out.println("\t'Q' - Quit");
        System.out.println("--------------------------------\n");
    }


    private void reserveSeat() {
        int seatId = 0;
        String customerEmail = getCustomerEmail();
        if (customerEmail != null) {
            seatId = readIntInput("Enter Seat Hold Id :");
            String confId = ticketService.reserveSeats(seatId, customerEmail);
            if (confId != null) {
                System.out.println("\n--- Successfully Reserved "+ confId);
            }
            else
            {
                System.out.println("\n--- Not Reserved -- \n Please enter valid email id or seat hold id before reserving ");
            }
        }
        else
        {
            System.out.println("\n--- We are not unable to reserve the seat for "+ seatId+"\n");
        }

    }

    private void holdSeat() {
        String customerEmail = getCustomerEmail();
        if (customerEmail != null) {
            int numSeats = readIntInput("Enter number of seats :");
            SeatHold seatHold = ticketService.findAndHoldSeats(numSeats, customerEmail);
            if (seatHold != null) {
                List<Seat> seatsHeld = seatHold.getSeatsHeld();
                if (seatsHeld != null) {
                    System.out.println("The following seats will be held for "+ customerEmail);
                    seatsHeld.forEach(seat -> {
                        System.out.print(seat.getSeatId()+"\t");
                    });
                    System.out.println("\n----------\nYour Seat Hold Id is "+ seatHold.getSeatHoldId());
                }
            }
        }
        else
        {
            System.out.println("\n--- You need a valid email address to hold a seat *** -------\n");

        }
    }

    private String getCustomerEmail() {
        int retry = 0;

        while (retry < 3) {
            String customerEmail = readInput("Enter Customer Email :");
            if (validEmail(customerEmail)) {
                return customerEmail;
            } else {
                System.out.println("\n--- *** ERROR : Please Enter a valid Email *** -------\n");
            }
            retry++;
        }
        return null;
    }

    private boolean validEmail(String email) {
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);
        return  (matcher.matches());
    }

    private void showNumberOfSeatsAvailable() {
        System.out.println("\nThe Number of Seats Available ="+ ticketService.numSeatsAvailable()+"\n");
    }

    private void showInvalidSelection() {
        System.out.println("\n----- Invalid Selection Please try again -----\n");

    }

    private void showGoodByeMsg() {
        System.out.println("\n----- Thank you for using ticketing system -----\n");

    }


    private int readIntInput(String s) {
        int retry = 0;
        while (retry < 3) {
            String intStr = readInput(s);
            int intVal = 0;
            if (intStr != null) {
                try {
                    intVal = Integer.valueOf(intStr).intValue();
                    return intVal;
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid Input\n");
                    retry++;
                }
            }
        }
        return 0;
    }

    private void viewSeatMap() {
        venue.printVenueSeatLayout();
    }

    private String readInput(String s) {
        System.out.print(s);
        Scanner scanner = new Scanner(System.in);
        return scanner.next().toUpperCase();
    }

}
