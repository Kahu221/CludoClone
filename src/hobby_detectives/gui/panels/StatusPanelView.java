package hobby_detectives.gui.panels;

import hobby_detectives.game.GameModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

public class StatusPanelView extends JPanel implements PropertyChangeListener {
    private final JLabel currentPlayer = new JLabel("Loading");
    private final JLabel currentDiceroll = new JLabel("Loading");
    private final GameModel model;

    public StatusPanelView(GameModel model) {
        this.model = model;
        this.model.addPropertyChangeListener(this);
        draw();
        this.add(currentPlayer);
        this.add(currentDiceroll);
    }

    public void propertyChange(PropertyChangeEvent event) {
        draw();
    }

    public void draw() {
        this.currentPlayer.setText("Current player: " + this.model.getCurrentPlayer().getCharacter().toString());
        this.currentDiceroll.setText("Your dice roll: " + this.model.getDiceRoll());
        this.revalidate();
        this.repaint();
    }
}
