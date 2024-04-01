package cvut.fel.pjv.pelikma3.utils;

import cvut.fel.pjv.pelikma3.pieces.Piece;

import java.io.Serializable;

public class Move implements Serializable {
    public Piece piece;
    public Position oldPosition;
    public Position newPosition;
    public Piece replacedPiece;
    public Move nextMove;       //mostly null - for castling

    public boolean pawnDoubleMove = false;
    public boolean enPassant = false;
    public boolean castling = false;
    public boolean bigCastling = false;
    public boolean smallCastling = false;
    public boolean promoMove = false;
    public Piece promotedPiece = null;

    public Move(Piece piece, Position position){
        this.piece = piece;
        this.newPosition = position;
        this.oldPosition = new Position(piece.position);
    }

    public Move(Piece piece, Position position, Piece replacedPiece){
        this.piece = piece;
        this.newPosition = position;
        this.oldPosition = new Position(piece.position);
        this.replacedPiece = replacedPiece;
    }


    public Move(Piece piece, Position position, Piece replacedPiece, Move nextMove){
        this.piece = piece;
        this.newPosition = position;
        this.nextMove = nextMove;
        this.oldPosition = new Position(piece.position);
        this.replacedPiece = replacedPiece;
    }

    public Move(Piece piece, Position position, Move nextMove){
        this.piece = piece;
        this.newPosition = position;
        this.oldPosition = new Position(piece.position);
        this.nextMove = nextMove;
    }
}
