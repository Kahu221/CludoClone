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
        gameController.figureOutIfCurrentPlayerCanRefuteAnyCards();
        refuteRender();
    }

    private void refuteRender() {
        this.removeAll();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel buttons = new JPanel();;
        JButton endRefutation = new JButton(
                "Finish refutation"
        );
        buttons.add(endRefutation);
        endRefutation.addActionListener(
                onClick -> {
                    this.gameController.endRefutationTurn();
                }
        );

        JLabel moved = new JLabel("ACTUALLY REFUTING");
        if (model.getCurrentPlayer().getCharacter().equals(model.characterThatGuessed)) {
            moved = new JLabel("SHOW THIS PLAYER WHAT CARDS HAVE BEEN REFUTED");
            moved.setFont(new Font("Arial", Font.PLAIN, 30));
        }

        JPanel cardsGuessedPanel = new JPanel();
        JLabel cardsGuessed = new JLabel(
                "The Cards Guessed were: " + this.model.getCurrentGuess()
        );
        cardsGuessedPanel.add(cardsGuessed);

        JPanel possibleRefutionsPanel = new JPanel();
        JLabel possibleRefutions = new JLabel(
                "The refutions r: " + this.model.refutableCards.toString()
        );
        possibleRefutionsPanel.add(possibleRefutions);

        JPanel whatCurrentPlayerHasPanel = new JPanel();
        JLabel whatCurrentPlayerHas = new JLabel(
                "You have: " + this.model.getCurrentPlayer().getCards().toString()
        );
        whatCurrentPlayerHasPanel.add(whatCurrentPlayerHas);

        this.gameController.endRefutingCycle();
        this.add(buttons);
        this.add(moved);
        this.add(cardsGuessedPanel);
        this.add(possibleRefutionsPanel);
        this.add(whatCurrentPlayerHasPanel);
    }
}
