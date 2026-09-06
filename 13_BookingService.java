
import java.util.ArrayList;
import java.util.List;

class BookingService {

    private PriceCalculator priceCalculator;
    private TicketPrinter ticketPrinter;
    private List<Booking> bookings;

    public BookingService(PriceCalculator priceCalculator, TicketPrinter ticketPrinter) {
        this.priceCalculator = priceCalculator;
        this.ticketPrinter = ticketPrinter;
        this.bookings = new ArrayList<>();
    }

    public Booking book(Customer customer, Show show, List<String> seatNumbers,
            Payment payment, boolean forcePaymentFailure) {
        List<ShowSeat> selectedSeats = findSeats(show, seatNumbers);

        if (selectedSeats == null || selectedSeats.isEmpty()) {
            System.out.println("Booking rejected: invalid or empty seat selection.");
            return null;
        }

        for (ShowSeat showSeat : selectedSeats) {
            if (!showSeat.isAvailable()) {
                System.out.println("Booking rejected: "
                        + showSeat.getSeat().getNumber() + " is already BOOKED.");
                return null;
            }
        }

        double total = priceCalculator.calculateTotal(selectedSeats);
        Booking booking = new Booking(customer, show, selectedSeats, total);

        for (ShowSeat showSeat : selectedSeats) {
            showSeat.bookSeat();
        }

        // Runtime polymorphism: Payment reference calls the correct subclass.
        boolean paid = !forcePaymentFailure && payment.pay(total);

        if (!paid) {
            for (ShowSeat showSeat : selectedSeats) {
                showSeat.cancelSeat();
            }
            booking.fail();
            System.out.println("Payment failed. Booking NOT confirmed and seats released.");
            return booking;
        }

        booking.setPaymentTransactionId(payment.getTransactionId());
        booking.confirm();
        bookings.add(booking);
        System.out.println("Payment successful. Booking confirmed.");
        ticketPrinter.printTicket(booking);
        return booking;
    }

    private List<ShowSeat> findSeats(Show show, List<String> seatNumbers) {
        List<ShowSeat> selected = new ArrayList<>();
        for (String number : seatNumbers) {
            ShowSeat seat = show.findShowSeat(number);
            if (seat == null) {
                System.out.println("Invalid seat number: " + number);
                return null;
            }
            selected.add(seat);
        }
        return selected;
    }

    public void cancelBooking(String bookingId) {
        Booking booking = findBooking(bookingId);

        if (booking == null) {
            System.out.println("Booking not found.");
            return;
        }

        if (booking.getStatus() != Booking.Status.CONFIRMED) {
            System.out.println("Only a CONFIRMED booking can be cancelled.");
            return;
        }

        for (ShowSeat showSeat : booking.getSeats()) {
            showSeat.cancelSeat();
        }

        booking.cancel();
        System.out.println("Booking " + bookingId + " cancelled. Seats are AVAILABLE again.");
    }

    // Additional feature: allow a customer to check a booking's current status.
    public void showBookingStatus(String bookingId) {
        Booking booking = findBooking(bookingId);
        if (booking == null) {
            System.out.println("Booking not found.");
            return;
        }
        System.out.println("Booking " + booking.getBookingId() + " status: " + booking.getStatus());
    }

    public Booking findBooking(String bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId().equalsIgnoreCase(bookingId)) {
                return booking;
            }
        }
        return null;
    }

    public void printMyTickets() {
        if (bookings.isEmpty()) {
            System.out.println("No confirmed bookings yet.");
            return;
        }

        for (Booking booking : bookings) {
            if (booking.getStatus() == Booking.Status.CONFIRMED) {
                ticketPrinter.printTicket(booking);
            }
        }
    }

    public void runDemoEdgeCases(Customer customer, Show show) {
        System.out.println("\n--- EDGE CASE 1: BOOKING ALREADY-BOOKED SEAT ---");
        List<String> firstSeat = List.of("A1");
        book(customer, show, firstSeat, new UpiPayment(), false);

        System.out.println("\nTrying A1 again:");
        book(customer, show, firstSeat, new CardPayment(), false);

        System.out.println("\n--- EDGE CASE 2: FAILED PAYMENT ---");
        Booking failed = book(customer, show, List.of("A2"),
                new UpiPayment(), true);
        if (failed != null) {
            System.out.println("Failed booking status: " + failed.getStatus());
        }

        System.out.println("\n--- EDGE CASE 3: CANCELLATION ---");
        Booking cancellable = book(customer, show, List.of("B1"),
                new CashPayment(), false);
        if (cancellable != null) {
            cancelBooking(cancellable.getBookingId());
        }

        System.out.println("\n--- EDGE CASE 4: INVALID SEAT ---");
        book(customer, show, List.of("Z99"),
                new UpiPayment(), false);
    }
}
