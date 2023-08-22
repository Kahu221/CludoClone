package hobby_detectives.gui.controller;
import hobby_detectives.board.world.Estate;
import hobby_detectives.board.world.UnreachableArea;
import hobby_detectives.engine.Position;
import hobby_detectives.gui.models.GameModel;
import hobby_detectives.gui.views.panels.PromptExitView;
import hobby_detectives.gui.views.GameView;

/**
 * Represents the 'controller' part of the Model-View-Controller architecture.
 * The game controller is responsible for:
 *   - Handling the game logic.
 *   - Responding to messages from the view.
 *   - Updating the model after applying the game logic rules.
 */
public class GameController {
    private final GameView gameView;
    private final GameModel model;

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
     * Prompts the game to exit by creating a new PromptExitView.
     */
    public void promptForGameExit() {
        var view = new PromptExitView(gameView);
        // PromptExitView disposes itself if cancelled,
        // and the program terminates if not,
        // so we don't need to do anything here
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

    /**
     * Executed when a view confirms that the new player has taken control of the device.
     * This action updates the model and begins the next player turn.
     */
    public void confirmPlayerChange() {
        this.model.setWaitingForPlayer(false);
        startPlayerTurn();
    }

    /**
     * This action starts a new player turn. It clears the state from the previous turn,
     * rolls the dice, and sets up actions for the player.
     */
    public void startPlayerTurn() {
        this.model.players.poll();
        this.model.rollDice();
    }

    public void tryMovePlayer(Position p) {
        var player = this.model.getCurrentPlayer();
        var t = this.model.getBoard().read(p);

        var successfulMove = true;
        // Check if the player is attempting to move into an estate.
        if (t instanceof Estate e) {
            this.model.getBoard().tryMoveIntoEstate(player, p, e);
            // Check if the player is attempting to move into an estate fill tile.
        } else if (t instanceof Estate.EstateFillTile eft) {
            this.model.getBoard().tryMoveIntoEstate(player, p, eft.parent);
        } else if (t instanceof UnreachableArea) {
            successfulMove = false;
        }
        if (successfulMove) {
            player.getTile().setPlayer(null);

            t.setPlayer(player);
            player.setTile(t);
        }

        this.model.notifyBoardUpdate();

        endTurn();
    }

    public void endTurn() {
        this.model.players.add(this.model.getCurrentPlayer());
        System.out.println("Setting to " + this.model.players.peek());
        this.model.setCurrentPlayer(this.model.players.peek());
        this.model.setWaitingForPlayer(true);
    }
//
//    public void promptPlayerForGuess(Player p, Estate estate) {}
//
//    public boolean attemptSolve(Player p, CardTriplet solveattempt) {}
//
//    public void turn(){}

}