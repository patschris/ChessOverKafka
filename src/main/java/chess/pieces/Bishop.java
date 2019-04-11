package chess.pieces;

import chess.*;
import chess.moves.Move;
import chess.moves.SingleMove;

import static chess.pieces.PieceType.BISHOP;

/** A bishop in a chess game.
 *  @author Wan Fung Chui
 */
public class Bishop implements Piece {

    /** A constructor for a piece on the board with color COLOR,
      * game GAME, and location (X, Y). */
    public Bishop(PieceColor color, Game game, int x, int y) {
        _color = color;
        _game = game;
        _x = x;
        _y = y;
    }

    public String imageString() {
        return _color.abbrev() + BISHOP.abbrev();
    }

    public PieceColor color() {
        return _color;
    }

    public PieceType type() {
        return BISHOP;
    }

    public boolean makeValidMove(int a, int b) {
        if (_game.get(a, b) != null
            && _game.get(a, b).color() == _color) {
            return false;
        } else if (a + b == _x + _y) {
            int dir = (a - _x) / Math.abs(a - _x);
            for (int i = _x + dir, j = _y - dir; i != a; i += dir, j -= dir) {
                if (_game.get(i, j) != null) {
                    return false;
                }
            }
            Move move = new SingleMove(this, _x, _y, _game.get(a, b), a, b);
            return makeMoveCareful(move);
        } else if (a - b == _x - _y) {
            int dir = (a - _x) / Math.abs(a - _x);
            for (int i = _x + dir, j = _y + dir; i != a; i += dir, j += dir) {
                if (_game.get(i, j) != null) {
                    return false;
                }
            }
            Move move = new SingleMove(this, _x, _y, _game.get(a, b), a, b);
            return makeMoveCareful(move);
        } else {
            return false;
        }
    }

    public void setLocation(int x, int y) {
        _x = x;
        _y = y;
    }

    public boolean hasMove() {
        if ((_x + 1 <= 7 && _y + 1 <= 7 && makeValidMove(_x + 1, _y + 1))
            || (_x + 1 <= 7 && _y - 1 >= 0 && makeValidMove(_x + 1, _y - 1))
            || (_x - 1 >= 0 && _y + 1 <= 7 && makeValidMove(_x - 1, _y + 1))
            || (_x - 1 >= 0 && _y - 1 >= 0 && makeValidMove(_x - 1, _y - 1))) {
            _game.undoMove();
            return true;
        } else {
            return false;
        }
    }

    public boolean canCapture(int a, int b) {
        if (a + b == _x + _y) {
            int dir = (a - _x) / Math.abs(a - _x);
            for (int i = _x + dir, j = _y - dir; i != a; i += dir, j -= dir) {
                if (_game.get(i, j) != null) {
                    return false;
                }
            }
            return true;
        } else if (a - b == _x - _y) {
            int dir = (a - _x) / Math.abs(a - _x);
            for (int i = _x + dir, j = _y + dir; i != a; i += dir, j += dir) {
                if (_game.get(i, j) != null) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /** Makes the move MOVE, and returns false if the move doesn't leave the
      * king in check, in which case the move is undone, and true otherwise. */
    private boolean makeMoveCareful(Move move) {
        _game.makeMove(move);
        if (_game.inCheck(_game.turn().opposite())) {
            _game.undoMove();
            return false;
        } else {
            return true;
        }
    }

    /** The game this piece belongs to. */
    private Game _game;

    /** The color of this piece. */
    private PieceColor _color;

    /** The x-location of this piece. */
    private int _x;

    /** THe y-location of this piece. */
    private int _y;

}
