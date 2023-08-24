package hobby_detectives.test;

import hobby_detectives.data.CharacterType;
import hobby_detectives.engine.Position;
import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;
import hobby_detectives.player.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(JUnit4.class)
public class GameControllerTests {
    private GameController controller;
    private GameModel model;
    @Before
    public void setUp() {
        this.model = new GameModel();
        this.controller = new GameController(this.model);
        this.model.setWaitingForPlayer(true);
        this.controller.startGame(List.of(
                new Player(CharacterType.MALINA, new ArrayList<>(), ""),
                new Player(CharacterType.PERCY, new ArrayList<>(), ""),
                new Player(CharacterType.BERT, new ArrayList<>(), ""),
                new Player(CharacterType.LUCINA, new ArrayList<>(), "")
        ));
        this.model.setWaitingForPlayer(true);
        this.controller.confirmPlayerChange();
        this.controller.startPlayerTurn();
    }
    @Test
    public void testPlayerCanMakeSimpleValidMove() {
        setDiceRoll(5);

        Player testPlayer = model.getCurrentPlayer();

        Position testPlayerPosition = testPlayer.getTile().getPosition();
        Position newValidPlayerPosition = new Position(
                testPlayerPosition.x() + 2, testPlayerPosition.y() - 2);

        this.controller.tryMovePlayer(newValidPlayerPosition);
        assertEquals(testPlayer.getTile().getPosition(), newValidPlayerPosition);
    }
    @Test
    public void testPlayerCannotMoveMoreThanAllocatedMoves() {
        setDiceRoll(4);
        Player testPlayer = model.getCurrentPlayer();

        Position testPlayerPosition = testPlayer.getTile().getPosition();
        Position newinValidPlayerPosition = new Position(
                testPlayerPosition.x() + 4, testPlayerPosition.y() - 2);
        this.controller.tryMovePlayer(newinValidPlayerPosition);

        assertEquals("You need " + 6 + " moves." + " You only have " + 4 + " moves.", model.getErrorMessage());
        assertNotEquals(testPlayer.getTile().getPosition(), newinValidPlayerPosition);
        assertEquals(model.getCurrentPlayer().getTile().getPosition(), testPlayerPosition);
    }
    @Test
    public void testPlayerCannotGoIntoUnreachableArea() { /* TODO: THERE NEEDS TO BE AN ERROR FOR THIS */
        setDiceRoll(8);
        Player testPlayer = model.getCurrentPlayer();

        Position testPlayerPosition = testPlayer.getTile().getPosition();
        Position newinValidPlayerPosition = new Position(
                testPlayerPosition.x() + 2, testPlayerPosition.y() - 4);
        this.controller.tryMovePlayer(newinValidPlayerPosition);

        assertNotEquals(testPlayer.getTile().getPosition(), newinValidPlayerPosition);
        assertEquals(model.getCurrentPlayer().getTile().getPosition(), testPlayerPosition);
    }
    @Test
    public void testPlayerCannotEnterEstateWithoutGoingThroughTheDoor() {
        setDiceRoll(8);
        Player testPlayer = model.getCurrentPlayer();

        Position testPlayerPosition = testPlayer.getTile().getPosition();
        Position newinValidPlayerPosition = new Position(
                testPlayerPosition.x() - 4, testPlayerPosition.y() - 2);
        this.controller.tryMovePlayer(newinValidPlayerPosition);

        assertEquals("You can't move there.", model.getErrorMessage());
        assertNotEquals(testPlayer.getTile().getPosition(), newinValidPlayerPosition);
        assertEquals(model.getCurrentPlayer().getTile().getPosition(), testPlayerPosition);
    }
    @Test
    public void testPlayerCanEnterAnEstateThroughTheDoor() {}
    @Test
    public void testThatPlayerCanOnlyLeaveEstateInCorrectDirection() {}

    public void setDiceRoll(int num) {
        try {
            Field diceRollField = model.getClass().getDeclaredField("diceRoll");
            diceRollField.setAccessible(true);

            diceRollField.setInt(model, num);
            assertEquals(num, model.getDiceRoll());
        } catch (Exception ignored) {}
    }


}
