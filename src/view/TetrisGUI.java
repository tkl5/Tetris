/*
 * TCSS 305 - Spring 2016 Assignment 6 - Tetris
 */

package view;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import model.Board;

/**
 * This class builds the GUI and Frame of the Tetris game, buildign the menu bar
 * and its button actions.
 * 
 * @author Tim
 * @version 28 May 2016
 */
public class TetrisGUI extends JPanel implements Observer {

    /** ID for serialization. */
    private static final long serialVersionUID = 1L;

    /** The JFrame. */
    private final JFrame myFrame;

    /** Tetris board. */
    private Board myBoard;

    /** Tetris Panel which the pieces move. */
    private TetrisPanel myTetrisPanel;

    /** The preview panel. */
    private NextPiecePanel myNextPiecePanel;

    /** The Scoring panel. */
    private FeaturesPanel myFeaturesPanel;

    /** New Game JMenu button. */
    private JMenuItem myNewGameMenuItem;

    /** End Game JMenu button. */
    private JMenuItem myEndGameMenuItem;

    /** Small board size button. */
    private JRadioButton mySmallSizeButton;

    /** Default board size button. */
    private JRadioButton myDefaultSizeButton;

    /** Large board size button. */
    private JRadioButton myLargeSizeButton;
    
    /** Checks if the game is over. */
    private boolean myGameOver;

    /**
     * Constructor for the GUI.
     */
    public TetrisGUI() {
        super(new BorderLayout());

        myFrame = new JFrame();
        myNextPiecePanel = new NextPiecePanel();
        myFeaturesPanel = new FeaturesPanel();
        myTetrisPanel = new TetrisPanel(this, myNextPiecePanel, myFeaturesPanel);

        setUpComponents();

    }

    /** Set up the GUI components. */
    public void setUpComponents() {
        final JPanel infoPanel = new JPanel(new BorderLayout());

        infoPanel.add(myFeaturesPanel, BorderLayout.CENTER);
        infoPanel.add(myNextPiecePanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.EAST);
        add(myTetrisPanel, BorderLayout.WEST);

    }

    /** Show the GUI. */
    public static void createAndShowGUI() {
        final JFrame frame = new JFrame("Tetris TCSS 305");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final TetrisGUI newContentPane = new TetrisGUI();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.setJMenuBar(newContentPane.createMenu(frame));
        frame.setResizable(false);

        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Creates the main menu bar.
     * 
     * @param theFrame the main frame.
     * @return JMenuBar
     */
    public JMenuBar createMenu(final JFrame theFrame) {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(buildGameMenu());
        menuBar.add(buildFeaturesMenu());
        return menuBar;
    }

    /**
     * Builds the Game menu button.
     * 
     * @return JMenu
     */
    public JMenu buildGameMenu() {
        final JMenu menu = new JMenu("Game");

        myNewGameMenuItem = new JMenuItem("New Game");
        myEndGameMenuItem = new JMenuItem("End Game");
        myEndGameMenuItem.setEnabled(false);

        myNewGameMenuItem.addActionListener(theEvent -> {
            myTetrisPanel.endGame();
            myTetrisPanel.newGame();
            myTetrisPanel.startTimer();
            myNewGameMenuItem.setEnabled(false);
            myEndGameMenuItem.setEnabled(true);
        });

        myEndGameMenuItem.addActionListener(theEvent -> {
            myTetrisPanel.endGame();
            JOptionPane.showMessageDialog(this, "Game ended.");
            myNewGameMenuItem.setEnabled(true);
            myEndGameMenuItem.setEnabled(false);
        });
        menu.setMnemonic(KeyEvent.VK_G);
        myNewGameMenuItem.setMnemonic(KeyEvent.VK_N);
        myEndGameMenuItem.setMnemonic(KeyEvent.VK_E);

        menu.add(myNewGameMenuItem);
        menu.add(myEndGameMenuItem);

        return menu;
    }

    /**
     * Builds the 'Features' menu bar item.
     * 
     * @return JMenu
     */
    private JMenu buildFeaturesMenu() {
        final JMenu menu = new JMenu("Features");
        final JMenu subMenu = new JMenu("Board Size...");
        final ButtonGroup group = new ButtonGroup();
        mySmallSizeButton = new JRadioButton("5 x 10");
        myDefaultSizeButton = new JRadioButton("10 x 20");
        myLargeSizeButton = new JRadioButton("20 x 40");

        myDefaultSizeButton.setSelected(true);

        group.add(mySmallSizeButton);
        group.add(myDefaultSizeButton);
        group.add(myLargeSizeButton);

        menu.setMnemonic(KeyEvent.VK_F);
        subMenu.add(mySmallSizeButton);
        subMenu.add(myDefaultSizeButton);
        subMenu.add(myLargeSizeButton);

        mySmallSizeButton.addActionListener(new MyBoardSizeListener());
        myDefaultSizeButton.addActionListener(new MyBoardSizeListener());
        myLargeSizeButton.addActionListener(new MyBoardSizeListener());

        menu.add(subMenu);
        menu.add(buildScoreMenu());
        return menu;
    }

    /**
     * Builds the score information menu.
     * 
     * @return JMenuItem
     */
    private JMenuItem buildScoreMenu() {
        final JMenuItem scoreMenu = new JMenuItem("Scoring");
        scoreMenu.addActionListener(theEvent -> {
            JOptionPane.showMessageDialog(myFrame,
                                          "One line:  100 Points\n"
                                                   + "Four Lines:  800 Points",
                                          "Scoring Algorithm", JOptionPane.PLAIN_MESSAGE);
        });
        return scoreMenu;
    }

    /**
     * Inner class for changing board sizes.
     * 
     * @author Tim
     *
     */
    private class MyBoardSizeListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            if (theEvent.getSource() == mySmallSizeButton) {
                myBoard = new Board(5, 10);
                myBoard.deleteObservers();
                myBoard.addObserver(myFeaturesPanel);
                myBoard.addObserver(myNextPiecePanel);
            
            } else if (theEvent.getSource() == myDefaultSizeButton) {
                myBoard = new Board(10, 20);
                myBoard.deleteObservers();
                myBoard.addObserver(myFeaturesPanel);
                myBoard.addObserver(myNextPiecePanel);

            } else if (theEvent.getSource() == myLargeSizeButton) {
                myBoard = new Board(20, 40);
                myBoard.deleteObservers();
                myBoard.addObserver(myFeaturesPanel);
                myBoard.addObserver(myNextPiecePanel);

            }
            repaint();
        }
    }

    @Override
    public void update(final Observable theObservable, final Object theArg) {
        if (theArg instanceof Boolean) {
            myGameOver = (boolean) theArg;
            if (myGameOver) {
                JOptionPane.showMessageDialog(this, "Game Over!");
                myNewGameMenuItem.setEnabled(true);
                myEndGameMenuItem.setEnabled(false);
            }
        }
    }
}
