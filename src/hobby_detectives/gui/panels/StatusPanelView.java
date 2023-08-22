package hobby_detectives.gui.panels;

import hobby_detectives.gui.models.StatusPanelModel;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

public class StatusPanelView extends JPanel implements PropertyChangeListener {
    private final JLabel currentPlayer = new JLabel("Loading");
    private final StatusPanelModel model;

    public StatusPanelView(StatusPanelModel model) {
        this.model = model;
        this.model.addPropertyChangeListener(this);
        draw();
        this.add(currentPlayer);
    }

    public void propertyChange(PropertyChangeEvent event) {
        draw();
    }

    public void draw() {
        currentPlayer.setText(this.model.getCharacterName());
    }

}
