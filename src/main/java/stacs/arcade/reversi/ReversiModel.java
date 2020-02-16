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
			handleFourInitialMoves(x,y);
		}else {
			piecesCaptured = getNumOfCapturedPieces(x, y);
			if(piecesCaptured == 0 )
				throw new IllegalMoveException(illegalMoveMessage + "Does not result to a captured piece of the opponent");
			board[x][y] = nextToMove();

		}
		totalMoves++;
		updateStones();
		switchPlayerTurn();
	}

	private void checkValidPlayer(PlayerColour playerColour) throws IllegalMoveException{
		if(playerColour != nextToMove())
			throw new IllegalMoveException(illegalMoveMessage + "This is not your turn, you cannot perform that move now.");
	}

	private void checkBoundaries(int x, int y) throws IllegalMoveException{
		if(x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
			throw new IllegalMoveException(illegalMoveMessage + "Field does not exists");
		}
	}

	private void handleFourInitialMoves(int x, int y) throws IllegalMoveException{
		/* Make sure that the first 4 moves are placed on fields that their coordinates only contain 3s and/or 4s and that the field is available
		 * If field is valid and not occupied. ->place, otherwise throw exception.
		 */

		if( (x == BOUNDARY_A || x == BOUNDARY_B) && (y == BOUNDARY_A || y == BOUNDARY_B) && getAt(x,y) == null){
			board[x][y] = nextToMove();
		} else if (getAt(x,y) != null){
			throw new IllegalMoveException(illegalMoveMessage + "This piece is occupied");
		} else {
			throw new IllegalMoveException(illegalMoveMessage + "This is an illegal move. The 4 initial pieces must be placed in the center");
		}
	}

	private int getNumOfCapturedPieces(int x, int y){
		/* Check for the surroundings field if it is inside the boundaries and that it does not have the same colour as the current player.
		 *		if yes ->  keep checking deeper towards that direction counting the number of captured pieces until a piece of the player is found.
		 * 		otherwise -> return piecesCaptured = 0.
		 */

		piecesCaptured = 0 ;
		checkTopLeftField(x,y);
		checkTopField(x,y);
		checkTopRightField(x,y);
		checkLeftField(x,y);
		checkRightField(x,y);
		checkBottomField(x,y);
		checkBottomLeftField(x,y);
		checkBottomField(x,y);
		checkBottomRightField(x,y);

		return piecesCaptured;
	}

	private void checkTopLeftField(int x, int y){
		int currentPlayerX = x;
		int currentPlayerY = y;
		int tempCounter = 0;

		while (x - 1 >= 0 && y - 1 >= 0 && nextToMove() != board[x-1][y-1] &&  board[x-1][y-1] != null) {
			tempCounter ++;
			x --;
			y--;
		}

		if(x - 1 >= 0 && y - 1 >= 0 && board[x-1][y-1] == nextToMove()){
			for (int i = 1; i <= tempCounter; i++) {
				capturePiece(currentPlayerX - i,currentPlayerY - i );
			}
		}
	}

	private void checkTopField(int x, int y){
		int  currentPlayerY = y;
		int tempCounter = 0;

		while (y - 1 >= 0 && nextToMove() != board[x][y-1] &&  board[x][y-1]!= null) {
			tempCounter++;
			y --;
		}

		if(y -1 >= 0  && board[x][y-1] == nextToMove()){
			for (int i = 1; i <= tempCounter; i++) {
				capturePiece(x,currentPlayerY - i);
			}
		}
	}


	private void checkTopRightField(int x, int y) {
		int tempCounter = 0;
		int currentPlayerX = x;
		int currentPlayerY = y;

		while (x + 1 < BOARD_WIDTH && y - 1 >= 0 && nextToMove() != board[x + 1][y - 1] && board[x + 1][y - 1] != null) {
			tempCounter++;
			x++;
			y--;
		}

		if (x + 1 < BOARD_WIDTH && y - 1 >= 0 && board[x + 1][y - 1] == nextToMove()) {
			for (int i = 1; i <= tempCounter; i++) {
				capturePiece(currentPlayerX + i, currentPlayerY - i);
			}
		}
	}

	private void checkLeftField(int x,int y){

		int tempCounter = 0;
		int currentPlayerX = x;

		while (x - 1 >= 1 && nextToMove() != board[x-1][y] && board[x-1][y]!= null) {
			tempCounter++;
			x--;
		}

		if(x - 1 >=0 && board[x-1][y] == nextToMove()){
			for (int i = 1; i <= tempCounter; i++) {
				capturePiece(currentPlayerX - i,y);
			}
		}
	}

	private void checkRightField(int x, int y){
		int currentPlayerX = x;
		int tempCounter = 0;

		while (x + 1 < BOARD_WIDTH && nextToMove() != board[x+1][y] &&  board[x+1][y] != null) {
			tempCounter++;
			x++;
		}

		if(x + 1 < BOARD_WIDTH && board[x+1][y] == nextToMove()){
			for (int i = 1; i <= tempCounter; i++) {
				capturePiece(currentPlayerX + i,y);
			}
		}
	}

	private void checkBottomLeftField(int x, int y){
		int tempCounter = 0;
		int currentPlayerX = x;
		int currentPlayerY = y;

		while (x - 1 >= 0 && y + 1 <= BOARD_HEIGHT && nextToMove() != board[x-1][y+1] && board[x-1][y+1] != null) {
			tempCounter++;
			x--;
			y++;
		}

		if(x - 1 >=0 && y + 1 < BOARD_HEIGHT && board[x-1][y+1] == nextToMove()){
			for (int i = 1; i <= tempCounter; i++) {
				capturePiece(currentPlayerX - i,currentPlayerY + i );
			}
		}
	}

	private void checkBottomField(int x, int y){
		int tempCounter = 0;
		int currentPlayerY = y;

		while (y + 1 < BOARD_HEIGHT && nextToMove() != board[x][y+1] && board[x][y+1] != null) {
			tempCounter++;
			y++;
		}

		if(y + 1 < BOARD_HEIGHT && board[x][y+1] == nextToMove()){
			for (int i = 1; i <= tempCounter; i++) {
				capturePiece(x,currentPlayerY + i);
			}
		}
	}

	private void checkBottomRightField(int x, int y){
		int tempCounter = 0;
		int currentPlayerX = x;
		int currentPlayerY = y;

		while (x + 1 < BOARD_WIDTH && y + 1 < BOARD_HEIGHT && nextToMove() != board[x+1][y+1] && board[x+1][y+1] != null) {
			tempCounter++;
			x++;
			y++;
		}

		if(x + 1 <BOARD_WIDTH && y + 1 <BOARD_HEIGHT && board[x+1][y+1] == nextToMove()){
			for (int i = 1; i <= tempCounter; i++) {
				capturePiece(currentPlayerX + i,currentPlayerY + i);
			}
		}
	}

	private void capturePiece(int xCaptured, int yCaptured){
		board[xCaptured][yCaptured] = nextToMove();
		piecesCaptured++;
	}

	private void updateStones(){
		/* Find which player is the one who made a capturing move
		 * Add the capturing pieces +1 (for the piece that he added to make the capturing move)
		 * Deduct the number of capturing pieces from the opponent if pieces>0. - If opponents pieces are 0 -> the player is making one of the 4st initial moves.
		 */
		if(nextToMove() == PlayerColour.BLACK){
			blackStones += piecesCaptured + 1;
			whiteStones -= piecesCaptured;
		}else {
			whiteStones += piecesCaptured + 1;
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
