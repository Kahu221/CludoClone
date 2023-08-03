package hobby_detectives.board;

import hobby_detectives.board.world.Estate;
import hobby_detectives.board.world.Tile;
import hobby_detectives.board.world.UnreachableArea;
import hobby_detectives.data.CharacterType;
import hobby_detectives.data.Direction;
import hobby_detectives.data.EstateType;
import hobby_detectives.data.WeaponType;
import hobby_detectives.engine.Position;
import hobby_detectives.game.*;
import hobby_detectives.player.Player;

import java.util.*;

public class Board {
    private final int boardSize;
    private final Random random = new Random();
    private final Queue<Player> players;
    private final List<Estate> estates;
    private final CardTriplet correctTriplet;
    private boolean gameRunning = true;
    private final Tile[][] board;
    private final Scanner inputScanner = new Scanner(System.in);

    //remeber players num are not always static can be 3 || 4 and need to figure out how to do these fkn rooms
    public Board(int boardSize) {
        this.boardSize = boardSize;
        this.board = new Tile[boardSize][boardSize];
        //initilisation of the board
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                this.board[row][col] = new Tile(new Position(row, col));
            }
        }

        this.players = new ArrayDeque<>();

        var playerSeeds = new HashMap<Player, Position>();
        playerSeeds.put(new Player(CharacterType.BERT), new Position(1, 9));
        playerSeeds.put(new Player(CharacterType.LUCINA), new Position(11, 1));
        playerSeeds.put(new Player(CharacterType.MALINA), new Position(9, 22));
        playerSeeds.put(new Player(CharacterType.PERCY), new Position(22, 14));

        for (var playerEntry : playerSeeds.entrySet()) {
            this.players.add(playerEntry.getKey());
            this.board[playerEntry.getValue().x()][playerEntry.getValue().y()].setPlayer(playerEntry.getKey());
            playerEntry.getKey().setTile(this.board[playerEntry.getValue().x()][playerEntry.getValue().y()]);
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

        // initialize cards and correct cards
        List<PlayerCard> playerCards = this.players.stream().map(PlayerCard::new).toList();
        Collections.shuffle(playerCards);
        List<WeaponCard> weaponCards = Arrays.stream(WeaponType.values()).map(WeaponCard::new).toList();
        Collections.shuffle(weaponCards);
        List<EstateCard> estateCards = estates.stream().map(estate -> new EstateCard(estate.type)).toList();
        Collections.shuffle(estateCards);

        correctTriplet = new CardTriplet(
                weaponCards.remove(0),
                estateCards.remove(0),
                playerCards.remove(0)
        );

        int playerSize = this.players.size();
    }

    private void applyUnreachableArea(Position origin, int width, int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                board[origin.x() + x][origin.y() + y] = new UnreachableArea(origin.add(new Position(x, y)));
            }
        }
    }

    private static void clear() {
        for (int clear = 0; clear < 1000; clear++) {
            System.out.println("\b");
        }
    }

    private void draw() {
        System.out.println("_________________________________________________");
        for (int col = 0; col < boardSize; col++) {
            for (int row = 0; row < boardSize; row++) {
                System.out.print(this.board[row][col].render());
            }
            System.out.println("|");
        }
        System.out.println("\n_________________________________________________");
    }

    /**
     * Runs the game, looping a sequence of turns until a victory state is achieved.
     * This method will block until the completion of the game.
     */
    public void runGame() {
        while (gameRunning) {
            turn();
        }
    }

    private void tryMoveIntoEstate(Player p, Position possibleTranslate, Estate e) {
        // Player is at a door.
        if (e.doors.stream().anyMatch(d -> d.add(e.getPosition()).equals(possibleTranslate))) {
            if (e.occupant.isEmpty()) {
                e.setPlayer(p);
                p.setTile(e);
                System.out.println(p.getCharacter() + " has entered " + e.type + ".");
            }
        }
    }

    /**
     * Reads the given position from the board. This method returns null if the position is out of bounds.
     */
    public Tile read(Position p) {
        if (p.x() >= this.boardSize || p.x() < 0 || p.y() >= this.boardSize || p.y() < 0) return null;
        return this.board[p.x()][p.y()];
    }

    /**
     * Validates an input string as a possible move by the provided player.
     *
     * @param input  The user input to validate.
     * @param player The player who is currently playing.
     * @return A boolean representing whether the player's input is valid.
     */
    public boolean invalidInput(String input, Player player) {
        input = input.toLowerCase();
        Optional<Position> positionOptional = getPosition(input, player.getTile().getPosition());

        if(player.getTile() instanceof Estate e) {
            List<Character> possibleDirections = e.doorDirections.stream().map(Direction::label).toList();
            Character directionPlayerWantsToLeaveEstate = input.charAt(0);
            return !possibleDirections.contains(directionPlayerWantsToLeaveEstate);
        }

        if (positionOptional.isEmpty()) {
            return true;
        }
        Position position = positionOptional.get();
        Tile tile = read(position);

        if(tile == null) {
            return true;
        }
        if(tile.occupant.isPresent()) {
            return true;
        }
        if(tile instanceof Estate) {
            return true;
        }
        if(tile instanceof Estate.EstateFillTile eft) {
            int count = 0;
            for (Position p : eft.parent.doors) {
                if(p.add(eft.parent.getPosition()).equals(position)) {
                    count++;
                }
            }
            return count == 0;
        }
        return false;
    }

    private Optional<Position> getPosition(String input, Position position) {
        for(char token : input.toLowerCase().toCharArray()){
            switch(token){
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

    /**
     * Processes a single turn in the game.
     * This method blocks while waiting for input.
     */
    private void turn() {
        //clear();
        Player player = this.players.poll();
        System.out.println("It is " + player.getCharacter().toString() + "'s turn to play.");

        var dice = random.nextInt(2, 13);
        draw();
        if (player.getTile() instanceof Estate e) {
            promptPlayerForGuess(player, e);
        }
        System.out.println("You have rolled " + dice + ". Type your moves as a string, i.e. 'LLUUR' for left-left-up-up-right.");
        var input = inputScanner.nextLine();

        while (input.length() > dice || invalidInput(input, player)) {
            if (input.length() > dice) {
                System.out.println("That is not the correct length. Your input must have " + dice + " or less inputs.");
                input = inputScanner.nextLine();
            } else if(invalidInput(input, player)) {
                System.out.println("""
                        Input invalid, would result in character going out of bounds or within another player.\s
                        Make sure there is a door in the direction you intend to leave if you are in an Estate!
                        Your input must stay within game borders and not obstruct other players, please re-enter:""");
                input = inputScanner.nextLine();
            }
        }
        
        processInput(input, player);
        this.players.add(player);
    }

    public CardTriplet promptPlayerForCardTriplet(Player p) {
        WeaponCard weaponGuessed = null;
        while (weaponGuessed == null) {
            System.out.println("Which weapon are you going to guess?");
            for (Card w : p.getCards().stream().filter(e -> e instanceof WeaponCard).toList()) {
                System.out.println("- " + ((WeaponCard) w).weapon);
            }

            var tWeaponGuessed = WeaponType.valueOf(inputScanner.next());
            weaponGuessed = (WeaponCard) p.getCards().stream()
                    .filter(e -> e instanceof WeaponCard ex && ex.weapon.equals(tWeaponGuessed))
                    .findFirst().orElse(null);
        }
        EstateCard estateGuessed = null;
        while (estateGuessed == null) {
            System.out.println("What estate are you going to guess?");
            for (Card e : p.getCards().stream().filter(e -> e instanceof EstateCard).toList()) {
                System.out.println("- " + ((EstateCard) e).estate);
            }

            var tEstateGuessed = EstateType.valueOf(inputScanner.next());
            estateGuessed = (EstateCard) p.getCards().stream()
                    .filter(e -> e instanceof EstateCard ex && ex.estate.equals(tEstateGuessed))
                    .findFirst().orElse(null);
        }

        PlayerCard characterGuessed = null;
        while (characterGuessed == null) {
            System.out.println("What character are you going to guess?");
            for (Card e : p.getCards().stream().filter(e -> e instanceof PlayerCard).toList()) {
                System.out.println("- " + ((PlayerCard) e).player);
            }
            var tCharacterGuessed = CharacterType.valueOf(inputScanner.next());
            characterGuessed = (PlayerCard) p.getCards().stream()
                    .filter(e -> e instanceof PlayerCard ex && ex.player.getCharacter().equals(tCharacterGuessed))
                    .findFirst().orElse(null);
        }

        return new CardTriplet(weaponGuessed, estateGuessed, characterGuessed);
    }

    public void promptPlayerForGuess(Player p, Estate estate) {
        System.out.println("You are in " + estate.type + ", which has the weapon " + estate.weapon + ".");
        System.out.println("Would you like to make a guess? Type 'yes' to guess, and anything else to skip.");
        var input = inputScanner.next().toLowerCase();
        if (input.equals("yes") || input.equals("y")) {
            CardTriplet triplet = promptPlayerForCardTriplet(p);

            if (this.correctTriplet.equals(triplet)) {
                System.out.println(p.getCharacter() + " has won the game!");
                gameRunning = false;
                return;
            }

            // Refutation
            CharacterType[] guessOrder = {CharacterType.LUCINA, CharacterType.BERT, CharacterType.MALINA, CharacterType.PERCY};
            for (CharacterType refuter : guessOrder) {
                Player refuterPlayer = this.players.stream().filter(e -> e.getCharacter().equals(refuter)).findFirst().get();
                if (!refuterPlayer.getAllowedToGuess()) continue;
                System.out.println("It is " + refuter + "'s turn to refute.");
                var possibleRefutationCards =
                        refuterPlayer.getCards().stream().filter(refuterCard -> triplet.contains(refuterCard)).toList();
                if (!possibleRefutationCards.isEmpty()) {
                    System.out.println("Please select one of the following cards to refute:");
                    for (var refuteCard : possibleRefutationCards) {
                        System.out.println("- " + refuteCard);
                    }
                }
            }
        }
    }

    /**
     * Processes the input of the player and move it to the correct position on the board.
     *
     * @param input
     */
    private void processInput(String input, Player p) {
        Queue<Character> inputQueue = new ArrayDeque<>();
        for (char i : input.toLowerCase().toCharArray()) {
            inputQueue.add(i);
        }

        /* Check if the player is currently in an estate */
        /* If they are we move them to a new location dependant on */
        /* The door they leave through */

        if(p.getTile() instanceof Estate estate) {
            Character token = inputQueue.poll();
            var playerPos = p.getTile().getPosition();
            p.getTile().setPlayer(null);

            List<Character> possibleDirections = estate.doorDirections.stream().map(Direction::label).toList();
            int tokenIndex = possibleDirections.indexOf(token);
            Position doorPosition = estate.doors.get(tokenIndex);

            Position possibleTranslate = switch (token) {
                case 'l' -> playerPos.add(new Position(-1 + doorPosition.x(), doorPosition.y()));
                case 'r' -> playerPos.add(new Position(1 + doorPosition.x(), doorPosition.y()));
                case 'u' -> playerPos.add(new Position(doorPosition.x(), -1 + doorPosition.y()));
                case 'd' -> playerPos.add(new Position(doorPosition.x(), 1 + doorPosition.y()));
                default -> playerPos;
            };

            movePlayerOnceByToken(possibleTranslate, p);
        }


        while (!inputQueue.isEmpty()) {
            char token = inputQueue.poll();
            var playerPos = p.getTile().getPosition();
            p.getTile().setPlayer(null);
            Position possibleTranslate = switch (token) {
                case 'l' -> playerPos.add(new Position(-1, 0));
                case 'r' -> playerPos.add(new Position(1, 0));
                case 'u' -> playerPos.add(new Position(0, -1));
                case 'd' -> playerPos.add(new Position(0, 1));
                default -> playerPos;
            };

            movePlayerOnceByToken(possibleTranslate, p);
        }
    }
    private void movePlayerOnceByToken(Position possibleTranslate, Player p) {
        // Discover what's on the board at that location
        Tile t = read(possibleTranslate);

        // Check if the player is attempting to move into an estate.
        if (t instanceof Estate e) {
            tryMoveIntoEstate(p, possibleTranslate, e);
            // Check if the player is attempting to move into an estate fill tile.
        } else if (t instanceof Estate.EstateFillTile eft) {
            tryMoveIntoEstate(p, possibleTranslate, eft.parent);
        } else {
            t.setPlayer(p);
            p.setTile(t);
        }
    }
}
