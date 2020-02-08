package stacs.arcade.reversi;

import java.lang.ref.PhantomReference;

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
	private static final int CONSTRAINED_MOVES = 4;
	private static final int BOUNDARY_A = 3;
	private static final int BOUNDARY_B = 4;
	private int totalMoves;

	private String illegalMoveMessage = "This is an illegal move - ";

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

		/* Check for moves outside the boundaries - If field does not exist, throw illegalMoveException.
		 * Checks if these is one of the 4 initial moves. If yes calls method to handle appropriately.
		 */

		checkBoundaries(x,y);

		if(totalMoves < CONSTRAINED_MOVES){
			handleFourInitialMoves(player,x,y);
		}
	}

	private void checkBoundaries(int x, int y) throws IllegalMoveException{
		if(x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
			throw new IllegalMoveException(illegalMoveMessage + "Field does not exists");
		}
	}

	private void handleFourInitialMoves(PlayerColour playerColour, int x, int y) throws IllegalMoveException{
		/* Make sure that the first 4 moves are placed on fields that their coordinates only contain 3s and/or 4s and that the field is available
		 * If field is valid and not occupied. ->place, otherwise throw exception.
		 */

		if( (x == BOUNDARY_A || x == BOUNDARY_B) && (y == BOUNDARY_A || y == BOUNDARY_B) && getAt(x,y) == null){
			board[x][y] = playerColour;
			totalMoves ++;
		} else {
			throw new IllegalMoveException(illegalMoveMessage + "This piece is occupied");
		}
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
