import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Block {

    private final BlockType type;
    private State[][] board;
    private Rectangle[][] visualBoard;
    private final int size;
    private boolean[][] shape;
    private final Color color;
    private int anchorX;
    private int anchorY;
    Block(BlockType type, State[][] board, Rectangle[][] visualBoard)
    {
        this.type = type;
        this.color = type.color;
        this.shape = type.shape;
        size = shape.length;
        this.board = board;
        this.visualBoard = visualBoard;
    }
    public void rotate(boolean rotateRight)
    {
        boolean[][] rotatedArr = doesRotatedBlockFit(rotateRight);
        if(rotatedArr == null) return;
        setBlockAreaToState(State.EMPTY);

        shape = rotatedArr;
        setBlockAreaToState(State.MOVING);
    }

    public void move(Direction direction)
    {
        if(!doesMovedBlockFit(direction)) return;
        setBlockAreaToState(State.EMPTY);

        anchorX += direction.x;
        anchorY += direction.y;
        setBlockAreaToState(State.MOVING);
    }
    public void hardDrop()
    {
        while(doesMovedBlockFit(Direction.Down))
            move(Direction.Down);
    }

    public boolean doesMovedBlockFit(Direction direction)
    {
        for (int i = 0; i < size; i++) {
            if(isWholeRowFalse(i)) continue;
            for (int j = 0; j < size; j++) {
                if(isWholeColumnFalse(j)) continue;
                int row = anchorY + i + direction.y;
                int column = anchorX + j + direction.x;
                if(row < 0 || row >= board.length || column < 0 || column >= board[i].length || board[row][column] == State.STATIC)
                    return false;
            }
        }
        return true;
    }

    public boolean[][] doesRotatedBlockFit(boolean rotateRight)
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
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int row = anchorY + i;
                int column = anchorX + j;
                if(row < 0 || row >= board.length || column < 0 || column >= board[i].length) return null;
                shapeCopy[i][j] = shape[startColumn + j * step][startRow - i * step];
            }
        }
        return shapeCopy;
    }


    void setBlockAreaToState(State state)
    {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(shape[i][j]) {
                    board[anchorY + i][anchorX + j] = state;
                    if(state == State.EMPTY) visualBoard[anchorY + i][anchorX + j].setFill(Color.BLACK);
                    else visualBoard[anchorY + i][anchorX + j].setFill(color);
                }
            }
        }
    }
    boolean[][] copy2dArray(boolean[][] arr)
    {
        boolean[][] copy = new boolean[arr.length][];
        for (int i = 0; i < arr.length; i++)
            copy[i] = arr[i].clone();
        return copy;
    }
    boolean isWholeRowFalse(int row)
    {
        for (int i = 0; i < size; i++) {
            if(shape[row][i]) return false;
        }
        return true;
    }

    boolean isWholeColumnFalse(int column)
    {
        for (int i = 0; i < size; i++) {
            if(shape[i][column]) return false;
        }
        return true;
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
