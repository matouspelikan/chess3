package cvut.fel.pjv.pelikma3.utils;

import cvut.fel.pjv.pelikma3.pieces.Piece;
import cvut.fel.pjv.pelikma3.pieces.PieceKnight;
import cvut.fel.pjv.pelikma3.pieces.PiecePawn;
import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.players.Player;
import cvut.fel.pjv.pelikma3.server.Client;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game{
    public GameBoard gameBoard;
    public Color downColor;
    public Color upColor;
    Random rand = new Random();

    public boolean isNetworkGame = false;
    public boolean ended = false;
    public Color winColor;

    Client client;

    public Color getColorTurn() {
        return colorTurn;
    }

    public void setColorTurn(Color colorTurn) {
        this.colorTurn = colorTurn;
    }

    Color colorTurn;

    public int iter = 10;

    public Game(Color upColor, Color downColor, boolean emptyBoard){
        this.upColor = upColor;
        this.downColor = downColor;
        gameBoard = new GameBoard(upColor, downColor, emptyBoard);
        colorTurn = Color.WHITE;
    }

    public Game(Color upColor, Color downColor, GameBoard gameBoard){
        this.upColor = upColor;
        this.downColor = downColor;
        GameBoard newgameboard = new GameBoard(upColor, downColor, false);
        this.gameBoard = newgameboard;
        if(downColor != Color.WHITE){
            gameBoard.rotateBoardDeep();
        }
        newgameboard.board = gameBoard.board;
        newgameboard.moveHistory = gameBoard.moveHistory;
        newgameboard.moveHistoryGUI = gameBoard.moveHistoryGUI;
        newgameboard.findKings();

        colorTurn = Color.WHITE;
    }

    public void initializeNetworkConnection(){
        client = new Client("localhost", 9800);
        client.connect();
    }

    public String newRoomRequest(){
        client.sendMessage("create");
        return null;
    }


    public void makePromotion(Piece piece, Position position){
        this.gameBoard.makePromotion(piece, position);
    }

    public Piece[][] getBoard(){
        return this.gameBoard.board;
    }

    public GameBoard getGameBoard(){
        return this.gameBoard;
    }

    public boolean TestCheckForColor(Color color){
        List<Piece> threats = gameBoard.checkForChecks(color);
        if(threats.stream().count() <= 0){
            return false;
        }
        return true;
    }

    public boolean TestCheckMateForColor(Color color){
        List<Piece> threats = gameBoard.checkForChecks(color);
        if(threats.stream().count() <= 0){
            return false;
        }
        List<Piece> allPiecesOfColor = gameBoard.getEveryPieceOfColor(color);
        for(Piece piece: allPiecesOfColor){
            if(this.getAllMoves(piece).stream().count()>0){
                return false;
            }
        }
        return true;
    }


    private List<Piece> getAllPieces() {
        List<Piece> allPieces = new ArrayList<>();
        Piece target = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if((target = this.gameBoard.getPiece(i, j, false)) != null)
                    allPieces.add(target);
            }
        }
        return allPieces;
    }

    public boolean checkForStaleMate(List<Piece> allPieces, Color color){
        if(!this.TestCheckMateForColor(color)) {
            for (Piece piece : allPieces) {
                if (piece.getColor() == color) {
                    if (this.getAllMoves(piece).stream().count() > 0){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean checkForDrawColored(List<Piece> allPieces, Color color) {
        if(checkForStaleMate(allPieces, color)){
            return true;
        }
        return false;
    }

    public boolean checkForDraw(Color color){
        List<Piece> allPieces = this.getAllPieces();
        if(allPieces.stream().count() <= 2)
            return true;
        if(checkForDrawColored(allPieces, color))
            return true;
        return false;
    }

    public Move randomMove(Color color){
        List<Piece> pieces = new ArrayList<>();
        Piece target = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if((target = this.gameBoard.getPiece(i, j, false)) != null && target.getColor() == color){
                    pieces.add(target);
                }
            }
        }
        Move move = null;
        while(move == null){
            if(pieces.stream().count() <= 0)
                return null;
            target = pieces.get(rand.nextInt((int)pieces.stream().count()));
            pieces.remove(target);
            move = randomMoveFromPiece(target);
        }
        return move;
    }

    private Move randomMoveFromPiece(Piece piece){
        List<Move> moves = this.getAllMoves(piece);

        if(moves.stream().count()>0){
            return moves.get(rand.nextInt((int)moves.stream().count()));
        }
        return null;
    }


    public List<Move> getAllMoves(Piece piece){
        gameBoard.updateRotatedBoard();

        List<Move> allMoves = piece.acceptGameBoard(gameBoard);
        List<Move> nonCheckMoves = new ArrayList<>();

        for(Move move : allMoves){
            if (gameBoard.testMoveForHomeCheck(move)){
                nonCheckMoves.add(move);
            }
        }
        return nonCheckMoves;
    }

    public Piece getPieceFromPosition(Position position){
        return this.gameBoard.getPiece(position.x, position.y, false);
    }

    public List<Position> makeMove(Move move, boolean forwarding){
        this.colorTurn = move.piece.getColor().otherColor();
        return this.gameBoard.makeMove(move, forwarding, true);
    }

    public List<Position> revertMove(){
        this.colorTurn = this.colorTurn.otherColor();
        return this.gameBoard.revertLastMove();
    }

    public Piece getGameBoardPosition(Position position){
        return this.gameBoard.getPiece(position.x, position.y, false);
    }

}
