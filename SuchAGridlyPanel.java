//package MinesweeperTakeTwo;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//
//public class SuchAGridlyPanel {
//    private int rows, maxRows;
//    private int columns, maxColumns;
//    private int countedMines, mineCountLeft;
//
//    private JPanel gamePanel;
//    private ActionListener buttonListener;
//    private MouseListener mouseListener;
//    private JButton[][] buttons;
//    private TheOneTrueGrid theOneTrueGrid;
//    private ToMineOrNotToMine[][] mineGrid;
//
//    /**
//     * This is the panel that the JButton grid is on for actual gameplay
//     */
//    public SuchAGridlyPanel() {
//        theOneTrueGrid = new TheOneTrueGrid();
//        mineGrid = theOneTrueGrid.getGameGrid();
//
//        rows = theOneTrueGrid.getRows(); //amount of rows and columns, equal to amount in TheOneTrueGrid
//        columns = theOneTrueGrid.getColumns();
//
//        //for use in flood fill (showEmptyArea) so if rows/columns is changed, so is variable in showEmptyArea
//        maxRows = rows;
//        maxColumns = columns;
//
//        countedMines = 0; //keeps track of how many mines are left vs how many have been flagged
//        mineCountLeft = theOneTrueGrid.getMines();
//
//        gamePanel = new JPanel();
//        gamePanel.setLayout(new GridLayout(rows,columns));
//
//
//        //adds buttons to layout
//        buttons = new JButton[rows][columns];
//        buttonListener = new ButtonListener();
//        mouseListener = new MListener();
//
//        for (rows = 0; rows < buttons.length; rows++) {
//            for (columns = 0; columns < buttons.length; columns++) {
//                buttons[rows][columns] = new JButton("  ");
//                gamePanel.add(buttons[rows][columns]);
//                buttons[rows][columns].addActionListener(buttonListener);
//                buttons[rows][columns].addMouseListener(mouseListener);
//            }
//        }
//    }
//
//
//    /**
//     * turns on/off isFlagged in each ToMineOrNotToMine object
//     * makes button not work if location in mineGrid is flagged
//     */
//    public void toggleFlag() {
//        if (!mineGrid[rows][columns].isFlagged()) {
//            buttons[rows][columns].setText("F");
//            buttons[rows][columns].removeActionListener(buttonListener);
//            countedMines++;
//            mineGrid[rows][columns].setFlagged(true);
//        } else {
//            buttons[rows][columns].setText("");
//            buttons[rows][columns].addActionListener(buttonListener);
//            countedMines--;
//            mineGrid[rows][columns].setFlagged(false);
//            if (mineGrid[rows][columns].isMine()) mineGrid[rows][columns].setFound(false);
//        }
//        if (mineGrid[rows][columns].isFlagged() && mineGrid[rows][columns].isMine()) {
//            mineGrid[rows][columns].setFound(true);
//        }
//        mineCountLeft = theOneTrueGrid.getMines() - countedMines;
//    }
//    public void checkForWinState() {
//        int countFound = 0;
//        for (rows = 0; rows < mineGrid.length; rows++) {
//            for (columns = 0; columns < mineGrid.length; columns++) {
//                if (mineGrid[rows][columns].isFound()) countFound++;
//            }
//        }
//        if (countFound == theOneTrueGrid.getMines()) {
//            JOptionPane.showMessageDialog(gamePanel, "You Won! Congratulations!",
//                    "Win", JOptionPane.DEFAULT_OPTION);
//            for (rows = 0; rows < buttons.length; rows++) {
//                for (columns = 0; columns < buttons.length; columns++) {
//                    if (mineGrid[rows][columns].isMine()) {
//                        buttons[rows][columns].setText("Mine!");
//                        buttons[rows][columns].invalidate();
//                    } else if (mineGrid[rows][columns].getAdjacency() != 0){
//                        buttons[rows][columns].setText(mineGrid[rows][columns].getAdjacency() + "");
//                        buttons[rows][columns].setBackground(Color.lightGray);
//                        buttons[rows][columns].invalidate();
//                    } else {
//                        buttons[rows][columns].setBackground(Color.lightGray);
//                        buttons[rows][columns].invalidate();
//                    }
//                }
//            }
//        }
//
//    }
//
//    /**
//     * flood fill algorithm that shows an empty area
//     * @param row  = current row
//     * @param column  = current column
//     * @param check used to prevent stackoverflow
//     */
//    public void showEmptyArea(int row, int column, boolean[][] check) {
//        //prevents outofbounds error
//        if (row >= 0 && row < maxRows &&
//                column >= 0 && column < maxColumns) {
//
//            //stops if square has already been checked or is flagged
//            if (check[row][column]) return;
//            if (mineGrid[row][column].isFlagged()) return;
//
//            //fills if square is not a mine xor the amount of adjacent mines = 0
//            //xor prevents filling entire board except for mines
//            if (!mineGrid[row][column].isMine() ^
//                    mineGrid[row][column].getAdjacency() > 0) {
//                revealSquare(row,column);
//                check[row][column] = true; //once square has been revealed, marks it checked
//
//                //recursively fills in all cardinal directions
//                showEmptyArea(row - 1, column, check);
//                showEmptyArea(row - 1, column + 1, check);
//                showEmptyArea(row, column + 1, check);
//                showEmptyArea(row + 1, column + 1, check);
//                showEmptyArea(row + 1, column, check);
//                showEmptyArea(row + 1, column - 1, check);
//                showEmptyArea(row, column - 1, check);
//                showEmptyArea(row - 1, column - 1, check);
//            }
//            //this shows the number of adjacent mines around empty area
//            if (mineGrid[row][column].getAdjacency() > 0) {
//                buttons[row][column].setText(mineGrid[row][column].getAdjacency() + "");
//                buttons[row][column].setBackground(Color.lightGray);
//            }
//        }
//        return;
//    }
//
//    /**
//     * Reveals all mines and adjacency numbers
//     * Also sets background of adjacent squares to light gray and invalidates all buttons
//     * Shows a "You Lost" message dialog
//     */
//    public void failState() {
//        for (rows = 0; rows < buttons.length; rows++) {
//            for (columns = 0; columns < buttons.length; columns++) {
//                if (mineGrid[rows][columns].isMine()) {
//                    buttons[rows][columns].setText("Mine!");
//                    buttons[rows][columns].invalidate();
//                } else if (mineGrid[rows][columns].getAdjacency() != 0){
//                    buttons[rows][columns].setText(mineGrid[rows][columns].getAdjacency() + "");
//                    buttons[rows][columns].setBackground(Color.lightGray);
//                    buttons[rows][columns].invalidate();
//                } else {
//                    buttons[rows][columns].setBackground(Color.lightGray);
//                    buttons[rows][columns].invalidate();
//                }
//            }
//        }
//        JOptionPane.showMessageDialog(gamePanel, "You Lost!",
//                                                "Fail", JOptionPane.OK_OPTION);
//    }
//
//    /**
//     * Recreates the mineGrid and resets the buttons to their original state
//     */
//    public void resetBoard() {
//        //remaking the mineGrid
//        TheOneTrueGrid newGrid = new TheOneTrueGrid();
//        mineGrid = newGrid.getGameGrid();
//
//        //resetting buttons
//        for (rows = 0; rows < buttons.length; rows++) {
//            for (columns = 0; columns < buttons.length; columns++) {
//                buttons[rows][columns].removeMouseListener(mouseListener); //fixes a bug where mouseListener doesn't work
//                buttons[rows][columns].addMouseListener(mouseListener); //reactivates mouseListener for all buttons
//                buttons[rows][columns].validate(); //all buttons were invalidated earlier
//                buttons[rows][columns].setBackground(null); //removes gray background on revealed buttons
//                buttons[rows][columns].setText(""); //self explanatory
//
//            }
//        }
//        countedMines = 0;
//        setMineCountLeft(theOneTrueGrid.getMines());
//    }
//
//    //simply shows a square has been visited
//    public void revealSquare(int row, int column) {
//        buttons[row][column].setBackground(Color.lightGray);
//        buttons[row][column].invalidate();
//        buttons[row][column].removeMouseListener(mouseListener);
//    }
//
//    public class MListener implements MouseListener {
//        @Override
//        public void mousePressed(MouseEvent e) {}
//        @Override
//        public void mouseEntered(MouseEvent e) {}
//        @Override
//        public void mouseExited(MouseEvent e) {}
//        @Override
//        public void mouseReleased(MouseEvent e) {
//            for (rows = 0; rows < buttons.length; rows++) {
//                for (columns = 0; columns < buttons.length; columns++) {
//                    if (e.getSource() == buttons[rows][columns] && SwingUtilities.isRightMouseButton(e)) {
//                        toggleFlag();
//                        checkForWinState();
//                        mineCountLeft = theOneTrueGrid.getMines() - countedMines;
//                    }
//
//                }
//            }
//        }
//        @Override
//        public void mouseClicked(MouseEvent e) {}
//    }
//
//    public class ButtonListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            boolean[][] check = new boolean[rows][columns]; //this is for the fill algorithm in showEmptyArea
//
//            for (rows = 0; rows < buttons.length; rows++) {
//                for (columns = 0; columns < buttons.length; columns++) {
//                    if (e.getSource() == buttons[rows][columns]) {
//                        //reveals all squares if a mine is pressed
//                        if (mineGrid[rows][columns].isMine()) {
//                            failState();
//                        } else {
//                            revealSquare(rows,columns);
//
//                            //shows number of adjacent mines
//                            if (mineGrid[rows][columns].getAdjacency() != 0) {
//                                buttons[rows][columns].setText(mineGrid[rows][columns].getAdjacency() + "");
//                            } else { //if there are no adjacent mines, will reveal an empty area
//                                showEmptyArea(rows, columns, check); //flood fill
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//    public JPanel getGamePanel() {
//        return gamePanel;
//    }
//
//    public void setGamePanel(JPanel gamePanel) {
//        this.gamePanel = gamePanel;
//    }
//
//    public int getRows() {
//        return rows;
//    }
//
//    public void setRows(int rows) {
//        this.rows = rows;
//    }
//
//    public int getMaxRows() {
//        return maxRows;
//    }
//
//    public void setMaxRows(int maxRows) {
//        this.maxRows = maxRows;
//    }
//
//    public int getColumns() {
//        return columns;
//    }
//
//    public void setColumns(int columns) {
//        this.columns = columns;
//    }
//
//    public int getMaxColumns() {
//        return maxColumns;
//    }
//
//    public void setMaxColumns(int maxColumns) {
//        this.maxColumns = maxColumns;
//    }
//
//    public JButton[][] getButtons() {
//        return buttons;
//    }
//
//    public void setButtons(JButton[][] buttons) {
//        this.buttons = buttons;
//    }
//
//    public ToMineOrNotToMine[][] getMineGrid() {
//        return mineGrid;
//    }
//
//    public void setMineGrid(ToMineOrNotToMine[][] mineGrid) {
//        this.mineGrid = mineGrid;
//    }
//
//    public int getMineCountLeft() {
//        return mineCountLeft;
//    }
//
//    public void setMineCountLeft(int mineCountLeft) {
//        this.mineCountLeft = mineCountLeft;
//    }
//}
