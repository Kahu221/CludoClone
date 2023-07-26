package hobby_detectives.board;

import java.util.*;

import hobby_detectives.data.CharacterType;
import hobby_detectives.data.RoomType;
import hobby_detectives.data.WeaponType;
import hobby_detectives.game.PlayerCard;
import hobby_detectives.game.RoomCard;
import hobby_detectives.game.WeaponCard;
import hobby_detectives.player.Player;

public class Board {
	private final int boardSize;
	private final Queue<Player> players;

	public PlayerCard correctPlayer;
	public WeaponCard correctWeapon;
	public RoomCard correctRoom;

	public Board(int boardSize) {
		this.boardSize = boardSize;
		this.players = new ArrayDeque<>();

		var weaponCards =
				Arrays.stream(WeaponType.values()).map(WeaponCard::new).toList();
		var roomCards = Arrays.stream(RoomType.values()).map(RoomCard::new).toList();
		var playerCards = Arrays.stream(CharacterType.values()).toList();
	}
}
