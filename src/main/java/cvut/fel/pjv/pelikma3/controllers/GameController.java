package cvut.fel.pjv.pelikma3.controllers;

import cvut.fel.pjv.pelikma3.SceneManager;
import cvut.fel.pjv.pelikma3.handlers.EditClickHandler;
import cvut.fel.pjv.pelikma3.handlers.GameboardTileClickHandler;
import cvut.fel.pjv.pelikma3.handlers.PromotionClickHandler;
import cvut.fel.pjv.pelikma3.pieces.*;
import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.timer.ChangeTimer;
import cvut.fel.pjv.pelikma3.timer.ChessTimer;
import cvut.fel.pjv.pelikma3.utils.Game;
import cvut.fel.pjv.pelikma3.utils.Move;
import cvut.fel.pjv.pelikma3.utils.Position;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//this is future
public class GameController {
    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    private SceneManager sceneManager;

    public Game game;

    int defaultTileWidth = 80;
    int defaultTileHeight =80;

    int defaultFirstMoveTime = 30000;
    int defaultMoveTime = 180000;

    @FXML
    private BorderPane pane;
    @FXML
    private ImageView myImageView;
    @FXML
    private VBox mainVBox;
    @FXML
    private HBox promotionBoxUp;
    @FXML
    private HBox promotionBoxUpWrapper;
    @FXML
    private HBox promotionBoxDown;
    @FXML
    private HBox promotionBoxDownWrapper;
    @FXML
    private HBox editBoxUp;
    @FXML
    private HBox editBoxDown;
    @FXML
    private Label lastMove;
    @FXML
    private ListView movesList;
    @FXML
    private Label timerLabelUp;
    @FXML
    private Label timerLabelDown;
    @FXML
    private HBox deleteButtonWrapper;
    @FXML
    private HBox startButtonWrapper;
    @FXML
    private Label winText;

    boolean pieceBeingClicked = false;
    Position clickedPositionPrevious = new Position(-1, -1);

    VBox wrapperArray[][] = new VBox[8][8];
    VBox wrapperWrapperArray[][] = new VBox[8][8];
    HashMap<String, Image> pieceToImageDictionary = new HashMap<>();
    HashMap<String, Piece> promPieces = new HashMap<>();
    HashMap<String, Piece> editPieces = new HashMap<>();
    Image focusEmptyImage = new Image(getClass().getResourceAsStream("/decorations/focus-empty.png"));
    Image focusPieceImage = new Image(getClass().getResourceAsStream("/decorations/focus-piece.png"));
    List<Move> markedMoves = new ArrayList<>();
    Move promotionMove;
    ChessTimer timer;
    ChessTimer startTimer;
    ChessTimer gameTimer;
    int guiMovesMade = 0;
    int revertCount = 0;

    //edit stuff
    boolean editing = true;
    boolean againstPC = false;
    boolean creatingNewPiece;
    Piece pieceBeingCreated;
    VBox lastVbox = null;
    VBox lastWrapperWrapperEditTileSelect = null;
    Piece lastEditSelectPiece = null;
    public Stage stage;
    int whiteKingCount = 1;
    int blackKingCount = 1;
    GameboardTileClickHandler gameboardClickHandler;
    PromotionClickHandler promotionClickHandler;

    public GameController(){
        this.PopulatePiecesDict("piecesoriginal");
    }

