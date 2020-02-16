package stacs.arcade.reversi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.font.TextHitInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static stacs.arcade.reversi.ReversiModel.PlayerColour.WHITE;
import static stacs.arcade.reversi.ReversiModel.PlayerColour.BLACK;


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
    public void mustRejectWhiteAsFirstPlayer() {
        assertThrows(IllegalMoveException.class,() -> this.model.makeMove(WHITE,4,3));
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

        this.model.makeMove(BLACK,3,2); //4-1
        assertEquals(1,this.model.getNoWhiteStones());
        assertEquals(4,this.model.getNoBlackStones());

        this.model.makeMove(WHITE,2,2);//3-3
        assertEquals(3,this.model.getNoBlackStones());
        assertEquals(3,this.model.getNoWhiteStones());

        this.model.makeMove(BLACK,1,2); //5-2

        this.model.makeMove(WHITE,3,5); //4-4
        this.model.makeMove(BLACK,5,5); //7-2
        assertEquals(7,this.model.getNoBlackStones());
        assertEquals(2,this.model.getNoWhiteStones());


    }
}
