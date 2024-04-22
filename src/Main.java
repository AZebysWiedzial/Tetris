import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public BlockType currentBlockType;
    public Block currentBlock;
    public final int CELLS_HORIZONTAL = 10;
    public final int CELLS_VERTICAL = 20;
    public int frameCount = 0;
    public State[][] board;

    @Override
    public void start(Stage primaryStage) throws Exception {
        board = prepareBoard();

        currentBlockType = drawNextBlock();
        placeNewBlock();
        printBoard();

        moveCurrentBlock(Direction.Left);
        printBoard();

        moveCurrentBlock(Direction.Down);
        printBoard();

        moveCurrentBlock(Direction.Right);
        printBoard();



        Timeline gameloop = new Timeline(new KeyFrame(Duration.millis(1000/60), event -> {
            if (frameCount >= 60)
            {
                moveCurrentBlock(Direction.Down);
                printBoard();
                frameCount = 0;
            }

            frameCount++;
        }));
        gameloop.setCycleCount(Animation.INDEFINITE);
        gameloop.play();
    }
    public State[][] prepareBoard()
    {
        State[][] board = new State[CELLS_VERTICAL][CELLS_HORIZONTAL];
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], State.EMPTY);
        }
        return board;
    }
    public void printBoard()
    {
        for (int i = 0; i < board.length; i++) {
            System.out.print("[");
            for (int j = 0; j < board[i].length; j++) {
                switch (board[i][j])
                {
                    case EMPTY:
                        System.out.print(0);
                        break;
                    case MOVING:
                        System.out.print(1);
                        break;
                    case STATIC:
                        System.out.print(2);
                        break;
                }
                if(j < board[i].length - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
        System.out.println();
    }
    public void placeNewBlock()
    {
        currentBlock = new Block(currentBlockType);
        currentBlock.setAnchorY(0);
        currentBlock.setAnchorX((board[0].length - currentBlock.getSize()) / 2);
        if(!doesBlockFit(Direction.Still))
        {
            System.out.println("cannot place new block");
            return;
        }
        int blockSize = currentBlock.getSize();
        int startColumn = (board[0].length - blockSize) / 2;
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                if(currentBlock.getShape()[i][j]) board[i][startColumn + j] = State.MOVING;
            }
        }
    }
    public void moveCurrentBlock(Direction direction)
    {
        if(!doesBlockFit(direction))
        {
            System.out.println("cannot move the block");
            return;
        }
        for (int i = 0; i < currentBlock.getSize(); i++) {
            for (int j = 0; j < currentBlock.getSize(); j++) {
                if(currentBlock.getShape()[i][j]) board[currentBlock.getAnchorY() + i][currentBlock.getAnchorX() + j] = State.EMPTY;
            }
        }

        for (int i = 0; i < currentBlock.getSize(); i++) {
            for (int j = 0; j < currentBlock.getSize(); j++) {
                if(currentBlock.getShape()[i][j]) board[currentBlock.getAnchorY() + i + direction.y][currentBlock.getAnchorX() + j + direction.x] = State.MOVING;
            }
        }
        currentBlock.setAnchorX(currentBlock.getAnchorX() + direction.x);
        currentBlock.setAnchorY(currentBlock.getAnchorY() + direction.y);
    }
    public boolean doesBlockFit(Direction direction)
    {
        for (int i = 0; i < currentBlockType.size; i++) {
            for (int j = 0; j < currentBlockType.size; j++) {
                int row = currentBlock.getAnchorY() + i + direction.y;
                int column = currentBlock.getAnchorX() + j + direction.x;
                if(row >= 0 && row < board.length && column >= 0 && column < board[i].length && board[row][column] == State.STATIC)
                    return false;
            }
        }
        return true;
    }
    public BlockType drawNextBlock()
    {
        return BlockType.values()[(int)(Math.random() * BlockType.values().length)];
    }
}