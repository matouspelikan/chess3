package cvut.fel.pjv.pelikma3.pieces;

import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.utils.Game;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import cvut.fel.pjv.pelikma3.utils.Move;
import cvut.fel.pjv.pelikma3.utils.Position;

import java.util.List;

public class PieceQueen extends Piece{


    public PieceQueen(){
        this.pieceType = PieceType.QUEEN;
    }

    public PieceQueen(Color color, Position position) {
        super(color, position);
        this.pieceType = PieceType.QUEEN;
    }

    public PieceQueen(Color color) {
        this.color = color;
        this.pieceType = PieceType.QUEEN;
    }

    @Override
    public Piece createSelf() {
        return new PieceQueen();
    }

    @Override
    public List<Move> acceptGameBoard(GameBoard gameBoard) {
        return gameBoard.possibleMoves(this);
    }

    @Override
    public String getImageIdentifier() {
        String s = "queen-";
        if (this.color == Color.WHITE)
            s+= "white";
        else
            s += "black";
        return s;
    }

    @Override
    public String getName() {
        return "queen";
    }

    @Override
    public Character getLetter() {
        return 'Q';
    }
}
