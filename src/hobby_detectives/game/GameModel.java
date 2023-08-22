package hobby_detectives.game;

import hobby_detectives.board.Board;
import hobby_detectives.board.world.Estate;
import hobby_detectives.board.world.Tile;
import hobby_detectives.data.*;
import hobby_detectives.engine.Position;
import hobby_detectives.player.Player;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Holds the data for the abstract game.
 */
public class GameModel {
    private final PropertyChangeSupport observable = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.observable.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.observable.removePropertyChangeListener(listener);
    }

    /**
     * The players in the game.
     */
    public final Queue<Player> players;
    private final Board board;
    private final Scanner inputScanner = new Scanner(System.in);
    private final Random random = new Random();
    private final CardTriplet correctTriplet;
    private boolean running = false;

    private Player currentPlayer = null;
    public Player getCurrentPlayer() { return this.currentPlayer; }
    public void setCurrentPlayer(Player p) {
        Player oldPlayer = this.currentPlayer;
        this.currentPlayer = p;
        this.observable.firePropertyChange("currentPlayer", oldPlayer, p);
    }

    private boolean waitingForPlayer = false;
    public boolean getWaitingForPlayer() { return this.waitingForPlayer; }
    public void setWaitingForPlayer(boolean wfp) {
        boolean old = waitingForPlayer;
        this.waitingForPlayer = wfp;
        this.observable.firePropertyChange("waitingForPlayer", old, wfp);
    }

    /**
     * All cards in play.
     */
    public Map<String, Card> allCards = new HashMap<>();

    public GameModel() {
        board = new Board(24, this);
        var numPlayers = 4; // TODO not this

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
        currentPlayer = players.peek();
    }

    /**
     * Runs the game. This method will block until the end of the game.
     */
//    public void run() {
//        running = true;
//        while (running) {
//            turn();
//        }
//    }

    /**
     * Asks the user for a boolean value, by continuously prompting the given prompt
     * until a valid boolean value is entered.
     */
    //HERE
//    public boolean askBoolean(String prompt) {
//        Optional<Boolean> hasResult = Optional.empty();
//        while (hasResult.isEmpty()) {
//            System.out.println(prompt);
//            var input = scannerNext().toLowerCase();
//            if (input.equals("true") || input.equals("t") || input.equals("yes") || input.equals("y")) {
//                hasResult = Optional.of(true);
//            } else if (input.equals("false") || input.equals("f") || input.equals("no") || input.equals("n")) {
//                hasResult = Optional.of(false);
//            }
//        }
//        return hasResult.get();
//    }


    /**
     * Prompts the user for a weapon.
     */
    //here
//    public WeaponCard promptWeapon() {
//        WeaponCard weaponGuessed = null;
//        while (weaponGuessed == null) {
//            System.out.println("Which weapon are you going to guess? (case sensitive)");
//            for(WeaponType w : WeaponType.values()) System.out.println("- " + w.toString());
//            String weaponType = "";
//            try {
//                weaponType = WeaponType.valueOf(scannerNext().toUpperCase()).toString();
//            } catch (Exception ignored) {}
//
//            weaponGuessed = (WeaponCard) allCards.getOrDefault(weaponType, null);
//        }
//        return weaponGuessed;
//    }

    /**
     * Prompts the user for an estate.
     */
    //here
//    public EstateCard promptEstate() {
//        EstateCard estateGuessed = null;
//        while (estateGuessed == null) {
//            System.out.println("What estate are you going to guess? (case sensitive)");
//            for(EstateType e: EstateType.values()) System.out.println("- " + e.toString());
//            String estateType = "";
//            try {
//                estateType = EstateType.valueOf(scannerNext().toUpperCase()).toString();
//            } catch (Exception ignored) {}
//
//            estateGuessed = (EstateCard)  allCards.getOrDefault(estateType, null);
//        }
//        return estateGuessed;
//    }

    /**
     * Prompts the user for a player card.
     */
    //here
//    public PlayerCard promptPlayer(Player currentPlayer) {
//        PlayerCard characterGuessed = null;
//        while (characterGuessed == null) {
//
//           System.out.println("What character are you going to guess? (case sensitive)");
//           Arrays.stream(CharacterType.values()).filter(type -> !currentPlayer.getCharacter().equals(type))
//                   .forEach(type -> System.out.println(type.toString()));
//           String characterType = "";
//
//           try {
//               characterType = CharacterType.valueOf(scannerNext().toUpperCase()).toString();
//           } catch (Exception ignored) {}
//           characterGuessed = (PlayerCard) allCards.getOrDefault(characterType,null);
//        }
//        return characterGuessed;
//    }

    /**
     * Changes the current player to the specified player, by prompting the current player
     * to pass the device to the next player.
     */
//    public void changeTo(Player player) {
//        System.out.println("Pass the tablet to " + player + " and then press ENTER.");
//        scannerNewLine();
//    }

