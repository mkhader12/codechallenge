# Ticketing Service Code Challenge

This ticketing service implementation facilates the discovery of available seats, temporarily holding the seat, and finally reserve the seats that was on hold for any given  venue.
 
**Assumptions**


- The venue for which the tickeing service is implemented would always have same number seats per row.
- There is no specific criteria mentioned for choosing the best seating so, assuming the best seats are the available seats from  the front row.
- The ticketing service will best find same row of seats for the group reservations but it doesn't guarantee that.
- The venue dimension will be supplied as input to the venue constructor. This code assumes 10 rows of seats and 10 seats per row.
- Execution of this program requires Java 8, Maven and access to git.
- There is no seat limitation for a customer to hold.
- At any given time, a customer is allowed to hold only one hold. However, after reserving the seats the customer is allowed to hold another one.
- The code is not optimized for multi-threading support.


**Code and Building**


git clone https://github.com/mkhader12/codechallenge.git

cd TicketService

mvn clean install

**Interactive Standalone Testing Program**

A Test Harness : com.wmt.tktsvc.client.TicketSvcTestHarness

Design Overview
Project can use the test harness program to run standalone. 

Algorithms used:
- Perfect Fit
- Best Fit
- Worst Fit

