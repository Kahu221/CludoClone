package hobby_detectives.gui.panels;

import hobby_detectives.game.GameModel;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

public class MapPanelView extends JPanel implements PropertyChangeListener {
    //private final JLabel icons[][];

    private final GameModel model;
    public MapPanelView(GameModel model) {
        this.model = model;
        this.model.addPropertyChangeListener(this);
        this.add(new JLabel("Map panel"));
        this.setBackground(Color.BLACK);
    }

    public void propertyChange(PropertyChangeEvent event) {

    }
}
