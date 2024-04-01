package cvut.fel.pjv.pelikma3.controllers;

import cvut.fel.pjv.pelikma3.pieces.*;
import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameData {

    public int timer1;
    public int timer2;
    public boolean againstPC;
    public Color downColor;
    public boolean emptyBoard;
    public Color colorToMove;
    public boolean networkGame;

    public boolean editBeforeStart;
    public GameBoard gameBoard;

    List<Piece> blackPieces = new ArrayList<>();
    List<Piece> whitePieces = new ArrayList<>();
    List<Piece> allPieces = new ArrayList<>();
    public HashMap<String, Image> identifierToImageMap = new HashMap<>();
    public HashMap<String, Piece> identifierToPieceMap = new HashMap<>();

    public GameData(int timer1, int timer2, boolean edit, boolean againstPC, Color downColor,
                    GameBoard customBoard, boolean emptyBoard, Color colorToMove, boolean networkGame){
        this.timer1 = timer1;
        this.timer2 = timer2;
        this.againstPC = againstPC;
        this.editBeforeStart = edit;
        this.downColor = downColor;
        this.emptyBoard = emptyBoard;
        this.gameBoard = customBoard;
        this.colorToMove = colorToMove;
        this.networkGame = networkGame;

        createLists();
        createMaps();
    }

    public List<Piece> getPieceslist(Color color){
        if(color == Color.BLACK)
            return blackPieces;
        else
            return whitePieces;
    }

    public List<Piece> getAllPieces() {
        return allPieces;
    }

    public void createLists(){

        this.blackPieces.add(new PiecePawn(Color.BLACK));
        this.blackPieces.add(new PieceKing(Color.BLACK));
        this.blackPieces.add(new PieceRook(Color.BLACK));
        this.blackPieces.add(new PieceBishop(Color.BLACK));
        this.blackPieces.add(new PieceKnight(Color.BLACK));
        this.blackPieces.add(new PieceQueen(Color.BLACK));

        this.whitePieces.add(new PiecePawn(Color.WHITE));
        this.whitePieces.add(new PieceKing(Color.WHITE));
        this.whitePieces.add(new PieceRook(Color.WHITE));
        this.whitePieces.add(new PieceBishop(Color.WHITE));
        this.whitePieces.add(new PieceKnight(Color.WHITE));
        this.whitePieces.add(new PieceQueen(Color.WHITE));

        allPieces.addAll(whitePieces);
        allPieces.addAll(blackPieces);

    }

    public void createMaps(){
        populateImageMap("piecesoriginal");
        populatePieceMap();
    }

    /**
     * Populated pieceToImageDictionary, a dictionary that stores paths to .png images to every piece
     * @param dirName path to folder where piece .png files are
     */
    public void populateImageMap(String dirName){
        String[] piecesNames = {"pawn-white", "pawn-black", "rook-black", "rook-white", "bishop-white", "bishop-black",
                "king-white", "king-black", "queen-black", "queen-white", "knight-black", "knight-white"};
        for (String s: piecesNames) {
            String path = "/" + dirName + "/" + s + ".png";
            Image newImage = new Image(getClass().getResourceAsStream(path));
            identifierToImageMap.put(s, newImage);
        }
    }

    public void populatePieceMap(){
        for(Piece piece: allPieces){
            identifierToPieceMap.put(piece.getImageIdentifier(), piece);
        }
    }

}
