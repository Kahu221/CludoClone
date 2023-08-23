package hobby_detectives.gui.views.panels;

import hobby_detectives.game.Card;
import hobby_detectives.gui.models.GameModel;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Represents the left pane on the game menu, responsible for showing information such as:
 * - current player turn
 * - the players cards
 * -
 */
public class StatusPanelView extends JPanel implements PropertyChangeListener {
    private final JLabel currentPlayer = new JLabel("Loading");
    private final JLabel currentDiceRoll = new JLabel("Loading");
    private final JButton guessButton = new JButton("Make Guess");
    private final JButton makeSolve = new JButton("Solve");

    private final JPanel cards = new JPanel();
    private final GameModel model;

    public StatusPanelView(GameModel model) {
        this.model = model;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.model.addPropertyChangeListener(this);
        cards.setLayout(new BoxLayout(cards, BoxLayout.Y_AXIS));
        //align all the stuff
        currentPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentDiceRoll.setAlignmentX(Component.CENTER_ALIGNMENT);
        cards.setAlignmentX(Component.CENTER_ALIGNMENT);
        //set margins
        currentPlayer.setBorder(new EmptyBorder(10,0,5,0));
        currentDiceRoll.setBorder(new EmptyBorder(10,0,100,0));

        //set font sizes
        currentPlayer.setFont(new Font("Arial", Font.PLAIN, 30));
        currentDiceRoll.setFont(new Font("Arial", Font.PLAIN, 30));
        cards.setFont(new Font("Arial", Font.PLAIN, 30));
        this.add(currentPlayer);
        this.add(currentDiceRoll);

        this.add(cards);
    }

    void redrawPanelView(){
        this.removeAll();
        this.cards.removeAll();
        this.currentPlayer.setText("Current player: " + this.model.getCurrentPlayer().getCharacter().toString());
        this.currentDiceRoll.setText("Your dice roll: " + this.model.getDiceRoll());
        JLabel cardsTitle = new JLabel("Your Cards");
        cardsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardsTitle.setFont(new Font("Arial", Font.PLAIN, 30));
        this.cards.add(cardsTitle);

        JPanel cardsContainer = new JPanel();

        for(Card c : this.model.getCurrentPlayer().getCards()){
            JPanel newCard = new JPanel();
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
        this.add(cards);
        this.revalidate();
        this.repaint();
    }

    public void propertyChange(PropertyChangeEvent event) {
        redrawPanelView();
    }
}