//    /** Method to create and close() a new instance of a Scanner for newLine input purposes
//     * Solves bugs and crashes caused by Scanner not being cleared from previous usage
//     * @return the output of the Scanner
//     */
//
//    public String scannerNewLine() {
//        Scanner newInputScanner = new Scanner(System.in);
//        return newInputScanner.nextLine();
//    }

//    /** Method to create and close() a new instance of a Scanner for next() input purposes
//     * Solves bugs and crashes caused by Scanner not being cleared from previous usage
//     * @return the output of the Scanner
//     */
//    public String scannerNext() {
//        Scanner newInputScanner = new Scanner(System.in);
//        return newInputScanner.next();
//    }
//    /**
//     * Prompts the player if they would like to make a guess, and then goes through
//     * the logic for making a guess.
//     * @param p The current player.
//     * @param estate The current estate that this player is in.
//     */
//    public void promptPlayerForGuess(Player p, Estate estate) {
//        System.out.println("You are in " + estate.type + ", which has the weapon " + estate.weapon + ".");
//        if (askBoolean("Would you like to make a guess? Type 'yes' to guess, and anything else to skip.")) {
//            WeaponCard weapon = promptWeapon();
//            PlayerCard player = promptPlayer(p);
//
//            // Refutation
//            var anyPlayerRefuted = false;
//            List<Card> refutedCards = new ArrayList<>();
//            Player firstRefute = players.peek();
//            Player currentRefute;
//
//            //after guess rotate through the players 1 by one checking weather they
//            //contain any cards made in the guess (containing a card will put it
//            // into the list etc...)
//            do{
//                currentRefute = players.poll();
//                System.out.println("It is " + currentRefute + "'s turn to refute");
//                changeTo(currentRefute);
//                board.draw();
//                List<Card> containedCards = new ArrayList<>();
//                //can condense this but brain not work
//                for(Card c : currentRefute.getCards()) {
//                    if(c == weapon || c == player || c == allCards.get(estate.type.toString())) containedCards.add(c);
//                }
//                if(containedCards.size() == 0) {
//                    players.offer(currentRefute);
//                    continue;
//                }
//                //can only refute this card so must return
//                if(containedCards.size() == 1){
//                    players.offer(currentRefute);
//                    refutedCards.add(containedCards.get(0));
//                    continue;
//                }
//                //TODO needs to be tested that the picking card works
//                System.out.println("Please type a card to be refuted from the following:");
//                for(Card c : containedCards) System.out.println("- " + c.toString());
//                Card cardToRefute = null;
//                while (cardToRefute == null){
//                    cardToRefute = allCards.getOrDefault(scannerNext().toUpperCase(),null);
//                    //making sure the player cant just call any random card
//                    if(!containedCards.contains(cardToRefute)) {
//                        System.out.println("Please choose a card that is listed!");
//                        cardToRefute = null;
//                    }
//                }
//                containedCards.add(cardToRefute);
//                players.offer(currentRefute);
//            }while(players.peek() != firstRefute);
//            changeTo(p);
//            //if refuted cards are empty, allow for solve attempt
//            if(refutedCards.isEmpty()) {
//                if (askBoolean("Your guess was unrefuted, would you like to solve?")) {
//                    attemptSolve(p, new CardTriplet(promptWeapon(), promptEstate(), promptPlayer(p)));
//                }
//            }
//            System.out.println("Your refuted cards are shown below\n--------------------");
//            for(Card c : refutedCards) System.out.println("- " + c.toString());
//            System.out.println("--------------------");
//        }
//    }

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
//    public void turn() {
//        Player player = players.poll();
//        changeTo(player);
//
//        var firstDice = random.nextInt(1, 6);
//        var secondDice = random.nextInt(1, 6);
//        var dice = firstDice + secondDice;
//
//        board.draw();
//        System.out.println("You have the following cards: " + player.getCards());
//
//        if (player.getTile() instanceof Estate e) {
//            promptPlayerForGuess(player, e);
//
//            if(!askBoolean("Would you like to leave?")) {
//                this.players.add(player);
//                return;
//            }
//        }
//        System.out.println("You have rolled " + dice + ". Type your moves as a string, i.e. 'LLUUR' for left-left-up-up-right.");
//        var input = scannerNewLine();
//
//        while (invalidInput(input, player, dice)) {
//            System.out.println("Please re-enter");
//            input = scannerNewLine();
//        }
//
//        processInput(input, player);
//        this.players.add(player);
//    }



//    private void movePlayerOnceByToken(Position possibleTranslate, Player p) {
//        // Discover what's on the board at that location
//        Tile t = board.read(possibleTranslate);
//
//        // Check if the player is attempting to move into an estate.
//        if (t instanceof Estate e) {
//            board.tryMoveIntoEstate(p, possibleTranslate, e);
//            // Check if the player is attempting to move into an estate fill tile.
//        } else if (t instanceof Estate.EstateFillTile eft) {
//            board.tryMoveIntoEstate(p, possibleTranslate, eft.parent);
//        } else {
//            t.setPlayer(p);
//            p.setTile(t);
//        }
//    }
}
