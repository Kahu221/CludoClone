package hobby_detectives.gui.views.panels;

import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;

import javax.swing.*;
import java.awt.*;

public class GuessNotificationView extends JPanel {

    public GuessNotificationView(GameModel model, GameController controller) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        var currentEstate = model.getEstateCurrentPlayerIsIn().name();
        var moved = new JLabel("You have entered " + currentEstate + " would you like to guess or end your turn?");
        moved.setFont(new Font("Arial", Font.PLAIN, 30));
        JPanel buttons = new JPanel();

        JButton guess = new JButton(
                "Make Guess"
        );

        JButton endTurn = new JButton(
                "End Turn"
        );

        buttons.add(guess);
        buttons.add(endTurn);

        guess.addActionListener(onClick -> {
            controller.promptPlayerForGuess();
        });

        endTurn.addActionListener(onClick -> {
            controller.endRefutationTurn();
        });

        this.add(moved);
        this.add(buttons);
    }
}
