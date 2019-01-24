import javax.swing.*;

public class main {


    public static void main(String[] args) {
        JFrame frame = new JFrame(); //simply to root dialog on
        GameStartDialog gameStartDialog = new GameStartDialog();
        gameStartDialog.makeDialog(frame); //makes the dialog
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(false);

    }
}
