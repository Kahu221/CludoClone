package hobby_detectives.gui;
import hobby_detectives.gui.status.StatusPanel;

import javax.swing.*;

public class GameFrame extends JFrame {

    public void start() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1000,800);

        this.getContentPane().add(new StatusPanel());

        this.setLocationRelativeTo(null); // centers the window in the center of the screen
        this.setVisible(true);
    }
}
