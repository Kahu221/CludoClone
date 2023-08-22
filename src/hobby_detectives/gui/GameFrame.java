package hobby_detectives.gui;
import hobby_detectives.board.Board;
import hobby_detectives.game.Game;
import hobby_detectives.gui.panels.MapPanel;
import hobby_detectives.gui.panels.StatusPanel;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public final int GAME_FRAME_WIDTH = 1200;
    public final int GAME_FRAME_HEIGHT = 800;

    public final StatusPanel statusPanel = new StatusPanel();
    public final MapPanel mapView = new MapPanel();

    public void start() {
        var gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
        gridBagLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
        this.setLayout(gridBagLayout);

        addGridComponent(statusPanel, 0, 0, 1, 4);
        addGridComponent(mapView, 1, 0, 3, 4);

        this.setBackground(Color.GREEN);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(GAME_FRAME_WIDTH, GAME_FRAME_HEIGHT);

        this.setLocationRelativeTo(null); // centers the window in the center of the screen
        this.setVisible(true);

        statusPanel.setUp();

    }

    private void addGridComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight) {
        var constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(component, constraints);
    }
}
