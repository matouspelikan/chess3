package cvut.fel.pjv.pelikma3.utils;

import cvut.fel.pjv.pelikma3.pieces.*;
import cvut.fel.pjv.pelikma3.players.Color;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GameBoard {
    private static final Logger logger = Logger.getLogger(GameBoard.class.getName());

    Piece[][] board = new Piece[8][8];
    Piece[][] rotatedBoard = new Piece[8][8];
    Color downColor;
    Color upColor;
    List<Move> moveHistory = new ArrayList<>();
    List<Move> moveHistoryGUI = new ArrayList<>();
    public Piece[] kings = new Piece[2];

    List<Position> vectorsQueen = new ArrayList<>();
    List<Position> vectorsKing = new ArrayList<>();
    List<Position> vectorsKnight = new ArrayList<>();
    List<Position> vectorsBishop = new ArrayList<>();

    private void initVectors() {
        vectorsQueen.add(new Position(-1, -1));
        vectorsQueen.add(new Position(-1, 0));
        vectorsQueen.add(new Position(-1, 1));
        vectorsQueen.add(new Position(0, -1));
//        vectorsQueen.add(new Position(0, 0));
        vectorsQueen.add(new Position(0, 1));
        vectorsQueen.add(new Position(1, -1));
        vectorsQueen.add(new Position(1, 0));
        vectorsQueen.add(new Position(1, 1));
        vectorsKing.add(new Position(-1, -1));
        vectorsKing.add(new Position(0, -1));
        vectorsKing.add(new Position(1, -1));
        vectorsKing.add(new Position(-1, 0));
        vectorsKing.add(new Position(1, 0));
        vectorsKing.add(new Position(-1, 1));
        vectorsKing.add(new Position(0, 1));
        vectorsKing.add(new Position(1, 1));
        vectorsKnight.add(new Position(2, 1));
        vectorsKnight.add(new Position(2, -1));
        vectorsKnight.add(new Position(-2, 1));
        vectorsKnight.add(new Position(-2, -1));
        vectorsKnight.add(new Position(1, 2));
        vectorsKnight.add(new Position(1, -2));
        vectorsKnight.add(new Position(-1, 2));
        vectorsKnight.add(new Position(-1, -2));
        vectorsBishop.add(new Position(1, 1));
        vectorsBishop.add(new Position(1, -1));
        vectorsBishop.add(new Position(-1, 1));
        vectorsBishop.add(new Position(-1, -1));
    }

    public GameBoard(Color upColor, Color downColor, boolean emptyBoard) {
        this.upColor = upColor;
        this.downColor = downColor;
        if(!emptyBoard){
            this.generateDefaultLayout();
        }
        initVectors();
        this.findKings();
    }

    public Piece[][] getBoard() {
        return board;
    }

    /**
     * iterates through the entire board and stores both kings in an array for faster future use
     */
    public void findKings(){
        int index = 0;
        Piece target;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (index > 2) {
                    logger.log(Level.SEVERE, "MORE THAN TWO KINGS IN GAME");
                }
                if ((target = getPiece(i, j, false)) != null && target.pieceType == PieceType.KING) {
                    if (target.getColor() == Color.WHITE){
                        kings[0] = target;
                    }
                    else if(target.getColor() == Color.BLACK){
                        kings[1] = target;
                    }
                }
            }
        }
    }

    public void setBoard(int x, int y, Piece piece){
        this.board[y][x] = piece;
    }

    private void copyHistory(){
        moveHistory.clear();
        for(Move move : moveHistoryGUI){
            moveHistory.add(move);
        }
    }

    /**
     * Clear move history if the gameboard is edited before start
     */
    public void clearHistories(){
        moveHistoryGUI.clear();
        moveHistory.clear();
    }

    /**
     * gets the back-th item from the end of movehistory
     * @param back number of steps back to past version of board
     * @return
     */
    public Move getMoveFromGui(int back){
        int len = (int)moveHistoryGUI.stream().count();
        return this.moveHistoryGUI.get(len-back);
    }

    public int getGUIHistoryCount(){
        return (int)moveHistoryGUI.stream().count();
    }

    /**
     * deletes count number of items from history, used if user is overwriting movehistory, deleting overwritten moves
     * @param count number of overwritten moves to remove
     */
    public void deleteFromHistory(int count){
        int len;
        for (int i = 0; i < count; i++) {
            len = (int)moveHistoryGUI.stream().count();
            moveHistoryGUI.remove(len - 1);
        }
        copyHistory();
    }

    public Move getLastMove() {
        int len = (int) (moveHistory.stream().count());
        if (len > 0)
            return this.moveHistory.get(len - 1);
        else
            return null;
    }

    /**
     * places the promoted piece to the board
     * @param piece new promoted piece
     * @param position coordinates of promoted piece
     */
    public void makePromotion(Piece piece, Position position) {
        getLastMove().promotedPiece = piece;
        this.board[position.y][position.x] = piece;
        this.updateRotatedBoard();
    }

    /**
     * @param move
     * @param forwarding
     * @param fromGUI
     * @return
     */
    public List<Position> makeMove(Move move, boolean forwarding, boolean fromGUI) {
        if(forwarding)
            fromGUI = false;
        if(move.piece.pieceType == PieceType.KING){
            if(move.piece.getColor() == Color.WHITE)
                this.kings[0] = move.piece;
            else
                this.kings[1] = move.piece;
        }
        moveHistory.add(move);
        if(fromGUI)
            moveHistoryGUI.add(move);
        List<Position> updated = new ArrayList<>();
        move.piece.hasMoved = true;

        if (move.piece.pieceType == PieceType.PAWN && (move.newPosition.y == 0 || move.newPosition.y == 7)) {
            if (move.newPosition.y == 7) {

            }
        }

        updated.add(new Position(move.piece.getPosition().x, move.piece.getPosition().y));
        updated.add(new Position(move.newPosition.x, move.newPosition.y));
        this.board[move.oldPosition.y][move.oldPosition.x] = null;
        this.board[move.newPosition.y][move.newPosition.x] = move.piece;
        move.piece.setPosition(new Position(move.newPosition.x, move.newPosition.y));
        if (move.replacedPiece != null) {
            move.replacedPiece.alive = false;
        }
        if (move.enPassant) {
            this.board[move.replacedPiece.getPosition().y][move.replacedPiece.getPosition().x] = null;
            updated.add(new Position(move.replacedPiece.getPosition()));
        }
        if (move.nextMove != null) {
            updated.addAll(makeMove(move.nextMove, false, true));
        }

        if (move.piece.isPromoted())
            move.promoMove = true;

        this.updateRotatedBoard();
        return updated;
    }


    /**
     * return the king of given color
     * @param color
     * @return
     */
    public Piece GetKing(Color color) {
        List<Piece> theRightKing = Arrays.stream(kings).filter(c -> c.getColor() == color).collect(Collectors.toList());
        return theRightKing.get(0);
    }


    /**
     * returns List that cointains every piece of given color that is currently alive
     * @param color
     * @return
     */
    public List<Piece> getEveryPieceOfColor(Color color) {
        List<Piece> pieces = new ArrayList<>();
        Piece target = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((target = getPiece(i, j, false)) != null && target.getColor() == color)
                    pieces.add(target);
            }
        }
        return pieces;
    }

    private List<Piece> isKingChecked(PieceKing king) {
        List<Piece> threats = new ArrayList<>();
        List<Piece> pieces = new ArrayList<>();
        for (Position vector : vectorsQueen) {
            Piece possibleThreat = this.firstPieceInDirection(king, vector);
            if (possibleThreat != null) {
                pieces.add(possibleThreat);
                List<Move> possibleThreatMoves = possibleThreat.acceptGameBoard(this);
                for (Move move : possibleThreatMoves) {
                    System.out.println(move.newPosition);
                    if (move.newPosition.equals(king.position)) { //CHECKKKK
                        threats.add(possibleThreat);
                    }
                }
            }
        }
        for (Position vector : vectorsKnight) {
            Piece possibleThreat = onlyFirstPieceInDirection(king, vector);
            if (possibleThreat != null) {
                pieces.add(possibleThreat);
                List<Move> possibleThreatMoves = possibleThreat.acceptGameBoard(this);
                for (Move move : possibleThreatMoves) {
                    if (move.newPosition.equals(king.position)) { //CHECKKKK
                        threats.add(possibleThreat);
                    }
                }
            }
        }
        System.out.println(threats);
        return threats;
    }


    /**
     * Checks if a king of given color is currently being checked by an enemy piece
     * @param color
     * @return List of pieces that are threatening the king
     */
    public List<Piece> checkForChecks(Color color) {
        List<Piece> theRightKing = Arrays.stream(kings).filter(c -> c.getColor() == color).collect(Collectors.toList());
        List<Piece> threats = new ArrayList<>();
        int iter = 0;
        for (Piece king : theRightKing) {
            if (iter > 0)
                logger.log(Level.SEVERE, "!!!check error!!!");
            iter++;
            threats.addAll(isKingChecked((PieceKing) king));
        }
        return threats;
    }

    /**
     * Check if making a move would result in having your king checked
     * @param move
     * @return true if king would be checked
     */
    public boolean testMoveForHomeCheck(Move move) {
        this.makeMove(move, false, false);
        List<Piece> threats = checkForChecks(move.piece.getColor());
        this.revertLastMove();
        if (threats.stream().count() == 0) {
            return true;
        }
        return false;
    }

    private Piece onlyFirstPieceInDirection(Piece piece, Position vector) {
        Position base = new Position(piece.getPosition());
        Piece target = null;
        base.x += vector.x;
        base.y += vector.y;
        if (isPositionOnBoard(base) && ((target = getPiece(base.x, base.y, false)) != null) && target.getColor() != piece.getColor()) {
            return target;
        }
        return null;
    }

    private Piece firstPieceInDirection(Piece piece, Position vector) {
        Position base = new Position(piece.getPosition());
        Piece target = null;
        base.x += vector.x;
        base.y += vector.y;
//        System.out.println("vector: " + vector.toString());
        while (isPositionOnBoard(base) && ((target = getPiece(base.x, base.y, false)) == null)) {
            if(vector.equals(new Position(0, -1))){
            }
            base.x += vector.x;
            base.y += vector.y;
        }
        if (isPositionOnBoard(base) && target.getColor() != piece.getColor())
            return target;
        return null;
    }



    /**
     * undoes the effect of last move, goes one step to past version of board
     * @return List of coordinates on board that were changed
     */
    public List<Position> revertLastMove() {
        int len = (int) moveHistory.stream().count();
        if (len <= 0)
            return null;

        List<Position> update = new ArrayList<>();
        Move lastMove = this.moveHistory.get(len - 1);
        moveHistory.remove(len - 1);
        this.board[lastMove.oldPosition.y][lastMove.oldPosition.x] = lastMove.piece;
//        lastMove.piece.position = new Position(lastMove.oldPosition);
        lastMove.piece.position = lastMove.oldPosition;
        update.add(lastMove.newPosition);
        if (lastMove.replacedPiece != null) {
            if (lastMove.enPassant) {
                this.board[lastMove.replacedPiece.position.y][lastMove.replacedPiece.position.x] = lastMove.replacedPiece;
                this.board[lastMove.newPosition.y][lastMove.newPosition.x] = null;
                update.add(lastMove.replacedPiece.position);
                update.add(lastMove.oldPosition);
            } else {
                this.board[lastMove.newPosition.y][lastMove.newPosition.x] = lastMove.replacedPiece;
                lastMove.replacedPiece.position = lastMove.newPosition;
                lastMove.replacedPiece.alive = true;
                update.add(lastMove.oldPosition);
            }
        } else {
            this.board[lastMove.newPosition.y][lastMove.newPosition.x] = null;
            update.add(lastMove.oldPosition);
        }

        if (lastMove.castling) {
            lastMove.piece.hasMoved = false;
            if (lastMove.piece.pieceType == PieceType.ROOK)
                update.addAll(revertLastMove());
        }

        return update;
    }

    private Piece[][] rotateBoard() {
        Piece[][] rotatedBoard = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                rotatedBoard[i][j] = this.board[7 - i][7 - j];
            }
        }
        return rotatedBoard;
    }

    public void rotateBoardDeep() {
        Piece[][] rotatedBoard = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                rotatedBoard[i][j] = this.board[7 - i][7 - j];
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(rotatedBoard[i][j] != null)
                    this.rotatePosition(rotatedBoard[i][j].position);
            }
        }
        rotateMoves(this.moveHistory);
        this.board = rotatedBoard;
    }

    public void updateRotatedBoard() {
        this.rotatedBoard = this.rotateBoard();
    }

    public void rotatePosition(Position position) {
        position.x = 7 - position.x;
        position.y = 7 - position.y;
    }

    private List<Move> rotateMoves(List<Move> moves) {
        for (Move move : moves) {
            rotatePosition(move.newPosition);
            rotatePosition(move.oldPosition);
        }
        return moves;
    }

    /**
     * return List of Moves each piece is able to make at given time
     * @param piece
     * @return
     */
    public List<Move> possibleMoves(Piece piece) {
        logger.log(Level.SEVERE, "this should not happen, possible moves on abstract Piece!");
        return null;
    }

    public List<Move> possibleMoves(PiecePawn pawn) {
        Position p = pawn.position;
        boolean rotated = false;
        if (pawn.getColor() == upColor) {
            this.rotatePosition(p);
            rotated = true;
        }

        Piece target;

        List<Move> moves = new ArrayList<>();
        if (pawn.getPosition().y == 1) {
            if (getPiece(p.x, p.y + 1, rotated) == null) {
                if (getPiece(p.x, p.y + 2, rotated) == null) {
                    Position newPosition = new Position(p.x, p.y + 2);
                    Move newMove = new Move(pawn, newPosition);
                    newMove.pawnDoubleMove = true;
                    moves.add(newMove);
                }
            }

        }
        if (isPositionOnBoard(new Position(p.x, p.y + 1)) && (target = getPiece(p.x, p.y + 1, rotated)) == null) {
            Position newPosition = new Position(p.x, p.y + 1);
            moves.add(new Move(pawn, newPosition));
        }

        if (isPositionOnBoard(new Position(p.x + 1, p.y + 1)) && ((target = getPiece(p.x + 1, p.y + 1, rotated)) != null) && target.getColor() != pawn.getColor()) {
            Position newPosition = new Position(p.x + 1, p.y + 1);
            moves.add(new Move(pawn, newPosition, target));
        }
        if (isPositionOnBoard(new Position(p.x - 1, p.y + 1)) && ((target = getPiece(p.x - 1, p.y + 1, rotated)) != null) && target.getColor() != pawn.getColor()) {
            Position newPosition = new Position(p.x - 1, p.y + 1);
            moves.add(new Move(pawn, newPosition, target));
        }

        if (rotated) {
            rotatePosition(p);
        }

        //en passant
        Move lastMove = getLastMove();
        if (lastMove != null) {
            Integer a = ((lastMove.newPosition.y - lastMove.oldPosition.y) / 2);
        }

        if (lastMove != null && lastMove.pawnDoubleMove && pawn.position.y == lastMove.newPosition.y
                && (pawn.position.x - 1 == lastMove.newPosition.x || pawn.position.x + 1 == lastMove.newPosition.x)) {
            Position newPosition = new Position(lastMove.newPosition.x, lastMove.newPosition.y - ((lastMove.newPosition.y - lastMove.oldPosition.y) / 2));
            if (rotated) rotatePosition(newPosition);
            Move newMove = new Move(pawn, newPosition, lastMove.piece);
            if (rotated) rotatePosition(newMove.oldPosition); //hotfix - dunno
            newMove.enPassant = true;
            moves.add(newMove);
        }

        if (rotated) {
            return rotateMoves(moves);
        } else
            return moves;
    }

    public List<Move> possibleMoves(PieceKing king) {
        Position basePosition = new Position(king.getPosition().x, king.getPosition().y);

        List<Move> moves = new ArrayList<Move>();
        Piece target = null;

        Position newPosition = new Position(0, 0);
        for (Position vector : vectorsKing) {
            newPosition.x = basePosition.x + vector.x;
            newPosition.y = basePosition.y + vector.y;

            if (isPositionOnBoard(newPosition)) {
                if ((target = getPiece(newPosition.x, newPosition.y, false)) == null) {
                    moves.add(new Move(king, new Position(newPosition)));
                } else {
                    if (target.getColor() != king.getColor()) {
                        moves.add(new Move(king, new Position(newPosition), target));
                    }
                }
            }
        }

        if (!king.hasMoved) {
            newPosition.x = basePosition.x + 1;
            newPosition.y = basePosition.y;
            int steps = 1;
            // right side castling
            while (isPositionOnBoard(newPosition) && (target = getPiece(newPosition.x, newPosition.y, false)) == null) {
                newPosition.x += 1;
                steps++;
            }
            if (target != null && target.pieceType == PieceType.ROOK && !((PieceRook) target).hasMoved) {
                if (steps == 4 || steps == 3) {
                    Move RookMove = new Move(target, new Position(basePosition.x + 1, basePosition.y));
                    RookMove.castling = true;
                    Move kingMove = new Move(king, new Position(basePosition.x + 2, basePosition.y), RookMove);
                    kingMove.castling = true;
                    moves.add(kingMove);

                    if (steps == 3){
                        kingMove.smallCastling = true;
                        RookMove.smallCastling = true;
                    }
                    else{
                        kingMove.bigCastling = true;
                        RookMove.bigCastling = true;
                    }
                } else {
                    logger.log(Level.SEVERE, "King Castling error");
                }
            }
            //left side castling
            newPosition.x = basePosition.x - 1;
            newPosition.y = basePosition.y;
            steps = 1;
            while (isPositionOnBoard(newPosition) && (target = getPiece(newPosition.x, newPosition.y, false)) == null) {
                newPosition.x -= 1;
                steps++;
            }
            if (target != null && target.pieceType == PieceType.ROOK && !((PieceRook) target).hasMoved) {
                if (steps == 4 || steps == 3) {
                    Move RookMove = new Move(target, new Position(basePosition.x - 1, basePosition.y));
                    RookMove.castling = true;
                    Move kingMove = new Move(king, new Position(basePosition.x - 2, basePosition.y), RookMove);
                    kingMove.castling = true;
                    moves.add(kingMove);

                    if (steps == 3){
                        kingMove.smallCastling = true;
                        RookMove.smallCastling = true;
                    }
                    else{
                        kingMove.bigCastling = true;
                        RookMove.bigCastling = true;
                    }

                } else {
                    logger.log(Level.SEVERE, "king castling error");
                }
            }
        }
        return moves;
    }

    public List<Move> possibleMoves(PieceKnight knight) {
        Position basePosition = knight.getPosition();

        Piece target;

        List<Position> positions = new ArrayList<>();
        List<Move> moves = new ArrayList<>();

        Position updatedPosition = new Position(0, 0);
        for (Position vector : vectorsKnight) {
            updatedPosition.x = basePosition.x + vector.x;
            updatedPosition.y = basePosition.y + vector.y;

            if (isPositionOnBoard(updatedPosition)) {
                if ((target = getPiece(updatedPosition.x, updatedPosition.y, false)) == null) {
                    Position newPosition = new Position(updatedPosition.x, updatedPosition.y);
                    positions.add(newPosition);
                    moves.add(new Move(knight, newPosition));
                } else {
                    if (target.getColor() != knight.getColor()) {
                        Position newPosition = new Position(updatedPosition.x, updatedPosition.y);
                        positions.add(newPosition);
                        moves.add(new Move(knight, newPosition, target));
                    }
                    continue;
                }
            }
        }
        return moves;
    }

    public List<Move> possibleMoves(PieceRook rook) {
        List<Move> moves = new ArrayList<>();
        List<Position> vectorRook = new ArrayList<>();
        vectorRook.add(new Position(0, 1));
        vectorRook.add(new Position(0, -1));
        vectorRook.add(new Position(1, 0));
        vectorRook.add(new Position(-1, 0));
        for (Position vector : vectorRook) {
            moves.addAll(getVectorMoves(rook, vector));
        }
        return moves;
    }

    public List<Move> possibleMoves(PieceBishop bishop) {
        List<Move> moves = new ArrayList<>();
        for (Position vector : vectorsBishop) {
            moves.addAll(getVectorMoves(bishop, vector));
        }
        return moves;
    }

    public List<Move> possibleMoves(PieceQueen queen) {
        List<Move> moves = new ArrayList<>();
        for (Position vector : vectorsQueen) {
            moves.addAll(getVectorMoves(queen, vector));
        }
        return moves;
    }

    private List<Move> getVectorMoves(Piece piece, Position vector) {
        Position currentPosition = new Position(piece.getPosition().x, piece.getPosition().y);
        List<Position> positions = new ArrayList<>();
        List<Move> moves = new ArrayList<>();
        Piece target;

        currentPosition.x += vector.x;
        currentPosition.y += vector.y;
        while (isPositionOnBoard(currentPosition)) {
            if ((target = getPiece(currentPosition.x, currentPosition.y, false)) == null) {
                Position newPosition = new Position(currentPosition.x, currentPosition.y);
                positions.add(newPosition);
                moves.add(new Move(piece, newPosition));
            } else {
                if (target.getColor() != piece.getColor()) {
                    Position newPosition = new Position(currentPosition.x, currentPosition.y);
                    positions.add(newPosition);
                    moves.add(new Move(piece, newPosition, target));
                }
                break;
            }
            currentPosition.x += vector.x;
            currentPosition.y += vector.y;
        }
        return moves;
    }

    public boolean isPositionOnBoard(Position position) {
        return position.x >= 0 && position.x < 8 && position.y >= 0 && position.y < 8;
    }

    public Piece getPiece(int x, int y, boolean rotated) {
        if (rotated)
            return this.rotatedBoard[y][x];
        else {
            return this.board[y][x];
        }
    }

    private void generateDefaultLayout() {
        board[0][0] = new PieceRook(downColor, new Position(0, 0));
        board[0][1] = new PieceKnight(downColor, new Position(1, 0));
        board[0][2] = new PieceBishop(downColor, new Position(2, 0));
        if (downColor == Color.BLACK) {
            board[0][3] = new PieceKing(downColor, new Position(3, 0));
            board[0][4] = new PieceQueen(downColor, new Position(4, 0));
        } else {
            board[0][3] = new PieceQueen(downColor, new Position(3, 0));
            board[0][4] = new PieceKing(downColor, new Position(4, 0));
        }
        board[0][5] = new PieceBishop(downColor, new Position(5, 0));
        board[0][6] = new PieceKnight(downColor, new Position(6, 0));
        board[0][7] = new PieceRook(downColor, new Position(7, 0));

        for (int i = 0; i < 8; i++) {
            board[1][i] = new PiecePawn(downColor, new Position(i, 1));
            board[6][i] = new PiecePawn(upColor, new Position(i, 6));
        }

        board[7][0] = new PieceRook(upColor, new Position(0, 7));
        board[7][1] = new PieceKnight(upColor, new Position(1, 7));
        board[7][2] = new PieceBishop(upColor, new Position(2, 7));
        if (downColor == Color.BLACK) {
            board[7][3] = new PieceKing(upColor, new Position(3, 7));
            board[7][4] = new PieceQueen(upColor, new Position(4, 7));
        } else {
            board[7][3] = new PieceQueen(upColor, new Position(3, 7));
            board[7][4] = new PieceKing(upColor, new Position(4, 7));
        }
        board[7][5] = new PieceBishop(upColor, new Position(5, 7));
        board[7][6] = new PieceKnight(upColor, new Position(6, 7));
        board[7][7] = new PieceRook(upColor, new Position(7, 7));
//        board[6][6] = new PiecePawn(downColor, new Position(6, 6));
    }

    private Position pgnToPosition(String sPos) {
        return new Position((int) (sPos.charAt(0) - 'a'), (int) (sPos.charAt(1) - '0') - 1);
    }

    /**
     * Creates movehistory from a pgn string
     * @param pgn
     * @param color
     */
    public void createMoveFromPgn(String pgn, Color color) throws IOException {
        int len = pgn.length();
        if (pgn.charAt(1) == '/' || pgn.charAt(1) == '-' && pgn.charAt(0) != 'O') { //score
            return;
        }
        Move matchingMove;
        boolean promo = false;
        Piece promoPiece = null;
        if ((pgn.charAt(len - 1) >= 'A' && pgn.charAt(len - 1) <= 'Z' && pgn.charAt(len - 1) != 'O')) { //promotion
            if (pgn.charAt(len - 1) == 'Q')
                promoPiece = new PieceQueen();
            else if (pgn.charAt(len - 1) == 'N')
                promoPiece = new PieceKnight();
            else if (pgn.charAt(len - 1) == 'R')
                promoPiece = new PieceRook();
            else if (pgn.charAt(len - 1) == 'B')
                promoPiece = new PieceBishop();
            String delete = pgn.substring(len - 2, len);
            pgn = pgn.replace(delete, "");
            len = pgn.length();
            promo = true;
        } else if ((pgn.charAt(len - 2) >= 'A' && pgn.charAt(len - 2) <= 'Z')) {
            if (pgn.charAt(len - 2) == 'Q')
                promoPiece = new PieceQueen();
            else if (pgn.charAt(len - 2) == 'N')
                promoPiece = new PieceKnight();
            else if (pgn.charAt(len - 2) == 'R')
                promoPiece = new PieceRook();
            else if (pgn.charAt(len - 2) == 'B')
                promoPiece = new PieceBishop();
            String delete = pgn.substring(len - 3, len - 1);
            pgn = pgn.replace(delete, "");
            len = pgn.length();
            promo = true;
        }

        if (pgn.charAt(0) == 'O') { //castling
            Piece king = new PieceKing();
            king.setColor(color);
            if (pgn.length() > 3) { //big castling
                matchingMove = findMatchingMove(king, null, false, true);
            } else {  //small castling
                matchingMove = findMatchingMove(king, null, true, false);
            }
        } else {
            String a;
            Position newPosition;
            if (pgn.charAt(pgn.length() - 1) == '+') {
                a = pgn.substring(pgn.length() - 3, pgn.length() - 1);
                len--;
            } else {
                a = pgn.substring(pgn.length() - 2, pgn.length());
            }
            newPosition = pgnToPosition(a);

            Character c;
            boolean replacing = false;
            int oldY = -1;
            int oldX = -1;
            Piece piece = null;
            if (len > 2) {
                c = pgn.charAt(len - 3);
                if ((c >= '1' && c <= '8')) {
                    oldY = (int) (c - '1');
                } else if ((c >= 'a' && c <= 'h')) {
                    oldX = (int) (c - 'a');
                } else if (c == 'x') {
                    replacing = true;
                } else if (c == 'R')
                    piece = new PieceRook();
                else if (c == 'K')
                    piece = new PieceKing();
                else if (c == 'Q')
                    piece = new PieceQueen();
                else if (c == 'B')
                    piece = new PieceBishop();
                else if (c == 'N')
                    piece = new PieceKnight();
            }
            if (len > 3) {
                c = pgn.charAt(len - 4);
                if ((c >= '1' && c <= '8')) {
                    oldY = (int) (c - '1');
                } else if ((c >= 'a' && c <= 'h')) {
                    oldX = (int) (c - 'a');
                } else if (c == 'x') {
                    replacing = true;
                } else if (c == 'R')
                    piece = new PieceRook();
                else if (c == 'K')
                    piece = new PieceKing();
                else if (c == 'Q')
                    piece = new PieceQueen();
                else if (c == 'B')
                    piece = new PieceBishop();
                else if (c == 'N')
                    piece = new PieceKnight();
            }
            if (len > 4) {
                c = pgn.charAt(len - 5);
                if ((c >= '1' && c <= '8')) {
                    oldY = (int) (c - '1');
                } else if ((c >= 'a' && c <= 'h')) {
                    oldX = (int) (c - 'a');
                } else if (c == 'x') {
                    replacing = true;
                } else if (c == 'R')
                    piece = new PieceRook();
                else if (c == 'K')
                    piece = new PieceKing();
                else if (c == 'Q')
                    piece = new PieceQueen();
                else if (c == 'B')
                    piece = new PieceBishop();
                else if (c == 'N')
                    piece = new PieceKnight();
            }
            if (piece == null)
                piece = new PiecePawn();
            piece.setColor(color);
            piece.setPosition(new Position(oldX, oldY));
            matchingMove = findMatchingMove(piece, newPosition, false, false);
        }

        if (matchingMove != null) {
            this.makeMove(matchingMove, false, true);

            if (promo) {
                promoPiece.setPosition(matchingMove.newPosition);
                promoPiece.setColor(color);
                this.makePromotion(promoPiece, promoPiece.getPosition());
            }
        } else {
//            logger.log(Level.SEVERE, "PGN parsing error, no matching move found for " + pgn);
            throw new IOException("PGN parsing error, no matching move found for " + pgn);
        }
    }

    private Move findMatchingMove(Piece piece, Position newPosition, boolean smallCastling, boolean bigCastling) {
        System.out.println(piece.pieceType.toString());
        if (smallCastling || bigCastling) {
            Piece king = Arrays.stream(kings).filter(c -> c.getColor() == piece.getColor()).findFirst().get();
            List<Move> moves = king.acceptGameBoard(this);
            if (smallCastling) {
                for (Move m : moves) {
                    if (m.smallCastling)
                        return m;
                }
            } else {
                for (Move m : moves) {
                    if (m.bigCastling)
                        return m;
                }
            }
            return null;
        }
        Piece potential = null;
        Move theMove = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                potential = this.getPiece(i, j, false);
                if (potential != null) {
                    System.out.println(potential.position.toString() + potential.pieceType.toString());
                    if ((theMove = movesContainPosition(potential.acceptGameBoard(this), newPosition)) != null) {
                        if (potential.pieceType == piece.pieceType && potential.getColor() == piece.getColor()) {
                            if (piece.getPosition().x != -1) {
                                if (piece.getPosition().x == potential.getPosition().x)
                                    return theMove;
                            } else if (piece.getPosition().y != -1) {
                                if (piece.getPosition().y == potential.getPosition().y)
                                    return theMove;
                            } else {
                                return theMove;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("returning null");
        System.out.println(newPosition.toString());
        return null;
    }

    private Move movesContainPosition(List<Move> moves, Position Position) {
        for (Move move : moves) {
            if (move.newPosition.equals(Position))
                return move;
        }
        return null;
    }

    /**
     * convert only the last move to a pgn string
     * @return
     */
    public String thisMoveToPgn() {
        List<String> movesS = getPngMovesList();
        int len = (int) movesS.stream().count();
        System.out.println("len:: " + len);
        System.out.println(movesS.get(len - 1));
        return movesS.get(len - 1);
    }


    /**
     * used for saving the current moveHistory to pgn string
     * @return
     */
    public String createPgnString(){
        List<String> movesS = getPngMovesList();
        String pgnFinal = "";
        int number = 1;
        for (int i = 0; i < movesS.stream().count(); i++) {
            if(i%2==0){
                pgnFinal += number + ".";
                number++;
            }
            pgnFinal += movesS.get(i) + " ";
        }
        return pgnFinal;
    }

    private String rotatePgn(String pgn){
        String newPgn = "";
        char c;
        for (int i = 0; i < pgn.length(); i++) {
            c = pgn.charAt(i);
            if(c >= 'a' && c <= 'h'){
                newPgn += (char)('h'-c+'a');
            }
            else if(c >= '1' && c <= '8'){
                newPgn += (char)('8'-c+'1');
            }
            else{
                newPgn += (char)c;
            }
        }
        return newPgn;
    }

    /**
     * converts movehistory to a list of pgn strings
     * @return
     */
    public List<String> getPngMovesList() {
        GameBoard newGameboard = new GameBoard(this.upColor, this.downColor, false);
        List<String> movesString = new ArrayList<>();
        String s;
        Piece q = newGameboard.getPiece(3, 0, false);
        for (Move move : moveHistory) {
            s = moveToPgn(move, newGameboard);
            if(this.upColor == Color.WHITE && s != null){
                s = this.rotatePgn(s);
            }
            if(s != null)
                movesString.add(s);
        }
        return movesString;
    }

    private String moveToPgn(Move move, GameBoard gameBoard) {
        System.out.println("kings positions: " + gameBoard.kings[0].position + gameBoard.kings[1].position);
        if(move.castling && move.piece.pieceType == PieceType.KING){
            gameBoard.makeMove(move, false, true);
            return null;
        }
        if (move.castling) {
            if (move.bigCastling) {
                return "O-O-O";
            } else if(move.smallCastling){
                return "O-O";
            }
        }

        List<Move> movesToSamePosition = piecesAbleToMakeMove(move, gameBoard);
        String s = "";
        if (move.piece.pieceType == PieceType.PAWN) {
            if (move.replacedPiece != null) {
                s += (char) ('a' + move.oldPosition.x);
                s += 'x';
            }
            s += (char) ('a' + move.newPosition.x);
            s += (char) ('1' + move.newPosition.y);
        } else {
            s += move.piece.getLetter();
            Move same = null;
            if (movesToSamePosition.stream().count() > 0) {
                if (movesToSamePosition.stream().count() > 1)
                    logger.log(Level.SEVERE, "Unexpected behaviour");
                same = movesToSamePosition.get(0);
                if (same.piece.getPosition().x == move.oldPosition.x) {
                    s += (char) ('1' + move.oldPosition.y);
                } else {
                    s += (char) ('a' + move.oldPosition.x);
                }
            }
            if (move.replacedPiece != null) {
                s += 'x';
            }
            s += (char) ('a' + move.newPosition.x);
            s += (char) ('1' + move.newPosition.y);
        }

        gameBoard.makeMove(move, false, true);

        if (move.promoMove) {
            s += '=';
            s += move.promotedPiece.getLetter();
            gameBoard.board[move.newPosition.y][move.newPosition.x] = move.promotedPiece;
            gameBoard.updateRotatedBoard();
        }
        if (gameBoard.checkForChecks(move.piece.getColor().otherColor()).stream().count() > 0) {
            s += '+';
        }
        return s;
    }

    private List<Move> piecesAbleToMakeMove (Move move, GameBoard gameBoard){
        Piece potential = null;
        List<Move> movesToSamePosition = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((potential = gameBoard.getPiece(i, j, false)) != null) {
                    if (potential.pieceType == move.piece.pieceType && !potential.getPosition().equals(move.oldPosition)
                            && potential.getColor() == move.piece.getColor()) {
                        List<Move> potentialMoves = potential.acceptGameBoard(gameBoard);
                        Move m = null;
                        if ((m = movesContainPosition(potentialMoves, move.newPosition)) != null) {
                            movesToSamePosition.add(m);
                        }
                    }
                }
            }
        }
        return movesToSamePosition;
    }

    public List<Move> getMoveHistoryGUI(){
        return this.moveHistoryGUI;
    }
}
