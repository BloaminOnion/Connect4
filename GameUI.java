//package com.udacity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by me 2021
 * The Main UI class containing game logic and backend 2D array
 */
public class GameUI extends JPanel {

    //object reference to the actual game to add plays and reflect plays in UI
    private Game game;
    //frame to display grid and plays
    private JFrame frame;
    //images
    private BufferedImage grid;
    private BufferedImage red;
    private BufferedImage yellow;

    /**
     * Creates a gameUI given a Game object
     * @param game
     */
    public GameUI(Game game) {

        this.game = game;

        // load images from resources files
        try {
            ClassLoader classLoader = GameUI.class.getClassLoader();
            grid = ImageIO.read(classLoader.getResourceAsStream("GameBoard.png"));
            red = ImageIO.read(classLoader.getResourceAsStream("RedPiece.png"));
            yellow = ImageIO.read(classLoader.getResourceAsStream("YellowPiece.png"));
        } catch (IOException ex) {
            System.out.println("Failed to load images");
        }

        //create new game buttons
        JButton newGameButton = new JButton("New Single Player Game");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.addActionListener(new ActionListener() {  // connects the new game button to its buttonPressed method
            public void actionPerformed(ActionEvent e)
            {
                newGameButtonPressed(false);
            }
        });

        JButton new2PlayerGameButton = new JButton("New 2 Player Game");
        new2PlayerGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        new2PlayerGameButton.addActionListener(new ActionListener() {  // connects the new game button to its buttonPressed method
            public void actionPerformed(ActionEvent e)
            {
                newGameButtonPressed(true);
            }
        });

        // control what happens when new game buttons cickes
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                //get grid dimensions and location relative to mouse click
                JPanel panel = (JPanel)e.getSource();
                double boardWidth = panel.getWidth();
                double boardHeight = panel.getHeight();
                double boardX = 0; // the horizontal gap left of the grid
                double boardY = 40; // the vertical gap above the grid
                //return if clicked outside the grid area
                if(e.getX()<boardX || e.getY() < boardY)
                    return;
                //calculate which column of the grid was clicked
                //int i = (int) (7*((e.getX()-boardX)/(boardWidth-boardX)));
                //take action and update grid 2D array based on where they clicked
                //panelMouseClicked(i);
                panelMouseClicked(3);
            }
        });

        //add buttons to the panel for display
        this.add(newGameButton);
        this.add(new2PlayerGameButton);

        //set panel dimensions
        final int WIDTH = 700;
        final int HEIGHT = 640;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        //create the frame that would include this panel and siplay it
        frame = new JFrame();
        frame.add(this);            // add this panel to the frame
        frame.pack();               // shrink the window to the appropriate size
        frame.setResizable(false);  // make the window not resizable
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // close the application when the window is closed
        frame.setVisible(true);     // show the window

    }




    @Override
    public void paint(Graphics g) {
        //draw everything as normal ..
        super.paint(g);
        // .. plus the grid lines ..
        g.drawImage(grid, 0, 40, null);
        // .. and loop over the game grid and display all game pieces
        for(int i=0; i<7; i++){
            for(int j=0; j<6; j++) {
                if(game.gridAt(i,j)=='r'){
                    //based on grid index, calculate the pixel location to draw the red image
                    g.drawImage(red, 100*i,100*j+40, null); // draw a red circle
                }
                else if(game.gridAt(i,j)=='y'){
                    //based on grid index, calculate the pixel location to draw the yellow image
                    g.drawImage(yellow, 100*i,100*j+40, null); // draw a yellow circle
                }
            }
        }
        
    }

    /**
     * Called when mouse is clicked within grid in a valid spot
     * @param i the x index of the 2D grid array
     * @param j the y index of the 2D grid array
     */
    private void panelMouseClicked(int i) {
        //apply this play in column i
        if(!game.playAt(i)){
            //escape if play was invalid
            return;
        }
        //refresh the display to reflect the new play
        this.repaint();
        //check if game ended
        if(game.doChecks()){
            //if game over no need to continue, so return
            return;
        }
        //let computer take turn or switch turns for next player
        game.nextTurn();
        //refresh the display to reflect computer's turn if played
        this.repaint();
        //check if game ended again after computer's turn
        if(game.doChecks()){
            //if game over no need to continue, so return
            return;
        }
    }

    public void gameOver(String message){
        JOptionPane.showMessageDialog(null, message, "Game Over!", JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Called when onw of the new game buttons is clicked
     * @param twoPlayer boolean: true if starting a 2 player game, false for single player game
     */
    private void newGameButtonPressed(boolean twoPlayer) {
        game.newGame(twoPlayer);
        this.repaint();
    }

}
