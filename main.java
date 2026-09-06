
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Cinema cinema = createCinema();
        List<Movie> movies = createMovies();
        List<Show> shows = createShows(cinema, movies);

        PriceCalculator priceCalculator = new PriceCalculator();
        TicketPrinter ticketPrinter = new TicketPrinter();
        BookingService bookingService = new BookingService(priceCalculator, ticketPrinter);
        Customer customer = createCustomer();

        while (true) {
            printMainMenu();
            int choice = readInt("Choose: ");

            switch (choice) {
                case 1:
                    listMovies(movies);
                    break;
                case 2:
                    bookTicket(movies, shows, customer, bookingService);
                    break;
                case 3:
                    cancelTicket(bookingService);
                    break;
                case 4:
                    bookingService.printMyTickets();
                    break;
                case 5:
                    searchMovies(movies);
                    break;
                case 6:
                    showAvailableSeatCounts(shows);
                    break;
                case 7:
                    checkBookingStatus(bookingService);
                    break;
                case 8:
                    runEdgeCaseDemo(customer, shows.get(0), bookingService);
                    break;
                case 0:
                    System.out.println("Thank you for using Movie Ticket Booking System.");
                    return;
                default:
                    System.out.println("Invalid menu choice. Please try again.");
            }
        }
    }

    private static Cinema createCinema() {
        Cinema cinema = new Cinema("Hill View Cinema");
        cinema.addScreen(1);
        cinema.addScreen(2);
        return cinema;
    }

    private static List<Movie> createMovies() {
        return new ArrayList<>(Arrays.asList(
                new Movie("12th Fail", "Hindi", 147),
                new Movie("Kalki 2898 AD", "Hindi", 181),
                new Movie("Interstellar", "English", 169)
        ));
    }

    private static List<Show> createShows(Cinema cinema, List<Movie> movies) {
        List<Show> shows = new ArrayList<>();
        shows.add(new Show(movies.get(0), cinema.getScreen(0), LocalTime.of(18, 0)));
        shows.add(new Show(movies.get(0), cinema.getScreen(1), LocalTime.of(21, 0)));
        shows.add(new Show(movies.get(1), cinema.getScreen(0), LocalTime.of(15, 30)));
        shows.add(new Show(movies.get(2), cinema.getScreen(1), LocalTime.of(19, 30)));
        return shows;
    }

    private static Customer createCustomer() {
        System.out.println(" MOVIE TICKET BOOKING ");
        String name = readNonEmpty("Enter customer name: ");
        String phone;
        while (true) {
            phone = readNonEmpty("Enter 10-digit phone number: ");
            if (phone.matches("\\d{10}")) {
                break;
            }
            System.out.println("Invalid phone number. Enter exactly 10 digits.");
        }
        return new Customer(name, phone);
    }

    private static void printMainMenu() {
        System.out.println("\nMOVIE TICKET BOOKING");
        System.out.println("1. Movies");
        System.out.println("2. Book");
        System.out.println("3. Cancel");
        System.out.println("4. My Tickets");
        System.out.println("5. Search Movies");
        System.out.println("6. Available Seat Counts");
        System.out.println("7. Check Booking Status");
        System.out.println("8. Run Edge-Case Demo");
        System.out.println("0. Exit");
    }

    private static void listMovies(List<Movie> movies) {
        System.out.println("\nCurrently Playing:");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + movies.get(i));
        }
    }

    // Additional feature: search movies by title or language.
    private static void searchMovies(List<Movie> movies) {
        String query = readNonEmpty("Search title or language: ").toLowerCase();
        boolean found = false;
        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(query)
                    || movie.getLanguage().toLowerCase().contains(query)) {
                System.out.println(movie);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching movies found.");
        }
    }

    // Additional feature: show available seat counts for every show.
    private static void showAvailableSeatCounts(List<Show> shows) {
        System.out.println("\nAvailable seats by show:");
        for (Show show : shows) {
            System.out.println(show.getMovie().getTitle() + " | Screen-"
                    + show.getScreen().getScreenNumber() + " | "
                    + show.getStartTime().format(DateTimeFormatter.ofPattern("hh:mm a"))
                    + " -> " + countAvailable(show) + " available");
        }
    }

    private static int countAvailable(Show show) {
        int count = 0;
        for (ShowSeat seat : show.getShowSeats()) {
            if (seat.isAvailable()) {
                count++;
            }
        }
        return count;
    }

    // Additional feature: check the status of an existing booking.
    private static void checkBookingStatus(BookingService service) {
        String bookingId = readNonEmpty("Enter booking ID: ");
        service.showBookingStatus(bookingId);
    }

    private static void bookTicket(List<Movie> movies, List<Show> shows,
            Customer customer, BookingService service) {
        listMovies(movies);
        int movieChoice = readInt("Choose movie: ") - 1;

        if (movieChoice < 0 || movieChoice >= movies.size()) {
            System.out.println("Invalid movie choice.");
            return;
        }

        List<Show> movieShows = new ArrayList<>();
        for (Show show : shows) {
            if (show.getMovie() == movies.get(movieChoice)) {
                movieShows.add(show);
            }
        }

        System.out.println("\nShows for " + movies.get(movieChoice).getTitle() + ":");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        for (int i = 0; i < movieShows.size(); i++) {
            Show show = movieShows.get(i);
            System.out.println("[" + (i + 1) + "] Screen-"
                    + show.getScreen().getScreenNumber() + "  "
                    + show.getStartTime().format(formatter));
        }

        int showChoice = readInt("Choose show: ") - 1;
        if (showChoice < 0 || showChoice >= movieShows.size()) {
            System.out.println("Invalid show choice.");
            return;
        }

        Show selectedShow = movieShows.get(showChoice);
        selectedShow.displaySeats();

        String input = readNonEmpty("Enter seat numbers (e.g. A1,B2): ");
        List<String> seatNumbers = parseSeatNumbers(input);

        double previewTotal = calculatePreviewTotal(selectedShow, seatNumbers);
        if (previewTotal < 0) {
            return;
        }

        System.out.printf("TOTAL: Rs. %.2f%n", previewTotal);
        System.out.println("Pay by: 1.UPI  2.Card  3.Cash");
        int paymentChoice = readInt("Choose payment method: ");

        Payment payment;
        switch (paymentChoice) {
            case 1:
                payment = new UpiPayment();
                break;
            case 2:
                payment = new CardPayment();
                break;
            case 3:
                payment = new CashPayment();
                break;
            default:
                System.out.println("Invalid payment choice.");
                return;
        }

        service.book(customer, selectedShow, seatNumbers, payment, false);
    }

    private static double calculatePreviewTotal(Show show, List<String> seatNumbers) {
        PriceCalculator calculator = new PriceCalculator();
        List<ShowSeat> selected = new ArrayList<>();

        for (String number : seatNumbers) {
            ShowSeat showSeat = show.findShowSeat(number);
            if (showSeat == null) {
                System.out.println("Invalid seat number: " + number);
                return -1;
            }
            if (!showSeat.isAvailable()) {
                System.out.println("Seat " + number + " is already BOOKED.");
                return -1;
            }
            selected.add(showSeat);
        }
        return calculator.calculateTotal(selected);
    }

    private static List<String> parseSeatNumbers(String input) {
        String[] parts = input.split(",");
        List<String> result = new ArrayList<>();

        for (String part : parts) {
            String seat = part.trim().toUpperCase();
            if (!seat.isEmpty() && !result.contains(seat)) {
                result.add(seat);
            }
        }
        return result;
    }

    private static void cancelTicket(BookingService service) {
        String bookingId = readNonEmpty("Enter booking ID: ");
        service.cancelBooking(bookingId);
    }

    private static void runEdgeCaseDemo(Customer customer, Show show,
            BookingService service) {
        service.runDemoEdgeCases(customer, show);
        show.displaySeats();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty.");
        }
    }
}
