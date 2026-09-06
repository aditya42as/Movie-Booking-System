
import java.util.ArrayList;
import java.util.List;

class Screen {

    private int screenNumber;
    private List<Seat> seats;

    public Screen(int screenNumber) {
        this.screenNumber = screenNumber;
        this.seats = new ArrayList<>();
        createDefaultSeats();
    }

    private void createDefaultSeats() {
        // Composition: Screen creates and owns its Seat objects.
        for (int i = 1; i <= 4; i++) {
            seats.add(new Seat("A" + i, Seat.SeatType.SILVER));
        }
        for (int i = 1; i <= 4; i++) {
            seats.add(new Seat("B" + i, Seat.SeatType.GOLD));
        }
        for (int i = 1; i <= 2; i++) {
            seats.add(new Seat("C" + i, Seat.SeatType.PLATINUM));
        }
    }

    public int getScreenNumber() {
        return screenNumber;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public Seat findSeat(String seatNumber) {
        for (Seat seat : seats) {
            if (seat.getNumber().equalsIgnoreCase(seatNumber)) {
                return seat;
            }
        }
        return null;
    }
}
