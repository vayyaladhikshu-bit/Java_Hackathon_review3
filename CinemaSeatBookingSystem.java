import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CinemaSeatBookingSystem {

    static Scanner sc = new Scanner(System.in);

    static String[] movies = {
        "Avengers: Endgame",
        "Interstellar",
        "Inception"
    };

    static String[] timings = {
        "10:00 AM",
        "2:00 PM",
        "6:00 PM"
    };

    static int[][] avengersSeats = new int[3][30];
    static int[][] interstellarSeats = new int[3][30];
    static int[][] inceptionSeats = new int[3][30];

    static int[] bookingId = new int[100];
    static int[] movieNo = new int[100];
    static int[] showNo = new int[100];
    static int[][] bookedSeats = new int[100][30];
    static int[] seatCount = new int[100];
    static String[] bookingType = new String[100];
    static double[] totalAmount = new double[100];
    static boolean[] confirmed = new boolean[100];

    static int totalBookings = 0;
    static int nextId = 1001;

    static int[][] getSeats(int movie) {
        if (movie == 0) {
            return avengersSeats;
        } else if (movie == 1) {
            return interstellarSeats;
        } else {
            return inceptionSeats;
        }
    }

    static void saveMovieSeats(PrintWriter out, int[][] seats) {
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                out.print(seats[i][j] + " ");
            }
            out.println();
        }
    }

    static void loadMovieSeats(Scanner file, int[][] seats) {
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (file.hasNextInt()) {
                    int status = file.nextInt();

                    if (status == 0 || status == 1) {
                        seats[i][j] = status;
                    } else {
                        seats[i][j] = 0;
                    }
                }
            }
        }
    }

    static void saveSeatStatus() {
        try (PrintWriter out = new PrintWriter("cinema_seats.txt")) {
            saveMovieSeats(out, avengersSeats);
            saveMovieSeats(out, interstellarSeats);
            saveMovieSeats(out, inceptionSeats);
        } catch (IOException e) {
            System.out.println("Unable to save seat status.");
        }
    }

    static void loadSeatStatus() {
        File file = new File("cinema_seats.txt");

        if (file.exists()) {
            try (Scanner input = new Scanner(file)) {
                loadMovieSeats(input, avengersSeats);
                loadMovieSeats(input, interstellarSeats);
                loadMovieSeats(input, inceptionSeats);
            } catch (IOException e) {
                System.out.println("Unable to load saved seat status.");
            }
        }
    }

    static int readChoice(String message, int min, int max) {
        while (true) {
            System.out.print(message);

            if (!sc.hasNextInt()) {
                System.out.println("Please enter a number.");
                sc.next();
                continue;
            }

            int choice = sc.nextInt();

            if (choice >= min && choice <= max) {
                return choice;
            }

            System.out.println(
                "Invalid choice! Enter a number from "
                + min + " to " + max + "."
            );
        }
    }

    static int readNumber(String message) {
        while (true) {
            System.out.print(message);

            if (sc.hasNextInt()) {
                return sc.nextInt();
            }

            System.out.println("Please enter a numeric booking ID.");
            sc.next();
        }
    }

    static void showMovies() {
        System.out.println("\n========== MOVIES LIST ==========");

        for (int i = 0; i < movies.length; i++) {
            System.out.println((i + 1) + ". " + movies[i]);
        }
    }

    static void showTimings(int movie) {
        System.out.println("\nShow timings for " + movies[movie]);

        for (int i = 0; i < timings.length; i++) {
            System.out.println((i + 1) + ". " + timings[i]);
        }
    }

    static int selectMovie() {
        showMovies();
        return readChoice("Enter movie number: ", 1, 3) - 1;
    }

    static int selectShow(int movie) {
        showTimings(movie);
        return readChoice("Enter show number: ", 1, 3) - 1;
    }

    static String seatName(int seat) {
        char row = (char) ('A' + seat / 6);
        int column = seat % 6 + 1;

        return "" + row + column;
    }

    static int getPrice(int seat) {
        int row = seat / 6;

        if (row < 2) {
            return 150;
        } else {
            return 100;
        }
    }

    static void showSeats(int movie, int show) {
        int[][] currentSeats = getSeats(movie);

        System.out.println("\n========== SEAT LAYOUT ==========");
        System.out.println("Movie: " + movies[movie]);
        System.out.println("Show: " + timings[show]);
        System.out.println("Screen is at the front\n");
        System.out.println("     1    2    3    4    5    6");

        for (int row = 0; row < 5; row++) {
            System.out.print((char) ('A' + row) + "  ");

            for (int col = 0; col < 6; col++) {
                int seat = row * 6 + col;

                if (currentSeats[show][seat] == 0) {
                    System.out.printf("[%s] ", seatName(seat));
                } else {
                    System.out.print("[ X ] ");
                }
            }

            System.out.println();
        }

        System.out.println("\nSeat number = Available");
        System.out.println("X = Booked");
        System.out.println("Rows A-B: Premium (Rs.150 per seat)");
        System.out.println("Rows C-E: Regular (Rs.100 per seat)");
    }

    static void recommendSeats(int movie, int show, int count) {
        int[][] currentSeats = getSeats(movie);

        System.out.println("\n========== SEAT RECOMMENDATIONS ==========");

        if (count == 1) {
            int[] preferredSeats = {14, 15, 8, 9, 20, 21};
            boolean found = false;

            for (int seat : preferredSeats) {
                if (currentSeats[show][seat] == 0) {
                    System.out.println("Recommended seat: " + seatName(seat));
                    found = true;
                    break;
                }
            }

            if (!found) {
                for (int i = 0; i < 30; i++) {
                    if (currentSeats[show][i] == 0) {
                        System.out.println(
                            "Recommended available seat: " + seatName(i)
                        );
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                System.out.println("No seats are available.");
            }
        } else {
            boolean found = false;

            if (count <= 6) {
                int[] rowOrder = {2, 1, 3, 0, 4};

                for (int r = 0; r < 5 && !found; r++) {
                    int row = rowOrder[r];

                    for (int start = 0; start <= 6 - count; start++) {
                        boolean available = true;

                        for (int j = 0; j < count; j++) {
                            int seat = row * 6 + start + j;

                            if (currentSeats[show][seat] == 1) {
                                available = false;
                                break;
                            }
                        }

                        if (available) {
                            System.out.print("Recommended seats: ");

                            for (int j = 0; j < count; j++) {
                                System.out.print(
                                    seatName(row * 6 + start + j) + " "
                                );
                            }

                            System.out.println();
                            found = true;
                            break;
                        }
                    }
                }
            }

            if (!found) {
                System.out.println(
                    "No suitable adjacent seats found. "
                    + "You can select available seats manually."
                );
            }
        }
    }

    static void bookSeats() {
        if (totalBookings >= 100) {
            System.out.println("Booking limit reached.");
            return;
        }

        int movie = selectMovie();
        int show = selectShow(movie);

        showSeats(movie, show);

        System.out.println("\n1. Individual Booking");
        System.out.println("2. Group Booking");

        int type = readChoice("Choose booking type: ", 1, 2);
        int count;

        if (type == 1) {
            count = 1;
        } else {
            count = readChoice("Enter number of seats (2-30): ", 2, 30);
        }

        int[][] currentSeats = getSeats(movie);
        int available = 0;

        for (int i = 0; i < 30; i++) {
            if (currentSeats[show][i] == 0) {
                available++;
            }
        }

        if (available < count) {
            System.out.println("Not enough seats available.");
            return;
        }

        recommendSeats(movie, show, count);

        if (type == 2 && count > 6) {
            System.out.println(
                "Your group is larger than one row. "
                + "Please select seats across multiple rows."
            );
        }

        int[] selected = new int[count];

        for (int i = 0; i < count; i++) {
            while (true) {
                System.out.println("\nSelect seat " + (i + 1));

                int row = readChoice("Enter row (1=A to 5=E): ", 1, 5);
                int col = readChoice("Enter seat number (1-6): ", 1, 6);

                int seat = (row - 1) * 6 + col - 1;

                if (currentSeats[show][seat] == 1) {
                    System.out.println(
                        "This seat is already booked. Choose another."
                    );
                    continue;
                }

                boolean duplicate = false;

                for (int j = 0; j < i; j++) {
                    if (selected[j] == seat) {
                        duplicate = true;
                        break;
                    }
                }

                if (duplicate) {
                    System.out.println(
                        "You have already selected this seat."
                    );
                } else {
                    selected[i] = seat;
                    break;
                }
            }
        }

        double total = 0;

        System.out.println("\n========== BOOKING SUMMARY ==========");

        for (int i = 0; i < count; i++) {
            System.out.println(
                seatName(selected[i]) + " - Rs." + getPrice(selected[i])
            );

            total += getPrice(selected[i]);
        }

        System.out.printf("Total amount: Rs.%.2f%n", total);

        System.out.println("\n1. Confirm Booking");
        System.out.println("2. Cancel Booking");

        int choice = readChoice("Enter choice: ", 1, 2);

        if (choice == 2) {
            System.out.println("Booking not confirmed.");
            return;
        }

        int b = totalBookings;

        bookingId[b] = nextId++;
        movieNo[b] = movie;
        showNo[b] = show;
        seatCount[b] = count;
        totalAmount[b] = total;
        bookingType[b] = (type == 1) ? "Individual" : "Group";
        confirmed[b] = true;

        for (int i = 0; i < count; i++) {
            bookedSeats[b][i] = selected[i];
            currentSeats[show][selected[i]] = 1;
        }

        totalBookings++;
        saveSeatStatus();

        System.out.println("\nBooking successful!");
        System.out.println("Your booking ID is: " + bookingId[b]);

        showReceipt(b);
    }

    static int findBooking(int id) {
        for (int i = 0; i < totalBookings; i++) {
            if (bookingId[i] == id) {
                return i;
            }
        }

        return -1;
    }

    static void showReceipt(int b) {
        System.out.println("\n========== BOOKING RECEIPT ==========");
        System.out.println("Booking ID: " + bookingId[b]);
        System.out.println("Movie: " + movies[movieNo[b]]);
        System.out.println("Show timing: " + timings[showNo[b]]);
        System.out.println("Booking type: " + bookingType[b]);
        System.out.println("Number of seats: " + seatCount[b]);
        System.out.println("\nSelected seats:");

        for (int i = 0; i < seatCount[b]; i++) {
            int seat = bookedSeats[b][i];

            System.out.println(
                seatName(seat) + " - Rs." + getPrice(seat)
            );
        }

        System.out.printf("Total amount: Rs.%.2f%n", totalAmount[b]);

        if (confirmed[b]) {
            System.out.println("Booking status: CONFIRMED");
        } else {
            System.out.println("Booking status: CANCELLED");
        }

        System.out.println("=====================================");
    }

    static void viewReceipt() {
        if (totalBookings == 0) {
            System.out.println(
                "No booking details are available in this session."
            );
            return;
        }

        int id = readNumber("Enter booking ID: ");
        int b = findBooking(id);

        if (b == -1) {
            System.out.println("Booking not found in this session.");
        } else {
            showReceipt(b);
        }
    }

    static void cancelBooking() {
        if (totalBookings == 0) {
            System.out.println(
                "No booking details are available in this session."
            );
            return;
        }

        int id = readNumber("Enter booking ID to cancel: ");
        int b = findBooking(id);

        if (b == -1) {
            System.out.println("Booking not found in this session.");
            return;
        }

        if (!confirmed[b]) {
            System.out.println("This booking is already cancelled.");
            return;
        }

        System.out.println("\nBooking details:");
        showReceipt(b);

        System.out.println("\n1. Confirm Cancellation");
        System.out.println("2. Go Back");

        int choice = readChoice("Enter choice: ", 1, 2);

        if (choice == 1) {
            int movie = movieNo[b];
            int show = showNo[b];
            int[][] currentSeats = getSeats(movie);

            for (int i = 0; i < seatCount[b]; i++) {
                int seat = bookedSeats[b][i];
                currentSeats[show][seat] = 0;
            }

            confirmed[b] = false;
            saveSeatStatus();

            System.out.println("\nBooking cancelled successfully!");
            System.out.println("The seats are available again.");
        } else {
            System.out.println("Cancellation stopped.");
        }
    }

    public static void main(String[] args) {
        loadSeatStatus();

        int choice;

        do {
            System.out.println("\n===== CINEMA SEAT BOOKING SYSTEM =====");
            System.out.println("1. View Movies List");
            System.out.println("2. View Movie Timings");
            System.out.println("3. View Seat Layout");
            System.out.println("4. Book Seats");
            System.out.println("5. View Booking Receipt");
            System.out.println("6. Cancel Booking");
            System.out.println("7. Exit");

            choice = readChoice("Enter your choice: ", 1, 7);

            switch (choice) {
                case 1:
                    showMovies();
                    break;

                case 2: {
                    int movie = selectMovie();
                    showTimings(movie);
                    break;
                }

                case 3: {
                    int movie = selectMovie();
                    int show = selectShow(movie);
                    showSeats(movie, show);
                    break;
                }

                case 4:
                    bookSeats();
                    break;

                case 5:
                    viewReceipt();
                    break;

                case 6:
                    cancelBooking();
                    break;

                case 7:
                    saveSeatStatus();
                    System.out.println(
                        "Thank you for using the Cinema Seat Booking System!"
                    );
                    System.out.println("Have a great day!");
                    break;
            }

        } while (choice != 7);

       
    }
}
