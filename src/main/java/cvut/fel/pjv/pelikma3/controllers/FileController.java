package cvut.fel.pjv.pelikma3.controllers;

import cvut.fel.pjv.pelikma3.utils.Move;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FileController {

    public Stage stage;
    public String fileName;
    public String filePath;

    public FileController(Stage stage){
        this.stage = stage;
    }

    public String getDataFromFile(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if(file == null)
            return null;
        this.fileName = file.getName();
        this.filePath = file.getPath();
        String data = null;
        try {
            data = this.readFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }

    public String getDataFromFile(File existingFile){
        String data = null;
        try {
            data = this.readFile(existingFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private String readFile(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        StringBuilder sb = new StringBuilder();
        int c;
        while((c = fileReader.read()) != -1){
            sb.append((char)c);
        }
        fileReader.close();
        return sb.toString();
    }

    public void saveFile(String data){
        System.out.println(data);
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PGN files (*.pgn)", "*.pgn");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(stage);

        if(file == null){
            System.out.println("invalid file");
            return;
        }
        else{
            try {
                PrintWriter writer = new PrintWriter(file);
                writer.println(data);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveMoveHistory(List<Move> moveHistory){
        ArrayList<Move> data = new ArrayList<>();
        for(Move move: moveHistory)
            data.add(move);
        try {
            FileOutputStream fileOut = new FileOutputStream("C:/Users/pelik/history.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(data);
            fileOut.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Move> loadMoveHistory(){
        List<Move> deserialized = new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream("C:/Users/pelik/history.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            deserialized = (ArrayList<Move>)in.readObject();
            in.close();
            fileIn.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("MOVE HISTORY");
        for(Move m : deserialized){
            System.out.println(m.oldPosition.toString() + m.newPosition + m.piece.pieceType.toString());
        }
        return deserialized;
    }
}
