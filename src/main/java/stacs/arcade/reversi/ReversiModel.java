package stacs.arcade.reversi;

/**
 * Implementation of the model for the Othello game.
 *
 * @author 190023753
 */
public class ReversiModel {

	public enum PlayerColour {BLACK, WHITE}
	private PlayerColour[][] board = null;
	private PlayerColour currentPlayerToMove;

	private static final int BOARD_WIDTH = 8;
	private static final int BOARD_HEIGHT = 8;
	private static final int CONSTRAINED_MOVES = 4;
	private static final int BOUNDARY_A = 3;
	private static final int BOUNDARY_B = 4;
	private int totalMoves, piecesCaptured,blackStones,whiteStones;

	private String illegalMoveMessage = "This is an illegal move - ";

	/**
	 * A simple constructor of the class, required for construction by the
	 * class that contains the tests.
	 * Calls method to initializes an empty board and sets the initial variable values.
	 */
	public ReversiModel() {
		initializeGame();
		totalMoves = 0;
		blackStones = 0;
		whiteStones = 0;
	}

	/**
	 * Initializes a PlayerColour instance to hold the positions of the board
	 * and sets BLACK as the first player to make a move.
	 */
	private void initializeGame(){

		board = new PlayerColour[BOARD_HEIGHT][BOARD_WIDTH];
		currentPlayerToMove = PlayerColour.BLACK;
	}

	/**
	 * Returns the colour of the piece at the given position, null if no piece is on this field.
	 *
	 * @param x the x position of the field
	 * @param y the y position of the field
	 * @return the PlayerColour instance at (x,y) or null
	 */
	public PlayerColour getAt(int x, int y) {
		if(board[x][y] != null)
			return board[x][y];
		return null;
	}

	/**
	 * Returns the player who is to move next.
	 *
	 * @return a PlayerColour object
	 */
	public PlayerColour nextToMove() {
		return currentPlayerToMove;
	}

