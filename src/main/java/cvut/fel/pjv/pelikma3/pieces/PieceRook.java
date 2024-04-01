package cvut.fel.pjv.pelikma3.pieces;

import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.utils.Game;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import cvut.fel.pjv.pelikma3.utils.Move;
import cvut.fel.pjv.pelikma3.utils.Position;

import java.util.List;

public class PieceRook extends Piece{

    public PieceRook(){
        this.pieceType = PieceType.ROOK;
    }

    public PieceRook(Color color, Position position) {
        super(color, position);
        this.pieceType = PieceType.ROOK;
    }

    public PieceRook(Color color) {
        this.color = color;
        this.pieceType = PieceType.ROOK;
    }

    @Override
    public Piece createSelf() {
        return new PieceRook();
    }

    @Override
    public List<Move> acceptGameBoard(GameBoard gameBoard) {
        return gameBoard.possibleMoves(this);
    }

    @Override
    public String getImageIdentifier() {
        String s = "rook-";
        if (this.color == Color.WHITE)
            s+= "white";
        else
            s += "black";
        return s;
    }

    @Override
    public String getName() {
        return "rook";
    }

    @Override
    public Character getLetter() {
        return 'R';
    }
}
