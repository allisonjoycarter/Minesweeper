
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GUIOnionSkin extends JFrame{
    private int rows, maxRows; //amount of rows and columns, equal to amount in TheOneTrueGrid
    private int columns, maxColumns;
    private int countedMines, mineCountLeft; //amount of mines assigned, mineCountLeft used for the Mine Counter
    private int minutes, seconds; //minutes and seconds for timer
    private boolean timerIsPaused; //to stop/start timer

    private JPanel mainPanel, gamePanel; //panels to put in JFrame
    private ActionListener buttonListener;
    private MouseListener mouseListener;
    private JButton[][] buttons;
    private JButton restart, exit, pauseTimer;
    private JLabel viewTimer; //JLabel for timer
    private JLabel minesLeft; //JLabel for counting mines
    private ImageIcon mine, flag;
    private int mines; //to set mines for TheOneTrueGrid
    private TheOneTrueGrid theOneTrueGrid;
    private ToMineOrNotToMine[][] mineGrid;
    private boolean[][] check; //for flood fill
    private Timer timer; //to keep track of time

    public GUIOnionSkin() {
        theOneTrueGrid = new TheOneTrueGrid();
        mineGrid = theOneTrueGrid.getGameGrid();

        //keeps rows and columns equal to theOneTrueGrid which carries all data for squares/mines/possibleMines
        rows = theOneTrueGrid.getRows();
        columns = theOneTrueGrid.getColumns();

        //for use in flood fill (showEmptyArea) so if rows/columns is changed, so is variable in showEmptyArea
        maxRows = rows;
        maxColumns = columns;

        countedMines = 0; //keeps track of how many mines are left vs how many have been flagged
        mines = theOneTrueGrid.getMines();
        mineCountLeft = mines;

        minutes = 0;
        seconds = 0;
        int delay = 1000; //milliseconds
        timerIsPaused = false; //allows timer to start
        ActionListener taskPerformer = e -> {
                //updates viewTimer every second
                if (!timerIsPaused) {
                    if (seconds == 60) { //starts minutes
                        minutes++;
                        seconds = 0;
                    }
                    if (seconds < 9) { //makes timer look pretty (0:00 format)
                        seconds++;
                        viewTimer.setText(minutes + ":0" + seconds);
                    } else {
                        seconds++;
                        viewTimer.setText(minutes + ":" + seconds);
                    }
                }
            };
//        timer.setRepeats(false);
        timer = new Timer(delay, taskPerformer);

        makingTheGrid(); //makes gamePanel
        outerLayer(); //makes mainPanel
    }

    /** makes the mainPanel, which contains gamePanel */
    public void outerLayer() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(550, 500));
        mainPanel.add(gamePanel, BorderLayout.CENTER);

        restart = new JButton("Restart");
        restart.addActionListener(new ButtonListener());
        exit = new JButton("Exit"); //quits game
        exit.addActionListener(new ButtonListener());
        pauseTimer = new JButton("Pause Timer");
        pauseTimer.addActionListener(e -> { //pauses the timer if the the timer is not already paused, else starts it
            if (!timerIsPaused) {
                timer.stop();
                timerIsPaused = true;
            } else {
                timer.start();
                timerIsPaused = false;
            }
        });
        viewTimer = new JLabel("0:00"); //gives that good minutes:seconds format
        viewTimer.setFont(new Font("Serif", Font.TRUETYPE_FONT, 20)); //bigger, better font
        viewTimer.setHorizontalAlignment(SwingConstants.CENTER);
        viewTimer.setBorder(BorderFactory.createLineBorder(Color.black));
