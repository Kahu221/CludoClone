package hobby_detectives.gui.controller;
import hobby_detectives.board.world.*;
import hobby_detectives.data.CharacterType;
import hobby_detectives.data.EstateType;
import hobby_detectives.data.WeaponType;
import hobby_detectives.engine.Position;
import hobby_detectives.game.*;
import hobby_detectives.gui.controller.pathing.BoardEdge;
import hobby_detectives.gui.controller.pathing.PathAlgorithm;
import hobby_detectives.gui.models.GameModel;
import hobby_detectives.player.Player;

import java.util.*;

/**
 * Represents the 'controller' part of the Model-View-Controller architecture.
 * The game controller is responsible for:
 *   - Handling the game logic.
 *   - Responding to messages from the view.
 *   - Updating the model after applying the game logic rules.
 */
public class GameController {
    private final GameModel model;

    public GameController(GameModel model){
        this.model = model;
    }

    public void startGame(List<Player> playerSeeds) {
        for (Player p : playerSeeds) {
            switch (p.getCharacter()) {
                case LUCINA -> {
                    this.model.getBoard().read(new Position(11, 1)).setPlayer(p);
                    p.setTile(this.model.getBoard().read(new Position(11, 1)));
                }
                case BERT -> {
                    this.model.getBoard().read(new Position(1, 9)).setPlayer(p);
                    p.setTile(this.model.getBoard().read(new Position(1, 9)));
                }
                case MALINA -> {
                    this.model.getBoard().read(new Position(9, 22)).setPlayer(p);
                    p.setTile(this.model.getBoard().read(new Position(9, 22)));
                }
                case PERCY -> {
                    this.model.getBoard().read(new Position(22, 14)).setPlayer(p);
                    p.setTile(this.model.getBoard().read(new Position(22, 14)));
                }
                default -> throw new Error();
            }
        }

        // initialize cards and correct cards
        List<PlayerCard> playerCards = new ArrayList<>(Arrays.stream(CharacterType.values()).map(PlayerCard::new).toList());
        for(Card c : playerCards) this.model.allCards.put(c.toString(), c);
        Collections.shuffle(playerCards);

        List<WeaponCard> weaponCards = new ArrayList<>(Arrays.stream(WeaponType.values()).map(WeaponCard::new).toList());
        Collections.shuffle(weaponCards);
        for(Card c : weaponCards) this.model.allCards.put(c.toString(), c);

        List<EstateCard> estateCards = new ArrayList<>(Arrays.stream(EstateType.values()).map(EstateCard::new).toList());
        for(Card c : estateCards) this.model.allCards.put(c.toString(), c);
        Collections.shuffle(estateCards);

        this.model.correctTriplet = new CardTriplet(
                weaponCards.remove(0),
                estateCards.remove(0),
                playerCards.remove(0)
        );

        var remainingCards = new ArrayDeque<Card>();
        remainingCards.addAll(playerCards);
        remainingCards.addAll(weaponCards);
        remainingCards.addAll(estateCards);
        int counter = 0;
        while (!remainingCards.isEmpty()) {
            Card current = remainingCards.pop();
            playerSeeds.get(counter).addCard(current);
            counter++;
            if (counter >= playerSeeds.size()) counter = 0;
        }
        this.model.players = new ArrayDeque<>(playerSeeds);

        this.model.setCurrentPlayer(this.model.players.peek());
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

    public void tryMovePlayer(Position desiredPosition) {
        this.model.resetHasMovedIntoEstate();
        this.model.setErrorMessage("");
        var player = this.model.getCurrentPlayer();
        var desiredTile = this.model.getBoard().read(desiredPosition);

        var playerPosition = player.getTile().getPosition();

        if(playerPosition.equals(desiredPosition)) {
            this.model.setErrorMessage("Cannot move onto yourself.");
            return;
        }

        if(player.getTile() instanceof Estate e) {
            List<Position> absoluteDoorPositions = e.doors.stream()
                    .map(oldPosition -> oldPosition.add(e.getPosition())).toList();

            List<List<BoardEdge>> possiblePaths = absoluteDoorPositions.stream()
                    .map(doorPosition ->
                            PathAlgorithm.findShortestPath(this.model.getBoard(), doorPosition, desiredPosition))
                    .toList();

            List<BoardEdge> shortestPath = possiblePaths.stream()
                    .min(Comparator.comparing(List::size))
                    .orElse(List.of());

            playerPosition = absoluteDoorPositions.get(possiblePaths.indexOf(shortestPath));
        }

        var path = PathAlgorithm.findShortestPath(this.model.getBoard(), playerPosition, desiredPosition);
        if (path.isEmpty()) {
            this.model.setErrorMessage("You can't move there.");
        } else if (path.size() > this.model.getDiceRoll()) {
            this.model.setErrorMessage("You need " + path.size() + " moves." + " You only have " + this.model.getDiceRoll() + " moves.");
        } else {
            if (desiredTile instanceof Estate e) {
                this.model.getBoard().tryMoveIntoEstate(player, desiredPosition, e);
                // Check if the player is attempting to move into an estate fill tile.
            } else if (desiredTile instanceof Estate.EstateFillTile eft) {
                boolean moved = this.model.getBoard().tryMoveIntoEstate(player, desiredPosition, eft.parent);
                if (moved) {
                    this.model.playerHasMovedIntoEstate();
                } else {
                    this.model.setErrorMessage("You can not move into the Estate");
                }
            } else {
                player.getTile().setPlayer(null);
                desiredTile.setPlayer(player);
                player.setTile(desiredTile);
            }
            this.model.useNumberOfMoves(path.size());
            this.model.notifyBoardUpdate();

            if(this.model.getDiceRoll() < 1 && model.getHasMovedIntoEstate()) {
                this.model.setErrorMessage("Please choose to make a guess or end turn");
            } else if (this.model.getDiceRoll() < 1) {
                endTurn();
            }
        }
    }



    public void endTurn() {
        this.model.setErrorMessage("");
        this.model.players.add(this.model.getCurrentPlayer());
        System.out.println("Setting to " + this.model.players.peek());
        this.model.setCurrentPlayer(this.model.players.peek());
        this.model.setWaitingForPlayer(true);
    }
//
    public void promptPlayerForGuess() {
        this.model.changeGuessState(true);
    }

//
    public void attemptSolve() {

    }

//    public void turn(){}

}