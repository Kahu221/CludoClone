package hobby_detectives.board;

import java.io.IOException;
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
	public Random random = new Random();
	private final Queue<Player> players;

	public PlayerCard correctPlayer;
	public WeaponCard correctWeapon;
	public RoomCard correctRoom;

	public Scanner inputScanner = new Scanner(System.in);

	public Board(int boardSize) {
		this.boardSize = boardSize;
		this.players = new ArrayDeque<>();

		var weaponCards =
				Arrays.stream(WeaponType.values()).map(WeaponCard::new).toList();
		var roomCards = Arrays.stream(RoomType.values()).map(RoomCard::new).toList();

		players.addAll(Arrays.stream(CharacterType.values()).map(Player::new).toList());
	}
	public static void clear(){
		for(int clear = 0; clear < 1000; clear++)
		{
			System.out.println("\b") ;
		}
	}

	public void draw() {
		clear();
		for (int x = 0; x < boardSize; x++) {
			for (int y = 0; y < boardSize; y++) {
				System.out.print("_");
			}
			System.out.println();
		}
	}

	public void runGame() {
		while (true) {
			turn();
		}
	}

	public void turn() {
		Player player = this.players.poll();
		System.out.println("It is " + player.getCharacter().toString() + "'s turn to play.");
		var dice = random.nextInt(2, 13);

		System.out.println("You have rolled " + dice + ". Type your moves as a string, i.e. 'LLUUR' for left-left-up-up-right.");
		var input = inputScanner.next();
		if (input.length() != dice) {
			while (input.length() != dice) {
				System.out.println("That is not the correct length. Your input must have " + dice + " inputs.");
				input = inputScanner.next();
			}
		}

		this.players.add(player);
	}
}
