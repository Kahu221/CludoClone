package hobby_detectives.gui.panels;

import hobby_detectives.engine.Position;
import hobby_detectives.game.GameModel;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

public class MapPanelView extends JPanel implements PropertyChangeListener {
    private final GameModel model;

    public MapPanelView(GameModel model) {
        this.setLayout(new GridLayout(24,24));
        this.model = model;
        this.model.addPropertyChangeListener(this);
        this.add(new JLabel("Map panel"));
    }

    public void propertyChange(PropertyChangeEvent event) {
       redraw();
    }

    public void redraw(){
        this.removeAll();
        var board = this.model.getBoard();
        for(int r = 0 ; r < 24 ; r++){
            for(int c = 0 ; c < 24 ; c++){
                var tile = new JButton(board.read(new Position(r,c)).render());
                tile.setBorder(null);
                int finalR = r;
                int finalC = c;
                tile.addActionListener(onclick -> {
                    // TODO send something to controller
                    System.out.println("tile clicked at " + finalR + finalC);
                });

                this.add(tile);
            }
        }
    }
}
