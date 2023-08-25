package hobby_detectives.gui.views.panels;

import hobby_detectives.data.CharacterType;
import hobby_detectives.data.WeaponType;
import hobby_detectives.game.Card;
import hobby_detectives.game.CardTriplet;
import hobby_detectives.game.PlayerCard;
import hobby_detectives.game.WeaponCard;
import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;
import hobby_detectives.gui.views.GameView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GuessView {
    ArrayList<JButton> weaponButtons = new ArrayList<>();
    ArrayList<JButton> characterButtons = new ArrayList<>();
    private final GameView parent;
    private GameModel model;
    private ArrayList<Card> chosenCards = new ArrayList<>();

    private JDialog frame;
    private GameController controller;
    public GuessView(GameView parent, GameController controller, GameModel model) {
        this.controller = controller;
        this.model = model;
        this.parent = parent;
        frame = new JDialog(this.parent, "Make Guess");
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Please pick one of each Card you would like to guess");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        frame.add(title);
        //each container to hold each type of card (each one is filled with a button for each card type)
        var estateCardsContainer = new JPanel();

        /* Manually guess the EstateCard for the Estate the player is currently in */
        JButton currentEstate = new JButton(model.getEstateCardForCurrentEstate().estate.name());
        currentEstate.setPreferredSize(new Dimension(130, 200));
        currentEstate.setBackground(Color.green);
        chosenCards.add(model.getEstateCardForCurrentEstate());
        estateCardsContainer.add(currentEstate);

        var weaponCardsContainer = new JPanel();
        for (WeaponType w : WeaponType.values()) {
            JButton currentWeapon = new JButton(w.name());
            currentWeapon.setPreferredSize(new Dimension(130, 200));
            currentWeapon.addActionListener(clicked -> {
                attemptToChooseCard(currentWeapon.getText());
            });
            weaponButtons.add(currentWeapon);
            weaponCardsContainer.add(currentWeapon);
        }
        var personCardsContainer = new JPanel();
        for (CharacterType c : CharacterType.values()) {
            JButton currentCharacter = new JButton(c.name());
            currentCharacter.setPreferredSize(new Dimension(130, 200));
            currentCharacter.addActionListener(clicked -> {
                attemptToChooseCard(currentCharacter.getText());
            });
            characterButtons.add(currentCharacter);
            personCardsContainer.add(currentCharacter);
        }
        //finalise and draw
        frame.getContentPane().add(estateCardsContainer);
        frame.getContentPane().add(weaponCardsContainer);
        frame.getContentPane().add(personCardsContainer);
        frame.pack();
        frame.setVisible(true);
    }

    //TODO logic here can defo be done better
    public void attemptToChooseCard(String cardName) {
        Card chosenCard = model.allCards.get(cardName);

        if (chosenCard instanceof WeaponCard && chosenCards.stream().noneMatch(c -> c instanceof WeaponCard)) {
            chosenCards.add(chosenCard);
            for (JButton b : weaponButtons)
                if (!b.getText().equals(cardName)) b.setEnabled(false);
                else b.setBackground(Color.green);
        } else if (chosenCard instanceof PlayerCard && chosenCards.stream().noneMatch(c -> c instanceof PlayerCard)) {
            chosenCards.add(chosenCard);
            for (JButton b : characterButtons)
                if (!b.getText().equals(cardName)) b.setEnabled(false);
                else b.setBackground(Color.green);
        }
        if (chosenCards.size() == 3) controller.confirmedGuess(returnGuessedCards());
    }

    //TODO THIS IS SO UGLY
    public CardTriplet returnGuessedCards() {
        frame.dispose();
        return new CardTriplet(
                (WeaponCard) chosenCards.stream().filter(c -> c instanceof WeaponCard).findFirst().orElse(null),
                this.model.getEstateCardForCurrentEstate(),
                (PlayerCard) chosenCards.stream().filter(c -> c instanceof PlayerCard).findFirst().orElse(null)
        );

    }

}
