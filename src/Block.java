import javafx.scene.paint.Color;

public class Block {

    BlockType type;
    int size;
    boolean[][] shape;
    Color color;
    Block(BlockType type)
    {
        this.type = type;
        this.color = type.color;
        this.shape = type.shape;
        size = shape.length;
    }
    public void rotate(boolean rotateRight)
    {
        boolean[][] shapeCopy = copy2dArray(shape);

        int startColumn = 0;
        int startRow = size - 1;
        int step = 1;

        if(rotateRight){
            startColumn = size - 1;
            startRow = 0;
            step = -1;
        }
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                shape[row][column] = shapeCopy[startColumn + column * step][startRow - row * step];
            }
        }
        System.out.println("rotated");
    }

    boolean[][] copy2dArray(boolean[][] arr)
    {
        boolean[][] copy = new boolean[arr.length][];
        for (int i = 0; i < arr.length; i++)
            copy[i] = arr[i].clone();
        return copy;
    }

}
