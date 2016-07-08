/*
 * TCSS 305 - Spring 2016 Assignment 6 - Tetris
 */

package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import model.Point;
import model.TetrisPiece;

/**
 * This class creates the panel to display the preview tetris piece.
 * 
 * @author Tim
 * @version 5 June 2016
 */
public class NextPiecePanel extends JPanel implements Observer {

    /**
     * Serialization.
     */
    private static final long serialVersionUID = 1L;

    /** Pixel size of each tetris block. */
    private static final int BLOCK_SIZE = 20;

    /** Dimensions of the panel. */
    private static final Dimension SIZE = new Dimension(150, 150);

    /** The tetris piece object belonging to this class. */
    private TetrisPiece myTetrisPiece;

    /** Array of points from the immutable Point class in model. */
    private Point[] myPoints;

    /** Constructs a preview piece panel. */
    public NextPiecePanel() {
        super(true);
        setupAppearance();
        myPoints = null;
    }

    /**
     * Draws the preview piece, taking coordinates from the TetrisPiece class.
     * 
     * @param theGraphics the graphics
     */
    public void drawPiece(final Graphics2D theGraphics) {

        theGraphics.drawRect(25, 25, BLOCK_SIZE * 5, BLOCK_SIZE * 5);
        for (int i = 0; i < myPoints.length; i++) {
            theGraphics.setPaint(Color.white);
            theGraphics.fillRect(myPoints[i].x() * BLOCK_SIZE + 35,
                                 myPoints[i].y() * BLOCK_SIZE + 27, BLOCK_SIZE, BLOCK_SIZE);
            theGraphics.setPaint(Color.black);
            theGraphics.drawRect(myPoints[i].x() * BLOCK_SIZE + 35,
                                 myPoints[i].y() * BLOCK_SIZE + 27, BLOCK_SIZE, BLOCK_SIZE);

        }
    }

    /** Set up the look. */
    private void setupAppearance() {
        setBackground(Color.WHITE);
        setPreferredSize(SIZE);
    }

    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        drawPiece(g2d);
    }

    @Override
    public void update(final Observable theObservable, final Object theArg) {
        if (theArg instanceof TetrisPiece) {
            myTetrisPiece = (TetrisPiece) theArg;
            myPoints = myTetrisPiece.getPoints();
        }
        repaint();
    }
}
