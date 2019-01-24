//package MinesweeperTakeTwo;
//
//import java.util.Random;
//
//public class MineMap {
//    private int rows;
//    private int columns;
//    private int mines;
//    private boolean[][] isMine;
//    private Object[][] adjGrid;
//
//    public MineMap() {
//        rows = 10;
//        columns = 10;
//        mines = 10;
//        isMine = new boolean[rows][columns];
//        Random random = new Random();
//
//        //assigns mines randomly to isMine
//        for(int i = 0; i < mines; i++) {
//            int randomRow = random.nextInt(rows); //number for random row and column
//            int randomColumn = random.nextInt(columns);
//            boolean randomPlace = isMine[randomRow][randomColumn];
//            if (randomPlace == false) {
//                isMine[randomRow][randomColumn] = true;
//            } else {
//                i -= 1;
//            }
//        }
//        adjacencyGrid();
//
//    }
//
//    public void adjacencyGrid() {
//        adjGrid = new Object[rows][columns];
//
//        //checking all sides for a mine
//        int mineCount = 0;
//
//        for (rows = 0; rows < adjGrid.length; rows++) {
//            for (columns = 0; columns < adjGrid.length; columns++) {
//                if (rows > 0 && columns > 0 && rows < 10 && columns < 10) {
//                    if (isMine[rows + 1][columns] == true) {
//                        mineCount++;
//                    } else if (isMine[rows + 1][columns + 1] == true) {
//                        mineCount++;
//                    } else if (isMine[rows + 1][columns - 1] == true) {
//                        mineCount++;
//                    } else if (isMine[rows][columns + 1] == true) {
//                        mineCount++;
//                    } else if (isMine[rows][columns - 1] == true) {
//                        mineCount++;
//                    } else if (isMine[rows - 1][columns + 1] == true) {
//                        mineCount++;
//                    } else if (isMine[rows - 1][columns] == true) {
//                        mineCount++;
//                    } else if (isMine[rows - 1][columns - 1] == true) {
//                        mineCount++;
//                    }
//                } else if (rows == 0 && columns > 0 && rows < 10 && columns < 10)
//
//                if (isMine[rows][columns] == true) {
//                    adjGrid[rows][columns] = "*mine*";
//                }
//                adjGrid[rows][columns] = mineCount;
//                mineCount = 0;
//
//            }
//        }
//    }
//
//    public boolean[][] getIsMine() {
//        return isMine;
//    }
//
//    public Object[][] getAdjGrid() {
//        return adjGrid;
//    }
//}
