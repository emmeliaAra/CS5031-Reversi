package stacs.arcade.reversi;

/**
 * Implementation of the model for the Othello game.
 * 
 * @author 190023753
 */
public class ReversiModel {

	public enum PlayerColour {BLACK, WHITE}
	private PlayerColour[][] board = null;
	private PlayerColour currentPlayer;

	private static final int BOARD_WIDTH = 8;
	private static final int BOARD_HEIGHT = 8;
	private static final int CONSTRAINED_MOVES = 4;
	private static final int BOUNDARY_A = 3;
	private static final int BOUNDARY_B = 4;
	private int totalMoves, piecesCaptured,blackStones,whiteStones;

	private String illegalMoveMessage = "This is an illegal move - ";

    /**
     * Needs a simple constructor, required for construction by the
     * class that contains the tests. 
     */
	public ReversiModel() {
		initializeBoard();
		totalMoves = 0;
		blackStones = 0;
		whiteStones = 0;
	}

	private void initializeBoard(){

		//Initialize board and set Black as the first playerToMove
		board = new PlayerColour[BOARD_HEIGHT][BOARD_WIDTH];
		currentPlayer = PlayerColour.BLACK;
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
		return currentPlayer;
	}
	
	/**
	 * Make a move by placing a piece of the given colour on the given field.
	 * @throws IllegalMoveException if it is not the player's move, if the field
	 * is already occupied or if the coordinates are out of range.
	 */
	public void makeMove(PlayerColour player, int x, int y) throws IllegalMoveException {

		/* Checks that then right player is playing - If not exception is thrown..
		 * Check for moves outside the boundaries - If field does not exist, throw illegalMoveException.
		 * Checks if these is one of the 4 initial moves. If yes calls method to handle appropriately.
		 */
		checkValidPlayer(player);
		checkBoundaries(x,y);

		if(totalMoves < CONSTRAINED_MOVES){
			handleFourInitialMoves(player,x,y);
		}else {
			piecesCaptured = getNumOfCapturedPieces(player, x, y);
			if(piecesCaptured == 0 )
				throw new IllegalMoveException(illegalMoveMessage + "Does not result to a captured piece of the opponent");
			updateStones(player);
		}
		switchPlayerTurn();
	}

	private void checkValidPlayer(PlayerColour playerColour) throws IllegalMoveException{
		if(playerColour != currentPlayer)
			throw new IllegalMoveException(illegalMoveMessage + "This is not your turn, you cannot perform that move now.");
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
			updateStones(playerColour);
		} else if (getAt(x,y) != null){
			throw new IllegalMoveException(illegalMoveMessage + "This piece is occupied");
		} else {
			throw new IllegalMoveException(illegalMoveMessage + "This is an illegal move. The 4 initial pieces must be placed in the center");
		}
	}

	private int getNumOfCapturedPieces(PlayerColour playerColour, int x, int y){
		/* Check for the surroundings field if it is inside the boundaries and that it does not have the same colour as the current player.
		 *		if yes -> Make that piece hold the colour of the player
		 *			   -> keep checking deeper towards that direction counting the number of captured pieces until a piece of the player is found.
		 * 		otherwise return piecesCaptured = 0.
		 */

		piecesCaptured = 0 ;

		//Top-left field
		while (x - 1 >= 0 && y - 1 >= 0 && playerColour != board[x-1][y-1] && null != board[x-1][y-1])
			capturePiece(playerColour,x,y,x-1,y-1);

		//Field above
		while (y - 1 >= 0 && playerColour != board[x][y-1] && null !=  board[x][y-1])
			capturePiece(playerColour,x,y,x,y-1);

		//Top-right field
		while (x + 1 < BOARD_WIDTH && y - 1 >=0 && playerColour != board[x+1][y-1]  && null != board[x+1][y-1])
			capturePiece(playerColour,x,y,x+1,y-1);


		//Field on the left
		while (x - 1 >= 1 && playerColour != board[x-1][y] && null != board[x-1][y])
			capturePiece(playerColour,x,y,x-1,y);

		//Field on the right
		while (x + 1 < BOARD_WIDTH && playerColour != board[x+1][y] && null != board[x+1][y])
			capturePiece(playerColour,x,y,x+1,y);

		//Bottom-left
		while (x - 1 >= 0 && y + 1 <= BOARD_HEIGHT && playerColour != board[x-1][y+1] && null != board[x-1][y+1])
			capturePiece(playerColour,x,y,x-1,y+1);

		//Field below
		while (y + 1 < BOARD_HEIGHT && playerColour != board[x][y+1] && null != board[x][y+1])
			capturePiece(playerColour,x,y,x,y+1);

		//bottom-right field.
		while (x + 1 < BOARD_WIDTH && y + 1 < BOARD_HEIGHT && playerColour != board[x+1][y+1] && null != board[x+1][y+1])
			capturePiece(playerColour,x,y,x+1,y+1);

		return piecesCaptured;
	}

	private void capturePiece(PlayerColour playerColour, int xPlayer, int yPlayer, int xCaptured, int yCaptured){
		board[xPlayer][yPlayer] = playerColour;
		board[xCaptured][yCaptured] = playerColour;
		piecesCaptured++;
	}
	
	private void updateStones(PlayerColour captor){

		/* Find which player is the one who made a capturing move
		 * Add the capturing pieces +1 (for the piece that he added to make the capturing move)
		 * Deduct the number of capturing pieces from the opponent if pieces>0. - If opponents pieces are 0 -> the player is making one of the 4st initial moves.
		 */
		if(captor == PlayerColour.BLACK){
			blackStones += piecesCaptured + 1;
			if(whiteStones > 0)
				whiteStones -= piecesCaptured;
		}else {
			whiteStones += piecesCaptured + 1;
			if(blackStones > 0 )
				blackStones -= piecesCaptured;
		}
	}

	private void switchPlayerTurn(){
		if(currentPlayer == PlayerColour.BLACK)
			currentPlayer = PlayerColour.WHITE;
		else currentPlayer = PlayerColour.BLACK;
	}

	/**
	 * Return the number of black stones currently on the board.
	 */
	public int getNoBlackStones() {
		return blackStones;
	}
	
	/**
	 * Return the number of white stones currently on the board.
	 */
	public int getNoWhiteStones() {
		return whiteStones;
	}
}
