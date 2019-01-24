import java.util.Random;

public class TheOneTrueGrid {
    private int rows;
    private int columns;
    private int mines; //how many mines the user wants
    private int adjMines; //amount of mines adjacent to square
    private ToMineOrNotToMine[][] gameGrid;

    /**
     * Makes the grid that holds information from ToMineOrNotToMine
     * Randomly assigns mines
     * Checks sides for adjacent mines and changes adjMines accordingly
     */
    public TheOneTrueGrid() {
        //amount of rows/columns, should be changed if buttons[][] from GUI is changed
        //default rows/columns
        rows = 9;
        columns = 9;

        mines = 10; //amount of mines
        resetMines(); //accounts for rows or columns > 10
        adjMines = 0; //default for the amount of adjacent mines

        gameGrid = makeGameGrid(rows, columns);

    }

    /**
     * actually creates the grid of ToMineOrNotToMine
     * @param row amount of rows
     * @param column amount of columns
     * @return the grid
     */
    public ToMineOrNotToMine[][] makeGameGrid(int row, int column) {
        gameGrid = new ToMineOrNotToMine[row][column]; //makes the grid of mine objects

        //adding mines (or not mines) to gameGrid
        for (row = 0; row < gameGrid.length; row++) {
            for (column = 0; column < gameGrid.length; column++) {

                //default settings for mines
                ToMineOrNotToMine possibleMine = new ToMineOrNotToMine(false, adjMines, false, false);

                //adding to array
                gameGrid[row][column] = possibleMine;
            }
        }

        //assigns mines randomly
        resetMines();
        randomAssignment();

        //once mines are assigned, program counts the amount of mines around each location
        countingAdjacency();

        return gameGrid;
    }

    /**
     * Changes mine amounts in case user selects more rows&columns
     */
    public void resetMines() {
        if (rows == 16) mines = 40;
        if (rows == 24) mines = 99;
    }

    /**
     * Randomly assigns mines to locations
     * Pretty self explanatory
     */
    public void randomAssignment() {
        Random random = new Random(); //needed a new random
        for(int i = 0; i < mines; i++) {
            //randomly picks a row and column
            int randomRow = random.nextInt(rows);
            int randomColumn = random.nextInt(columns);

            ToMineOrNotToMine tempMine = gameGrid[randomRow][randomColumn]; //variable for the randomly selected mine

            //checks to see if location is already a mine
            if (!tempMine.isMine()) {
                tempMine.setMine(true);
                gameGrid[randomRow][randomColumn] = tempMine;
            } else {
                //goes back if location is already a mine, so you always end up with 'mines' amount of mines
                i--;
            }
        }
    }

    /**
     * Counts adjacent mines using helper methods for easy understanding
     * Checks for corners, then checks for sides, then executes for middle squares
     * Executes for all items in gameGrid, unless the item is a mine
     * If it is a mine, the adjacent mine count is the default 0 mines
     */
    public void countingAdjacency() {
        //these allow for checking sides and corners along the bottom and right side
        //subtract 1 to prevent throwing an out of bounds error
        //the array indexes are from 0-9 (for total of 10 rows/columns), so 9 is the furthest row/column
        //made a new variable so rows/columns could be changed and this will still work without needing change
        int maxRows = rows - 1;
        int maxColumns = columns - 1;

        for (rows = 0; rows < gameGrid.length; rows++) {
            for (columns = 0; columns < gameGrid.length; columns++) {
                //checks to see if location is a mine
                if(gameGrid[rows][columns].isMine()){

                }
                else {
                    //checks for corners and then sides, in clockwise order
                    if (rows == 0 && columns == 0) { //top left corner
                        if (east(rows, columns)) adjMines++;
                        if (southeast(rows, columns)) adjMines++;
                        if (south(rows, columns)) adjMines++;


                    } else if (columns == maxColumns && rows == 0) { //top right corner
                        if (west(rows, columns)) adjMines++;
                        if (southwest(rows, columns)) adjMines++;
                        if (south(rows, columns)) adjMines++;


                    } else if (columns == maxColumns && rows == maxRows) { //bottom right corner
                        if (north(rows, columns)) adjMines++;
                        if (northwest(rows, columns)) adjMines++;
                        if (west(rows, columns)) adjMines++;


                    } else if (columns == 0 && rows == maxRows) { //bottom left corner
                        if (north(rows, columns)) adjMines++;
                        if (northeast(rows, columns)) adjMines++;
                        if (east(rows, columns)) adjMines++;


                    } else if (columns == 0) { //left side
                        if (north(rows, columns)) adjMines++;
                        if (northeast(rows, columns)) adjMines++;
                        if (east(rows, columns)) adjMines++;
                        if (southeast(rows, columns)) adjMines++;
                        if (south(rows, columns)) adjMines++;


                    } else if (rows == 0) { //top side
                        if (west(rows, columns)) adjMines++;
                        if (southwest(rows, columns)) adjMines++;
                        if (south(rows, columns)) adjMines++;
                        if (southeast(rows, columns)) adjMines++;
                        if (east(rows, columns)) adjMines++;


                    } else if (columns == maxColumns) { //right side
                        if (south(rows, columns)) adjMines++;
                        if (southwest(rows, columns)) adjMines++;
                        if (west(rows, columns)) adjMines++;
                        if (northwest(rows, columns)) adjMines++;
                        if (north(rows, columns)) adjMines++;


                    } else if (rows == maxRows) { //bottom side
                        if (west(rows, columns)) adjMines++;
                        if (northwest(rows, columns)) adjMines++;
                        if (north(rows, columns)) adjMines++;
                        if (northeast(rows, columns)) adjMines++;
                        if (east(rows, columns)) adjMines++;


                    } else { //middle
                        if (north(rows, columns)) adjMines++;
                        if (northeast(rows, columns)) adjMines++;
                        if (east(rows, columns)) adjMines++;
                        if (southeast(rows, columns)) adjMines++;
                        if (south(rows, columns)) adjMines++;
                        if (southwest(rows, columns)) adjMines++;
                        if (west(rows, columns)) adjMines++;
                        if (northwest(rows, columns)) adjMines++;
                    }
                    gameGrid[rows][columns].setAdjacency(adjMines);
                    adjMines = 0;
                }
            }
        }
    }

    /**
     * Simple helper methods to make directions easier to understand
     * Prevents the use of [rows +- 1][columns +- 1] for every location in every direction
     * @return boolean for mine in each respective direction
     */
    private boolean north(int row, int column){
        return gameGrid[row - 1][column].isMine();
    }

    private boolean northeast(int row, int column){
        return gameGrid[row - 1][column + 1].isMine();
    }

    private boolean east(int row, int column){
        return gameGrid[row][column + 1].isMine();
    }

    private boolean southeast(int row, int column){
        return gameGrid[row + 1][column + 1].isMine();
    }

    private boolean south(int row, int column){
        return gameGrid[row + 1][column].isMine();
    }

    private boolean southwest(int row, int column){
        return gameGrid[row + 1][column - 1].isMine();
    }

    private boolean west(int row, int column){
        return gameGrid[row][column - 1].isMine();
    }

    private boolean northwest(int row, int column){
        return gameGrid[row - 1][column - 1].isMine();
    }

    //getters and setters
    public ToMineOrNotToMine[][] getGameGrid() {
        return gameGrid;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getMines() {
        return mines;
    }
}
