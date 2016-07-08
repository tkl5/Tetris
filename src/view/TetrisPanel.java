/**
 * TCSS 305 - Spring 2016 Assignment 6 - Tetris
 */

package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Board;

/**
 * This class creates the main panel for Tetris for the game pieces to animate.
 * 
 * @author Tim
 * @version 28 May 2016
 */
public class TetrisPanel extends JPanel implements Observer, ActionListener {

    /** Serialization. */
    private static final long serialVersionUID = 1L;

    /** Sets the game board dimension. */
    private static final Dimension PREFERRED_SIZE = new Dimension(200, 400);

    /** Pixel size of each tetris block. */
    private static final int BLOCK_SIZE = 20;
    
    /** String that is passed from the update method. */
    private String myString;

    /** Game timer. */
    private Timer myTimer;

    /** Board instance. */
    private Board myBoard;

    /** Preview Panel instance. */
    private NextPiecePanel myPreviewPanel;

    /** GUI instance. */
    private TetrisGUI myTetrisGUI;

    /** Features panel instance. */
    private FeaturesPanel myFeaturesPanel;

    /** Pause state of the game. */
    private boolean myPauseState;

    /** Anonymous class of the key listener for the pause button. */
    private KeyAdapter myPauseListener = new KeyAdapter() {
        @Override
        public void keyPressed(final KeyEvent theEvent) {
            final int key = theEvent.getKeyCode();

            if (key == KeyEvent.VK_P) {
                pause();
            }

        }
    };

    /** Anonymous class of the the key Listener for keyboard controls. */
    private KeyAdapter myKeyboardListener = new KeyAdapter() {
        @Override
        public void keyPressed(final KeyEvent theEvent) {
            final int key = theEvent.getKeyCode();

            if (key == KeyEvent.VK_LEFT) {
                myBoard.left();
            }
            if (key == KeyEvent.VK_RIGHT) {
                myBoard.right();
            }
            if (key == KeyEvent.VK_Z) {
                myBoard.rotateCCW();
            }
            if (key == KeyEvent.VK_X) {
                myBoard.rotateCW();
            }
            if (key == KeyEvent.VK_DOWN) {
                myBoard.down();
            }
            if (key == KeyEvent.VK_SPACE) {
                myBoard.drop();
            }
        }
    };

    /**
     * Constructs the Tetris Panel.
     * 
     * @param theTetrisGUI GUI class
     * @param thePreviewPanel Next Piece Panel
     * @param theFeaturesPanel Score and info panel
     */
    public TetrisPanel(final TetrisGUI theTetrisGUI, 
                       final NextPiecePanel thePreviewPanel,
                       final FeaturesPanel theFeaturesPanel) {
        super(true);
        myTetrisGUI = theTetrisGUI;
        myPreviewPanel = thePreviewPanel;
        myFeaturesPanel = theFeaturesPanel;
        myBoard = new Board();
        myPauseState = false;

        addObservers();
        setTimerToLevel();
        //setUpTimer();
        setupAppearance();
        myBoard.newGame();
    }

    /** Add the observers to this panel. */
    private void addObservers() {
        myBoard.addObserver(myPreviewPanel);
        myBoard.addObserver(myTetrisGUI);
        myBoard.addObserver(myFeaturesPanel);
        myBoard.addObserver(this);

    }

    /** Set up the look. */
    private void setupAppearance() {
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(PREFERRED_SIZE);
        setFocusable(true);
    }
    
    private void setTimerToLevel() {
        myTimer = new Timer(1000, this);

        int level = myFeaturesPanel.getCurrentLevel();
        if (level == 2) {
            myTimer = new Timer(100, this);
        }
        
    }

    /** Set up the timer for this panel. */
    private void setUpTimer() {
        myTimer.setDelay(1000);
    }

    /** Pause the game, disable key listeners. */
    private void pause() {
        myPauseState = !myPauseState;
        final JLabel label = new JLabel("Paused", JLabel.CENTER);
        label.setFont(new Font("Verdana", 1, 20));
        label.setVisible(true);

        if (myPauseState) {
            removeKeyListener(myKeyboardListener);
            myTimer.stop();
            label.setForeground(Color.white);
            this.setBackground(Color.black);
            this.add(label);

        } else {
            addKeyListener(myKeyboardListener);
            myTimer.start();
            this.setBackground(Color.lightGray);
            this.remove(label);
        }
    }

    /** Starts the game timer. */
    public void startTimer() {
        myTimer.start();
    }

    /** Ends the game. */
    public void endGame() {
        myTimer.stop();
        removeKeyListener(myKeyboardListener);
        removeKeyListener(myPauseListener);

    }

    /** Starts a new game. */
    public void newGame() {
        myBoard.newGame();
        addKeyListener(myKeyboardListener);
        addKeyListener(myPauseListener);
        myFeaturesPanel.resetScore();
    }

    /**
     * Returns the pause state.
     * 
     * @return if the game is currently paused.
     */
    public boolean getPausedState() {
        return myPauseState;
    }

    /**
     * Parse the toString output of the board into Graphic2D shapes. -122 is a
     * flawed Y calibration to attempt centering the board in the panel.
     * 
     * @param theGraphics
     */
    public void drawBoard(final Graphics2D theGraphics) {
        int x = 0;
        int y = -122;
        //final String str = myBoard.toString();
        final String[] newStr = myString.split("\n");

        for (int i = 0; i < newStr.length; i++) {
            y += BLOCK_SIZE;
            final String row = newStr[i];

            for (char ch : row.toCharArray()) {
                if (Character.isLetter(ch) || ch == '0') {
                    theGraphics.setPaint(Color.DARK_GRAY);
                    theGraphics.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                    theGraphics.setPaint(Color.white);
                    theGraphics.drawRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                }
                x += BLOCK_SIZE;
            }
            x = 0 - BLOCK_SIZE;
        }
    }

    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        drawBoard(g2d);
    }

    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        myBoard.step();
    }

    @Override
    public void update(final Observable theObservable, final Object theArg) {
        if (theArg instanceof String) {
            myString = (String) theArg;

            repaint();
        }
    }
}
