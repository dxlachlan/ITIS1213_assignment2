//Daniel L. Campbell - ITIS 1213 

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woffortune;

import java.util.Scanner;
import java.util.*;

/**
 * WofFortuneGame class Contains all logistics to run the game
 *
 * @author clatulip
 */
public class WofFortuneGame {

    private boolean puzzleSolved = false;

    private Wheel wheel;
    private Player player1;
    //initial game phrase
    private String phrase = "Once upon a time";
    //private Letter[] letter_array = new Letter[16];

    //ArrayList - collects user-inputted phrase (Each Char)
    private ArrayList<Letter> phraseArray = new ArrayList<Letter>();
    //ArrayList - collects hardcoded game phrases
    private ArrayList<String> phraseBankArray = new ArrayList<String>();
    //ArrayList - collects the players who will play the game
    private ArrayList<Player> players = new ArrayList<Player>();
    //ArrayList - collects the hardcoded game prizes
    private ArrayList<String> prizeArray = new ArrayList<String>();

    /**
     * Constructor
     *
     * @param wheel Wheel
     * @throws InterruptedException
     */
    public WofFortuneGame(Wheel wheel) throws InterruptedException {
        // get the wheel
        this.wheel = wheel;

        // do all the initialization for the game
        setUpGame();

    }

    /**
     * Plays the game
     *
     * @throws InterruptedException
     */
    public void playGame() throws InterruptedException {
        // while the puzzle isn't solved, keep going
        while (!puzzleSolved) {
            // let the current player play
            for (int i = 0; i < players.size(); i++) {
                playTurn(players.get(i));
            }
        }
    }

    /**
     * Sets up all necessary information to run the game
     */
    private void setUpGame() {

        //import scanner to allow user input
        Scanner sc = new Scanner(System.in);

        // create a single player 
        //player1 = new Player("Player1");
        //PART 2 - Call soloPlayPhrases
        /*
         Prep the game for solo play with multiple phrases by calling method 
         to store 10 (hardcoded/preset) phrases into ArrayList
         */
        soloPlayPhrases();

        //Part 3 - B - Ask how many users will be playing
        userNumDisplay();

        try {
            //collect number of players to be played
            int numPlayers = sc.nextInt();

            //ask for name of each player
            for (int i = 0, p = 1; i < numPlayers; i++, p++) {
                System.out.println("Enter Player " + p + "'s Name!");
                String playerName = sc.next();
                Player player = new Player(playerName);
                players.add(player);
            }

        } catch (InputMismatchException inputMismatch) {
            System.out.println("Failed! Data Mismatch!");
            System.out.println(inputMismatch);
        }

        // print out the rules
        System.out.println("RULES!");
        System.out.println("Each player gets to spin the wheel, to get a number value");
        System.out.println("Each player then gets to guess a letter. If that letter is in the phrase, ");
        System.out.println(" the player will get the amount from the wheel for each occurence of the letter");
        System.out.println("If you have found a letter, you will also get a chance to guess at the phrase");
        System.out.println("Each player only has three guesses, once you have used up your three guesses, ");
        System.out.println("you can still guess letters, but no longer solve the puzzle.");
        System.out.println();

        //Assignment 2 - PART II - B
        /* 
         Ask the User if they want a custom phrase
         */
        //display prompt/query
        System.out.println("Dear currentUser,");
        System.out.println();
        System.out.println("Would you like to enter your own phrase? (Y/N)");
        try {
            //if the input == y or == Y then allow custom input
            char letter = sc.next().charAt(0);
            System.out.println();
            if ((letter == 'Y') || (letter == 'y')) {
                System.out.println("Enter a new phrase.");
                System.out.println();
                sc.useDelimiter("\n");
                phrase = sc.next();
            } else {

                //generate random number within ArrayList range
                int randomNum = 0
                        + (int) (Math.random() * phraseBankArray.size() - 1);

                //use random int to pick phrase out of phraseBankArray<String> 
                //ArrayList and set it as the current phrase
                phrase = phraseBankArray.get(randomNum);

            }

        //else if input != y or != Y then use normal phrase
            // for each character in the phrase, create a letter and add to letters array
            for (int i = 0; i < phrase.length(); i++) {
                Letter x = new Letter(phrase.charAt(i));
                //@ the current index, add the current letter in the phrase
                phraseArray.add(i, x);
            }
            // setup done
        } catch (Exception general) {
            System.out.println("Failed! Exception Thrown!");
            System.out.println(general);
        }

    }

