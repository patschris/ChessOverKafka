package chess.chessgui;

import chess.game.Game;
import chess.pieces.Piece;
import chess.pieces.PieceColor;
import gui.Chat;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import java.awt.event.MouseEvent;
import static chess.chessgui.GameDisplay.CELL;
import static chess.chessgui.GameDisplay.MARGIN;

/** A top-level GUI for Chess.
 *  @author Wan Fung Chui
 */

public class ChessGUI extends TopLevel {

	PieceColor pieceColor;
	private int init_x;
	private int init_y;
	private int releasedX;
	private int releasedY;
	private int flag = 0;
	public String myself;
	public String opponent;

	/** A new window with given TITLE and displaying GAME. 
	 *  */
	public ChessGUI(String title, Game game, PieceColor pieceColor, String myself, String opponent) throws InterruptedException {
		super(title, true);
		
		this.myself = myself;
		this.opponent = opponent;

		this.pieceColor = pieceColor;
		_game = game;
		addLabel("Welcome to 2-Player Chess. "
				+ "Click a piece and then its destination to play! "
				+ "WHITE's turn.", "turn",
				new LayoutSpec("y", 1, "x", 0));

		addMenuButton("Options->Chat", "chat");
		addMenuButton("Options->Stats", "stats");
		addMenuButton("Options->Quit", "quit");

		_display = new GameDisplay(game);
		add(_display, new LayoutSpec("y", 2, "width", 2));
		
		
		display(true);

	}
	
	public void setMHmouse() {
		_display.setMouseHandler("press", this, "mousePressed");
	}

	public void setMHturn() {
		_display.setMouseHandler("press", this, "waitForYourTurn");
	}

	
	/** Respond to the "New Game" button. 
	 * */
	/*public void newGame(String dummy) {
        _game.newGame();
        repaint(true);
    }*/


	public void waitForYourTurn(MouseEvent event) {
		JOptionPane.showMessageDialog(null, "Wait for your opponent to make his move!");
	}

	/** Respond to the "Quit" button. */
	public void quit(String dummy) {
		closeChat();
		_game.quit();
	}

	public void chat(String dummy) throws BadLocationException {
		if (chat == null) chat = new Chat(this);
	}

	public void closeChat () {
		if (chat != null) chat.dispose();
	}

	public void setChat (Chat c) {
		chat = c;
	}

	public void stats(String dummy) {
		System.out.println("stats");
	}

	/** Respond to the "Undo" button. */
	/* public void undo(String dummy) {
        _game.undoMove();
        _game.setSelectedX(-1);
        _game.setSelectedY(-1);
        repaint(true);
    }*/

	/** Action in response to mouse-pressed event EVENT. */
	public synchronized void mousePressed(MouseEvent event) {
		if (_game.selectedX() == -1) {
			int pressedX = (event.getX() - MARGIN) / CELL;
			int pressedY = (event.getY() - MARGIN) / CELL;
			Piece selected = _game.get(pressedX, pressedY);
			if (selected != null && selected.color() == _game.turn()) {
				_game.setSelectedX(pressedX);
				_game.setSelectedY(pressedY);
				_display.repaint();
			}
		} else {
			int releasedX = (event.getX() - MARGIN) / CELL;
			int releasedY = (event.getY() - MARGIN) / CELL;
			Piece selected = _game.get(_game.selectedX(), _game.selectedY());
			this.setInit_x(_game.selectedX());
			this.setInit_y(_game.selectedY());
			_game.setSelectedX(-1);
			_game.setSelectedY(-1);	
			this.setFlag(repaintReceive(selected.makeValidMove(releasedX, releasedY)));
			this.setReleasedX(releasedX);
			this.setReleasedY(releasedY);
		}

	}

	/** Repaints the GUI display, with a move invalid if not VALIDMOVE. */
	public int repaintReceive(boolean validMove) {
		String label;
		if (validMove) {
			if (_game.noMoves()) {
				if (_game.inCheck(_game.turn())) {
					label = "CHECKMATE, " + _game.turn().opposite().string()
							+ " wins.";
				}
				else {
					label = "STALEMATE, game ends in draw.";
				}
			} else {
				
				label = _game.turn().string() + "'s turn.";
			}


			setLabel("turn", label);

			_display.repaint();

			return 1;

		}
		else {
			label = "Invalid Move. " + _game.turn().string() + "'s turn.";

			setLabel("turn", label);

			_display.repaint();

			return 0;
		}


	}

	public int getInit_x() {
		return init_x;
	}

	public void setInit_x(int init_x) {
		this.init_x = init_x;
	}

	public int getInit_y() {
		return init_y;
	}

	public void setInit_y(int init_y) {
		this.init_y = init_y;
	}

	public int getReleasedX() {
		return releasedX;
	}

	public void setReleasedX(int releasedX) {
		this.releasedX = releasedX;
	}

	public int getReleasedY() {
		return releasedY;
	}

	public void setReleasedY(int releasedY) {
		this.releasedY = releasedY;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	/** The chessboard widget. */
	public final GameDisplay _display;

	/** The game being consulted. */
	public final Game _game;

	private Chat chat = null;

}