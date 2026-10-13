import java.util.Scanner;

class Booking {
    String movieName;
    double ticketPrice;
    int tickets;
    String customerName;

    
    Booking() {
        movieName = "";
        ticketPrice = 0;
        tickets = 0;
        customerName = "";
    }

    Booking(String movieName, double ticketPrice,
            int tickets, String customerName) {
        this.movieName = movieName;
        this.ticketPrice = ticketPrice;
        this.tickets = tickets;
        this.customerName = customerName;
    }

    void bookTickets(int tickets) {
        this.tickets = tickets;
    }

    void bookTickets(int tickets, String customerName) {
        this.tickets = tickets;
        this.customerName = customerName;
    }

    
    double calculateAmount() {
        double total = ticketPrice * tickets;

        if (tickets >= 5) {
            total = total - (total * 0.10);
        }

        return total;
    }

    
    void displayBooking() {
        if (tickets <= 0) {
            System.out.println("Invalid number of tickets!");
            return;
        }

        double total = ticketPrice * tickets;

        System.out.println("\n--- Booking Summary ---");
        System.out.println("Movie Name: " + movieName.toUpperCase());
        System.out.println("Customer Name: " + customerName.toUpperCase());
        System.out.println("Ticket Price: Rs. " + ticketPrice);
        System.out.println("Number of Tickets: " + tickets);
        System.out.println("Total Amount: Rs. " + total);

        if (tickets >= 5) {
            System.out.println("Discount: 10%");
        } else {
            System.out.println("Discount: 0%");
        }

        System.out.println("Final Amount: Rs. " + calculateAmount());
    }

    
    void compareCustomer(String name) {
        if (customerName.equalsIgnoreCase(name)) {
            System.out.println("Customer names match.");
        } else {
            System.out.println("Customer names do not match.");
        }
    }
}

public class CinemaBooking {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Cinema Ticket Booking System ===");

        System.out.print("Enter movie name: ");
        String movie = sc.nextLine();

        System.out.print("Enter ticket price: ");
        double price = sc.nextDouble();

        System.out.print("Enter number of tickets: ");
        int tickets = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter customer name: ");
        String customer = sc.nextLine();

        
        Booking b1 = new Booking(movie, price, tickets, customer);

        b1.displayBooking();

        System.out.print("\nEnter customer name to compare: ");
        String name = sc.nextLine();

        b1.compareCustomer(name);

        
        System.out.println("\n--- Update Booking ---");

        System.out.print("Enter new number of tickets: ");
        int newTickets = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter customer name: ");
        String newCustomer = sc.nextLine();

        b1.bookTickets(newTickets, newCustomer);
        b1.displayBooking();

        
    }
}

