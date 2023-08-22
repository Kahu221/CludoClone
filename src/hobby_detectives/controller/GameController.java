package hobby_detectives.controller;
import hobby_detectives.gui.views.GameView;
import hobby_detectives.game.*;

public class GameController {
    private GameView gameView;
    private GameModel model;

    public GameController(){
        this.model = new GameModel();
        this.gameView = new GameView(this.model, this);
    }

    /**
     * Starts the game:
     * - Sets up the model.
     * - Tells the model that we are waiting for the first player.
     */
    public void start() {
        this.model.setWaitingForPlayer(true);
    }

    /**
     * Asks the user for a boolean value, by continuously prompting the given prompt
     * until a valid boolean value is entered.
     */
//    public boolean askBoolean(){}
//
//    public WeaponCard promptWeaponCard(){}
//
//    public EstateCard promptEstate() {}
//
//    public PlayerCard promptPlayer(Player currentPlayer) {}
//
    public void confirmPlayerChange() {
        this.model.setWaitingForPlayer(false);
    }

    public void changePlayer(){
        //poll and push
    }
//
//    public void promptPlayerForGuess(Player p, Estate estate) {}
//
//    public boolean attemptSolve(Player p, CardTriplet solveattempt) {}
//
//    public void turn(){}

}