package hobby_detectives.game;

import hobby_detectives.board.Board;
import hobby_detectives.board.world.Estate;
import hobby_detectives.board.world.Tile;
import hobby_detectives.board.world.UnreachableArea;
import hobby_detectives.data.*;
import hobby_detectives.engine.Position;
import hobby_detectives.player.Player;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Holds the logic and data for the abstract game.
 */
public class Game {
    /**
     * The players in the game.
     */
    public final Queue<Player> players;
    private final Board board;
    private final Scanner inputScanner = new Scanner(System.in);
    private final Random random = new Random();
    private final CardTriplet correctTriplet;
    private boolean running = false;

    /**
     * All cards in play.
     */
    public Map<String, Card> allCards = new HashMap<>();

    public Game() {
        board = new Board(24, this);
        System.out.println("How many players are playing? (3 or 4)");

        var numPlayers = inputScanner.nextInt();
        if (numPlayers < 3 || numPlayers > 4) {
            System.out.println("That is not a valid player size.");
            System.exit(0);
        }
        //initililze players
        var playerSeeds = new ArrayList<Player>();
        IntStream.range(0, numPlayers).forEach(i -> {
            Player currentPlayer = new Player(CharacterType.values()[i], new ArrayList<Card>(), CharacterColors.values()[i].getColor());
            playerSeeds.add(currentPlayer);
            switch (CharacterType.values()[i]) {
                case LUCINA -> {
                    board.read(new Position(11, 1)).setPlayer(currentPlayer);
                    currentPlayer.setTile(board.read(new Position(11, 1)));
                }
                case BERT -> {
                    board.read(new Position(1, 9)).setPlayer(currentPlayer);
                    currentPlayer.setTile(board.read(new Position(1, 9)));
                }
                case MALINA -> {
                    board.read(new Position(9, 22)).setPlayer(currentPlayer);
                    currentPlayer.setTile(board.read(new Position(9, 22)));
                }
                case PERCY -> {
                    board.read(new Position(22, 14)).setPlayer(currentPlayer);
                    currentPlayer.setTile(board.read(new Position(22, 14)));
                }
                default -> throw new Error();
            }
        });

        // initialize cards and correct cards
        List<PlayerCard> playerCards = new ArrayList<>(Arrays.stream(CharacterType.values()).map(PlayerCard::new).toList());
        for(Card c : playerCards) this.allCards.put(c.toString(), c);
        Collections.shuffle(playerCards);

        List<WeaponCard> weaponCards = new ArrayList<>(Arrays.stream(WeaponType.values()).map(WeaponCard::new).toList());
        Collections.shuffle(weaponCards);
        for(Card c : weaponCards) this.allCards.put(c.toString(), c);

        List<EstateCard> estateCards = new ArrayList<>(Arrays.stream(EstateType.values()).map(EstateCard::new).toList());
        for(Card c : estateCards) this.allCards.put(c.toString(), c);
        Collections.shuffle(estateCards);

        correctTriplet = new CardTriplet(
                weaponCards.remove(0),
                estateCards.remove(0),
                playerCards.remove(0)
        );

        var remainingCards = new ArrayDeque<Card>();
        remainingCards.addAll(playerCards);
        remainingCards.addAll(weaponCards);
        remainingCards.addAll(estateCards);
        int counter = 0;
        while (!remainingCards.isEmpty()) {
            Card current = remainingCards.pop();
            playerSeeds.get(counter).addCard(current);
            counter++;
            if (counter >= numPlayers) counter = 0;
        }


        players = new ArrayDeque<>(playerSeeds);
    }

    /**
     * Runs the game. This method will block until the end of the game.
     */
    public void run() {
        running = true;
        while (running) {
            turn();
        }
    }

    /**
     * Asks the user for a boolean value, by continuously prompting the given prompt
     * until a valid boolean value is entered.
     */
    public boolean askBoolean(String prompt) {
        Optional<Boolean> hasResult = Optional.empty();
        while (hasResult.isEmpty()) {
            System.out.println(prompt);
            var input = inputScanner.next().toLowerCase();
            if (input.equals("true") || input.equals("t") || input.equals("yes") || input.equals("y")) {
                hasResult = Optional.of(true);
            } else if (input.equals("false") || input.equals("f") || input.equals("no") || input.equals("n")) {
                hasResult = Optional.of(false);
            }
        }
        return hasResult.get();
    }


