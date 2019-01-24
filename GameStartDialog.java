
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameStartDialog implements ActionListener{
    private JDialog startDialog; //dialog for game options
    private JPanel themePanel; //panel to select theme
    private JRadioButton classicTheme;
    private JRadioButton catTheme;
    private ButtonGroup themeGroup; //for radiobuttons
    private JComboBox sizeList; //combobox for boardsize
    private JButton start; //to start game
    private JButton quit; //to quit (frame.EXIT_ON_CLOSE in main does not work, so this is replacement)
    private int theseRows; //rows
    private int theseColumns; //columns
    private int theseMines; //amount of mines

    private ImageIcon cat = new ImageIcon("cat.png");
    private ImageIcon mine = new ImageIcon("bomb.png");
    private ImageIcon paw = new ImageIcon("paw.png");
    private ImageIcon flag = new ImageIcon("flag.png");
    private ImageIcon flagToUse;
    private ImageIcon mineToUse;

    public GameStartDialog() {

        //simply making start button
        start = new JButton("Start Game!");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == start) {
                    //if user doesn't select anything (uses default) ActionListener doesn't fire
                    //therefore these would be null
                    //so this is basically making default settings
                    if (theseRows == 0)
                        sizeList.setSelectedItem("9x9");
                    GUIOnionSkin gui = new GUIOnionSkin();
                    if (mineToUse == null) {
                        mineToUse = mine;
                        flagToUse = flag;
                    }
                    //setting settings for gui
                    gui.setMine(mineToUse);
                    gui.setFlag(flagToUse);
                    gui.setRows(theseRows);
                    gui.setColumns(theseColumns);
                    gui.resetBoard();
                    startDialog.dispose(); //getting rid of dialog
                    gui.setVisible(true);
                    gui.toFront(); //for some reason there was a problem, this fixed.
                }
            }
        });

        //button to exit
        quit = new JButton("Quit Game");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //comboBox creation
        String[] boardSizes = {"9x9", "16x16", "24x24"};
        sizeList = new JComboBox(boardSizes);
        sizeList.setSelectedIndex(0); //starts with 9x9 selected
        sizeList.addActionListener(this);

        //making RadioButtons
        classicTheme = new JRadioButton("Classic Theme"); //regular bombs and flags
        classicTheme.addActionListener(new RadioListener());
        classicTheme.setSelected(true);
        catTheme = new JRadioButton("Cat Theme"); //cats and paws~~~!
        catTheme.addActionListener(new RadioListener());

        //ButtonGroup keeps one selected at a time/at all times
        themeGroup = new ButtonGroup();
        themeGroup.add(classicTheme);
        themeGroup.add(catTheme);

        //panel to put buttons on
        themePanel = new JPanel();
        themePanel.setLayout(new FlowLayout());

        themePanel.add(classicTheme);

        //shows pics so user know what icons will look like
        themePanel.add(new JLabel(flag));
        themePanel.add(new JLabel(mine));

        themePanel.add(catTheme);

        //cat and paw pics added
        themePanel.add(new JLabel(paw));
        themePanel.add(new JLabel(cat));

    }

    /**
     * creates dialog
     * @param parent the root/parent frame
     */
    public void makeDialog(JFrame parent) {
        startDialog = new JDialog(parent, true); //modal so user can't press buttons while it's open

        startDialog.setLayout(new GridBagLayout()); //yay! GridBagLayout! fun!
        GridBagConstraints constraints = new GridBagConstraints();

        //RadioButtons for themes
        constraints.weightx = 0.5;
        constraints.weighty = 1; //has pics so I added weight so it can take up more space
        constraints.gridx = 0;
        constraints.gridy = 0;
        startDialog.add(themePanel, constraints);

        //ComboBox is boring so I tried to keep it small
        constraints.weightx = 0.5;
        constraints.weighty = 0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(5,30, 0, 30);
        constraints.fill = GridBagConstraints.HORIZONTAL;
//        sizeList.setMaximumSize(new Dimension(startDialog.getWidth(), 50));
        startDialog.add(sizeList, constraints);

        //Start button
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.insets = new Insets(5,5,0,5); //insets look nice
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL; //fills up the rest of the horizontal space
        constraints.gridy = 2;
        startDialog.add(start, constraints);

        //quit button
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.insets = new Insets(5,5,5,5);
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridy = 3;
        startDialog.add(quit, constraints);

        startDialog.setMinimumSize(new Dimension(500, 200));

        //cannot EXIT_ON_CLOSE and disposing doesn't end program properly
        startDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        startDialog.setVisible(true);
        startDialog.pack();
    }

    /**
     * ActionListeners for RadioButtons
     */
    public class RadioListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == classicTheme) {
                mineToUse = mine;
                flagToUse = flag;
            } else if (e.getSource() == catTheme) {
                mineToUse = cat;
                flagToUse = paw;
            }
        }
    }

    /**
     * ActionListener for ComboBox
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox comboBox = (JComboBox) e.getSource(); /** THIS is the reason I made so many ActionListeners, kept throwing errors */
        if (comboBox.getSelectedItem().equals("9x9")) {
            theseRows = 9;
            theseColumns = 9;
            theseMines = 10;
        } else if (comboBox.getSelectedItem().equals("16x16")) {
            theseRows = 16;
            theseColumns = 16;
            theseMines = 40;
        } else {
            theseRows = 24;
            theseColumns = 24;
            theseMines = 99;
        }

    }
}
