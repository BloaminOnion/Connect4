//package com.udacity;

import java.util.Arrays;

/**
 * Created by me 2021
 * The Main class containing game logic and backend 2D array
 */
public class Game {

    private char turn; // who's turn is it, 'r' or 'y' ? r always starts
    private boolean twoPlayer; // true if this is a 2 player game, false if AI playing
    private char [][] grid; // a 2D array of chars representing the game grid
    private int freeSpots; // counts the number of empty spots remaining on the board (starts from 42 and counts down)
    private static GameUI gui;
    private int boardRows = 6; // Y dimensions of grid
    private int boardColumns = 7; // X dimensions of grid
    private int [] piecesInColumn = new int[boardColumns]; // Total number of player pieces in each column (max = boardRows)

    /**
     * Create a new single player game
     *
     */
    public Game() {
        newGame(false);
    }

    /**
     * Create a new game by clearing the 2D grid and restarting the freeSpots counter and setting the turn to x
     * @param twoPlayer: true if this is a 2 player game, false if playing against the computer
     */
    public void newGame(boolean twoPlayer){
        //sets a game to one or two player
        this.twoPlayer = twoPlayer;

        // initialize all chars in 6x7 game grid to '-'
        grid = new char[boardColumns][boardRows];
        //fill all empty slots with -
        for (int i=0; i<boardColumns; i++){
            for (int j=0; j<boardRows; j++){
                grid[i][j] = '-';
            }
        }
        //start with 42 free spots and decrement by one every time a spot is taken
        freeSpots = boardRows * boardColumns;
        //reset number of pieces in each column
        piecesInColumn = new int[boardColumns];
        //r always starts
        turn = 'r';
    }


    /**
     * Gets the char value at that particular position in the grid array
     * @param i the x index of the 2D array grid
     * @param j the y index of the 2D array grid
     * @return the char value at the position (i,j):
     *          'r' if r has played here
     *          'y' if y has played here
     *          '-' if no one has played here
     *          '!' if i or j is out of bounds
     */
    public char gridAt(int i, int j){
        if(i>=boardColumns||j>=boardRows||i<0||j<0)
            return '!';
        return grid[i][j];
    }

    /**
     * Places current player's char at position (i,j)
     * Uses the variable turn to decide what char to use
     * @param i the x index of the 2D array grid
     * @param j the y index of the 2D array grid
     * @return boolean: true if play was successful, false if invalid play
     */
    public boolean playAt(int columnToPlay){
        //check for index boundries
        if(columnToPlay>=boardColumns||columnToPlay<0)
            return false;
        //check if this position is available
        if(piecesInColumn[columnToPlay]>=boardRows){
            return false; //bail out if not available
        }
        //update grid with new play based on who's turn it is
        grid[columnToPlay][boardRows - 1 - piecesInColumn[columnToPlay]] = turn;
        //update free spots
        freeSpots--;
        //update number of pieces in that column
        piecesInColumn[columnToPlay]++;
        return true;
    }


    /**
     * Override
     * @return string format for 2D array values
     */
    public String toString(){
        return Arrays.deepToString(this.grid);
    }

    /**
     * Performs the winner chack and displayes a message if game is over
     * @return true if game is over to start a new game
     */
    public boolean doChecks() {
        //check if there's a winner or tie ?
        String winnerMessage = checkGameWinner(grid, turn);
        if (!winnerMessage.equals("None")) {
            gui.gameOver(winnerMessage);
            newGame(false);
            return true;
        }
        return false;
    }

    /**
     * Allows computer to play in a single player game or switch turns for 2 player game
     */
    public void nextTurn(){
        //check if single player game, then let computer play turn
        if(!twoPlayer){
            if(freeSpots == 0){
                return ; //bail out if no more free spots
            }
            int aiColumn;
            do {
                //randomly pick a position
                aiColumn = (int) (Math.random() * boardColumns);
            }while(piecesInColumn[aiColumn]>=boardRows); //keep trying if this spot was taken
            //update grid with new play, computer is always y
            grid[aiColumn][boardRows - 1 - piecesInColumn[aiColumn]] = 'y'; 
            //update free spots
            freeSpots--;
            //update number of pieces in that column
            piecesInColumn[aiColumn]++;
        }
        else{
            //change turns
            if(turn == 'r'){
                turn = 'y';
            }
            else{
                turn = 'r';
            }
        }
        return;
    }


    /**
     * Checks if the game has ended either because a player has won, or if the game has ended as a tie.
     * If game hasn't ended the return string has to be "None",
     * If the game ends as tie, the return string has to be "Tie",
     * If the game ends because there's a winner, it should return "Red wins" or "Yellow wins" accordingly
     * @param grid 2D array of characters representing the game board
     * @return String indicating the outcome of the game: "Red wins" or "Yellow wins" or "Tie" or "None"
     */
    public String checkGameWinner(char [][]grid, char turn){
        String result = "None";

        boolean isWinner = false;
        
        // Checking for vertical wins
        for (int i = 0 ; i < boardColumns ; i++) {
        	for (int j = 0 ; j < boardRows - 3 ; j++) {
        		if (grid[i][j] == turn && grid[i][j+1] == turn && grid[i][j+2] == turn && grid[i][j+3] == turn) {
        			isWinner = true;
        		}
        	}
        }
        
        // Checking for horizontal wins
        for (int i = 0 ; i < boardColumns - 3 ; i++) {
        	for (int j = 0 ; j < boardRows ; j++) {
        		if(grid[i][j] == turn && grid[i+1][j] == turn && grid[i+2][j] == turn && grid[i+3][j] == turn) {
        			isWinner = true;
        		}
        	}
        }
        
        // Checking for diagonal wins
        for (int i = 0 ; i < boardColumns - 3 ; i++) {
        	for (int j = 0 ; j < boardRows - 3 ; j++) {
        		if (grid[i][j] == turn && grid[i+1][j+1] == turn && grid[i+2][j+2] == turn && grid[i+3][j+3] == turn) {
        			isWinner = true;
        		}
        	}
        }
        
        for (int i = 3 ; i < boardColumns ; i++) {
        	for (int j = 3 ; j < boardRows ; j++) {
        		if (grid[i][j] == turn && grid[i-1][j-1] == turn && grid[i-2][j-2] == turn && grid[i-3][j-3] == turn) {
        			isWinner = true;
        		}
        	}
        }
        
        
        if (isWinner) {
        	if (turn == 'r') {
        		result = "Red wins!";
        	} else {
        		result = "Yellow wins!";
        	}
        }
        	
        if (freeSpots <= 0) {
        	result = "All spots filled, start a new game.";
        }
        return result;
    }

    /**
     * Main function
     * @param args command line arguments
     */
    public static void main(String args[]){
        Game game = new Game();
        gui = new GameUI(game);
    }

}