    /**
     * Prompts the user for a weapon.
     */
    public WeaponCard promptWeapon() {
        WeaponCard weaponGuessed = null;
        while (weaponGuessed == null) {
            System.out.println("Which weapon are you going to guess? (case sensitive)");
            for(WeaponType w : WeaponType.values()) System.out.println("- " + w.toString());
            String weaponType = "";
            try {
                weaponType = WeaponType.valueOf(inputScanner.next().toUpperCase()).toString();
            } catch (Exception ignored) {}

            weaponGuessed = (WeaponCard) allCards.getOrDefault(weaponType, null);
        }
        return weaponGuessed;
    }

    /**
     * Prompts the user for an estate.
     */
    public EstateCard promptEstate() {
        EstateCard estateGuessed = null;
        while (estateGuessed == null) {
            System.out.println("What estate are you going to guess? (case sensitive)");
            for(EstateType e: EstateType.values()) System.out.println("- " + e.toString());
            String estateType = "";
            try {
                estateType = EstateType.valueOf(inputScanner.next().toUpperCase()).toString();
            } catch (Exception ignored) {}

            estateGuessed = (EstateCard)  allCards.getOrDefault(estateType, null);
        }
        return estateGuessed;
    }

    /**
     * Prompts the user for a player card.
     */
    public PlayerCard promptPlayer(Player currentPlayer) {
        PlayerCard characterGuessed = null;
        while (characterGuessed == null) {

           System.out.println("What character are you going to guess? (case sensitive)");
           Arrays.stream(CharacterType.values()).filter(type -> !currentPlayer.getCharacter().equals(type))
                   .forEach(type -> System.out.println(type.toString()));
           String characterType = "";

           try {
               characterType = CharacterType.valueOf(inputScanner.next().toUpperCase()).toString();
           } catch (Exception ignored) {}
           characterGuessed = (PlayerCard) allCards.getOrDefault(characterType,null);
        }
        return characterGuessed;
    }

    /**
     * Changes the current player to the specified player, by prompting the current player
     * to pass the device to the next player.
     */
    public void changeTo(Player player) {
        System.out.println("Pass the tablet to " + player + " and then press ENTER.");
        inputScanner.nextLine();
    }

    /**
     * Prompts the player if they would like to make a guess, and then goes through
     * the logic for making a guess.
     * @param p The current player.
     * @param estate The current estate that this player is in.
     */
    public void promptPlayerForGuess(Player p, Estate estate) {
        System.out.println("You are in " + estate.type + ", which has the weapon " + estate.weapon + ".");
        if (askBoolean("Would you like to make a guess? Type 'yes' to guess, and anything else to skip.")) {
            WeaponCard weapon = promptWeapon();
            PlayerCard player = promptPlayer(p);

            // Refutation
            var anyPlayerRefuted = false;
            List<Card> refutedCards = new ArrayList<>();
            Player firstRefute = players.peek();
            Player currentRefute;

            //after guess rotate through the players 1 by one checking weather they
            //contain any cards made in the guess (containing a card will put it
            // into the list etc...)
            do{
                currentRefute = players.poll();
                System.out.println("it is " + currentRefute + "Turn to refute");
                changeTo(currentRefute);
                List<Card> containedCards = new ArrayList<>();
                //can condense this but brain not work
                for(Card c : currentRefute.getCards()) {
                    if(c == weapon || c == player || c == allCards.get(estate.type.toString())) containedCards.add(c);
                }
                if(containedCards.size() == 0) {
                    players.offer(currentRefute);
                    continue;
                }
                //can only refute this card so must return
                if(containedCards.size() == 1){
                    players.offer(currentRefute);
                    refutedCards.add(containedCards.get(0));
                    continue;
                }
                //TODO needs to be tested that the picking card works
                System.out.println("Please type a card to be refuted from the following:");
                for(Card c : containedCards) System.out.println("- " + c.toString());
                Card cardToRefute = null;
                while (cardToRefute == null){
                    cardToRefute = allCards.getOrDefault(inputScanner.next().toUpperCase(),null);
                    //making sure the player cant just call any random card
                    if(!containedCards.contains(cardToRefute)) {
                        System.out.println("Please choose a card that is listed!");
                        cardToRefute = null;
                    }
                }
                containedCards.add(cardToRefute);
                players.offer(currentRefute);
            }while(players.peek() != firstRefute);
            changeTo(p);
            //if refuted cards are empty, allow for solve attempt
            if(refutedCards.isEmpty()) {
                if (askBoolean("Your guess was unrefuted, would you like to solve?")) {
                    attemptSolve(p, new CardTriplet(promptWeapon(), promptEstate(), promptPlayer(p)));
                }
            }
            System.out.println("Your refuted cards are shown below\n--------------------");
            for(Card c : refutedCards) System.out.println("- " + c.toString());
            System.out.println("--------------------");
        }
    }

