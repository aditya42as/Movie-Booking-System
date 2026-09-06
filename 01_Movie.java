class Movie {
    private String title;
    private String language;
    private int durationMinutes;

    public Movie(String title, String language, int durationMinutes) {
        this.title = title;
        this.language = language;
        this.durationMinutes = durationMinutes;
    }

    public Movie(Movie other) {
        this(other.title, other.language, other.durationMinutes);
    }

    public String getTitle() { return title; }
    public String getLanguage() { return language; }
    public int getDurationMinutes() { return durationMinutes; }

    @Override
    public String toString() {
        return title + " | " + language + " | " + durationMinutes + " min";
    }
}
