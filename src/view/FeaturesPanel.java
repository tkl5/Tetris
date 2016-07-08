/*
 * TCSS 305 - Spring 2016 Assignment 6 - Tetris
 */

package view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

/**
 * This class builds the panel that displays the score and level.
 * 
 * @author Tim Liu
 * @version 5 June 2016
 */
public class FeaturesPanel extends JPanel implements Observer {

    /** Serialization. */
    private static final long serialVersionUID = 1L;

    /** X coordinate for placement. */
    private static final int SCORE_X_COORD = 10;

    /** X coordinate for placement. */
    private static final int KEYBOARD_X_COORD = 5;

    /** Font size for the score display. */
    private static final int SCORE_FONT_SIZE = 16;

    /** Font size for the keyboard controls. */
    private static final int CONTROLS_FONT_SIZE = 12;
    
    /** */
    private static final int LEVEL_INCREMENT = 5;

    /** Number of points rewarded for clearing a single line. */
    private static final int POINTS_PER_LINECLEAR = 100;

    /** Number of points rewarded for clearing four lines in a row. */
    private static final int POINTS_PER_4_LINECLEAR = 800;

    /**
     * Miliseconds to subtract from the current delay to increase difficulty.
     */
    private static final int DIFFICULTY_VALUE = 200;

    /** The current level. */
    private static final int CURRENT_LEVEL = 1;

    /** Number of lines cleared. */
    private int myLinesCleared;

    /** Initial value of lines cleared. */
    private int myOldLinesCleared;

    /** Current score. */
    private int myScore;

    /** The level. */
    private int myLevel = CURRENT_LEVEL;

    /** Starting delay. */
    private int myStartingDelay;

    /** Current delay. */
    private int myCurrentDelay;

    /** Difficulty decrement. */

    /** Constructs the panel. */
    public FeaturesPanel() {
        super();

    }

    /**
     * Writes the text for the controls and scores.
     * 
     * @param theGraphics the graphics
     */
    private void drawHelpPanel(final Graphics theGraphics) {
        theGraphics.setFont(new Font("SansSerif", Font.PLAIN, SCORE_FONT_SIZE));
        theGraphics.drawString("Score:         " + myScore, SCORE_X_COORD, 20);
        theGraphics.drawString("Lines:          " + myLinesCleared, SCORE_X_COORD, 40);
        theGraphics.drawString("Level:          " + myLevel, SCORE_X_COORD, 60);

        theGraphics.setFont(new Font("Calibri", Font.PLAIN, CONTROLS_FONT_SIZE));
        theGraphics.drawString("Move right:     Right Arrow ", KEYBOARD_X_COORD, 150);
        theGraphics.drawString("Move left:       Left Arrow ", KEYBOARD_X_COORD, 165);
        theGraphics.drawString("Move down:   Down Arrow ", KEYBOARD_X_COORD, 180);
        theGraphics.drawString("Rotate CCW:   Z  ", KEYBOARD_X_COORD, 195);
        theGraphics.drawString("Rotate CW:     X  ", KEYBOARD_X_COORD, 210);
        theGraphics.drawString("Drop:               Space ", KEYBOARD_X_COORD, 225);
        theGraphics.drawString("Pause:            P", KEYBOARD_X_COORD, 240);

        repaint();
    }

    /** Calculates the ongoing score. */
    private void calculateScore() {
        myOldLinesCleared = myOldLinesCleared - myLinesCleared;
        if (myOldLinesCleared == -4) {
            myScore += POINTS_PER_4_LINECLEAR;
        }
        else {
            myScore = POINTS_PER_LINECLEAR * myLinesCleared;
        }
    }

    /** Sets the current level of the game. */
    private void setCurrentLevel() {
        if (myLinesCleared % LEVEL_INCREMENT == 0) {
            myLevel++;
        }
    }

    /**
     * The current move delay for each piece, decreases as you advance levels.
     * 
     * @return the delay
     */
//    public int getCurrentDelay() {
//        if (myLevel == 2) {
//            myStartingDelay -= DIFFICULTY_VALUE;
//            return myStartingDelay;
//        }
//        if (myLevel == 3) {
//            myStartingDelay -= DIFFICULTY_VALUE;
//
//            return myStartingDelay;
//        }
//        if (myLevel == 4) {
//            myStartingDelay -= DIFFICULTY_VALUE;
//
//            return myStartingDelay;
//        }
//        return myStartingDelay;
//
//    }
    
    /** Gives access to the current level */   
    public int getCurrentLevel() {
        return myLevel;
    }
    
    /** Resets the stats. */
    public void resetScore() {
        myLinesCleared = 0;
        myScore = 0;
        myLevel = 1;
    }

    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        drawHelpPanel(g2d);

    }

    @Override
    public void update(final Observable theObservable, final Object theArg) {
        if (theArg instanceof Integer[]) {
            myLinesCleared += ((Integer[]) theArg).length;
            calculateScore();
            setCurrentLevel();
        }
        repaint();
    }
}
