package hobby_detectives.gui.views.panels;

import hobby_detectives.game.Card;
import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;
import hobby_detectives.gui.views.GameView;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Represents the left pane on the game menu, responsible for showing information such as:
 * - current player turn
 * - the players cards
 * -
 */
public class StatusPanelView extends JPanel implements PropertyChangeListener {
    private final GameController controller;
    private final GameModel model;
    private final JLabel currentPlayer = new JLabel("Loading");
    private final JLabel currentDiceRoll = new JLabel("Loading");
    private final JLabel errorMessage = new JLabel("");
    private final JButton solveButton = new JButton("Solve");
    private final JButton endTurnButton = new JButton("End Turn");
    private final JPanel cards = new JPanel();
    private GameView parent;


    public StatusPanelView(GameModel model, GameController controller, GameView parent) {
        this.parent = parent;
        this.controller = controller;
        this.model = model;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.model.addPropertyChangeListener(this);
        cards.setLayout(new BoxLayout(cards, BoxLayout.Y_AXIS));
        //align all the stuff
        currentPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentDiceRoll.setAlignmentX(Component.CENTER_ALIGNMENT);
        cards.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        //set margins
        currentPlayer.setBorder(new EmptyBorder(10,0,5,0));
        currentDiceRoll.setBorder(new EmptyBorder(10,0,30,0));
        errorMessage.setBorder(new EmptyBorder(10,0,30,0));

        //add actionListner to buttons
        solveButton.addActionListener(
                onclick -> {
                    if (this.model.getCurrentPlayer().isAllowedToSolve()) {
                        this.controller.attemptSolve();
                    } else {
                        this.model.setErrorMessage("You have already attempted to solve!");
                    }
        });
        endTurnButton.addActionListener(
                onclick -> {
                    this.controller.endTurn();
                }
        );


        //set font sizes
        currentPlayer.setFont(new Font("Arial", Font.PLAIN, 30));
        currentDiceRoll.setFont(new Font("Arial", Font.PLAIN, 30));
        errorMessage.setFont(new Font("Arial", Font.PLAIN, 30));
        cards.setFont(new Font("Arial", Font.PLAIN, 30));
        this.add(currentPlayer);
        this.add(currentDiceRoll);

        addButtons();
        this.add(cards);
    }

    private JPanel addButtons(){

        JPanel buttonContainer = new JPanel();
        buttonContainer.add(solveButton);
        buttonContainer.add(endTurnButton);
        buttonContainer.validate();
        return buttonContainer;
    }

    void redrawPanelView(){

        this.removeAll();
        this.cards.removeAll();
        this.currentPlayer.setText("Current player: " + this.model.getCurrentPlayer().getCharacter().toString());
        this.currentDiceRoll.setText("Your dice roll: " + this.model.getDiceRoll());
        this.errorMessage.setText(this.model.getErrorMessage());
        JLabel cardsTitle = new JLabel("Your Cards");
        cardsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardsTitle.setFont(new Font("Arial", Font.PLAIN, 30));
        this.cards.add(cardsTitle);

        JPanel cardsContainer = new JPanel();

        for(Card c : this.model.getCurrentPlayer().getCards()){
            JPanel newCard = new JPanel();
            //TODO these cards to not scale with size and need to be fixeed in future
            newCard.setPreferredSize(new Dimension(150,300));
            newCard.add(new JLabel(c.toString()));
            newCard.setBackground(new Color((int) Math.floor(Math.random() * 254), (int) Math.floor(Math.random() * 254), (int) Math.floor(Math.random() * 254)));
            cardsContainer.add(newCard);
        }
        cards.add(cardsContainer);
        cards.revalidate();
        cards.repaint();
        this.add(currentPlayer);
        this.add(currentDiceRoll);
        this.add(this.errorMessage);
        this.add(addButtons());
        this.add(cards);
        this.revalidate();
        this.repaint();

    }

    public void drawGuess(){
        new GuessView(this.parent, this.controller, this.model);
    }

    public void propertyChange(PropertyChangeEvent event) {
        if(event.getPropertyName().equals("wantsToGuess") && event.getNewValue().equals(true)) drawGuess();
        redrawPanelView();
    }
}
