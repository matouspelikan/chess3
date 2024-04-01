package cvut.fel.pjv.pelikma3.players;

public enum Color {
    BLACK, WHITE;

    public Color otherColor(){
        if(this == Color.WHITE)
            return Color.BLACK;
        return Color.WHITE;
    }
}
