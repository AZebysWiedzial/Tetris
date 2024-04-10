import javafx.scene.paint.Color;

public class Block {

    Type type;
    public enum Type {
        OBlock(4, Color.SANDYBROWN, new boolean[][]{
                        {false, false, false, false},
                        {false, true, true, false},
                        {false, true, true, false},
                        {false, false, false, false},}),
        IBlock(4, Color.LIGHTBLUE, new boolean[][]{
                {false, false, false, false},
                {true, true, true, true},
                {false, false, false, false},
                {false, false, false, false},
        }),
        JBlock(3, Color.BLUE, new boolean[][]{
                {true, false, false},
                {true, true, true},
                {false, false, false},
        }),
        LBlock(3, Color.ORANGE, new boolean[][]{
                {false, false, true},
                {true, true, true},
                {false, false, false}
        }),
        TBlock(3, Color.PURPLE, new boolean[][]{
                {false, true, false},
                {true, true, true},
                {false, false, false}
        }),
        SBlock(3, Color.LIGHTGREEN, new boolean[][]{
                {false, true, true},
                {true, true, false},
                {false, false, false}
        }),
        ZBlock(3, Color.RED, new boolean[][]{
                {true, true, false},
                {true, true, false},
                {false, false, false}
        });

        Type(int size, Color color, boolean[][] shape) {}
    }
    int size;
    Color color;
//    public void rotate()
//    {
//        boolean[][] shapeCopy = copy2dArray();
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                shape[i][j] = shapeCopy[size-i-1][i];
//            }
//        }
//    }

    boolean[][] copy2dArray(boolean[][] arr)
    {
        boolean[][] copy = new boolean[arr.length][];
        for (int i = 0; i < arr.length; i++)
            copy[i] = arr[i].clone();
        return copy;
    }

}