    /** Substitute for constructor, as the constuctor of javaFX contoller can not be explicitly called
     * @param gameData represent data from editing stage of game
     * @param stage default javaFX stage
     */
    public void setParams(GameData gameData, Stage stage){ //after this.game != null
        this.stage = stage;
        this.GenerateGameBoard();
        this.CreatePromotionBox();
        this.CreateEditBox();
        this.DrawBoard(this.game.getBoard());
        if(gameData != null){
            startTimer = new ChessTimer(timerLabelUp, timerLabelDown,this, defaultFirstMoveTime, defaultFirstMoveTime, this.game.upColor == Color.WHITE, true);
            gameTimer = new ChessTimer(timerLabelUp, timerLabelDown,this, gameData.timer1, gameData.timer2, this.game.upColor == Color.WHITE, false);
            Platform.runLater(new ChangeTimer(timerLabelUp, startTimer.MinuteFormat(defaultFirstMoveTime)));  //display default values
            Platform.runLater(new ChangeTimer(timerLabelDown, startTimer.MinuteFormat(defaultFirstMoveTime)));
            if(gameData.editBeforeStart)
                this.game.getGameBoard().clearHistories();
            if(gameData.againstPC)
                this.againstPC = true;
            if(gameData.emptyBoard){
                blackKingCount = 0;
                whiteKingCount = 0;
            }
        }else{
            startTimer = new ChessTimer(timerLabelUp, timerLabelDown,this, defaultFirstMoveTime, defaultFirstMoveTime, this.game.upColor == Color.WHITE, true);
            gameTimer = new ChessTimer(timerLabelUp, timerLabelDown,this, defaultMoveTime, defaultMoveTime, this.game.upColor == Color.WHITE, false);
            Platform.runLater(new ChangeTimer(timerLabelUp, startTimer.MinuteFormat(defaultFirstMoveTime)));
            Platform.runLater(new ChangeTimer(timerLabelDown, startTimer.MinuteFormat(defaultFirstMoveTime)));
            editBoxUp.setVisible(false);
            deleteButtonWrapper.setVisible(false);
            editBoxDown.setVisible(false);
            startButtonWrapper.setVisible(false);
        }

        startTimer.setDaemon(true);
        gameTimer.setDaemon(true);
        timer = startTimer;

        if(gameData == null || (gameData!= null && !gameData.editBeforeStart)){
            this.StopEditing();
        }
    }

    /**
     * Hides all javaFX components
     */
    @FXML
    public void StopEditing(){
        //checks if there is at least one king for each color
        if(whiteKingCount < 1){
            logger.log(Level.SEVERE, "White king missing!");
            return;
        }
        else if (blackKingCount < 1){
            logger.log(Level.SEVERE, "Black king missing!");
            return;
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                wrapperWrapperArray[i][j].getStyleClass().removeIf(c -> c.toString().equals("edit-hover"));
            }
        }
        this.editing = false;
        editBoxUp.setVisible(false);
        deleteButtonWrapper.setVisible(false);
        startButtonWrapper.setVisible(false);
        editBoxDown.setVisible(false);
        if(lastWrapperWrapperEditTileSelect != null)
            lastWrapperWrapperEditTileSelect.getStyleClass().removeIf(c -> c.toString().equals("edit-tile-select"));
        timer.start();

        if(this.againstPC && this.game.upColor == this.game.getColorTurn())
            PCMove(true, null, this.game.downColor);

