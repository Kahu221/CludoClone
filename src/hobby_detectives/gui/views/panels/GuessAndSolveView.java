package hobby_detectives.gui.views.panels;

import hobby_detectives.board.world.Estate;
import hobby_detectives.data.CharacterType;
import hobby_detectives.data.EstateType;
import hobby_detectives.data.WeaponType;
import hobby_detectives.game.Card;
import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;
import hobby_detectives.gui.views.GameView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GuessAndSolveView{

    private final GameView parent;
    private ArrayList<Card> chosenCards = new ArrayList<>();


    public GuessAndSolveView(GameView parent, GameController controller) {
        this.parent = parent;
        var frame = new JDialog(this.parent, "Make Guess");
        frame.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Please pick one of each Card you would like to guess");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        frame.getContentPane().add(title);
        //each container to hold each type of card (each one is filled with a button for each card type)
        var estateCardsContainer = new JPanel();
        for(EstateType e : EstateType.values()){
            JButton currentEstate = new JButton(e.name());
            currentEstate.addActionListener(clicked -> {
                attemptToChooseCard(currentEstate.getText());
            });
            estateCardsContainer.add(currentEstate);
        }
        var weaponCardsContainer = new JPanel();
        for(WeaponType w : WeaponType.values()){
            JButton currentWeapon = new JButton(w.name());
            currentWeapon.addActionListener(clicked -> {
                attemptToChooseCard(currentWeapon.getText());
            });
            weaponCardsContainer.add(currentWeapon);

        }
        var personCardsContainer = new JPanel();
        for(CharacterType c : CharacterType.values()){
            JButton currentCharacter = new JButton(c.name());
            currentCharacter.addActionListener(clicked -> {
                attemptToChooseCard(currentCharacter.getText());
            });
            personCardsContainer.add(currentCharacter);
        }
        //finalise and draw


        render();
    }

    public void attemptToChooseCard(String cardName){
        
    }
    public void render(){

    }
}
