
class Seat {

    public enum SeatType {
        SILVER, GOLD, PLATINUM
    }

    private String number;
    private SeatType type;

    public Seat(String number, SeatType type) {
        this.number = number;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public SeatType getType() {
        return type;
    }

    @Override
    public String toString() {
        return number + " (" + type + ")";
    }
}