	/**
	 * Make a move by placing a piece of the given colour on the given field.
	 *
	 * @param player the PlayerColour of the player to make the move
	 * @param x      the x position of the field that the player wants to place its piece
	 * @param y      the y position of the field that the player wants to place its piece
	 * @throws IllegalMoveException if it is not the player's move,
	 * if the field is already occupied if the coordinates are out of range or is an illegal move.
	 */
	public void makeMove(PlayerColour player, int x, int y) throws IllegalMoveException {

		/* Checks that the right player is playing - If not, throws illegalMoveException.
		 * Check for moves outside the boundaries - If field does not exist, throws illegalMoveException.
		 * Checks if the move is one of the 4 initial moves - If yes calls method to handle appropriately.
		 * If not 4 initial moves calls method to check if that move will result to a the capture of a piece - If not throws illegalMoveException.
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

	/**
	 * This method compares the PlayerColour value passed in the makeMove method with the nextToMove() method's
	 * return variable to validate that the correct player is making a move.
	 * @param playerColour the Colour of player that to check.
	 * @throws IllegalMoveException if the playerColour does not match the return value of the nextToMove method.
	 */
	private void checkValidPlayer(PlayerColour playerColour) throws IllegalMoveException{
		if(playerColour != nextToMove())
			throw new IllegalMoveException(illegalMoveMessage + "This is not your turn, you cannot perform that move now.");
	}

	/**
	 * This method checks that the coordinates of the field that the new move will be made are valid.
	 * Valid fields have x and y values from 0-7
	 * @param x the x position of the field that the player wants to place its piece
	 * @param y the y position of the field that the player wants to place its piece
	 * @throws IllegalMoveException when the field is not valid.
	 */
	private void checkBoundaries(int x, int y) throws IllegalMoveException{
		if(x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
			throw new IllegalMoveException(illegalMoveMessage + "Field does not exists");
		}
	}

	/**
	 * This method is used to validate that the four initial pieces are placed in the center of the board.
	 * The four valid moves are (3,3), (3,4), (4,3), (4,4)
	 * @param x the x position of the field that the player wants to place its piece
	 * @param y the y position of the field that the player wants to place its piece
	 * @throws IllegalMoveException when the field is not valid or is occupied.
	 */
	private void handleFourInitialMoves(int x, int y) throws IllegalMoveException{

		if( (x == BOUNDARY_A || x == BOUNDARY_B) && (y == BOUNDARY_A || y == BOUNDARY_B) && getAt(x,y) == null){
			board[x][y] = nextToMove();
		} else if (getAt(x,y) != null){
			throw new IllegalMoveException(illegalMoveMessage + "This piece is occupied");
		} else {
			throw new IllegalMoveException(illegalMoveMessage + "This is an illegal move. The 4 initial pieces must be placed in the center");
		}
	}

	/**
	 * This method checks each of the 8 surrounding directions of the fields that the player wants to place its piece.
	 * Keeps track of the captured pieces.
	 * @param x the x position of the field that the player wants to place its piece
	 * @param y the y position of the field that the player wants to place its piece
	 * @return an integer variable representing the number of captured pieces.
	 */
	private int getNumOfCapturedPieces(int x, int y){

		/* Calls each method to check one of the 8 surrounding directions.
		 * Foreach direction checks that the first field is not empty and that is occupied by the opponents piece.
		 * Keeps checking until it a null piece or a piece with the current player colour is found.
		 * The captured pieces are then updated.
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

	/**
	 * Checks the top-left field.
	 * @param currentPlayerX the x position of the field that the player wants to place its piece
	 * @param currentPlayerY the y position of the field that the player wants to place its piece
	 */
	private void checkTopLeftField(int currentPlayerX, int currentPlayerY){
		int x = currentPlayerX;
		int y = currentPlayerY;
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

	/**
	 * Checks the top field.
	 * @param currentPlayerX the x position of the field that the player wants to place its piece
	 * @param currentPlayerY the y position of the field that the player wants to place its piece
	 */
	private void checkTopField(int currentPlayerX, int currentPlayerY){
		int  x = currentPlayerX;
		int  y = currentPlayerY;
		int tempCounter = 0;

		while (y - 1 >= 0 && nextToMove() != board[x][y-1] &&  board[x][y-1]!= null) {
			tempCounter++;
			y --;
		}

		if(y -1 >= 0  && board[x][y-1] == nextToMove()){
			for (int i = 1; i <= tempCounter; i++) {
				capturePiece(currentPlayerX,currentPlayerY - i);
			}
		}
	}

	/**
	 * Checks the top-right field.
	 * @param currentPlayerX the x position of the field that the player wants to place its piece
	 * @param currentPlayerY the y position of the field that the player wants to place its piece
	 */
	private void checkTopRightField(int currentPlayerX, int currentPlayerY) {
		int tempCounter = 0;
		int  x = currentPlayerX;
		int  y = currentPlayerY;

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

	/**
	 * Checks the left field.
	 * @param currentPlayerX the x position of the field that the player wants to place its piece
	 * @param currentPlayerY the y position of the field that the player wants to place its piece
	 */
	private void checkLeftField(int currentPlayerX, int currentPlayerY){

		int x = currentPlayerX;
		int y = currentPlayerY;
		int tempCounter = 0;

		while (x - 1 >= 1 && nextToMove() != board[x-1][y] && board[x-1][y]!= null) {
			tempCounter++;
			x--;
		}

		if(x - 1 >=0 && board[x-1][y] == nextToMove()){
			for (int i = 1; i <= tempCounter; i++) {
				capturePiece(currentPlayerX - i,currentPlayerY);
			}
		}
	}

	/**
	 * Checks the right field.
	 * @param currentPlayerX the x position of the field that the player wants to place its piece
	 * @param currentPlayerY the y position of the field that the player wants to place its piece
	 */
	private void checkRightField(int currentPlayerX, int currentPlayerY){
		int x = currentPlayerX;
		int y = currentPlayerY;
		int tempCounter = 0;

		while (x + 1 < BOARD_WIDTH && nextToMove() != board[x+1][y] &&  board[x+1][y] != null) {
			tempCounter++;
			x++;
		}

		if(x + 1 < BOARD_WIDTH && board[x+1][y] == nextToMove()){
			for (int i = 1; i <= tempCounter; i++) {
				capturePiece(currentPlayerX + i,currentPlayerY);
			}
		}
	}

	/**
	 * Checks the bottom-left field.
	 * @param currentPlayerX the x position of the field that the player wants to place its piece
	 * @param currentPlayerY the y position of the field that the player wants to place its piece
	 */
	private void checkBottomLeftField(int currentPlayerX, int currentPlayerY){
		int tempCounter = 0;
		int x = currentPlayerX;
		int y = currentPlayerY;

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

	/**
	 * Checks the bottom field.
	 * @param currentPlayerX the x position of the field that the player wants to place its piece
	 * @param currentPlayerY the y position of the field that the player wants to place its piece
	 */
	private void checkBottomField(int currentPlayerX, int currentPlayerY){
		int tempCounter = 0;
		int x = currentPlayerX;
		int y = currentPlayerY;

		while (y + 1 < BOARD_HEIGHT && nextToMove() != board[x][y+1] && board[x][y+1] != null) {
			tempCounter++;
			y++;
		}

		if(y + 1 < BOARD_HEIGHT && board[x][y+1] == nextToMove()){
			for (int i = 1; i <= tempCounter; i++) {
				capturePiece(currentPlayerX,currentPlayerY + i);
			}
		}
	}

	/**
	 * Checks the bottom-right field.
	 * @param currentPlayerX the x position of the field that the player wants to place its piece
	 * @param currentPlayerY the y position of the field that the player wants to place its piece
	 */
	private void checkBottomRightField(int currentPlayerX, int currentPlayerY){
		int tempCounter = 0;
		int  x = currentPlayerX;
		int  y = currentPlayerY;

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

	/**
	 * This method captures a piece by changing its colour to the colour of the capturer.
	 * @param xCaptured the x position of the field that the player wants to place its piece
	 * @param yCaptured the x position of the field that the player wants to place its piece
	 */
	private void capturePiece(int xCaptured, int yCaptured){
		board[xCaptured][yCaptured] = nextToMove();
		piecesCaptured++;
	}

	/**
	 * This method updates the stones of the player, by adding the number of piecesCaptured to the stones of the currentPlayer and
	 * deducting it from the opponents stones, when a capturing move is made.
	 * It also adds one to the current player because he/she placed a piece to make perform the move.
	 */
	private void updateStones(){
		if(nextToMove() == PlayerColour.BLACK){
			blackStones += piecesCaptured + 1;
			whiteStones -= piecesCaptured;
		}else {
			whiteStones += piecesCaptured + 1;
			blackStones -= piecesCaptured;
		}
	}

	/**
	 * This method switches the players turn, by changing teh value of the currentPlayerToMove object.
	 */
	private void switchPlayerTurn(){
		if(currentPlayerToMove == PlayerColour.BLACK)
			currentPlayerToMove = PlayerColour.WHITE;
		else currentPlayerToMove = PlayerColour.BLACK;
	}

	/**
	 * Return the number of black stones currently on the board.
	 *
	 * @return the no black stones
	 */
	public int getNoBlackStones() {
		return blackStones;
	}

	/**
	 * Return the number of white stones currently on the board.
	 *
	 * @return the no white stones
	 */
	public int getNoWhiteStones() {
		return whiteStones;
	}
}
