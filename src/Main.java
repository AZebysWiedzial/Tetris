import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
    public final int STROKE_WIDTH = 2;
    public final int FONT_SIZE = 15;
    public final int BLOCKS_TO_PREDICT = 3;
    public final int POINTS_PER_COMBO = 50;
    public final int POINTS_PER_SOFT_DROP = 1;
    public final int POINTS_PER_HARD_DROP = 2;
    public final int[] POINTS_FOR_LINES = {100, 300, 500, 800};
    public static final Color COLOR = Color.WHITE;
    public int frameCount = 0;
    public int score;
    public int linesDeleted;
    public int time;
    public int comboCount;
    public boolean isInGame = true;
    public boolean hasSwitchedBlock = false;
    public AnchorPane root;
    public GridPane visualBoard;
    public Block currentBlock;
    public BlockType heldBlock;
    public BlockType[] nextBlocks;
    public State[][] board;
    public Rectangle[][] rectangleBoard;

    Text txtScoreNum = new Text("0");
    Text txtTimeNum = new Text("0");
    Text txtLinesNum = new Text("0");
    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new AnchorPane();
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.setFill(Color.BLACK);

        visualBoard = new GridPane();
        visualBoard.setLayoutX((SCENE_WIDTH - CELLS_HORIZONTAL * CELL_SIZE) / 2);
        visualBoard.setLayoutY((SCENE_HEIGHT - CELLS_VERTICAL * CELL_SIZE) / 3);
        visualBoard.setStyle("-fx-border-color: white; -fx-border-width: " + STROKE_WIDTH + "px;");

        nextBlocks = new BlockType[BLOCKS_TO_PREDICT];
        for (int i = 0; i < BLOCKS_TO_PREDICT; i++) {
            nextBlocks[i] = pickRandomBlockType();
        }

        prepareBoards();

        //All the UI setup:
        VBox statistics = new VBox(10);
        statistics.setAlignment(Pos.CENTER);
        statistics.setStyle("-fx-padding: 10px; -fx-border-color: white; -fx-border-width: " + STROKE_WIDTH + "px;");
        statistics.setLayoutX(20);
        statistics.setLayoutY(100);

        Text txtScore = new Text("SCORE:");
        txtScore.setFont(Font.font(FONT_SIZE));
        txtScore.setFill(COLOR);
        Text txtTime = new Text("TIME:");
        txtTime.setFont(Font.font(FONT_SIZE));
        txtTime.setFill(COLOR);
        Text txtLines = new Text("LINES:");
        txtLines.setFont(Font.font(FONT_SIZE));
        txtLines.setFill(COLOR);

        txtScoreNum.setFont(Font.font(FONT_SIZE));
        txtScoreNum.setFill(COLOR);

        txtTimeNum.setFont(Font.font(FONT_SIZE));
        txtTimeNum.setFill(COLOR);

        txtLinesNum.setFont(Font.font(FONT_SIZE));
        txtLinesNum.setFill(COLOR);

        statistics.getChildren().addAll(txtScore, txtScoreNum, txtLines, txtLinesNum, txtTime, txtTimeNum);




        root.getChildren().addAll(visualBoard, statistics);



        primaryStage.setScene(scene);
        primaryStage.show();


        placeNewBlock();

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
                    score += POINTS_PER_SOFT_DROP;
                    updateScore();
                    break;
                case SPACE:
                    int tilesMovedDown = currentBlock.hardDrop();
                    score += tilesMovedDown * POINTS_PER_HARD_DROP;
                    updateScore();

                    deleteFullRows();
                    placeNewBlock();
                    break;
                case R:
                    currentBlock.rotate(true);
                    break;
                case C:
                    switchHeldBlock();
                    break;
                case ESCAPE:
                    isInGame = !isInGame;
                    break;
            }
        });


        Timeline gameloop = new Timeline(new KeyFrame(Duration.millis(1000/60), event -> {
            if(!isInGame) return;
            if(frameCount % 5 == 0)
            {
                adjustForEmptyRows();
            }
            if (frameCount % 60 == 0)
            {
                if(currentBlock.doesMovedBlockFit(Direction.Down))
                {
                    currentBlock.move(Direction.Down);
                }
                else
                {
                    currentBlock.setBlockAreaToState(State.STATIC);

                    deleteFullRows();
                    placeNewBlock();
                }

                time++;
                updateTime();

                frameCount = 0;
            }

            frameCount++;
        }));
        gameloop.setCycleCount(Animation.INDEFINITE);
        gameloop.play();

    }

    public void switchHeldBlock()
    {

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
                    rectangleBoard[i + 1][j].setFill(rectangleBoard[i][j].getFill());
                    rectangleBoard[i][j].setFill(Color.BLACK);
                }
            }
        }
    }

    public void deleteFullRows()
    {
        int fullRows = 0;
        int pointsToAdd = 0;
        for (int i = 0; i < board.length; i++) {
            if(isWholeRowInTheSameState(i, State.STATIC))
            {
                fullRows++;
                System.out.println("Full row detected: " + i);
                Arrays.fill(board[i], State.EMPTY);

                for (int j = 0; j < rectangleBoard[i].length; j++) {
                    rectangleBoard[i][j].setFill(Color.BLACK);
                }
            }
        }
        if(fullRows > 0)
        {
            pointsToAdd += POINTS_FOR_LINES[fullRows - 1];
            pointsToAdd += POINTS_PER_COMBO * comboCount;
        }
        else comboCount = 0;

        score += pointsToAdd;
        linesDeleted += fullRows;
        updateScore();
        updateDeletedLines();
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
        currentBlock = new Block(drawNextBlock(), board, rectangleBoard);
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
        currentBlock.setBlockAreaToState(State.MOVING);
        isInGame = false;
        System.out.println("game over");
    }

    public BlockType drawNextBlock()
    {
        BlockType nextBlock = nextBlocks[0];
        for (int i = 0; i < nextBlocks.length - 1; i++) {
            nextBlocks[i] = nextBlocks[i + 1];
        }
        nextBlocks[nextBlocks.length - 1] = pickRandomBlockType();

        return nextBlock;
    }

    public BlockType pickRandomBlockType()
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
    public void updateScore()
    {
        txtScoreNum.setText(Integer.toString(score));
    }
    public void updateTime()
    {
        txtTimeNum.setText(Integer.toString(time));
    }
    public void updateDeletedLines()
    {
        txtLinesNum.setText(Integer.toString(linesDeleted));
    }
}