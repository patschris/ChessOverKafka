package main;

import chess.game.Game;
import chess.game.GameCore;
import chess.pieces.PieceColor;

/** Main class of the Chess program.
 * @author Wan Fung Chui
 */


public class Main {

    /** Opens and begins a new game of chess. 
     *  */
    public static void main(String... dummy) throws InterruptedException {
        //PieceColor pieceColor = PieceColor.WHITE;
    	//new Game(pieceColor);
    	
//    	Game g = new Game(PieceColor.BLACK);
//    	GameCore gamec = new GameCore(PieceColor.BLACK, g, "demo_topic", "demo_topic_2");
    	
    	Game g = new Game(PieceColor.WHITE);
    	GameCore gamec = new GameCore(PieceColor.WHITE, g, "whitewrt", "blackwrt");
    	
    	gamec.startgame();
    }

}
