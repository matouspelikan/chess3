package cvut.fel.pjv.pelikma3.pieces;

import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.utils.Game;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import cvut.fel.pjv.pelikma3.utils.Move;
import cvut.fel.pjv.pelikma3.utils.Position;

import java.util.List;

public class PieceKing extends Piece{

    public PieceKing(){
        this.pieceType = PieceType.KING;
    }

    public PieceKing(Color color, Position position) {
        super(color, position);
        this.pieceType = PieceType.KING;
    }
    public PieceKing(Color color) {
        this.color = color;
        this.pieceType = PieceType.KING;
    }

    @Override
    public Piece createSelf() {
        return new PieceKing();
    }

    @Override
    public List<Move> acceptGameBoard(GameBoard gameBoard) {
        return gameBoard.possibleMoves(this);
    }

    @Override
    public String getImageIdentifier() {
        String s = "king-";
        if (this.color == Color.WHITE)
            s+= "white";
        else
            s += "black";
        return s;
    }

    @Override
    public String getName() {
        return "king";
    }

    @Override
    public Character getLetter() {
        return 'K';
    }
}
