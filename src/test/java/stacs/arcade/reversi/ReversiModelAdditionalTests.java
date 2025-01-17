package stacs.arcade.reversi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static stacs.arcade.reversi.ReversiModel.PlayerColour.WHITE;
import static stacs.arcade.reversi.ReversiModel.PlayerColour.BLACK;

/**
 * A set of unit tests for the model class of the Reversi game.
 *
 * @author 190023753
 */
public class ReversiModelAdditionalTests {

    ReversiModel model = null;

    @BeforeEach
    void setup() {
        this.model = new ReversiModel();
    }

    @Test
    public void initialMovesWillNotCapture() throws IllegalMoveException {

        this.model.makeMove(BLACK,4,3);
        assertEquals(1,this.model.getNoBlackStones());
        assertEquals(0,this.model.getNoWhiteStones());

        this.model.makeMove(WHITE,3,3);
        assertEquals(1,this.model.getNoWhiteStones());
        assertEquals(1,this.model.getNoBlackStones());
        assertEquals(BLACK,this.model.getAt(4,3));

        this.model.makeMove(BLACK,3,4);
        assertEquals(2,this.model.getNoBlackStones());
        assertEquals(1,this.model.getNoWhiteStones());
        assertEquals(WHITE,this.model.getAt(3,3));

        this.model.makeMove(WHITE,4,4);
        assertEquals(2,this.model.getNoWhiteStones());
        assertEquals(2,this.model.getNoBlackStones());
        assertEquals(BLACK,this.model.getAt(4,3));
        assertEquals(BLACK,this.model.getAt(3,4));
    }

    @Test
    public void shouldNotAllowPlayerToPlayTwiceInARow() throws IllegalMoveException {
        this.model.makeMove(BLACK,3,4);
        assertThrows(IllegalMoveException.class,() -> this.model.makeMove(BLACK,3,3));
    }

    @Test
    public void mustEnforceTakingTurnAfterMakeMove() throws IllegalMoveException {
        this.model.makeMove(BLACK,3,4);
        assertEquals(WHITE,this.model.nextToMove());

        this.model.makeMove(WHITE,4,4);
        assertEquals(BLACK,this.model.nextToMove());
    }

    @Test
    public void mustRejectCapturingWhenPieceNotSurrounded() throws IllegalMoveException {
        this.model.makeMove(BLACK,4,3);
        this.model.makeMove(WHITE,3,3);
        this.model.makeMove(BLACK,3,4);
        this.model.makeMove(WHITE,4,4);

        this.model.makeMove(BLACK,3,2); //4-1
        assertEquals(1,this.model.getNoWhiteStones());
        assertEquals(4,this.model.getNoBlackStones());

        this.model.makeMove(WHITE,2,2);//3-3
        assertEquals(3,this.model.getNoBlackStones());
        assertEquals(3,this.model.getNoWhiteStones());
    }

    @Test
    public void mustExecuteCapturingMovesByCapturingMoreThanOnePiece() throws IllegalMoveException {

        this.model.makeMove(BLACK,4,3);
        this.model.makeMove(WHITE,3,3);
        this.model.makeMove(BLACK,3,4);
        this.model.makeMove(WHITE,4,4);

        this.model.makeMove(BLACK,3,2);
        assertEquals(BLACK,this.model.getAt(3,3));

        this.model.makeMove(WHITE,2,2);
        assertEquals(WHITE,this.model.getAt(3,3));

        this.model.makeMove(BLACK,1,2);
        assertEquals(BLACK,this.model.getAt(2,2));

        this.model.makeMove(WHITE,3,5);
        assertEquals(WHITE,this.model.getAt(3,4));

        this.model.makeMove(BLACK,5,5);
        assertEquals(BLACK,this.model.getAt(3,3));
        assertEquals(BLACK,this.model.getAt(4,3));
    }