        updatePgnMoveList();
    }


    /**
     * crucial method that takes care of the event when a gameboard tile is clicked
     * @param clickedPosition coordinates where the click occured
     */
    public void SquareClicked(Position clickedPosition){
        if(game.ended)
            return;
        Piece clickedPiece;
        if(editing && creatingNewPiece){
            Piece newPiece = pieceBeingCreated.createSelf();
            newPiece.setColor(pieceBeingCreated.getColor());
            newPiece.setPosition(new Position(clickedPosition));
            if(this.game.getBoard()[clickedPosition.y][clickedPosition.x] != null &&
                    this.game.getBoard()[clickedPosition.y][clickedPosition.x].pieceType == PieceType.KING){
                if(this.game.getBoard()[clickedPosition.y][clickedPosition.x].getColor() == Color.WHITE) {
                    whiteKingCount--;
                }

                else
                    blackKingCount--;
            }
            if(newPiece.pieceType == PieceType.KING){
                if(newPiece.getColor() == Color.WHITE){
                    if(whiteKingCount > 0)
                        return;
                    whiteKingCount++;
                    this.game.getGameBoard().kings[0] = newPiece;
                }
                if(newPiece.getColor() == Color.BLACK){
                    if(blackKingCount > 0)
                        return;
                    blackKingCount++;
                    this.game.getGameBoard().kings[1] = newPiece;
                }
            }
            this.game.getBoard()[clickedPosition.y][clickedPosition.x] = newPiece;
            this.game.getGameBoard().setBoard(clickedPosition.x, clickedPosition.y, newPiece);
            RedrawBoard(Arrays.asList(clickedPosition));
            return;
        }
        else if(editing){
            if((clickedPiece = game.getGameBoardPosition(clickedPosition)) != null){
                if(lastWrapperWrapperEditTileSelect != null)
                    lastWrapperWrapperEditTileSelect.getStyleClass().removeIf(c -> c.toString().equals("edit-tile-select"));
                if(lastWrapperWrapperEditTileSelect ==  wrapperWrapperArray[clickedPosition.y][clickedPosition.x]){
                    lastWrapperWrapperEditTileSelect = null;
                    return;
                }
                wrapperWrapperArray[clickedPosition.y][clickedPosition.x].getStyleClass().add("edit-tile-select");
                lastWrapperWrapperEditTileSelect = wrapperWrapperArray[clickedPosition.y][clickedPosition.x];
                lastEditSelectPiece = clickedPiece;
            }
            return;
        }


        Move moveMade;
        if((clickedPiece = game.getGameBoardPosition(clickedPosition)) == null){
            if(pieceBeingClicked){
                System.out.println(markedMoves);
                if((moveMade = getMarkedMoveFromPosition(clickedPosition)) != null){
                    makeMove(moveMade, false, true);
                }
            }
            restoreDecorations();
        }
        else{
            logger.log(Level.INFO, "clicked position: " + clickedPiece.position.toString() + clickedPiece.pieceType + clickedPiece.getColor());
            if(pieceBeingClicked && clickedPiece.position.equals(clickedPositionPrevious)){
                restoreDecorations();
            }
            else if((moveMade = getMarkedMoveFromPosition(clickedPosition)) != null){
                makeMove(moveMade, false, true);
                restoreDecorations();
            }
            else{
                restoreDecorations();
                pieceBeingClicked = true;
                clickedPositionPrevious = clickedPosition;
                if(clickedPiece.getColor() == game.getColorTurn()){  //Hráč který je na tahu klikl na svou figurku
                    wrapperWrapperArray[clickedPosition.y][clickedPosition.x].getStyleClass().add("clicked");
                    List<Move> moves = game.getAllMoves(clickedPiece);
                    for (Move move: moves) {
                        markPossibleMove(move, move.replacedPiece);
                    }
                }
            }
        }
    }


    /**
     * @param moveMade
     * @param forwarding
     * @param humanMove
     */
    public void makeMove(Move moveMade, boolean forwarding, boolean humanMove){
        if(revertCount > 0 && !forwarding){
            this.game.getGameBoard().deleteFromHistory(revertCount);
            revertCount = 0;
        }
        undecorateLastMove();
        this.RedrawBoard(game.makeMove(moveMade, forwarding));
        decorateLastMove();
        decorateChecks(moveMade.piece.getColor());

        if(moveMade.piece.isPromoted() && forwarding){
            this.createPromotedPiece(moveMade.promotedPiece);
        }
        else if(moveMade.piece.isPromoted()){
            showPromotion(moveMade);
            return;
        }
        lastMove.setText(this.game.getGameBoard().thisMoveToPgn());
        updatePgnMoveList();

        manageTimer(moveMade);

        //checks whether a move resulted in a draw
        if(this.game.checkForDraw(moveMade.piece.getColor().otherColor())){
            timer.gameEnded = true;
            this.endGame(false, false, true);
        }

        //makes a random enemy if playing against PC
        PCMove(humanMove, moveMade, moveMade.piece.getColor());
    }

    public void PCMove(boolean humanMove, Move moveMade, Color moveColor){
        if(againstPC && humanMove && (moveMade == null || !moveMade.piece.isPromoted())){
            Move randomMove = this.game.randomMove(moveColor.otherColor());
            if(randomMove == null)
                return;
            this.makeMove(randomMove, false, false);
        }
        if(this.game.checkForDraw(moveColor.otherColor())){
            timer.gameEnded = true;
            this.endGame(false, false, true);
        }
    }


    /**
     * Ends the game, stops the timers, displays result message
     * @param upWins true, if the winner is the player on upper side of board
     * @param time true, if
     * @param draw true, if it is a draw
     */
    public void endGame(boolean upWins, boolean time, boolean draw){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                restoreDecorations();
            }
        });
        this.game.ended = true;
        if (draw){
            this.game.winColor = null;
            displayResultMessage("remíza");
        }
        else if(upWins){
            this.game.winColor = this.game.upColor;
            displayResultMessage(game.upColor.toString() + " vyhrává hru");
        }
        else{
            this.game.winColor = this.game.downColor;
            displayResultMessage(game.downColor.toString() + " vyhrává hru");
        }
    }

    private void displayResultMessage(String message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                winText.setText(message);
            }
        });
    }


    /**
     * Populated pieceToImageDictionary, a dictionary that stores paths to .png images to every piece
     * @param dirName path to folder where piece .png files are
     */
    public void PopulatePiecesDict(String dirName){
        String[] piecesNames = {"pawn-white", "pawn-black", "rook-black", "rook-white", "bishop-white", "bishop-black",
                "king-white", "king-black", "queen-black", "queen-white", "knight-black", "knight-white"};
        for (String s: piecesNames) {
            String path = "/" + dirName + "/" + s + ".png";
            Image newImage = new Image(getClass().getResourceAsStream(path));
            pieceToImageDictionary.put(s, newImage);
        }
    }

    /**
     * Populates HBox that displays options of pieces to promote to when the opportunity arrises
     * @param promotionbox javaFX component
     * @param color either BLACK or WHITE
     * @param promotionPieces Pieces to be displayed in promotionBox
     */
    private void PopulatePromotionBox(HBox promotionbox, String color, HashMap<String, Piece> promotionPieces){
        List<Map.Entry<String, Piece>> l =  new ArrayList<>(promotionPieces.entrySet());
        l.sort(new Comparator<Map.Entry<String, Piece>>() {
            @Override
            public int compare(Map.Entry<String, Piece> o1, Map.Entry<String, Piece> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        for(Map.Entry<String, Piece> e : l){
            VBox newWrapper = new VBox();
            promotionbox.getChildren().add(newWrapper);
            ImageView newView = new ImageView(pieceToImageDictionary.get(e.getKey().toString() + "-" + color));
            newView.setFitHeight(0.8*defaultTileHeight);
            newView.setFitWidth(0.8*defaultTileWidth);
            newWrapper.getChildren().add(newView);
            newWrapper.getStyleClass().add("promotionBox");
            newWrapper.setId(e.getValue().pieceType.toString().toLowerCase(Locale.ROOT)+ "-" + color);
            newWrapper.setOnMouseClicked(new PromotionClickHandler(this.game, this, promotionBoxUp, promotionBoxDown, promotionPieces));
        }
    }


    /**
     * Populates the Hbox that contains each piece and is used for editing the gameboard before the game starts
     * @param editBox javaFX component
     * @param color color of pieces being put in editBox, either black or white
     * @param editPieces Dictionary containing every piece
     */
    private void PopulateEditBox(HBox editBox, String color, HashMap<String, Piece> editPieces){
        List<Map.Entry<String, Piece>> l =  new ArrayList<>(editPieces.entrySet());
        l.sort(new Comparator<Map.Entry<String, Piece>>() {
            @Override
            public int compare(Map.Entry<String, Piece> o1, Map.Entry<String, Piece> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        for(Map.Entry<String, Piece> e : l){
            VBox newWrapper = new VBox();
            editBox.getChildren().add(newWrapper);
            ImageView newView = new ImageView(pieceToImageDictionary.get(e.getKey().toString() + "-" + color));
            newView.setFitHeight(0.8*defaultTileHeight);
            newView.setFitWidth(0.8*defaultTileWidth);
            newWrapper.getChildren().add(newView);
            newWrapper.getStyleClass().add("promotionBox");
            newWrapper.setId(e.getValue().pieceType.toString().toLowerCase(Locale.ROOT)+ "-" + color);
            newWrapper.setOnMouseClicked(new EditClickHandler(this.game, this, this.editPieces));
        }
    }


    /**
     * Creates Promotion boxes for both sides, then populated them with images of each piece
     */
    private void CreatePromotionBox(){
        this.promPieces = new HashMap<>();
//        promPieces.put("pawn", new PiecePawn());
        promPieces.put("rook", new PieceRook());
        promPieces.put("bishop", new PieceBishop());
        promPieces.put("queen", new PieceQueen());
        promPieces.put("knight", new PieceKnight());

        PopulatePromotionBox(promotionBoxUp, this.game.downColor.toString().toLowerCase(Locale.ROOT), promPieces);
        PopulatePromotionBox(promotionBoxDown, this.game.upColor.toString().toLowerCase(Locale.ROOT), promPieces);

        promotionBoxUp.setVisible(false);
        promotionBoxDown.setVisible(false);
    }


    /**
     * Creates edit boxes, which are used when editing the gameboard
     */
    private void CreateEditBox(){
        this.editPieces = new HashMap<>();
        editPieces.put("pawn", new PiecePawn());
        editPieces.put("rook", new PieceRook());
        editPieces.put("bishop", new PieceBishop());
        editPieces.put("queen", new PieceQueen());
        editPieces.put("knight", new PieceKnight());
        editPieces.put("king", new PieceKing());

        PopulateEditBox(editBoxDown, this.game.downColor.toString().toLowerCase(Locale.ROOT), editPieces);
        PopulateEditBox(editBoxUp, this.game.upColor.toString().toLowerCase(Locale.ROOT), editPieces);
    }


    /**
     * Draw the gamebaord for first time
     * @param pieces state of board represented ar two-dimensional array
     */
    private void DrawBoard(Piece[][] pieces){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ImageView newImageView = new ImageView();
                newImageView.setPreserveRatio(true);
                newImageView.setSmooth(true);
                newImageView.setFitWidth(defaultTileWidth);
                newImageView.setFitHeight(defaultTileHeight);
                wrapperArray[i][j].getChildren().add(newImageView);

                if (pieces[i][j] != null){
                    newImageView.setImage(pieceToImageDictionary.get(pieces[i][j].getImageIdentifier()));
                }
            }
        }
    }

    /**
     * Redraws the boards on certain positions
     * @param positions positions to be redrawn
     */
    private void RedrawBoard(List<Position> positions){
        Piece target = null;
        if (positions == null)
            return;
        for(Position p : positions){
            wrapperArray[p.y][p.x].getChildren().clear();
            ImageView view = new ImageView();
            wrapperArray[p.y][p.x].getChildren().add(view);
            view.setFitHeight(defaultTileHeight);
            view.setFitWidth(defaultTileWidth);
            if((target = game.getPieceFromPosition(p)) != null){
                view.setImage(pieceToImageDictionary.get(target.getImageIdentifier()));
            }
        }
    }

    /**
     * generates javafx components that will contain the gameboard - piece images
     */
    private void GenerateGameBoard(){
        for (int i = 0; i < 8; i++) {
            HBox newHbox = new HBox();
            mainVBox.getChildren().add(newHbox);
            for (int j = 0; j < 8; j++) {
                VBox wrapperWrapper = new VBox();
                VBox newWrapper = new VBox();
                wrapperWrapper.getChildren().add(newWrapper);
                newWrapper.setId(Integer.toString(7-i) + "-" + Integer.toString(j));
                wrapperWrapper.getStyleClass().add("image-view-wrap");

                if ((i + j) % 2 == 1)
                    wrapperWrapper.getStyleClass().add("dark-tile");
                else
                    wrapperWrapper.getStyleClass().add("light-tile");

                newWrapper.setOnMouseClicked(new GameboardTileClickHandler(this.game, this));

                wrapperArray[7-i][j] = newWrapper;
                wrapperWrapperArray[7-i][j] = wrapperWrapper;
                newHbox.getChildren().add(wrapperWrapper);
            }
        }
    }


    /**
     *
     * @param position
     * @return
     */
    private Move getMarkedMoveFromPosition(Position position){
        for(Move m : markedMoves){
            if (m.newPosition.equals(position)){
                return m;
            }
        }
        return null;
    }

    /**
     * shows promotion boxes when a pawn get promoted
     * @param moveMade last move the pawn made before promotion
     */
    private void showPromotion(Move moveMade){
        this.promotionMove = moveMade;
        if(moveMade.piece.getPosition().y == 7){
            showPromotionBox(promotionBoxUpWrapper, promotionBoxUp, moveMade);
        }
        else{
            showPromotionBox(promotionBoxDownWrapper, promotionBoxDown, moveMade);
        }
    }

    private void showPromotionBox(HBox promotionBoxWrapper, HBox promotionBox, Move moveMade){
        promotionBox.setVisible(true);
        logger.log(Level.INFO, moveMade.piece.getPosition().toString());
        Double padding = ((moveMade.piece.getPosition().x + 0.5) * defaultTileWidth *4 ) - (2 * defaultTileWidth * 0.8) + 5;
        if(padding >= defaultTileWidth*8 - (defaultTileWidth*0.8*4))
            padding = defaultTileWidth*8 - (defaultTileWidth*0.8*4);
        else if(padding < 0)
            padding = 0.0;
        promotionBoxWrapper.styleProperty().set("-fx-padding: 0 0 0 "+ padding.toString());
    }

    /**
     * Gets called from click handler when a piece in promotion gets clicked, creates that piece and puts it on board
     * instead of the pawn
     * @param piece new promoted piece replacing the pawn
     */
    public void createPromotedPiece(Piece piece){
        piece.setPosition(promotionMove.piece.position);
        piece.setColor(promotionMove.piece.getColor());
        game.makePromotion(piece, piece.position);
        List<Position> positionsToRedraw = new ArrayList<>();
        positionsToRedraw.add(piece.position);
        RedrawBoard(positionsToRedraw);
        decorateChecks(piece.getColor());

        boolean isCheckMate = this.game.TestCheckMateForColor(piece.getColor().otherColor());
        if(isCheckMate){
            timer.gameEnded = true;
            this.endGame(piece.getColor() == this.game.upColor, false, false);
        }

        if(piece.getColor() == game.upColor){
            timer.setUpTurn(false);
        }
        else{
            timer.setUpTurn(true);
        }
        lastMove.setText(this.game.getGameBoard().thisMoveToPgn());
        movesList.getItems().add(this.game.getGameBoard().thisMoveToPgn());

        PCMove(true, null, piece.getColor());
    }


    /**
     * Highlights last move on the gameboard
     */
    public void decorateLastMove(){
        Move move = this.game.getGameBoard().getLastMove();
        if(move == null)
            return;
        wrapperArray[move.newPosition.y][move.newPosition.x].getStyleClass().add("last-move");
        wrapperArray[move.oldPosition.y][move.oldPosition.x].getStyleClass().add("last-move");
    }


    /**
     * removes highlighting from certain position
     * @param position
     */
    public void undecoratePosition(Position position){
        wrapperArray[position.y][position.x].getStyleClass().removeIf(c->c.toString().equals("last-move"));
    }


    /**
     * removes highlighting from last move
     */
    public void undecorateLastMove(){
        Move move = this.game.getGameBoard().getLastMove();
        if (move == null)
            return;
        undecoratePosition(move.newPosition);
        undecoratePosition(move.oldPosition);
    }


    /**
     * highlights a king if he is being checked by enemy piece, also removes highlights if kings is not checked
     * @param color
     */
    public void decorateChecks(Color color){
        Piece myking = this.game.getGameBoard().GetKing(color);
        this.wrapperArray[myking.getPosition().y][myking.getPosition().x].getStyleClass().removeIf(c->c.toString().equals("check-king"));
        if(this.game.getGameBoard().getLastMove() != null) {
            Position oldKingPosition = this.game.getGameBoard().getLastMove().oldPosition;
            this.wrapperArray[oldKingPosition.y][oldKingPosition.x].getStyleClass().removeIf(c -> c.toString().equals("check-king"));
        }
        Piece otherking = this.game.getGameBoard().GetKing(color.otherColor());
        this.wrapperArray[otherking.getPosition().y][otherking.getPosition().x].getStyleClass().removeIf(c->c.toString().equals("check-king"));


        boolean otherchecking = this.game.TestCheckForColor(color.otherColor());
        if(otherchecking){
            this.wrapperArray[otherking.getPosition().y][otherking.getPosition().x].getStyleClass().add("check-king");
        }

        boolean mychecking = this.game.TestCheckForColor(color);
        if(mychecking){
            this.wrapperArray[myking.getPosition().y][myking.getPosition().x].getStyleClass().add("check-king");
        }
    }

    /**
     * removes highlighting from pieces that were highlighted on previous move
     */
    public void restoreDecorations(){
        restoreMarkedPositions();
        if (game.getGameBoard().isPositionOnBoard(clickedPositionPrevious)){
            wrapperWrapperArray[clickedPositionPrevious.y][clickedPositionPrevious.x].getStyleClass().removeIf(c->c.toString().equals("clicked"));
        }
        pieceBeingClicked = false;
    }

    public void restoreDecorationsAll(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
//                wrapperWrapperArray[i][j].getStyleClass().removeIf(c->c.toString().equals("clicked"));
            }
        }
    }


    /**
     * highlights all possible moves that a piece can make
     * @param move potential move
     * @param replacedPiece piece being attacked by the move
     */
    public void markPossibleMove(Move move, Piece replacedPiece){
        wrapperArray[move.newPosition.y][move.newPosition.x].getStyleClass().add("marked-move");
        if(replacedPiece != null && !move.enPassant){

            Image topImage = pieceToImageDictionary.get(replacedPiece.getImageIdentifier());
            Image bottomImage = focusPieceImage;

            ImageView bottomView = new ImageView(bottomImage);
            bottomView.setFitHeight(defaultTileHeight);
            bottomView.setFitWidth(defaultTileWidth);
            ImageView topView = new ImageView(topImage);
            topView.setFitHeight(defaultTileHeight);
            topView.setFitWidth(defaultTileWidth);
            topView.setBlendMode(BlendMode.SRC_OVER);

            Group blend = new Group(bottomView, topView);

            wrapperArray[move.newPosition.y][move.newPosition.x].getChildren().clear();
            wrapperArray[move.newPosition.y][move.newPosition.x].getChildren().add(blend);

        }
        else{
            ImageView view = new ImageView(this.focusEmptyImage);
            view.setFitWidth(defaultTileWidth);
            view.setFitHeight(defaultTileHeight);

            wrapperArray[move.newPosition.y][move.newPosition.x].getChildren().clear();
            wrapperArray[move.newPosition.y][move.newPosition.x].getChildren().add(view);
        }
        markedMoves.add(move);
    }


    /**
     * undoes the effect of markPossibleMove()
     */
    public void restoreMarkedPositions(){
        Piece target = null;
        for (Move move : markedMoves){
            target = game.getPieceFromPosition(move.newPosition);
            wrapperArray[move.newPosition.y][move.newPosition.x].getChildren().clear();
            ImageView restoreView = new ImageView();
            wrapperArray[move.newPosition.y][move.newPosition.x].getChildren().add(restoreView);
            restoreView.setFitHeight(defaultTileHeight);
            restoreView.setFitWidth(defaultTileWidth);
            if (target != null) {
                restoreView.setImage(pieceToImageDictionary.get(target.getImageIdentifier()));
            }
            this.wrapperArray[move.newPosition.y][move.newPosition.x].getStyleClass().removeIf(c->c.toString().equals("marked-move"));
        }
        markedMoves.clear();
    }

    /**
     * updates javaFx ListView component that contains pgn strings of all moves made so far in current game
     */
    public void updatePgnMoveList(){
        List<String> moves = this.game.getGameBoard().getPngMovesList();
        movesList.getItems().clear();
        for(String move:moves){
            movesList.getItems().add(move);
        }
    }


    public void manageTimer(Move moveMade){
        guiMovesMade++;
        if(guiMovesMade == 1 && !timer.started){
            if(moveMade.piece.getColor() == this.game.upColor){
                Platform.runLater(new ChangeTimer(timerLabelUp, startTimer.MinuteFormat(gameTimer.upTime)));
            }
            else{
                Platform.runLater(new ChangeTimer(timerLabelDown, startTimer.MinuteFormat(gameTimer.downTime)));
            }
        }

        if(guiMovesMade == 2){
            if(timer.isStartTimer())
                timer.gameEnded = true;
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timer.upTime = gameTimer.upTime;
            timer.downTime = gameTimer.downTime;
            Platform.runLater(new ChangeTimer(timerLabelUp, startTimer.MinuteFormat(gameTimer.upTime)));
            Platform.runLater(new ChangeTimer(timerLabelDown, startTimer.MinuteFormat(gameTimer.downTime)));
            timer = gameTimer;
            if(!timer.started)
                timer.start();
        }

        boolean b = this.game.TestCheckMateForColor(moveMade.piece.getColor().otherColor());
        if(b){
            timer.gameEnded = true;
            this.endGame(moveMade.piece.getColor() == this.game.upColor, false, false);
        }

        if(moveMade.piece.getColor() == game.upColor){
            timer.setUpTurn(false);
        }
        else{
            timer.setUpTurn(true);
        }
    }

    /**
     * goes one step to past version of gameboard
     */
    @FXML
    private void revert(){
        if(this.game.getGameBoard().getGUIHistoryCount() - revertCount <= 0)
            return;

        revertCount++;
        restoreDecorations();
        guiMovesMade--;
        undecorateLastMove();
        RedrawBoard(this.game.revertMove());
        decorateLastMove();
        decorateChecks(Color.WHITE);
        decorateChecks(Color.BLACK);
    }

    /**
     * inverse of revert
     */
    @FXML
    private void forward(){
        if (revertCount > 0){
            Move move = this.game.getGameBoard().getMoveFromGui(revertCount);
            this.makeMove(move, true, false);
            revertCount--;
        }
    }


    /**
     * Creates new piece, called from editing mode
     * @param piece piece being created
     * @param vBox piece's wrapper
     */
    public void createnewPiece(Piece piece, VBox vBox){
        if(piece.pieceType == PieceType.KING && piece.getColor() == Color.WHITE && whiteKingCount > 0)
            return;
        if(piece.pieceType == PieceType.KING && piece.getColor() == Color.BLACK && blackKingCount > 0)
            return;

        if(this.creatingNewPiece == true && piece.pieceType == pieceBeingCreated.pieceType && piece.getColor() == pieceBeingCreated.getColor()){
            vBox.getStyleClass().removeIf(c -> c.toString().equals("edit-clicked"));

            this.creatingNewPiece = false;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    wrapperWrapperArray[i][j].getStyleClass().removeIf(c -> c.toString().equals("edit-hover"));
                }
            }
            return;
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                wrapperWrapperArray[i][j].getStyleClass().add("edit-hover");
            }
        }

        this.creatingNewPiece = true;
        this.pieceBeingCreated  = piece;
        if(lastVbox != null){
            lastVbox.getStyleClass().removeIf(c-> c.toString().equals("edit-clicked"));
        }
        vBox.getStyleClass().add("edit-clicked");
        lastVbox = vBox;
    }

    /**
     * Deletes piece from the gameboard during editing the gameboard
     */
    @FXML
    private void DeletePiece(){
        if(lastWrapperWrapperEditTileSelect != null){
            lastWrapperWrapperEditTileSelect.getStyleClass().removeIf(c -> c.toString().equals("edit-tile-select"));
            if(this.game.getBoard()[lastEditSelectPiece.getPosition().y][lastEditSelectPiece.getPosition().x].pieceType == PieceType.KING){
                if(this.game.getBoard()[lastEditSelectPiece.getPosition().y][lastEditSelectPiece.getPosition().x].getColor() == Color.WHITE)
                    whiteKingCount--;
                else
                    blackKingCount--;
            }
            this.game.getBoard()[lastEditSelectPiece.getPosition().y][lastEditSelectPiece.getPosition().x] = null;
            this.RedrawBoard(Arrays.asList(lastEditSelectPiece.getPosition()));
            lastWrapperWrapperEditTileSelect = null;
        }
    }

    /**
     * returns back to main menu
     */
    @FXML
    private void mainMenu(){
        this.sceneManager.SetMainScene();

    }

    /**
     * saves the game in pgn format to desired location
     */
    @FXML
    private void saveGame(){
        String data = this.game.getGameBoard().createPgnString();
        if(this.game.ended){
            if(this.game.winColor == Color.WHITE){
                data += "1-0";
            }
            else if(this.game.winColor == Color.BLACK){
                data += "0-1";
            }
            else if (this.game.winColor == null){
                data += "1/2-1/2";
            }
        }

        FileController fileController = new FileController(stage);
        fileController.saveFile(data);
//        fileController.saveMoveHistory(this.game.gameBoard.getMoveHistoryGUI());
    }

}
