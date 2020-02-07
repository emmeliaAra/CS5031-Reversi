package stacs.arcade.reversi;

/**
 * Implementation of the model for the Othello game.
 * 
 * @author 190023753
 */
public class ReversiModel {

	public enum PlayerColour {BLACK, WHITE}
	private PlayerColour[][] board = null;
	private PlayerColour nextPlayerToMove;

	private static final int BOARD_WIDTH = 8;
	private static final int BOARD_HEIGHT = 8;
	private int totalMoves;

    /**
     * Needs a simple constructor, required for construction by the
     * class that contains the tests. 
     */
	public ReversiModel() {
		initializeBoard();
		totalMoves = 0;
	}

	private void initializeBoard(){

		//Initialize board and set Black as the first playerToMove
		board = new PlayerColour[BOARD_HEIGHT][BOARD_WIDTH];
		nextPlayerToMove = PlayerColour.BLACK;
	}

	/**
	 * Returns the colour of the piece at the given position, null if no piece is on this field.
	 */
	public PlayerColour getAt(int x, int y) {
		if(board[x][y] != null)
			return board[x][y];
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

		//Make sure that the first 4 moves are placed on fields that their coordinates only contain 3s and/or 4s
		if(totalMoves < 4 && !(x == 3 || x == 4) && !(y == 3 || y == 4)){
			throw new IllegalMoveException("This is an illegal move - First 4 pieces must be placed in the middle of the field");
		}
		totalMoves ++;
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
