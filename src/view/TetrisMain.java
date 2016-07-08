/*
 * TCSS 305 - Spring 2016 Assignment 6 - Tetris
 */

package view;

import java.awt.EventQueue;

import model.Board;

/**
 * This class provides the start point for the Tetris game.
 * 
 * @author Tim Liu
 * @version 28 May 2016
 */
public final class TetrisMain {

    /**
     * Private constructor to inhibit instantiation.
     */
    private TetrisMain() {
        throw new IllegalStateException();
    }

    /**
     * The start point for the program.
     * 
     * @param theArgs command line arguments - ignored in this program
     */
    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                TetrisGUI.createAndShowGUI();
            }
        });
    }

}
