package hobby_detectives.test;

import hobby_detectives.data.CharacterType;
import hobby_detectives.gui.controller.GameController;
import hobby_detectives.gui.models.GameModel;
import hobby_detectives.player.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ControllerModelTests {
	private GameController controller;
	private GameModel model;
	@Before
	public void setUp() {
		this.model = new GameModel();
		this.controller = new GameController(this.model);
		this.controller.startGame(List.of(
				new Player(CharacterType.LUCINA, new ArrayList<>(), ""),
				new Player(CharacterType.BERT, new ArrayList<>(), ""),
				new Player(CharacterType.MALINA, new ArrayList<>(), ""),
				new Player(CharacterType.PERCY, new ArrayList<>(), "")
		));
		this.model.setWaitingForPlayer(true);

	}

	@Test
	public void testInitialise() {
		assertTrue("Does not start waiting for player", this.model.getWaitingForPlayer());
		assertEquals("model.players.size should equal 4", this.model.players.size(), 4);
		assertNotNull("getCurrentPlayer must not be null", this.model.getCurrentPlayer());
		assertNotNull("model.board must not be null", this.model.getBoard());
	}

	@Test
	public void testStartPlayerTurn() {
		this.controller.startPlayerTurn();
		assertEquals("model.players.size should equal 3 after starting turn", this.model.players.size(), 3);
		assertNotEquals("model.diceRoll should not be zero after starting turn", this.model.getDiceRoll(), 0);
	}

	@Test
	public void testPlayerSimulatedValidMove() {
		this.controller.startPlayerTurn();
	}
}
