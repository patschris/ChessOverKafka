package structures;

public class PersonalStats {
    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private int draws;
    private int white;
    private int black;
    private float avgMoves;

    public PersonalStats(int gamesPlayed, int gamesWon, int gamesLost, int draws, int white, int black, float avgMoves) {
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.draws = draws;
        this.white = white;
        this.black = black;
        this.avgMoves = avgMoves;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getDraws() {
        return draws;
    }

    public int getWhite() {
        return white;
    }

    public int getBlack() {
        return black;
    }

    public float getAvgMoves() {
        return avgMoves;
    }
}