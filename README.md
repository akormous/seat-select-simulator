# seat-select-simulator
A web-based simulator that show how seat selection works in ticket booking systems such as BookMyShow, TicketMaster etc.

## Overview

- A person chooses to go to an event
- He/She tries to book a ticket of the event
- There is an arrangement of seats at the event
- The prices of those seats differ
- The position of those seats matter to the user
- Let's say the event is very popular, and millions of users try to book those seats at the same time
- How to handle the concurrency of such system?
- The system has to be available, scalable, reliable

```mermaid
sequenceDiagram

actor u as user
participant fe as frontend
participant be as backend
participant db as database


fe ->> be : GET /getAllSeats
be -->> fe : List<Seat>
fe -->> fe : Render List<Seat>
u ->> fe : select seats (max. 5) and Proceed
fe ->> be : POST /bookSeats List<Integer> seatIds
be ->> db : Isolation @SERIALIZATION book seats set reserved = true
db -->> be : OK
alt Error
    db -->> be : Error: [Already reserved, seat not found etc]
    be -->> fe : Seat already reserved / Seat not found
    fe -->> u : Seat already reserved / Seat not found
end

db -->> be : OK
be -->> fe : OK
fe -->> u : Seats reserved successfully!

```

## DB design

```mermaid
erDiagram

SEAT {
    long id
    boolean is_reserved
}

```

## Deployment steps

1. Create `jar` file using `./mvnw clean package`
2. Upload that `jar` file in AWS Lambda
3. Test it and done