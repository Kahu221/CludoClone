package hobby_detectives.game;

import hobby_detectives.board.Board;
import hobby_detectives.board.world.Estate;
import hobby_detectives.board.world.Tile;
import hobby_detectives.board.world.UnreachableArea;
import hobby_detectives.data.CharacterType;
import hobby_detectives.data.Direction;
import hobby_detectives.data.EstateType;
import hobby_detectives.data.WeaponType;
import hobby_detectives.engine.Position;
import hobby_detectives.player.Player;

import java.util.*;
import java.util.stream.IntStream;

public class Game {
    public final Queue<Player> players;
    private final Board board;
    private final Scanner inputScanner = new Scanner(System.in);
    private final Random random = new Random();
    private final CardTriplet correctTriplet;
    private boolean running = false;

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
            Player currentPlayer = new Player(CharacterType.values()[i], new ArrayList<Card>());
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
        Collections.shuffle(playerCards);
        List<WeaponCard> weaponCards = new ArrayList<>(Arrays.stream(WeaponType.values()).map(WeaponCard::new).toList());
        Collections.shuffle(weaponCards);
        List<EstateCard> estateCards = new ArrayList<>(Arrays.stream(EstateType.values()).map(EstateCard::new).toList());
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

//        Bert (1,9)
//        Lucina (11,1)
//        Malina (9,22)
//        Percy (22,14)


//        Collections.shuffle(remainingCards);
//
//        var cardsPerPlayer = (int) Math.ceil(remainingCards.size() / (float) numPlayers);
//
//        var playerSeeds = new HashMap<Player, Position>();
//        for (var playerEntry : playerSeeds.entrySet()) {
//            this.players.add(playerEntry.getKey());
//            board.read(playerEntry.getValue()).setPlayer(playerEntry.getKey());
//            playerEntry.getKey().setTile(board.read(playerEntry.getValue()));
//        }
        /*
        Starting positions:
        Bert (1,9)
        Lucina (11,1)
        Malina (9,22)
        Percy (22,14)

                playerSeeds.put(new Player(CharacterType.BERT,
                remainingCards.subList(0, Math.min(remainingCards.size(), cardsPerPlayer))), new Position(1, 9));
        playerSeeds.put(new Player(CharacterType.LUCINA,
                remainingCards.subList(cardsPerPlayer, Math.min(remainingCards.size(), cardsPerPlayer * 2))), new Position(11, 1));
        playerSeeds.put(new Player(CharacterType.MALINA,
                remainingCards.subList(cardsPerPlayer * 2, Math.min(remainingCards.size(), cardsPerPlayer * 3))), new Position(9, 22));
        if (numPlayers == 4) playerSeeds.put(new Player(CharacterType.PERCY,
                remainingCards.subList(cardsPerPlayer * 3, Math.min(remainingCards.size(), cardsPerPlayer * 4))), new Position(22, 14));

         */
    }

    public void run() {
        running = true;
        while (running) {
            turn();
        }
    }

    public boolean askBoolean(String prompt) {
        Optional<Boolean> hasResult = Optional.ofNullable(null);
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


    public WeaponCard promptWeapon(Player p) {
        WeaponCard weaponGuessed = null;
        while (weaponGuessed == null) {
            System.out.println("Which weapon are you going to guess?");
            for (Card w : p.getCards().stream().filter(e -> e instanceof WeaponCard).toList()) {
                System.out.println("- " + ((WeaponCard) w).weapon);
            }

            var tWeaponGuessed = WeaponType.valueOf(inputScanner.next().toUpperCase());
            weaponGuessed = (WeaponCard) p.getCards().stream()
                    .filter(e -> e instanceof WeaponCard ex && ex.weapon.equals(tWeaponGuessed))
                    .findFirst().orElse(null);
        }
        return weaponGuessed;
    }

    public EstateCard promptEstate(Player p) {
        EstateCard estateGuessed = null;
        while (estateGuessed == null) {
            System.out.println("What estate are you going to guess?");
            for (Card e : p.getCards().stream().filter(e -> e instanceof EstateCard).toList()) {
                System.out.println("- " + ((EstateCard) e).estate);
            }

            var tEstateGuessed = EstateType.valueOf(inputScanner.next().toUpperCase());
            estateGuessed = (EstateCard) p.getCards().stream()
                    .filter(e -> e instanceof EstateCard ex && ex.estate.equals(tEstateGuessed))
                    .findFirst().orElse(null);
        }
        return estateGuessed;
    }

    public PlayerCard promptPlayer(Player p) {
        PlayerCard characterGuessed = null;
        while (characterGuessed == null) {
            System.out.println("What character are you going to guess?");
            for (Card e : p.getCards().stream().filter(e -> e instanceof PlayerCard).toList()) {
                System.out.println("- " + e);
            }
            var tCharacterGuessed = CharacterType.valueOf(inputScanner.next().toUpperCase());
            characterGuessed = (PlayerCard) p.getCards().stream()
                    .filter(e -> e instanceof PlayerCard ex && ex.character.equals(tCharacterGuessed))
                    .findFirst().orElse(null);
        }
        return characterGuessed;
    }

    public CardTriplet promptPlayerForCardTriplet(Player p) {
        return new CardTriplet(promptWeapon(p), promptEstate(p), promptPlayer(p));
    }

    public void changeTo(Player player) {
        System.out.println("Pass the tablet to " + player + " and then press ENTER.");
        inputScanner.nextLine();
    }

    public void promptPlayerForGuess(Player p, Estate estate) {
        System.out.println("You are in " + estate.type + ", which has the weapon " + estate.weapon + ".");
        if (askBoolean("Would you like to make a guess? Type 'yes' to guess, and anything else to skip.")) {
            WeaponCard weapon = promptWeapon(p);
            PlayerCard player = promptPlayer(p);

            // Refutation
            var anyPlayerRefuted = false;
            CharacterType[] guessOrder = {CharacterType.LUCINA, CharacterType.BERT, CharacterType.MALINA, CharacterType.PERCY};
            for (CharacterType refuter : guessOrder) {
                if (refuter.equals(p.getCharacter())) continue;
                Player refuterPlayer = players.stream().filter(e -> e.getCharacter().equals(refuter)).findFirst().get();
                if (!refuterPlayer.getAllowedToGuess()) continue;
                var possibleRefutationCards =
                        refuterPlayer.getCards().stream()
                                .filter(refuterCard -> weapon == refuterCard || player == refuterCard || new EstateCard(estate.type).equals(refuterCard))
                                .toList();
                if (!possibleRefutationCards.isEmpty()) {
                    changeTo(refuterPlayer);
                    System.out.println("It is your turn to refute.");
                    Card refutedCard = null;
                    while (refutedCard == null) {
                        System.out.println("Please select one of the following cards to refute:");
                        for (var refuteCard : possibleRefutationCards) {
                            System.out.println("- " + refuteCard);
                        }
                        String cardName = inputScanner.next();
                        refutedCard = possibleRefutationCards.stream().filter(e -> e.toString().equals(cardName)).findFirst().orElse(null);
                        if (refutedCard != null) {
                            anyPlayerRefuted = true;
                        }
                    }
                }
            }
            if (!anyPlayerRefuted) {
                changeTo(p);
                board.unrefutedCards.add(weapon);
                board.unrefutedCards.add(player);
                board.unrefutedCards.add(new EstateCard(estate.type));
                if (askBoolean("Your guess was unrefuted. Would you like to attempt to solve, using your guess?")) {
                    if (attemptSolve(p, new CardTriplet(weapon, new EstateCard(estate.type), player))) {
                    }
                }
            }
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
        inputScanner.nextLine();

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
