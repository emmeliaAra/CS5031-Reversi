package stacs.arcade.reversi;

/**
 * Implementation of the model for the Othello game.
 * 
 * @author <your matric number here, not your email address!>
 */
public class ReversiModel {

	public enum PlayerColour {BLACK, WHITE};
	
	private PlayerColour winner = null;
	
	public ReversiModel() {		
	}
	
	/**
	 * Returns the colour of the piece at the given position, null if no piece is on this field.
	 */
	public PlayerColour getAt(int x, int y) {
		return null;
	}
	
	/**
	 * Returns the player who is to move next.
	 */
	public PlayerColour nextToMove() {		
		return null;
	}
	
	/**
	 * Make a move by placing a piece of the given colour on the given field.
	 * @throws IllegalMoveException if it is not the player's move, if the field
	 * is already occupied or if the coordinates are out of range.
	 */
	public void makeMove(PlayerColour player, int x, int y) throws IllegalMoveException {		
	}
	
	/**
	 * Return the number of black stones currently on the board.
	 */
	public int getNoBlackStones() {
		return 0;
	}
	
	/**
	 * Return the number of white stones currently on the board.
	 */
	public int getNoWhiteStones() {
		return 0;
	}
}
