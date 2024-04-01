package cvut.fel.pjv.pelikma3.pieces;

import cvut.fel.pjv.pelikma3.utils.Game;
import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import cvut.fel.pjv.pelikma3.utils.Move;
import cvut.fel.pjv.pelikma3.utils.Position;

import java.util.List;

public class PieceKnight extends Piece{


    public PieceKnight(){
        this.pieceType = PieceType.KNIGHT;
    }

    public PieceKnight(Color color, Position position) {
        super(color, position);
        this.pieceType = PieceType.KNIGHT;
    }

    public PieceKnight(Color color) {
        this.color = color;
        this.pieceType = PieceType.KNIGHT;
    }


    @Override
    public Piece createSelf() {
        return new PieceKnight();
    }

    @Override
    public List<Move> acceptGameBoard(GameBoard gameBoard) {
        return gameBoard.possibleMoves(this);
    }

    @Override
    public String getImageIdentifier() {
        String s = "knight-";
        if (this.color == Color.WHITE)
            s+= "white";
        else
            s += "black";
        return s;
    }

    @Override
    public String getName() {
        return "knight";
    }

    @Override
    public Character getLetter() {
        return 'N';
    }
}
