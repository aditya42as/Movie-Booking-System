
class ShowSeat {

    public enum Status {
        AVAILABLE, BOOKED
    }

    private Seat seat;
    private Status status;

    public ShowSeat(Seat seat) {
        this.seat = seat;
        this.status = Status.AVAILABLE;
    }

    public Seat getSeat() {
        return seat;
    }

    public Status getStatus() {
        return status;
    }

    // Encapsulation: status can only be changed through validation methods.
    public boolean bookSeat() {
        if (status == Status.BOOKED) {
            return false;
        }
        status = Status.BOOKED;
        return true;
    }

    public void cancelSeat() {
        status = Status.AVAILABLE;
    }

    public boolean isAvailable() {
        return status == Status.AVAILABLE;
    }
}
