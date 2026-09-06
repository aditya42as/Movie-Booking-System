
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class Show {

    private Movie movie;
    private Screen screen;
    private LocalTime startTime;
    private List<ShowSeat> showSeats;

    public Show(Movie movie, Screen screen, LocalTime startTime) {
        this.movie = movie;       // Aggregation: existing Movie
        this.screen = screen;     // Aggregation: existing Screen
        this.startTime = startTime;
        this.showSeats = new ArrayList<>();

        // Composition: Show creates status objects for this screening.
        for (Seat seat : screen.getSeats()) {
            showSeats.add(new ShowSeat(seat));
        }
    }

    public Movie getMovie() {
        return movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public List<ShowSeat> getShowSeats() {
        return showSeats;
    }

    public ShowSeat findShowSeat(String seatNumber) {
        for (ShowSeat showSeat : showSeats) {
            if (showSeat.getSeat().getNumber().equalsIgnoreCase(seatNumber)) {
                return showSeat;
            }
        }
        return null;
    }

    public void displaySeats() {
        System.out.println("\n" + movie.getTitle() + " | Screen-"
                + screen.getScreenNumber() + " | "
                + startTime.format(DateTimeFormatter.ofPattern("hh:mm a")));
        System.out.println("([ ] = AVAILABLE, [X] = BOOKED)");
        System.out.print("SILVER   ");
        printRange("A");
        System.out.print("GOLD     ");
        printRange("B");
        System.out.print("PLATINUM ");
        printRange("C");
    }

    private void printRange(String row) {
        for (ShowSeat ss : showSeats) {
            if (ss.getSeat().getNumber().startsWith(row)) {
                System.out.print("[" + (ss.isAvailable() ? " " : "X") + "]"
                        + ss.getSeat().getNumber() + " ");
            }
        }
        System.out.println();
    }
}
