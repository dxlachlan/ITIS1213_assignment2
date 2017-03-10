//Daniel L. Campbell - ITIS 1213 
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woffortune;

import java.util.*;

/**
 * Class that defines a player for a game with monetary winnings and a limited
 * number of choices
 *
 * @author clatulip
 */
public class Player {

    private int winnings = 0; // amount of money won
    private String name; // player's name
    private int numGuesses = 0; // number of times they've tried to solve puzzle
    //ArrayList - Collects the prizes earned for each player
    private ArrayList<String> playerPrizes = new ArrayList<String>();

    /**
     * Constructor
     *
     * @param name String that is the player's name
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Getter - gets the amount of earnings, each user has won.
     *
     * @return int amount of money won so far
     */
    public int getWinnings() {
        return winnings;
    }

    /**
     * Getter gets the prizes for current user
     * @return String of prize each player has earned
     */
    public ArrayList<String> getPrizes() {
        return playerPrizes;
    }

    /**
     * Adds the prize for the current user
     * @param prize string value to be stored as prize
     */
    public void addPrize(String prize) {
        playerPrizes.add(prize);
    }

    /**
     * Adds amount to the player's winnings
     *
     * @param amount int money to add
     */
    public void incrementScore(int amount) {
        if (amount < 0) {
            return;
        }
        this.winnings += amount;
    }

    /**
     * Getter - gets the current users name
     *
     * @return String player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter - gets the current users number of guesses
     *
     * @return int the number of guesses used up
     */
    public int getNumGuesses() {
        return numGuesses;
    }

    /**
     * Add 1 to the number of guesses used up
     */
    public void incrementNumGuesses() {
        this.numGuesses++;
    }

    /**
     * Resets for a new game (only number of guesses) This does not reset the
     * winnings.
     */
    public void reset() {
        this.numGuesses = 0;
    }

    /**
     * bankrupt method that clears each players prizes and earnings.
     */
    public void bankrupt() {
        this.winnings = 0;
        playerPrizes.clear();
    }

}
