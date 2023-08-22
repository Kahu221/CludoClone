package hobby_detectives.gui.views;
import hobby_detectives.gui.controller.GameController;
import hobby_detectives.game.GameModel;
import hobby_detectives.gui.panels.MapPanelView;
import hobby_detectives.gui.panels.StatusPanelView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GameView extends JFrame implements PropertyChangeListener {
    public final int GAME_FRAME_WIDTH = 1200;
    public final int GAME_FRAME_HEIGHT = 800;

    private final GameController controller;
    private final GameModel model;
    
    public final StatusPanelView statusPanel;
    public final MapPanelView mapView;

    private final JButton acknowledgePresence;

    public GameView(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;
        statusPanel = new StatusPanelView(model);
        mapView = new MapPanelView(model);
        this.model.addPropertyChangeListener(this);

        acknowledgePresence = new JButton("Please pass the tablet to " + this.model.getCurrentPlayer().getCharacter().toString());
        acknowledgePresence.addActionListener(onClick -> {
            this.controller.confirmPlayerChange();
        });

        var gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
        gridBagLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
        this.setLayout(gridBagLayout);

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.promptForGameExit();
            }
        });
        this.setSize(GAME_FRAME_WIDTH, GAME_FRAME_HEIGHT);

        this.setLocationRelativeTo(null); // centers the window in the center of the screen
        this.setVisible(true);
    }

    public void propertyChange(PropertyChangeEvent event) {
        String propName = event.getPropertyName();
        switch (propName){
            case "waitingForPlayer" -> {
                if ((Boolean) event.getNewValue()) {
                    this.remove(this.mapView);
                    this.remove(this.statusPanel);

                    // show the "please pass to x" screen
                    this.add(acknowledgePresence);
                    this.revalidate();
                    this.repaint();
                } else {
                    this.remove(this.acknowledgePresence);
                    addGridComponent(statusPanel, 0, 0, 1, 4);
                    addGridComponent(mapView, 1, 0, 3, 4);
                    this.revalidate();
                    this.repaint();
                }
            }

        }
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
