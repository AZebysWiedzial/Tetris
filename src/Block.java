import javafx.scene.paint.Color;


public class Block {

    private BlockType type;
    private int size;
    private boolean[][] shape;
    private Color color;
    private int anchorX;
    private int anchorY;
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

    public void move(Direction direction)
    {

    }
//    public boolean doesBlockFit(Direction direction)
//    {
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                int row = getAnchorY() + i + direction.y;
//                int column = getAnchorX() + j + direction.x;
//                if(row >= 0 && row < board.length && column >= 0 && column < board[i].length && board[row][column] == State.STATIC)
//                    return false;
//            }
//        }
//        return true;
//    }

    boolean[][] copy2dArray(boolean[][] arr)
    {
        boolean[][] copy = new boolean[arr.length][];
        for (int i = 0; i < arr.length; i++)
            copy[i] = arr[i].clone();
        return copy;
    }

    public int getAnchorX() {
        return anchorX;
    }

    public void setAnchorX(int anchorX) {
        this.anchorX = anchorX;
    }

    public int getAnchorY() {
        return anchorY;
    }

    public void setAnchorY(int anchorY) {
        this.anchorY = anchorY;
    }

    public int getSize() {
        return size;
    }

    public boolean[][] getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }
}
