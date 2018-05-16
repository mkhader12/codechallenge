package com.wmt.tktsvc;


import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TicketSvcClient {
    private static TicketService ticketService;
    private static Venue venue;

    public void printMenu() {
        System.out.println("\n----- Ticketing System -----\n");
        System.out.println("\t'V' - View Seating Chart");
        System.out.println("\t'N' - Number of seats available");
        System.out.println("\t'H' - Hold Seat");
        System.out.println("\t'R' - Reserve Seat");
        //System.out.println("\t'F' - Find Best Seat");
        //System.out.println("\t'C' - Choose Seat");
        System.out.println("\t'Q' - Quit");
        System.out.println("--------------------------------\n");


    }

    public static void main(String[] args) {
        TicketSvcClient svcClient = new TicketSvcClient();
        venue=new Venue(10, 10);
        ticketService =  new TicketServiceImpl(venue);

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
                case "F":
                    svcClient.findBestSeat();
                    break;
                case "C":
                    svcClient.chooseSeat();
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
                    System.out.println("\n----------\n");
                    System.out.println("Your Seat Hold Id is "+ seatHold.getSeatHoldId());
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

    private void chooseSeat() {
        viewSeatMap();

        int row = readIntInput("Choose the row number:");
        int seatNo = readIntInput("Choose the seat number:");
        int numSeats = readIntInput("Enter number of seats :");

        try {
            int numSeatsHeld = venue.holdSeats(row, seatNo, numSeats);
            System.out.println("*** Seats will be held successfully ***");

        } catch (SeatNotAvailableException e) {
            System.out.println("*** Seat Not available ***");
        }


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

    private void findBestSeat() {
        int numSeats = readIntInput("Enter number of seats :");

        try {
            List<Seat> seats = venue.findBestSeats(numSeats);
            if (seats != null) {
                System.out.println(seats.size() +" seats held successfully");
            }
            viewSeatMap();

        } catch (SeatNotAvailableException e) {
            System.out.println("*** Seat Not available ***");
        }
    }

    private void viewSeatMap() {
        venue.printSeats();
    }

    private String readInput(String s) {
        System.out.print(s);
        Scanner scanner = new Scanner(System.in);
        return scanner.next().toUpperCase();
    }

}
