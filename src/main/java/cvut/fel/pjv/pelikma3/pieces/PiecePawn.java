package cvut.fel.pjv.pelikma3.pieces;

import cvut.fel.pjv.pelikma3.utils.Game;
import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import cvut.fel.pjv.pelikma3.utils.Move;
import cvut.fel.pjv.pelikma3.utils.Position;

import java.util.List;

public class PiecePawn extends Piece{

    public PiecePawn(){
        this.pieceType = PieceType.PAWN;
    }

    public PiecePawn(Color color, Position position) {
        super(color, position);
        this.pieceType = PieceType.PAWN;
    }

    public PiecePawn(Color color) {
        this.color = color;
        this.pieceType = PieceType.PAWN;
    }

    @Override
    public Piece createSelf() {
        return new PiecePawn();
    }

    @Override
    public boolean isPromoted() {
        return this.position.y == 7 || this.position.y == 0;
    }

    @Override
    public List<Move> acceptGameBoard(GameBoard gameBoard) {
        return gameBoard.possibleMoves(this);
    }

    @Override
    public String getImageIdentifier() {
        String s = "pawn-";
        if (this.color == Color.WHITE)
            s+= "white";
        else
            s += "black";
        return s;
    }

    @Override
    public String getName() {
        return "pawn";
    }

    @Override
    public Character getLetter() {
        return null;
    }
}
