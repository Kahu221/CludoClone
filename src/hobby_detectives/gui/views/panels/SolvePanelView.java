package hobby_detectives.gui.views.panels;

import hobby_detectives.board.world.Estate;
import hobby_detectives.data.CharacterType;
import hobby_detectives.data.EstateType;
import hobby_detectives.data.WeaponType;
import hobby_detectives.game.*;
import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;
import hobby_detectives.gui.views.GameView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SolvePanelView extends JPanel {

    private final GameModel model;

    private final ArrayList<Card> solveCards = new ArrayList<>();

    private final ArrayList<JButton> estateButtons = new ArrayList<>();
    private final ArrayList<JButton> weaponButtons = new ArrayList<>();
    private final ArrayList <JButton> characterButtons = new ArrayList<>();


    public SolvePanelView(GameView parent, GameModel model, GameController controller) {
        this.model = model;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Please pick one of each Card");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        this.add(title);

        var estateCardsContainer = new JPanel();
        for(EstateType e : EstateType.values()) {
            JButton currentEstate = new JButton(e.name());
            currentEstate.setPreferredSize(new Dimension(130,200));
            currentEstate.addActionListener(clicked -> {
                attemptToChooseCard(currentEstate.getText());
            });
            estateButtons.add(currentEstate);
            estateCardsContainer.add(currentEstate);
        }

        var weaponCardsContainer = new JPanel();
        for(WeaponType w : WeaponType.values()){
            JButton currentWeapon = new JButton(w.name());
            currentWeapon.setPreferredSize(new Dimension(130,200));
            currentWeapon.addActionListener(clicked -> {
                attemptToChooseCard(currentWeapon.getText());
            });
            weaponButtons.add(currentWeapon);
            weaponCardsContainer.add(currentWeapon);
        }

        var personCardsContainer = new JPanel();
        for(CharacterType c : CharacterType.values()){
            JButton currentCharacter = new JButton(c.name());
            currentCharacter.setPreferredSize(new Dimension(130,200));
            currentCharacter.addActionListener(clicked -> {
                attemptToChooseCard(currentCharacter.getText());
            });
            characterButtons.add(currentCharacter);
            personCardsContainer.add(currentCharacter);
        }
        //finalise and draw

        JButton accept = new JButton("Accept");
        accept.addActionListener(clicked -> {
            System.out.println(solveCards);
            if(solveCards.size() == 3) {
                controller.computeSolveAttempt(returnSolvedCards());
            }
        });

        this.add(estateCardsContainer);
        this.add(weaponCardsContainer);
        this.add(personCardsContainer);
        accept.setAlignmentX(CENTER_ALIGNMENT);
        this.add(accept);
        this.setVisible(true);
}

    //TODO logic here can defo be done better
    public void attemptToChooseCard(String cardName){
        Card chosenCard = model.allCards.get(cardName);
        if(chosenCard instanceof EstateCard && solveCards.stream().noneMatch(c -> c instanceof EstateCard)) {
            solveCards.add(chosenCard);
            for(JButton b : estateButtons)
                if(!b.getText().equals(cardName)) b.setEnabled(false);
                else b.setBackground(Color.green);
        }
        else if(chosenCard instanceof WeaponCard && solveCards.stream().noneMatch(c -> c instanceof WeaponCard)){
            solveCards.add(chosenCard);
            for(JButton b : weaponButtons)
                if(!b.getText().equals(cardName)) b.setEnabled(false);
                else b.setBackground(Color.green);
        }
        else if(chosenCard instanceof PlayerCard && solveCards.stream().noneMatch(c -> c instanceof PlayerCard)) {
            solveCards.add(chosenCard);
            for(JButton b : characterButtons)
                if(!b.getText().equals(cardName)) b.setEnabled(false);
                else b.setBackground(Color.green);
        }
    }

    public CardTriplet returnSolvedCards(){
        return new CardTriplet(
                (WeaponCard) solveCards.stream().filter(c -> c instanceof WeaponCard)
                        .findFirst()
                        .orElse(null),
                (EstateCard) solveCards.stream().filter(c -> c instanceof EstateCard)
                        .findFirst()
                        .orElse(null),
                (PlayerCard) solveCards.stream().filter(c -> c instanceof PlayerCard)
                        .findFirst()
                        .orElse(null)
        );
    }
}
