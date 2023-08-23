package hobby_detectives.gui.views.panels;

import hobby_detectives.board.world.Tile;
import hobby_detectives.data.CharacterType;
import hobby_detectives.engine.Position;
import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

/**
 * This view is responsible for rendering the map, which takes up the majority of the screen.
 * It also handles user inputs and forwards them to the GameController,
 * and deals with other user events.
 */
public class MapPanelView extends JPanel implements PropertyChangeListener {
    private final GameModel model;
    private final GameController controller;

    public MapPanelView(GameModel model, GameController controller) {
        this.setLayout(new GridLayout(24, 24));
        this.model = model;
        this.controller = controller;
        this.model.addPropertyChangeListener(this);
        this.add(new JLabel("Map panel"));
    }

    /**
     * @param event A PropertyChangeEvent object describing the event source
     *              and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent event) {
        redraw();
    }

    /**
     * Redraws the board by removing all the tiles and then adding them back and
     * setting the color of each tile (each "Tile" is a JButton)
     */
    public void redraw() {
        System.out.println("Redrawing map");
        this.removeAll();
        var board = this.model.getBoard();
        for (int r = 0; r < 24; r++) {
            for (int c = 0; c < 24; c++) {
                final var currentPosition = new Position(c, r);
                Tile current = board.read(currentPosition);

                var tileOnBoard = new JButton("");

                if (current.occupant.isPresent()) {
                    OccupantDetail occupantDetail = getOccupantDetails(current.occupant.get().getCharacter());
                    System.out.println(occupantDetail.name() + " -> " + current.occupant.get().getTile().getPosition());
                    tileOnBoard.setBackground(occupantDetail.color());
                    tileOnBoard.setText(occupantDetail.name());
                } else {
                    switch (current.render()) {
                        case "*" -> tileOnBoard.setBackground(new Color(131, 84, 15));
                        case "_" -> tileOnBoard.setBackground(new Color(0, 153, 0));
                        case "#" -> tileOnBoard.setBackground(Color.darkGray);
                        case "H" -> tileOnBoard.setBackground(Color.orange);
                        case "C" -> tileOnBoard.setBackground(Color.blue);
                        case "P" -> tileOnBoard.setBackground(Color.cyan);
                        case "M" -> tileOnBoard.setBackground(Color.magenta);
                        case "V" -> tileOnBoard.setBackground(Color.YELLOW);
                    }
                }
                tileOnBoard.setBorder(null);
                tileOnBoard.addActionListener(onclick -> {
                    controller.tryMovePlayer(currentPosition);
                });

                this.add(tileOnBoard);
            }
        }

        this.revalidate();
        this.repaint();
    }

    private OccupantDetail getOccupantDetails(CharacterType characterType) {
        if (characterType.equals(CharacterType.LUCINA)) {
            return new OccupantDetail(CharacterType.LUCINA.toString().substring(0,1),
                    Color.PINK);
        } else if (characterType.equals(CharacterType.BERT)) {
            return new OccupantDetail(CharacterType.BERT.toString().substring(0,1),
                    Color.YELLOW);
        } else if (characterType.equals(CharacterType.MALINA)) {
            return new OccupantDetail(CharacterType.MALINA.toString().substring(0,1),
                    Color.ORANGE);
        } else if (characterType.equals(CharacterType.PERCY)) {
            return new OccupantDetail(CharacterType.PERCY.toString().substring(0,1),
                    Color.RED);
        }

        return new OccupantDetail("NOT A VALID CHARACTER", Color.BLACK);
    }
}

record OccupantDetail(String name, Color color) {
}
