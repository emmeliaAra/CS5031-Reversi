package stacs.arcade.reversi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static stacs.arcade.reversi.ReversiModel.PlayerColour.WHITE;
import static stacs.arcade.reversi.ReversiModel.PlayerColour.BLACK;

/**
 * A set of unit tests for the model class of the Reversi game.
 * 
 * @author alex.voss@st-andrews.ac.uk
 */
public class ReversiModelTests {
	
	ReversiModel model = null;
	
	@BeforeEach 
	void setup() {
		//this.model = new ReversiModelSolution(); 
		this.model = new ReversiModel();
	}
	
	@Test
	public void mustInitialiseEmptyBoard() {
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				assertEquals(this.model.getAt(x, y), null);
			}
		}
	}
	
	@Test 
	public void mustUpdateBoard() throws IllegalMoveException {
		this.model.makeMove(BLACK, 4, 4);
		assertEquals(this.model.getAt(4, 4), BLACK);
	}
	
	@Test void mustCountStones() throws IllegalMoveException {
		assertEquals(this.model.getNoBlackStones(), 0);
		assertEquals(this.model.getNoWhiteStones(), 0);
		this.model.makeMove(BLACK, 4, 4);
		assertEquals(this.model.getNoBlackStones(), 1);
		assertEquals(this.model.getNoWhiteStones(), 0);
		this.model.makeMove(WHITE, 3, 3);
		assertEquals(this.model.getNoBlackStones(), 1);
		assertEquals(this.model.getNoWhiteStones(), 1);
	}
	
	@Test
	public void mustEnforceTurnTaking() {
		assertThrows(IllegalMoveException.class, () -> {
			this.model.makeMove(WHITE, 4, 4);	
		});		
	}
	
	@Test
	public void mustRejectMoveToOccupiedField() throws IllegalMoveException {
		this.model.makeMove(BLACK, 4, 4);
		assertThrows(IllegalMoveException.class, () -> {
			this.model.makeMove(BLACK, 4, 4);	
		});
	}
	
	@Test
	public void mustRejectMoveOutOfBounds() {
		assertThrows(IllegalMoveException.class, () -> {
			this.model.makeMove(BLACK, 8, 1);	
		});
		assertThrows(IllegalMoveException.class, () -> {
			this.model.makeMove(BLACK, 1, 8);	
		});	
		assertThrows(IllegalMoveException.class, () -> {
			this.model.makeMove(BLACK, -1, 1);	
		});	
		assertThrows(IllegalMoveException.class, () -> {
			this.model.makeMove(BLACK, 1, -1);	
		});	
	}
	
	@Test
	public void mustRejectInitialMoveOutsideCenterFour() {
		assertThrows(IllegalMoveException.class, () -> {
			this.model.makeMove(BLACK, 1, 1);	
		});	
	}
	
	@Test
	public void mustAcceptValidInitialMoves() throws IllegalMoveException {		
		this.model.makeMove(BLACK, 3, 3);
		this.model.makeMove(WHITE, 3, 4);
		this.model.makeMove(BLACK, 4, 3);
		this.model.makeMove(WHITE, 4, 4);
	}
	
	@Test 
	public void mustRejectMovesThatDoNotCapture() throws IllegalMoveException {
		this.model.makeMove(BLACK, 3, 3);
		this.model.makeMove(WHITE, 3, 4);
		this.model.makeMove(BLACK, 4, 3);
		this.model.makeMove(WHITE, 4, 4);
		assertThrows(IllegalMoveException.class, () -> {
			this.model.makeMove(BLACK, 1, 1);
		});
	}
	
	@Test
	public void mustExecuteCapturingMoves() throws IllegalMoveException {
		this.model.makeMove(BLACK, 3, 3);
		this.model.makeMove(WHITE, 3, 4);
		this.model.makeMove(BLACK, 4, 3);
		this.model.makeMove(WHITE, 4, 4);
		
		this.model.makeMove(BLACK, 4, 5);			
		assertEquals(this.model.getAt(4, 5), BLACK);
		assertEquals(this.model.getAt(4, 4), BLACK);
				
		this.model.makeMove(WHITE, 5, 6);				
		assertEquals(this.model.getAt(5, 6), WHITE);
		assertEquals(this.model.getAt(4, 5), WHITE);
		
		this.model.makeMove(BLACK, 3, 5);
		assertEquals(this.model.getAt(3, 5), BLACK);
		assertEquals(this.model.getAt(3, 4), BLACK);
	}			
		
}
