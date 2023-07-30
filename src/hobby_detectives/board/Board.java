package hobby_detectives.board;

import java.util.*;

import hobby_detectives.board.world.Tile;
import hobby_detectives.data.CharacterType;
import hobby_detectives.data.RoomType;
import hobby_detectives.data.WeaponType;
import hobby_detectives.engine.Position;
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
	public Tile [][] board;
	public Scanner inputScanner = new Scanner(System.in);

	//remeber players num are not always static can be 3 || 4 and need to figure out how to do these fkn rooms
	public Board(int boardSize) {

		this.boardSize = boardSize;
		this.board = new Tile[boardSize][boardSize];
		//initilisation of the board
		for(int row = 0 ; row < boardSize ; row++){
			for(int col = 0 ; col < boardSize ; col++){
				this.board[row][col] = new Tile(new Position(row, col));
			}
		}

		this.players = new ArrayDeque<>();

		//var weaponCards =
				Arrays.stream(WeaponType.values()).map(WeaponCard::new).toList();
		var roomCards = Arrays.stream(RoomType.values()).map(RoomCard::new).toList();

		players.addAll(Arrays.stream(CharacterType.values()).map(Player::new).toList());

		//putting players on the board (defo a better way to do this)
		int start = 6;
		for(Player p : players) {
			this.board[start][0].setPlayer(p);
			p.setTile(this.board[start][0]);
			start+= 3;
		}
	}
	public static void clear(){
		for(int clear = 0; clear < 1000; clear++)
		{
			System.out.println("\b") ;
		}
	}

	public void draw() {
		System.out.println("_________________________________________________");
		for (int col = 0; col < boardSize; col++) {
			for (int row = 0; row < boardSize; row++) {
				System.out.print(this.board[row][col].render());

			}
			System.out.println("|");
		}
		System.out.println("\n_________________________________________________");
	}

	public void runGame() {
		while (true) {
			turn();
		}
	}

	/**
	 * Process the input of the player and move it to the correct position on the board
	 * @param input where player moves
	 */
	void processInput(String input, Player p){
		System.out.println(input + p.toString());
		Position playerPos = p.getTile().getPosition();
		//remove player from board (could cause issues if inccorect input is put in)
		this.board[playerPos.x()][playerPos.y()].setPlayer(null);


		playerPos = getPosition(input, playerPos);
		//put player on new pos
		p.setTile(this.board[playerPos.x()][playerPos.y()]);
		this.board[playerPos.x()][playerPos.y()].setPlayer(p);
	}

	public boolean invalidInput(String input, Player player) {
		Position position = player.getTile().getPosition();
		position = getPosition(input, position);
		return position.x() < 0 || position.x() > boardSize - 1 || position.y() < 0 || position.y() > boardSize - 1 || board[position.x()][position.y()].occupant.isPresent();
	}

	private Position getPosition(String input, Position position) {
		for(char token : input.toLowerCase().toCharArray()){
			switch(token){
				case 'l' -> position = new Position(position.x() - 1, position.y());
				case 'r' -> position = new Position(position.x() + 1, position.y());
				case 'u' -> position = new Position(position.x(), position.y() - 1);
				case 'd' -> position = new Position(position.x(), position.y() + 1);
			}
		}
		return position;
	}

	public void turn() {
		clear();
		Player player = this.players.poll();
		System.out.println("It is " + player.getCharacter().toString() + "'s turn to play.");
		var dice = random.nextInt(2, 13);
		draw();
		System.out.println("Above shows your position on the board!");
		System.out.println("You have rolled " + dice + ". Type your moves as a string, i.e. 'LLUUR' for left-left-up-up-right.");
		var input = inputScanner.next();

		if (input.length() != dice) {
			while (input.length() > dice) {
				System.out.println("That is not the correct length. Your input must have " + dice + " inputs.");
				input = inputScanner.next();
			}
		} else if(invalidInput(input, player)) {
			while (invalidInput(input, player)) {
				System.out.println("Input invalid, would result in character going out of bounds or within another player. Your input must stay within game borders and not obstruct other players, please re-enter:");
				input = inputScanner.next();
			}
		}
		processInput(input, player);
		this.players.add(player);
	}
}
