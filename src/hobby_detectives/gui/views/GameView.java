package hobby_detectives.gui.views;
import hobby_detectives.gui.models.StatusPanelModel;
import hobby_detectives.gui.panels.MapPanelView;
import hobby_detectives.gui.panels.StatusPanelView;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {
    public final int GAME_FRAME_WIDTH = 1200;
    public final int GAME_FRAME_HEIGHT = 800;

    public final StatusPanelView statusPanel;
    public final MapPanelView mapView = new MapPanelView();

    public GameView(StatusPanelModel statusPanelModel) {
        statusPanel = new StatusPanelView(statusPanelModel);
    }

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
