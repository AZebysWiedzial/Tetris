import javafx.scene.paint.Color;

public enum Type {
    IBlock(Color.LIGHTBLUE), JBlock(Color.BLUE), LBlock(Color.ORANGE), OBlock(Color.YELLOW), SBlock(Color.LIGHTGREEN), TBlock(Color.PURPLE), ZBlock(Color.RED);
    private Color color;
    Type(Color color)
    {
        this.color = color;
    }
}
