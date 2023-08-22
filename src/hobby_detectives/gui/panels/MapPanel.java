package hobby_detectives.gui.panels;

import hobby_detectives.game.Game;

import javax.swing.*;

public class MapPanel extends JPanel {
    //private final JLabel icons[][];
    private Game game;
    public MapPanel(Game game) {
        this.add(new JLabel("Map panel"));
        this.game = game;

    }
}
