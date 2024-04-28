import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public final int CELLS_HORIZONTAL = 10;
    public final int CELLS_VERTICAL = 20;
    public final int CELL_SIZE = 20;
    public final int SCENE_WIDTH = 800;
    public final int SCENE_HEIGHT = 600;
    public AnchorPane root;
    public GridPane visualBoard;
    public BlockType currentBlockType;
    public Block currentBlock;
    public int frameCount = 0;
    public State[][] board;
    public Rectangle[][] rectangleBoard;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new AnchorPane();
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        visualBoard = new GridPane();
        visualBoard.setLayoutX((SCENE_WIDTH - CELLS_HORIZONTAL * CELL_SIZE) / 2);
        visualBoard.setLayoutY((SCENE_HEIGHT - CELLS_VERTICAL * CELL_SIZE) / 3);
        root.getChildren().add(visualBoard);

        prepareBoards();

        currentBlockType = drawNextBlock();
        placeNewBlock();
        printBoard();

        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode())
            {
                case LEFT:
                    currentBlock.move(Direction.Left);
                    break;
                case RIGHT:
                    currentBlock.move(Direction.Right);
                    break;
                case DOWN:
                    currentBlock.move(Direction.Down);
                    break;
                case SPACE:
                    currentBlock.hardDrop();
                    break;
                case R:
                    currentBlock.rotate(true);
            }
        });


        Timeline gameloop = new Timeline(new KeyFrame(Duration.millis(1000/60), event -> {
            if(frameCount % 20 == 0)
            {
                adjustForEmptyRows();
                printBoard();
            }
            if (frameCount % 60 == 0)
            {
                if(currentBlock.doesMovedBlockFit(Direction.Down))
                {
                    currentBlock.move(Direction.Down);
                }
                else {
                    currentBlock.setBlockAreaToState(State.STATIC);
                    currentBlockType = drawNextBlock();
                    placeNewBlock();
                }

                deleteFullRows();

                frameCount = 0;
            }

            frameCount++;
        }));
        gameloop.setCycleCount(Animation.INDEFINITE);
        gameloop.play();
    }

    public void adjustForEmptyRows()
    {
        for (int i = board.length - 2; i >= 0; i--) {
            if(!isWholeRowInTheSameState(i + 1, State.EMPTY)) continue;

            for (int j = 0; j < board[i].length; j++)
            {
                if(board[i][j] == State.STATIC)
                {
                    board[i][j] = State.EMPTY;
                    board[i + 1][j] = State.STATIC;
                }
            }
        }
    }

    public void deleteFullRows()
    {
        for (int i = 0; i < board.length; i++) {
            if(isWholeRowInTheSameState(i, State.STATIC)) Arrays.fill(board[i], State.EMPTY);
        }
    }

    public void prepareBoards()
    {
        visualBoard.getChildren().clear();
        board = new State[CELLS_VERTICAL][CELLS_HORIZONTAL];
        rectangleBoard = new Rectangle[CELLS_VERTICAL][CELLS_HORIZONTAL];

        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], State.EMPTY);
            for (int j = 0; j < board[i].length; j++) {
                Rectangle rectangle = new Rectangle(CELL_SIZE, CELL_SIZE, Color.BLACK);
                rectangleBoard[i][j] = rectangle;
                visualBoard.add(rectangle, j, i);
            }
        }
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
        currentBlock = new Block(currentBlockType, board, rectangleBoard);
        currentBlock.setAnchorY(0);
        currentBlock.setAnchorX((board[0].length - currentBlock.getSize()) / 2);
        if(!currentBlock.doesMovedBlockFit(Direction.Still))
        {
            gameOver();
            return;
        }
        currentBlock.setBlockAreaToState(State.MOVING);
    }

    private void gameOver()
    {

    }

    public BlockType drawNextBlock()
    {
        return BlockType.values()[(int)(Math.random() * BlockType.values().length)];
    }
    public boolean isWholeRowInTheSameState(int row, State state)
    {
        for (int i = 0; i < board[row].length; i++)
        {
            if(board[row][i] != state) return false;
        }
        return true;
    }
}