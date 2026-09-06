
import java.util.List;

class PriceCalculator {

    public static final double SILVER_PRICE = 150.0;
    public static final double GOLD_PRICE = 250.0;
    public static final double PLATINUM_PRICE = 400.0;

    public double calculateTotal(List<ShowSeat> seats) {
        double total = 0;
        for (ShowSeat showSeat : seats) {
            total += priceOf(showSeat.getSeat().getType());
        }
        return total;
    }

    // Compile-time polymorphism: overloaded pricing method.
    public double calculateTotal(ShowSeat seat) {
        return priceOf(seat.getSeat().getType());
    }

    private double priceOf(Seat.SeatType type) {
        switch (type) {
            case SILVER:
                return SILVER_PRICE;
            case GOLD:
                return GOLD_PRICE;
            case PLATINUM:
                return PLATINUM_PRICE;
            default:
                throw new IllegalArgumentException("Unknown seat type");
        }
    }
}
