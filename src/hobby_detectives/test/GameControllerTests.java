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
                new Player(CharacterType.PERCY, new ArrayList<>(), ""),
                new Player(CharacterType.BERT, new ArrayList<>(), ""),
                new Player(CharacterType.LUCINA, new ArrayList<>(), ""),
                new Player(CharacterType.MALINA, new ArrayList<>(), "")
        ));
        this.model.setWaitingForPlayer(true);
        this.controller.confirmPlayerChange();
        this.controller.startPlayerTurn();
    }

    @Test
    public void testValidMove() {
        try {
            Field diceRollField = model.getClass().getDeclaredField("diceRoll");
            diceRollField.setAccessible(true);

            diceRollField.setInt(model, 5);
            assertEquals(5, model.getDiceRoll());
        } catch (Exception ignored) {}

        Position currentPlayerPosition = model.getCurrentPlayer().getTile().getPosition();
        Position newValidPlayerPosition = new Position(
                currentPlayerPosition.x() + 2, currentPlayerPosition.y() + 1);

        this.controller.tryMovePlayer(newValidPlayerPosition);
    }



}
