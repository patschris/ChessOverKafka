package structures;

public class GlobalStats {
    private int gamesPlayed;
    private int whiteWon;
    private int blackWon;
    private float avgMoves;

    public GlobalStats(int gamesPlayed, int whiteWon, int blackWon, float avgMoves) {
        this.gamesPlayed = gamesPlayed;
        this.whiteWon = whiteWon;
        this.blackWon = blackWon;
        this.avgMoves = avgMoves;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getWhiteWon() {
        return whiteWon;
    }

    public int getBlackWon() {
        return blackWon;
    }

    public float getAvgMoves() {
        return avgMoves;
    }
}
