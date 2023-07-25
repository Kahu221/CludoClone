package hobby_detectives.board;

import java.util.ArrayDeque;
import java.util.Queue;

import hobby_detectives.player.Player;

public class Board {
	private final int boardSize;
	private final Queue<Player> players;
	public Board(int boardSize) {
		this.boardSize = boardSize;
		this.players = new ArrayDeque<>();
	}
}
