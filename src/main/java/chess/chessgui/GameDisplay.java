package chess.chessgui;

import chess.game.Game;
import chess.pieces.Piece;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.InputStream;
import java.io.IOException;

/** A widget for the display of the Chess GUI.
 *  @author Wan Fung Chui
 */
public class GameDisplay extends Pad {

    /** A factor that can be changed to alter the size of the GUI. */
    public static final double MULTIPLIER = 1;

    /** Length and width of the square playing surface. */
    public static final int BOARD = (int) Math.round(700 * MULTIPLIER);

    /** Length and width of a single cell in the chessboard. */
    public static final int CELL = (int) Math.round(74 * MULTIPLIER);

    /** Distance from the chessboard edge to the first cell. */
    public static final int MARGIN = (int) Math.round(53 * MULTIPLIER);

    /** Constructs a graphical representation of GAME. */
    public GameDisplay(Game game) {
        _game = game;
        setPreferredSize(BOARD, BOARD);
        setMaximumSize(BOARD, BOARD);
        setMinimumSize(BOARD, BOARD);
    }

    /** Return an Image read from the resource named NAME. */
    private Image getImage(String name) {
        InputStream in = getClass().getResourceAsStream("/chess/images/" + name);
        try {
            return ImageIO.read(in);
        } catch (IOException e) {
            return null;
        }
    }

    /** Return an Image of PIECE. */
    private Image getPieceImage(Piece piece) {
        return getImage("pieces/" + piece.imageString() + ".png");
    }

    /** Draw PIECE at X, Y on G. */
    private void paintPiece(Graphics2D g, Piece piece, int x, int y) {
        if (piece != null) {
            g.drawImage(getPieceImage(piece), x, y, CELL, CELL, null);
        }
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        Rectangle b = g.getClipBounds();
        g.fillRect(0, 0, b.width, b.height);
        g.fillRect(1,0,b.width, b.height);
        g.drawImage(getImage("board/chessboard.jpg"), 0, 0, BOARD, BOARD, null);



        if (_game.inCheck(_game.turn())) {
            g.drawImage(getImage("board/inCheck.png"),
                    CELL * _game.kingX(_game.turn()) + MARGIN,
                    CELL * _game.kingY(_game.turn()) + MARGIN, CELL, CELL, null);
        }
        if (_game.selectedX() != -1) {
            g.drawImage(getImage("board/selected.png"),
                    CELL * _game.selectedX() + MARGIN,
                    CELL * _game.selectedY() + MARGIN, CELL, CELL, null);
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                paintPiece(g, _game.get(i, j), CELL * i + MARGIN, CELL * j + MARGIN);
            }
        }
    }

    /** The game this display is displaying. */
    private final Game _game;

}