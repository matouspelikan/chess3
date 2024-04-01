package cvut.fel.pjv.pelikma3.pgnUtils;

import cvut.fel.pjv.pelikma3.pieces.Piece;
import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.utils.GameBoard;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PgnParser {
    public GameBoard gameboard;
    public File file;
    String fileContents;
    public boolean failed = false;

    public Color getColorToMove() {
        return colorToMove;
    }

    private Color colorToMove;

    public PgnParser(String fileContents){
        this.gameboard = new GameBoard(Color.BLACK, Color.WHITE, false);
        this.fileContents = fileContents;
        try {
            this.colorToMove = this.CreateMoveHistory();
        } catch (IOException e) {
            failed = true;
            e.printStackTrace();
        }
    }

    public Color CreateMoveHistory() throws IOException {
        int index = 0;
        //skip brackets
        while(!(fileContents.charAt(index) == '1' && fileContents.charAt(index+1) == '.'
                && (index==0 || fileContents.charAt(index-1) == '\n'))){
            index++;
        }

        String toBeReplaced = fileContents.substring(0, index);
        fileContents = fileContents.replace(toBeReplaced, "");

//        List<String> a = Arrays.stream(fileContents.split("\\s|\\."))
//                .filter(c -> c.length() > 2 || (c.length() > 0 && c.charAt(0) >= 'a' && c.charAt(0) <= 'z')).collect(Collectors.toList());
        List<String> a = Arrays.stream(fileContents.split("\\s|\\."))
            .filter(c -> c.length() > 0 || (c.length() > 0 && c.charAt(0) >= 'a' && c.charAt(0) <= 'z') && c.charAt(0) != '\n').collect(Collectors.toList());

        Color colorTurn = null;

        for (int i = 0; i < a.size(); i++) {
//            if(a.get(i).equals("cxd4"))
//                break;
            if(i%3==0){ //number of move, do nothing

            }
            else if(i % 3 == 1){  //white move
                System.out.println("prvni: " + a.get(i));
                this.gameboard.createMoveFromPgn(a.get(i), Color.WHITE);
                colorTurn = Color.BLACK;
            }
            else if (i % 3 == 2){  //black move
                System.out.println("druhy: " + a.get(i));
                this.gameboard.createMoveFromPgn(a.get(i), Color.BLACK);
                colorTurn = Color.WHITE;
            }
        }
        System.out.println("INNER COLOR TO MOVE" + colorTurn.toString());
        return colorTurn;

    }
}
