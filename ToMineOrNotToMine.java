public class ToMineOrNotToMine {
    private boolean isMine;
    private int adjacency;
    private boolean isFlagged;
    private boolean isFound;

    /**
     * Makes a mine (or not a mine) object
     * This way the amount of adjacent mines are stored int TheOneTrueGrid gameGrid[][],
     * as well as whether the location is a mine or not
     */
    public ToMineOrNotToMine(boolean isMine, int adjacency, boolean isFlagged, boolean isFound) {
        this.isMine = isMine;
        this.adjacency = adjacency;
        this.isFlagged = isFlagged;
        this.isFound = isFound;
    }

    /**
     * getters and setters, yay~~!
     */
    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public int getAdjacency() {
        return adjacency;
    }

    public void setAdjacency(int adjacency) {
        this.adjacency = adjacency;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public boolean isFound() {
        return isFound;
    }

    public void setFound(boolean found) {
        isFound = found;
    }
}
