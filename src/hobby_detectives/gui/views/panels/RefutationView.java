package hobby_detectives.gui.views.panels;

import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;

import javax.swing.*;
import java.awt.*;

public class RefutationView extends JPanel {
    private GameModel model;
    private GameController gameController;

    public RefutationView(GameModel model, GameController controller) {
        this.model = model;
        this.gameController = controller;
        refuteRender();
    }

    private void refuteRender() {
        this.removeAll();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel moved = new JLabel("ACTUALLY REFUTING");

        JPanel buttons = new JPanel();

        if (model.getCurrentPlayer().getCharacter().equals(model.characterThatGuessed)) {
            moved = new JLabel("SHOW THIS PLAYER WHAT CARDS HAVE BEEN REFUTED");
            moved.setFont(new Font("Arial", Font.PLAIN, 30));
        }

        JButton endRefutation = new JButton(
                "Finish refutation"
        );

        buttons.add(endRefutation);

        endRefutation.addActionListener(
                onClick -> {
                    this.gameController.endRefutationTurn();
                }
        );

        this.gameController.endRefutingCycle();
        this.add(buttons);
        this.add(moved);
    }
}
