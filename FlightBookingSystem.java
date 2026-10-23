
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Abstract base class: Abstraction
abstract class Passenger {
    private String name;
    private int age;

    public Passenger(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public abstract double getDiscountRate();

    public abstract String getPassengerType();
}

// Inheritance
class RegularPassenger extends Passenger {
    public RegularPassenger(String name, int age) {
        super(name, age);
    }

    @Override
    public double getDiscountRate() {
        return 0.0;
    }

    @Override
    public String getPassengerType() {
        return "Regular";
    }
}

class StudentPassenger extends Passenger {
    public StudentPassenger(String name, int age) {
        super(name, age);
    }

    @Override
    public double getDiscountRate() {
        return 0.10;
    }

    @Override
    public String getPassengerType() {
        return "Student";
    }
}

// Flight class: Encapsulation
class Flight {
    private String code;
    private String airline;
    private String source;
    private String destination;
    private int availableSeats;
    private double baseFare;

    public Flight(String code, String airline, String source,
                  String destination, int availableSeats, double baseFare) {
        this.code = code;
        this.airline = airline;
        this.source = source;
        this.destination = destination;
        this.availableSeats = availableSeats;
        this.baseFare = baseFare;
    }

    public String getCode() {
        return code;
    }

    public String getAirline() {
        return airline;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public boolean hasEnoughSeats(int tickets) {
        return tickets > 0 && tickets <= availableSeats;
    }

    public void reserveSeats(int tickets) {
        if (hasEnoughSeats(tickets)) {
            availableSeats -= tickets;
        }
    }

    public void displayFlight() {
        System.out.printf(
            "%-8s %-18s %-15s %-15s %-8d Rs. %.2f%n",
            code, airline, source, destination, availableSeats, baseFare
        );
    }
}

// Booking class
class Booking {
    private String bookingId;
    private Flight flight;
    private Passenger passenger;
    private int tickets;
    private double subtotal;
    private double discount;
    private double totalFare;

    public Booking(String bookingId, Flight flight,
                   Passenger passenger, int tickets) {
        this.bookingId = bookingId;
        this.flight = flight;
        this.passenger = passenger;
        this.tickets = tickets;

        subtotal = flight.getBaseFare() * tickets;
        discount = subtotal * passenger.getDiscountRate();
        totalFare = subtotal - discount;
    }

    public void displaySummary() {
        System.out.println("\n========== BOOKING SUMMARY ==========");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Flight Code: " + flight.getCode());
        System.out.println("Airline: " + flight.getAirline());
        System.out.println("Route: " + flight.getSource()
                           + " to " + flight.getDestination());
        System.out.println("Passenger: " + passenger.getName());
        System.out.println("Age: " + passenger.getAge());
        System.out.println("Passenger Type: "
                           + passenger.getPassengerType());
        System.out.println("Tickets: " + tickets);
        System.out.printf("Base fare per ticket: Rs. %.2f%n",
                          flight.getBaseFare());
        System.out.printf("Subtotal: Rs. %.2f%n", subtotal);
        System.out.printf("Discount (%.0f%%): Rs. %.2f%n",
                          passenger.getDiscountRate() * 100, discount);
        System.out.printf("Final amount: Rs. %.2f%n", totalFare);
        System.out.println("======================================");
    }
}

// Main booking system
public class FlightBookingSystem {
    private Scanner scanner = new Scanner(System.in);
    private List<Flight> flights = new ArrayList<>();
    private Booking latestBooking;
    private int nextBookingNumber = 1001;

    public FlightBookingSystem() {
        flights.add(new Flight("PN101", "Air India",
                "Pune", "Delhi", 20, 4000));
        flights.add(new Flight("MB102", "IndiGo",
                "Mumbai", "Bengaluru", 25, 3500));
        flights.add(new Flight("PN103", "Akasa Air",
                "Pune", "Hyderabad", 15, 3000));
    }

    private void displayFlights() {
        System.out.println("\n================ AVAILABLE FLIGHTS ================");
        System.out.printf("%-8s %-18s %-15s %-15s %-8s %s%n",
                "Code", "Airline", "Source", "Destination",
                "Seats", "Base Fare");
        for (Flight flight : flights) {
            flight.displayFlight();
        }
        System.out.println("====================================================");
    }

    private Flight findFlightByCode(String code) {
        for (Flight flight : flights) {
            if (flight.getCode().equalsIgnoreCase(code)) {
                return flight;
            }
        }
        return null;
    }

    private String readNonBlank(String message) {
        while (true) {
            System.out.print(message);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Error: This field cannot be blank.");
        }
    }

    private int readPositiveInt(String message) {
        while (true) {
            System.out.print(message);
            String value = scanner.nextLine().trim();

            try {
                int number = Integer.parseInt(value);
                if (number > 0) {
                    return number;
                }
                System.out.println("Error: Enter a number greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Error: Enter a valid whole number.");
            }
        }
    }

    private void bookFlight() {
        System.out.println("\n========== BOOK A FLIGHT ==========");

        String code = readNonBlank("Enter flight code: ");
        Flight flight = findFlightByCode(code);

        if (flight == null) {
            System.out.println("Booking failed: Invalid flight code.");
            return;
        }

        System.out.println("Available seats: " + flight.getAvailableSeats());

        String name = readNonBlank("Enter passenger name: ");
        int age = readPositiveInt("Enter age: ");

        System.out.print("Passenger type (Regular/Student): ");
        String type = scanner.nextLine().trim();

        Passenger passenger;

        if (type.equalsIgnoreCase("Regular")) {
            passenger = new RegularPassenger(name, age);
        } else if (type.equalsIgnoreCase("Student")) {
            passenger = new StudentPassenger(name, age);
        } else {
            System.out.println(
                "Booking failed: Choose Regular or Student."
            );
            return;
        }

        int tickets = readPositiveInt("Enter number of tickets: ");

        if (!flight.hasEnoughSeats(tickets)) {
            System.out.println(
                "Booking failed: Not enough seats available."
            );
            System.out.println(
                "No booking was created. Available seats remain unchanged."
            );
            return;
        }

        // Update seats and save only after validation succeeds.
        Booking booking = new Booking(
            "BK" + nextBookingNumber, flight, passenger, tickets
        );

        flight.reserveSeats(tickets);
        latestBooking = booking;
        nextBookingNumber++;

        System.out.println("\nBooking confirmed!");
        booking.displaySummary();
    }

    private void viewBooking() {
        if (latestBooking == null) {
            System.out.println("\nNo confirmed booking exists yet.");
        } else {
            latestBooking.displaySummary();
        }
    }

    public void start() {
        System.out.println("Welcome to Kiran Air Booking!");

        while (true) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. View Flights");
            System.out.println("2. Book Flight");
            System.out.println("3. View Booking");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayFlights();
                    break;
                case "2":
                    bookFlight();
                    break;
                case "3":
                    viewBooking();
                    break;
                case "4":
                    System.out.println(
                        "Thank you for using Kiran Air Booking!"
                    );
                    scanner.close();
                    return;
                default:
                    System.out.println(
                        "Invalid choice. Please enter 1, 2, 3, or 4."
                    );
            }
        }
    }

    public static void main(String[] args) {
        FlightBookingSystem system = new FlightBookingSystem();
        system.start();
    }
}