//        viewTimer.setBackground(Color.);
        minesLeft = new JLabel("Mines Left: " + mineCountLeft);
        minesLeft.setFont(new Font("Serif", Font.TRUETYPE_FONT, 20));
        minesLeft.setHorizontalAlignment(SwingConstants.CENTER);
        minesLeft.setBorder(BorderFactory.createLineBorder(Color.black));

        //making a separate panel so buttons line across the top
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));
        buttonPanel.add(restart);
        buttonPanel.add(exit);
        buttonPanel.add(pauseTimer);
        buttonPanel.add(viewTimer);
        buttonPanel.add(minesLeft);
        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        //adding everything to frame
        getContentPane().add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(750,700));
        pack();
        setVisible(true);
        setResizable(true);
    }

    /** makes the gamePanel grid of button */
    public void makingTheGrid() {
        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(rows, columns));


        //adds buttons to layout
        buttons = new JButton[rows][columns];
        buttonListener = new ButtonListener();
        mouseListener = new MListener();

        for (rows = 0; rows < buttons.length; rows++) {
            for (columns = 0; columns < buttons.length; columns++) {
                buttons[rows][columns] = new JButton();
                gamePanel.add(buttons[rows][columns]);
                buttons[rows][columns].addActionListener(buttonListener);
                buttons[rows][columns].addMouseListener(mouseListener);
            }
        }
    }

    /**
     * turns on/off isFlagged in each ToMineOrNotToMine object
     * makes button not work if location in mineGrid is flagged
     */
    public void toggleFlag() {
        if (!mineGrid[rows][columns].isFlagged()) {
            buttons[rows][columns].setIcon(flag);
            buttons[rows][columns].removeActionListener(buttonListener);
            countedMines++;
            mineGrid[rows][columns].setFlagged(true);
        } else {
            buttons[rows][columns].setIcon(null);
            buttons[rows][columns].addActionListener(buttonListener);
            countedMines--;
            mineGrid[rows][columns].setFlagged(false);
            if (mineGrid[rows][columns].isMine()) mineGrid[rows][columns].setFound(false);
        }
        if (mineGrid[rows][columns].isFlagged() && mineGrid[rows][columns].isMine()) {
            mineGrid[rows][columns].setFound(true);
        }
        mineCountLeft = mines - countedMines;
        updateCounter();
    }

    public void checkForWinState() {
        int countFound = 0;
        int mineAmount = 0;
        for (rows = 0; rows < mineGrid.length; rows++) {
            for (columns = 0; columns < mineGrid.length; columns++) {
                if (mineGrid[rows][columns].isFound()) countFound++;
                if (mineGrid[rows][columns].isMine()) mineAmount++;
            }
        }
        //win if the count of found mines is equal to the amount of mines
        //also making sure that the amount of flags is not more than the amount of found mines
        //otherwise you could win by flagging the whole board
        if (countFound == mineAmount &&
                !(countedMines > countFound)) {
            JOptionPane.showMessageDialog(gamePanel, "You Won! Congratulations!",
                    "Win", JOptionPane.DEFAULT_OPTION);

            //reveals all squares
            for (rows = 0; rows < buttons.length; rows++) {
                for (columns = 0; columns < buttons.length; columns++) {
                    if (mineGrid[rows][columns].isMine()) {
                        buttons[rows][columns].setIcon(mine);
                        buttons[rows][columns].invalidate();
                    } else if (mineGrid[rows][columns].getAdjacency() != 0){
                        buttons[rows][columns].setText(mineGrid[rows][columns].getAdjacency() + "");
                        buttons[rows][columns].setBackground(Color.lightGray);
                        buttons[rows][columns].invalidate();
                    } else {
                        buttons[rows][columns].setBackground(Color.lightGray);
                        buttons[rows][columns].invalidate();
                    }
                }
            }
        }
        timer.stop(); //also this
    }

    /**
     * flood fill algorithm that shows an empty area
     * @param row  = current row
     * @param column  = current column
     * @param check used to prevent stackoverflow
     */
    public void showEmptyArea(int row, int column, boolean[][] check) {
        //prevents outofbounds error
        if (row >= 0 && row < maxRows &&
                column >= 0 && column < maxColumns) {

            //stops if square has already been checked or is flagged
            if (check[row][column]) return;
            if (mineGrid[row][column].isFlagged()) return;

            //fills if square is not a mine xor the amount of adjacent mines = 0
            //xor prevents filling entire board except for mines
            if (!mineGrid[row][column].isMine() ^
                    mineGrid[row][column].getAdjacency() > 0) {
                revealSquare(row,column);
                check[row][column] = true; //once square has been revealed, marks it checked

                //recursively fills in all cardinal directions
                showEmptyArea(row - 1, column, check);
                showEmptyArea(row - 1, column + 1, check);
                showEmptyArea(row, column + 1, check);
                showEmptyArea(row + 1, column + 1, check);
                showEmptyArea(row + 1, column, check);
                showEmptyArea(row + 1, column - 1, check);
                showEmptyArea(row, column - 1, check);
                showEmptyArea(row - 1, column - 1, check);
            }
            //this shows the number of adjacent mines around empty area
            if (mineGrid[row][column].getAdjacency() > 0) {
                buttons[row][column].setText(mineGrid[row][column].getAdjacency() + "");
                buttons[row][column].setBackground(Color.lightGray);
            }
        }
        return;
    }

    /**
     * Reveals all mines and adjacency numbers
     * Also sets background of adjacent squares to light gray and invalidates all buttons
     * Shows a "You Lost" message dialog
     */
    public void failState() {
        for (rows = 0; rows < buttons.length; rows++) {
            for (columns = 0; columns < buttons.length; columns++) {

                //if the mine is flagged incorrectly
                if (mineGrid[rows][columns].isFlagged() && !mineGrid[rows][columns].isFound()){
                    buttons[rows][columns].setBackground(Color.lightGray);
//                    buttons[rows][columns].setText(mineGrid[rows][columns].getAdjacency() + "");
                    buttons[rows][columns].invalidate();

                    //if the mine is flagged correctly
                } else if (mineGrid[rows][columns].isFound()) {
                    buttons[rows][columns].setBackground(Color.GREEN);
                    buttons[rows][columns].setIcon(mine);
                    buttons[rows][columns].invalidate();
                } else if (mineGrid[rows][columns].isMine()) {
                    buttons[rows][columns].setIcon(mine);
                    buttons[rows][columns].invalidate();
                } else if (mineGrid[rows][columns].getAdjacency() != 0){
                    buttons[rows][columns].setText(mineGrid[rows][columns].getAdjacency() + "");
                    buttons[rows][columns].setBackground(Color.lightGray);
                    buttons[rows][columns].invalidate();
                } else {
                    buttons[rows][columns].setBackground(Color.lightGray);
                    buttons[rows][columns].invalidate();
                }
            }
        }
        timer.stop();
        JOptionPane.showMessageDialog(gamePanel, "You Lost!",
                "Fail", JOptionPane.OK_OPTION);
    }

    /** Recreates the mineGrid and resets the buttons to their original state */
    public void resetBoard() {
        //remaking the mineGrid
        TheOneTrueGrid newGrid = new TheOneTrueGrid();
        newGrid.setRows(rows);
        newGrid.setColumns(columns);
        maxRows = rows;
        maxColumns = columns;

        mineGrid = newGrid.makeGameGrid(rows, columns);

        mainPanel.remove(gamePanel);
        //resetting buttons
        gamePanel.removeAll();
        buttons = new JButton[rows][columns];
        gamePanel.setLayout(new GridLayout(rows, columns));
        for (rows = 0; rows < buttons.length; rows++) {
            for (columns = 0; columns < buttons.length; columns++) {
                buttons[rows][columns] = new JButton();
                gamePanel.add(buttons[rows][columns]);
                buttons[rows][columns].addActionListener(buttonListener);
                buttons[rows][columns].addMouseListener(mouseListener);

            }
        }
        mainPanel.add(gamePanel); //readding panel

        countedMines = 0;
        mines = newGrid.getMines(); //setting the mine amount
        mineCountLeft = mines;
        updateCounter();
        seconds = 0; //resetting timer
        viewTimer.setText("0:00");
        timer.start();
    }

    //simply shows a square has been visited
    public void revealSquare(int row, int column) {
        buttons[row][column].setBackground(Color.lightGray);
        buttons[row][column].invalidate();
        buttons[row][column].removeMouseListener(mouseListener);
    }

    /** for right click (flagging) */
    public class MListener implements MouseListener {
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {
            for (rows = 0; rows < buttons.length; rows++) {
                for (columns = 0; columns < buttons.length; columns++) {
                    if (e.getSource() == buttons[rows][columns] &&
                            SwingUtilities.isRightMouseButton(e) && //right mouse button!
                            buttons[rows][columns].getBackground() != Color.lightGray) { //prevents flagging revealed mines
                        toggleFlag();
                        checkForWinState();
                        mineCountLeft = mines - countedMines;
                    }

                }
            }
        }
        @Override
        public void mouseClicked(MouseEvent e) {}
    }

    JFrame frame = this; //for reopening game settings dialog
    public class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            check = new boolean[rows][columns]; //this is for the fill algorithm in showEmptyArea

            if (e.getSource() == restart) {
                timer.stop();
                int n = JOptionPane.showConfirmDialog(frame,
                        "Would you like to keep your current game settings?",
                        "Confirm Settings",
                        JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION){
                    for (rows = 0; rows < buttons.length; rows++) {
                        for (columns = 0; columns < buttons.length; columns++) {
                            buttons[rows][columns].removeMouseListener(mouseListener); //fixes a bug where mouseListener doesn't work
                            buttons[rows][columns].addMouseListener(mouseListener); //reactivates mouseListener for all buttons
                            buttons[rows][columns].validate(); //all buttons were invalidated earlier
                            buttons[rows][columns].setBackground(null); //removes gray background on revealed buttons
                            buttons[rows][columns].setIcon(null); //self explanatory
                        }
                    }
                    resetBoard();
                } else if (n == JOptionPane.NO_OPTION) {
                    GameStartDialog startDialog = new GameStartDialog();
                    startDialog.makeDialog(frame); //opens dialog
                    dispose(); //gets rid of old game
                }
            } else if (e.getSource() == exit) {
                System.exit(0);
            }

            for (rows = 0; rows < buttons.length; rows++) {
                for (columns = 0; columns < buttons.length; columns++) {
                    check[rows][columns] = false;
                    if (e.getSource() == buttons[rows][columns]) {
                        if (timerIsPaused) {
                            timerIsPaused = false;
                            timer.start();
                        }

                        //reveals all squares if a mine is pressed
                        if (mineGrid[rows][columns].isMine()) {
                            failState();
                        } else {
                            revealSquare(rows,columns);

                            //shows number of adjacent mines
                            if (mineGrid[rows][columns].getAdjacency() != 0) {
                                buttons[rows][columns].setText(mineGrid[rows][columns].getAdjacency() + "");
                            } else { //if there are no adjacent mines, will reveal an empty area
                                showEmptyArea(rows, columns, check); //flood fill
                            }
                        }
                    }
                }
            }
        }
    }

    public void setMine(ImageIcon mine) {
        this.mine = mine;
    }

    public void setFlag(ImageIcon flag) {
        this.flag = flag;
    }

    public void updateCounter() {
        minesLeft.setText("Mines Left: " + mineCountLeft);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }
}
