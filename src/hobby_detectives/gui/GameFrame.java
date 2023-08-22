package hobby_detectives.gui;
import hobby_detectives.board.Board;
import hobby_detectives.game.Game;
import hobby_detectives.gui.panels.MapPanel;
import hobby_detectives.gui.panels.StatusPanel;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {


    public void start() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1000,800);
        this.getContentPane().add(new StatusPanel(), BorderLayout.EAST);
        this.getContentPane().add(new MapPanel(new Game()), BorderLayout.CENTER);


        this.setLocationRelativeTo(null); // centers the window in the center of the screen
        this.setVisible(true);
    }
}
