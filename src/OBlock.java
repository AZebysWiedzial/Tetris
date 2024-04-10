import javafx.scene.paint.Color;

public class OBlock extends Block{
    OBlock()
    {
        size = 4;
        color = Color.SANDYBROWN;
        shape = new boolean[][]{
                {false, false, false, false},
                {false, true, true, false},
                {false, true, true, false},
                {false, false, false, false},
        };
    }

    @Override
    public void rotate() {

    }
}
