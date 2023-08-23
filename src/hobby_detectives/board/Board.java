package hobby_detectives.board;

import hobby_detectives.board.world.*;
import hobby_detectives.data.Direction;
import hobby_detectives.data.EstateType;
import hobby_detectives.data.WeaponType;
import hobby_detectives.engine.Position;
import hobby_detectives.game.Card;
import hobby_detectives.gui.models.GameModel;
import hobby_detectives.game.WeaponCard;
import hobby_detectives.player.Player;

import java.util.*;

/**
 * Represents the game board, holding data and logic that relates
 * to the data on the board and drawing the board.
 */
public class Board {
    private final int boardSize;

    /**
     * Cards that have been guessed and not refuted.
     */
    public final List<Card> unrefutedCards = new ArrayList<>();
    private final List<Estate> estates;
    private final Tile[][] board;
    private final GameModel game;

    //remeber players num are not always static can be 3 || 4 and need to figure out how to do these fkn rooms
    public Board(int boardSize, GameModel game) {
        this.game = game;
        this.boardSize = boardSize;
        this.board = new Tile[boardSize][boardSize];
        //initilisation of the board
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                this.board[row][col] = new Tile(new Position(row, col));
            }
        }

        var weapons = new ArrayList<>(List.of(WeaponType.values()));
        Collections.shuffle(weapons);

        // initialise estates
        estates = List.of(
                new Estate(new Position(2, 2), 5, 5, EstateType.HAUNTED_HOUSE, weapons.remove(0),
                        List.of(new Position(4, 1), new Position(3, 4)),
                        List.of(Direction.RIGHT, Direction.DOWN)
                ),

                new Estate(new Position(2, 17), 5, 5, EstateType.CALAMITY_CASTLE, weapons.remove(0),
                        List.of(new Position(1, 0), new Position(4, 1)),
                        List.of(Direction.UP, Direction.RIGHT)
                        ),

                new Estate(new Position(9, 10), 6, 4, EstateType.VISITATION_VILLA, weapons.remove(0),
                        List.of(new Position(0, 2), new Position(3, 0), new Position(2, 3), new Position(5, 1)),
                        List.of(Direction.LEFT, Direction.UP, Direction.DOWN, Direction.RIGHT)
                        ),

                new Estate(new Position(17, 2), 5, 5, EstateType.MANIC_MANOR, weapons.remove(0),
                        List.of(new Position(0, 3), new Position(3, 4)),
                        List.of(Direction.LEFT, Direction.DOWN)
                        ),

                new Estate(new Position(17, 17), 5, 5, EstateType.PERIL_PALACE, weapons.remove(0),
                        List.of(new Position(1, 0), new Position(0, 3)),
                        List.of(Direction.UP, Direction.LEFT)
                        )
        );

        for (Estate e : estates) {
            board[e.getPosition().x()][e.getPosition().y()] = e;
            for (Tile fill : e.generateFillTiles()) {
                board[fill.getPosition().x()][fill.getPosition().y()] = fill;
            }
        }

        // initialise unreachable areas
        applyUnreachableArea(new Position(11, 5), 2, 2);
        applyUnreachableArea(new Position(5, 11), 2, 2);
        applyUnreachableArea(new Position(11, 17), 2, 2);
        applyUnreachableArea(new Position(17, 11), 2, 2);
    }

    private void applyUnreachableArea(Position origin, int width, int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                board[origin.x() + x][origin.y() + y] = new UnreachableArea(origin.add(new Position(x, y)));
            }
        }
    }

    /**
     * Attempts to move a player into an estate using the translation position.
     */
    public boolean tryMoveIntoEstate(Player p, Position possibleTranslate, Estate e) {
        // Player is at a door.
        if (e.doors.stream().anyMatch(d -> d.add(e.getPosition()).equals(possibleTranslate))) {
            if (e.occupant.isEmpty()) {
                e.setPlayer(p);
                p.setTile(e);

                WeaponCard newWeaponCard = new WeaponCard(e.weapon);
                boolean weaponAlreadyInHand = p.getCards().stream()
                        .anyMatch(card -> card.equals(newWeaponCard));
                if(!weaponAlreadyInHand) {
                    p.getCards().add(newWeaponCard);
                }

                //game.promptPlayerForGuess(p, e);
                return true;
            }
        }
        return false;
    }

    /**
     * Reads the given position from the board. This method returns null if the position is out of bounds.
     */
    public Tile read(Position p) {
        if (p.x() >= this.boardSize || p.x() < 0 || p.y() >= this.boardSize || p.y() < 0) return null;
        return this.board[p.x()][p.y()];
    }

    /**
     * Returns the position specified by a given input token.
     * Does not perform validation that the returned position is valid on the game board.
     *
     * @return An empty optional if the input is invalid.
     */
    public Optional<Position> getPosition(String input, Position position) {
        for(char token : input.toLowerCase().toCharArray()){
            switch(token) {
                case 'l' -> position = new Position(position.x() - 1, position.y());
                case 'r' -> position = new Position(position.x() + 1, position.y());
                case 'u' -> position = new Position(position.x(), position.y() - 1);
                case 'd' -> position = new Position(position.x(), position.y() + 1);
                default -> {
                    return Optional.empty();
                }
            }
        }
        return Optional.ofNullable(position);
    }

    private boolean moveableArea(Position position) {
        if (read(position) instanceof UnreachableArea) {
            return false;
        }
        if (read(position) instanceof Estate.EstateFillTile) {
            return false;
        }
        if (read(position) instanceof Estate) {
            return false;
        }
        return true;
    }
}
