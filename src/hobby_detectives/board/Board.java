package hobby_detectives.board;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import hobby_detectives.board.world.Estate;
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
	private final List<Estate> estates;

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

		var roomCards = Arrays.stream(RoomType.values()).map(RoomCard::new).toList();

		var playerSeeds = new HashMap<Player, Position>();
		playerSeeds.put(new Player(CharacterType.BERT), new Position(1,9));
		playerSeeds.put(new Player(CharacterType.LUCINA), new Position(11,1));
		playerSeeds.put(new Player(CharacterType.MALINA), new Position(9,22));
		playerSeeds.put(new Player(CharacterType.PERCY), new Position(22,14));

		for (var playerEntry : playerSeeds.entrySet()) {
			this.players.add(playerEntry.getKey());
			this.board[playerEntry.getValue().x()][playerEntry.getValue().y()].setPlayer(playerEntry.getKey());
			playerEntry.getKey().setTile(this.board[playerEntry.getValue().x()][playerEntry.getValue().y()]);
		}

		var weapons = new ArrayList<>(List.of(WeaponType.values()));
		Collections.shuffle(weapons);

		// initialise estates
		estates = List.of(
				new Estate(new Position(2,2), 5, 5, RoomType.HAUNTED_HOUSE, weapons.remove(0),
						List.of(new Position(4,1), new Position(3, 4))),

				new Estate(new Position(2, 17), 5, 5, RoomType.CALAMITY_CASTLE, weapons.remove(0),
						List.of(new Position(1, 0), new Position(4, 1))),

				new Estate(new Position(9, 10), 6, 4, RoomType.VISITATION_VILLA, weapons.remove(0),
						List.of(new Position(0, 2), new Position(3, 0), new Position(2, 3), new Position(5, 1))),

				new Estate(new Position(17, 2), 5, 5, RoomType.MANIC_MANOR, weapons.remove(0),
						List.of(new Position(0, 3), new Position(3, 4))),

				new Estate(new Position(17, 17), 5, 5, RoomType.PERIL_PALACE, weapons.remove(0),
						List.of(new Position(1, 0), new Position(0, 3)))
		);

		for (Estate e : estates) {
			board[e.getPosition().x()][e.getPosition().y()] = e;
			for (Tile fill : e.generateFillTiles()) {
				board[fill.getPosition().x()][fill.getPosition().y()] = fill;
			}
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
	 * @param input
	 */
	void processInput(String input, Player p){
		Queue<Character> inputQueue = new ArrayDeque<>();
		for (char i : input.toLowerCase().toCharArray()) {
			inputQueue.add(i);
		}
		while (!inputQueue.isEmpty()) {
			char token = inputQueue.poll();
			var playerPos = p.getTile().getPosition();
			p.getTile().setPlayer(null);
			Position possibleTranslate = switch (token) {
				case 'l' -> playerPos.add(new Position(-1,0));
				case 'r' -> playerPos.add(new Position(1,0));
				case 'u' -> playerPos.add(new Position(0,-1));
				case 'd' -> playerPos.add(new Position(0,1));
				default -> playerPos;
			};

			// Discover what's on the board at that location
			Tile t = read(possibleTranslate);
			System.out.println(t);
			if (t instanceof Estate e) {
				tryMoveIntoEstate(p,possibleTranslate,e,t);
			} else if (t instanceof Estate.EstateFillTile eft) {
				tryMoveIntoEstate(p, possibleTranslate, eft.parent,t);
			}
		}
	}

	public void tryMoveIntoEstate(Player p, Position possibleTranslate, Estate e, Tile t) {
		// Player is at a door.
		if (e.doors.stream().anyMatch(d -> d.add(e.getPosition()).equals(possibleTranslate))) {
			if (e.occupant.isEmpty()) {
				e.setPlayer(p);
				p.setTile(t);
				System.out.println(p.getCharacter() + " has entered " + e.type + ".");
			}
		}
	}

	public Tile read(Position p) {
		if (p.x() >= this.boardSize || p.x() < 0 || p.y() >= this.boardSize || p.y() < 0) return null;
		return this.board[p.x()][p.y()];
	}

	public boolean validInput(String input, Player player) {
		Position position = player.getTile().getPosition();
		for(char token : input.toLowerCase().toCharArray()){
			switch(token){
				case 'l' -> position = new Position(position.x() - 1, position.y());
				case 'r' -> position = new Position(position.x() + 1, position.y());
				case 'u' -> position = new Position(position.x(), position.y() - 1);
				case 'd' -> position = new Position(position.x(), position.y() + 1);
				default -> {return false;}
			}
		}
		return position.x() >= 0 && position.x() <= boardSize - 1 && position.y() >= 0 && position.y() <= boardSize - 1;
	}

	public void turn() {
		//clear();
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
		} else if(!validInput(input, player)) {
			while (!validInput(input, player)) {
				System.out.println("Input invalid, would result in character going out of bounds. Your input must stay within game borders");
				input = inputScanner.next();
			}
		}
		processInput(input, player);
		this.players.add(player);
	}
}