    public boolean attemptSolve(Player p, CardTriplet solveattempt) {
        if (this.correctTriplet.equals(solveattempt)) {
            System.out.println("Your guess was correct. You have won.");
            this.running = false;
            return true;
        } else {
            System.out.println("Your guess was incorrect. You are no longer able to make solve attempts.");
            p.setAllowedToGuess(false);
            return false;
        }
    }

    /**
     * Processes a single turn in the game.
     * This method blocks while waiting for input.
     */
    public void turn() {
        Player player = players.poll();
        changeTo(player);

        var dice = random.nextInt(2, 13);
        board.draw();
        System.out.println("You have the following cards: " + player.getCards());

        if (player.getTile() instanceof Estate e) {
            promptPlayerForGuess(player, e);
        }
        System.out.println("You have rolled " + dice + ". Type your moves as a string, i.e. 'LLUUR' for left-left-up-up-right.");
        var input = inputScanner.nextLine();

        while (invalidInput(input, player, dice)) {
            System.out.println("Please re-enter");
            input = inputScanner.nextLine();
        }

        processInput(input, player);
        this.players.add(player);
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

        if (p.getTile() instanceof Estate estate) {
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
        Tile t = board.read(possibleTranslate);

        // Check if the player is attempting to move into an estate.
        if (t instanceof Estate e) {
            board.tryMoveIntoEstate(p, possibleTranslate, e);
            // Check if the player is attempting to move into an estate fill tile.
        } else if (t instanceof Estate.EstateFillTile eft) {
            board.tryMoveIntoEstate(p, possibleTranslate, eft.parent);
        } else {
            t.setPlayer(p);
            p.setTile(t);
        }
    }


    /**
     * Validates an input string as a possible move by the provided player.
     *
     * @param input  The user input to validate.
     * @param player The player who is currently playing.
     * @return A boolean representing whether the player's input is valid.
     */
    public boolean invalidInput(String input, Player player, int dice) {
        input = input.toLowerCase();
        Optional<Position> positionOptional = board.getPosition(input, player.getTile().getPosition());

        if (input.length() > dice) {
            System.out.println("That is not the correct length. Your input must have " + dice + " or less inputs");
            return true;
        }

        if (player.getTile() instanceof Estate e) {
            List<Character> possibleDirections = e.doorDirections.stream().map(Direction::label).toList();
            Character directionPlayerWantsToLeaveEstate = input.charAt(0);

            if (!possibleDirections.contains(directionPlayerWantsToLeaveEstate)) {
                System.out.println("You cannot exit " + e + " in that direction");
                return true;
            }
        }

        if (positionOptional.isEmpty()) {
            System.out.println("You have entered an invalid input e.g. rgl");
            return true;
        }
        Position position = positionOptional.get();
        Tile tile = board.read(position);

        if (tile == null) {
            System.out.println("Your move would make you go out of bounds");
            return true;
        }
        if (tile.occupant.isPresent()) {
            System.out.println("Your move would obstruct another player");
            return true;
        }
        if (tile instanceof UnreachableArea) {
            System.out.println("Your move would place you in an unreachable area");
            return true;
        }
        if (tile instanceof Estate) {
            System.out.println("Your move would place you on an estate");
            return true;
        }
        if (tile instanceof Estate.EstateFillTile eft) {
            int count = 0;
            for (Position p : eft.parent.doors) {
                if (p.add(eft.parent.getPosition()).equals(position)) {
                    count++;
                }
            }
            if (count == 0) {
                System.out.println("Your move would place you on an estate");
                return true;
            }
            return false;
        }
        return false;
    }
}
