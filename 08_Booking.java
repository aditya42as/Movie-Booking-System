
import java.util.ArrayList;
import java.util.List;

class Booking {

    public enum Status {
        PENDING, CONFIRMED, CANCELLED, FAILED
    }

    private static int nextBookingId = 1001;

    private String bookingId;
    private Customer customer;
    private Show show;
    private List<ShowSeat> seats;
    private double totalAmount;
    private Status status;
    private String paymentTransactionId;

    public Booking(Customer customer, Show show, List<ShowSeat> seats, double totalAmount) {
        this.bookingId = "BK" + nextBookingId++;
        this.customer = customer;
        this.show = show;
        this.seats = new ArrayList<>(seats);
        this.totalAmount = totalAmount;
        this.status = Status.PENDING;
        this.paymentTransactionId = "-";
    }

    public String getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Show getShow() {
        return show;
    }

    public List<ShowSeat> getSeats() {
        return seats;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Status getStatus() {
        return status;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public void confirm() {
        status = Status.CONFIRMED;
    }

    public void cancel() {
        status = Status.CANCELLED;
    }

    public void fail() {
        status = Status.FAILED;
    }

    // Compile-time polymorphism: overloaded method.
    public void addSeat(ShowSeat seat) {
        seats.add(seat);
    }

    public void addSeat(ShowSeat seat, double extraAmount) {
        seats.add(seat);
        totalAmount += extraAmount;
    }
}
