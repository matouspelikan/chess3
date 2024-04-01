package cvut.fel.pjv.pelikma3.pieces;

import cvut.fel.pjv.pelikma3.utils.Game;
import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import cvut.fel.pjv.pelikma3.utils.Move;
import cvut.fel.pjv.pelikma3.utils.Position;

import java.io.Serializable;
import java.util.List;

public class Piece implements Serializable {


    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean hasMoved = false;
    public Position position;

    public void setColor(Color color) {
        this.color = color;
    }

    public void setColor(String color){
        if(color.equals("black"))
            this.color = Color.BLACK;
        else if(color.equals("white"))
            this.color = Color.WHITE;
        else{
            System.out.println("Probleeem s color init");
        }
    }

    Color color;
    String imageIdentifier;
    public boolean alive;
    public PieceType pieceType;
    public boolean editRotated = false;

    public String getName() {
        return name;
    }

    protected String name;

    public Piece(){

    }

    public Piece(Color color, Position position){
        this.color = color;
        this.position = position;
        this.alive = true;
    }

    public Piece createSelf(){
        System.out.println("returning piece error");
        return new Piece();
    }

    public boolean isPromoted(){
        return false;
    }

    public List<Move> acceptGameBoard(GameBoard gameBoard){
        return gameBoard.possibleMoves(this);
    }

    public String getImageIdentifier() {
        return "abstract error";
    }

    public Character getLetter(){
        return 'Z';
    }
}
