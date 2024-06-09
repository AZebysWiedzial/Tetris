package Tetris;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Block {

    private final BlockType type;
    private final Rectangle[][] visualBoard;
    private final int size;
    private boolean[][] shape;
    private Color color;
    private final State[][] board;
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

        //TODO: If cannot rotate, check if the rotated shape fits after moving it in either direction

        shape = rotatedArr;
        setBlockAreaToState(State.MOVING);
    }


    public void move(Direction direction, State state)
    {
        if(!doesMovedBlockFit(direction)) return;
        setBlockAreaToState(State.EMPTY);

        anchorX += direction.x;
        anchorY += direction.y;
        setBlockAreaToState(state);
    }
    public int hardDrop()
    {
        int moves = 0;
        while(doesMovedBlockFit(Direction.Down))
        {
            move(Direction.Down, State.MOVING);
            moves++;
        }
        setBlockAreaToState(State.STATIC);

        return moves;
    }

    public boolean doesMovedBlockFit(Direction direction)
    {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(!shape[i][j]) continue;
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
    void safeSetBlockAreaToState(State state)
    {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(shape[i][j] && (board[anchorY + i][anchorX + j] == State.EMPTY || board[anchorY + i][anchorX + j] == State.GHOST)) {
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

    public Color getColor() {
        return color;
    }
    public void setColor(Color color){
        this.color = color;
    }

    public boolean[][] getShape() {
        return shape;
    }

    public void setShape(boolean[][] shape) {
        this.shape = shape;
    }

    public void setAnchorX(int anchorX) {
        this.anchorX = anchorX;
    }

    public void setAnchorY(int anchorY) {
        this.anchorY = anchorY;
    }

    public int getSize() {
        return size;
    }
    public BlockType getType()
    {
        return type;
    }

    public int getAnchorX() {
        return anchorX;
    }

    public int getAnchorY() {
        return anchorY;
    }


}
