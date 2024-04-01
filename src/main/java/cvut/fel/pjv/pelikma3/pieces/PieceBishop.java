package cvut.fel.pjv.pelikma3.pieces;

import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.utils.Game;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import cvut.fel.pjv.pelikma3.utils.Move;
import cvut.fel.pjv.pelikma3.utils.Position;

import java.util.List;

public class PieceBishop extends Piece{

    public PieceBishop(){
        this.pieceType = PieceType.BISHOP;
    }

    public PieceBishop(Color color, Position position) {
        super(color, position);
        this.pieceType = PieceType.BISHOP;
    }

    public PieceBishop(Color color) {
        this.color = color;
        this.pieceType = PieceType.BISHOP;
    }

    @Override
    public Piece createSelf() {
        return new PieceBishop();
    }

    @Override
    public List<Move> acceptGameBoard(GameBoard gameBoard) {
        return gameBoard.possibleMoves(this);
    }

    @Override
    public String getImageIdentifier() {
        String s = "bishop-";
        if (this.color == Color.WHITE)
            s+= "white";
        else
            s += "black";
        return s;
    }

    @Override
    public String getName() {
        return "bishop";
    }


    @Override
    public Character getLetter() {
        return 'B';
    }
}
