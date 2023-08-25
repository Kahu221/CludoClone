package hobby_detectives.gui.views.panels;

import hobby_detectives.game.Card;
import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import java.util.*;

public class RefutationView extends JPanel {
    private GameModel model;
    private GameController gameController;
    public RefutationView(GameModel model, GameController controller) {
        this.model = model;
        this.gameController = controller;
        refuteRender();
    }

    private void refuteRender(){
        this.removeAll();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        var moved = new JLabel("BIGGER");
        moved.setAlignmentY(Component.TOP_ALIGNMENT);
        moved.setFont(new Font("Arial", Font.PLAIN, 30));

        JPanel buttons = new JPanel();

        JButton endRefutation = new JButton(
                "Finish refutation"
        );

        buttons.add(endRefutation);

        endRefutation.addActionListener(
                onClick -> {
                    this.gameController.endRefutationTurn();
                }
        );
        this.add(buttons);
        this.add(moved);
    }
}
