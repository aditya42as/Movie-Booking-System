
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

class TicketPrinter {

    public void printTicket(Booking booking) {
        String seats = booking.getSeats().stream()
                .map(s -> s.getSeat().getNumber())
                .collect(Collectors.joining(", "));

        String time = booking.getShow().getStartTime()
                .format(DateTimeFormatter.ofPattern("hh:mm a"));

        System.out.println("\n==============================================");
        System.out.println("                 TICKET");
        System.out.println("==============================================");
        System.out.println("Booking ID : " + booking.getBookingId());
        System.out.println("Customer   : " + booking.getCustomer().getName());
        System.out.println("Movie      : " + booking.getShow().getMovie().getTitle());
        System.out.println("Screen     : Screen-" + booking.getShow().getScreen().getScreenNumber());
        System.out.println("Time       : " + time);
        System.out.println("Seats      : " + seats);
        System.out.printf("Amount     : Rs. %.2f%n", booking.getTotalAmount());
        System.out.println("Payment ID : " + booking.getPaymentTransactionId());
        System.out.println("Status     : " + booking.getStatus());
        System.out.println("==============================================");
    }
}