    @Test
    public void mustCountStonesAfterCapturing() throws IllegalMoveException {

        this.model.makeMove(BLACK,4,3);
        this.model.makeMove(WHITE,3,3);
        this.model.makeMove(BLACK,3,4);
        this.model.makeMove(WHITE,4,4);

        this.model.makeMove(BLACK,3,2);
        assertEquals(1,this.model.getNoWhiteStones());
        assertEquals(4,this.model.getNoBlackStones());

        this.model.makeMove(WHITE,2,2);
        assertEquals(3,this.model.getNoBlackStones());
        assertEquals(3,this.model.getNoWhiteStones());

        this.model.makeMove(BLACK,1,2);
        assertEquals(5,this.model.getNoBlackStones());
        assertEquals(2,this.model.getNoWhiteStones());

        this.model.makeMove(WHITE,3,5);
        assertEquals(4,this.model.getNoBlackStones());
        assertEquals(4,this.model.getNoWhiteStones());

        this.model.makeMove(BLACK,5,5);
        assertEquals(7,this.model.getNoBlackStones());
        assertEquals(2,this.model.getNoWhiteStones());
    }

    @Test
    public void mustRejectCaptureWithEmptyFieldInBetween() throws IllegalMoveException {

        this.model.makeMove(BLACK,4,3);
        this.model.makeMove(WHITE,3,3);
        this.model.makeMove(BLACK,3,4);
        this.model.makeMove(WHITE,4,4);

        assertThrows(IllegalMoveException.class,() -> this.model.makeMove(BLACK,4,6));
    }

    @Test
    public void mustUpdateBoardAfterCapture() throws IllegalMoveException {

        this.model.makeMove(BLACK,4,3);
        this.model.makeMove(WHITE,3,3);
        this.model.makeMove(BLACK,3,4);
        this.model.makeMove(WHITE,4,4);

        this.model.makeMove(BLACK,4,5);
        assertEquals(this.model.getAt(4, 4), BLACK);
    }

    @Test
    public void mustCaptureInMultipleDimensions() throws IllegalMoveException{

        this.model.makeMove(BLACK,3,3);
        this.model.makeMove(WHITE,3,4);
        this.model.makeMove(BLACK,4,3);
        this.model.makeMove(WHITE,4,4);

        this.model.makeMove(BLACK,5,5);
        this.model.makeMove(WHITE,3,2);
        this.model.makeMove(BLACK,2,2);
        this.model.makeMove(WHITE,5,4);

        assertEquals(WHITE,this.model.getAt(4,4));
        assertEquals(WHITE,this.model.getAt(4,3));
    }

    @Test
    public void mustAllowValidPieceInTheCorners() throws IllegalMoveException{

        this.model.makeMove(BLACK,3,3);
        this.model.makeMove(WHITE,3,4);
        this.model.makeMove(BLACK,4,3);
        this.model.makeMove(WHITE,4,4);

        this.model.makeMove(BLACK,5,5);
        this.model.makeMove(WHITE,3,2);
        this.model.makeMove(BLACK,2,2);
        this.model.makeMove(WHITE,5,4);
        this.model.makeMove(BLACK,3,5);
        this.model.makeMove(WHITE,6,6);
        this.model.makeMove(BLACK,7,7); // BOTTOM RIGHT CORNER

        this.model.makeMove(WHITE,2,5);
        this.model.makeMove(BLACK,4,2);
        this.model.makeMove(WHITE,5,2);
        this.model.makeMove(BLACK,6,2);
        this.model.makeMove(WHITE,4,5);
        this.model.makeMove(BLACK,1,6);
        this.model.makeMove(WHITE,1,1);
        this.model.makeMove(BLACK,0,0); // TOP LEFT CORNER

        this.model.makeMove(WHITE,4,1);
        this.model.makeMove(BLACK,3,6);
        this.model.makeMove(WHITE,6,1);
        this.model.makeMove(BLACK,7,0); // TOP RIGHT CORNER

        this.model.makeMove(WHITE,2,4);
        this.model.makeMove(BLACK,6,5);
        this.model.makeMove(WHITE,0,7); // BOTTOM LEFT CORNER

        assertNotEquals(null,this.model.getAt(0,0));
        assertNotEquals(null,this.model.getAt(0,7));
        assertNotEquals(null,this.model.getAt(7,7));
        assertNotEquals(null,this.model.getAt(7,0));
    }
}