    /**
     * One player's turn in the game Spin wheel, pick a letter, choose to solve
     * puzzle if letter found
     *
     * @param player
     * @throws InterruptedException
     */
    private void playTurn(Player player) throws InterruptedException {
        int money = 0;
        try {
            Scanner sc = new Scanner(System.in);

            System.out.println(player.getName() + ", you have $" + player.getWinnings());
            System.out.println("Spin the wheel! <press enter>");
            sc.nextLine();
            System.out.println("<SPINNING>");
            Thread.sleep(200);
            Wheel.WedgeType type = wheel.spin();
            System.out.print("The wheel landed on: ");
            switch (type) {
                //If the wedge type is MONEY
                case MONEY:
                    money = wheel.getAmount();
                    System.out.println("$" + money);
                    break;
                //If the wedge type is LOSE_TURN
                case LOSE_TURN:
                    System.out.println("LOSE A TURN");
                    System.out.println("So sorry, you lose a turn.");
                    return; // doesn't get to guess letter
                //If the wedge type is BANKRUPT
                case BANKRUPT:
                    System.out.println("BANKRUPT");
                    player.bankrupt();
                    return; // doesn't get to guess letter
                //If the wedge type is PRIZE
                case PRIZE:
                    System.out.println("YOU HAVE WON A PRIZE!");

                    this.gamePrizes();
                    int randomNum = ((int) (Math.random() * prizeArray.size()));
                    String prize = prizeArray.get(randomNum);
                    System.out.println(player.getName() + " has won a " + prize);
                    player.addPrize(prize);
                    break;

                default:

            }
            System.out.println("");
            System.out.println("Here is the puzzle:");
            showPuzzle();
            System.out.println();
            System.out.println(player.getName() + ", please guess a letter.");
        //String guess = sc.next();

            char letter = sc.next().charAt(0);
            if (!Character.isAlphabetic(letter)) {
                System.out.println("Sorry, but only alphabetic characters are allowed. You lose your turn.");
            } else {
                // search for letter to see if it is in
                int numFound = 0;
                for (Letter l : phraseArray) {
                    if ((l.getLetter() == letter) || (l.getLetter() == Character.toUpperCase(letter))) {
                        l.setFound();
                        numFound += 1;
                    }
                }
                if (numFound == 0) {
                    System.out.println("Sorry, but there are no " + letter + "'s.");
                } else {
                    if (numFound == 1) {
                        System.out.println("Congrats! There is 1 letter " + letter + ":");
                    } else {
                        System.out.println("Congrats! There are " + numFound + " letter " + letter + "'s:");
                    }
                    System.out.println();
                    showPuzzle();
                    System.out.println();
                    player.incrementScore(numFound * money);
                    System.out.println("You earned $" + (numFound * money) + ", and you now have: $" + player.getWinnings());

                    try {
                        System.out.println("Would you like to try to solve the puzzle? (Y/N)");
                        letter = sc.next().charAt(0);
                        System.out.println();
                        if ((letter == 'Y') || (letter == 'y')) {
                            solvePuzzleAttempt(player);
                        }
                    } catch (Exception general) {
                        System.out.println("Failed! Exception Thrown!");
                        System.out.println(general);
                    }

                }

            } //Catch the interrupted exception
        } catch (InterruptedException interrupt) {
            System.out.println("Failed! Interrupted Exception Thrown!");
            System.out.println(interrupt);
        }
    }

    /**
     * Logic for when user tries to solve the puzzle
     *
     * @param player
     */
    private void solvePuzzleAttempt(Player player) {

        if (player.getNumGuesses() >= 3) {
            System.out.println("Sorry, but you have used up all your guesses.");
            return;
        }
        try {
            player.incrementNumGuesses();
            System.out.println("What is your solution?");
            Scanner sc = new Scanner(System.in);
            sc.useDelimiter("\n");
            String guess = sc.next();
            if (guess.compareToIgnoreCase(phrase) == 0) {
                System.out.println(" ");
                System.out.println("Congratulations! You guessed it!");
            //puzzleSolved = true;

            // Round is over. Write message with final stats
                //PART 3 - E - Print out the winning user's name
                System.out.println(player.getName() + " is the Winner!");
                System.out.println();

                //PART 3 - E - Print out the earnings of each player
                for (int i = 0; i < players.size(); i++) {
                    Player user = players.get(i);

                    System.out.println(user.getName() + " has won a total of");
                    System.out.println("$" + user.getWinnings());
                    System.out.println(user.getName() + " has also won the following prizes:");
                    System.out.println(players.get(i).getPrizes());

                }

                puzzleSolved = true;

            } else {
                System.out.println("Sorry, but that is not correct.");
            }
        } catch (Exception general) {
            System.out.println("Failed! Exception Thrown");
            System.out.println(general);
        }
    }

    /**
     * Display the puzzle on the console
     */
    private void showPuzzle() {
        System.out.print("\t\t");
        for (Letter l : phraseArray) {
            if (l.isSpace()) {
                System.out.print("   ");
            } else {
                if (l.isFound()) {
                    System.out.print(Character.toUpperCase(l.getLetter()) + " ");
                } else {
                    System.out.print(" _ ");
                }
            }
        }
        System.out.println();

    }

    /**
     * For a new game reset player's number of guesses to 0
     */
    public void reset() {
        player1.reset();
        puzzleSolved = true;
    }

    //Assignment 2 - Part 2 - B
    /*
     Method to initialize new Arraylist<String> with 10 random phrases
     */
    public void soloPlayPhrases() {
        //10 Phrases+
        phraseBankArray.add("wutang is for the children");
        phraseBankArray.add("mighty healthy");
        phraseBankArray.add("thirty six chambers");
        phraseBankArray.add("liquid swords");
        phraseBankArray.add("street corners");
        phraseBankArray.add("shadow boxing");
        phraseBankArray.add("raising hell with the flavor");
        phraseBankArray.add("ghostface killah");
        phraseBankArray.add("masta killah");
        phraseBankArray.add("method man");
    }

    /**
     * Visual/Semantic Display Method
     * For Querying Number of Users
     */
    public void userNumDisplay() {
        System.out.println();
        System.out.println("* **  * (ﾉಥ益ಥ）ﾉ * ** *");
        System.out.println();
        System.out.println("How many people are playing?");
        System.out.println();

    }

    /**
     * Method to add the prices to the game
     */
    public void gamePrizes() {
        //list of prizes to add to the prizeArray(list of possible prizes)
        prizeArray.add("1 - Teriyaki Riceball");
        prizeArray.add("A dirty, old, beat-up, 1970's AMC Gremlin");
        prizeArray.add("A Ghostface Killah - Mighty Healthy Single LP");
        prizeArray.add("A trip to a cartel-ran Resort in Cozumel, Mexico");
        prizeArray.add("Two Shadow-boxing Classes taught by the RZA");
        prizeArray.add("A Pink iMac G3");
        prizeArray.add("Two Double Cheeseburgers");
        prizeArray.add("A used boxset of 'Freaks and Geeks'");
        prizeArray.add("A Pair of Starbury Basketball Shoes");
        prizeArray.add("3 Bic Mechanical Pencils");
    }

}
