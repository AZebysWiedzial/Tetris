package Tetris;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Arrays;

import static Tetris.Main.*;

public class GameLoop {
    public static final int CELLS_HORIZONTAL = 10;
    public static final int CELLS_VERTICAL = 20;
    public static final int CELL_SIZE = 20;
    public static final int BLOCKS_TO_PREDICT = 5;
    public static final int POINTS_PER_COMBO = 50;
    public static final int POINTS_PER_SOFT_DROP = 1;
    public static final int POINTS_PER_HARD_DROP = 2;
    public static final int[] POINTS_FOR_LINES = {100, 300, 500, 800};
    public static final Color COLOR = Color.WHITE;
    public static int frameCount = 0;
    public static int score;
    public static int linesDeleted;
    public static int time;
    public static int comboCount;
    public static boolean hasSwitchedBlock = false;
    public static boolean showGhostBlock = false;
    public static Block currentBlock;
    public static Block ghostBlock;
    public static BlockType heldBlock;
    public static BlockType[] nextBlocks;
    public static State[][] board;
    public static Rectangle[][] rectangleBoard;

    static AnchorPane root = Main.gameLoopRoot;
    public static GridPane visualBoard;
    static VBox visualNextBlocks;
    static Group visualHeldBlock;
    static CheckBox cbShowGhostBlock;
    static Text txtScoreNum = new Text("0");
    static Text txtTimeNum = new Text("0");
    static Text txtLinesNum = new Text("0");
    static void setup()
    {


        visualBoard = new GridPane();
        visualBoard.setLayoutX((SCENE_WIDTH - CELLS_HORIZONTAL * CELL_SIZE) / 2);
        visualBoard.setLayoutY((SCENE_HEIGHT - CELLS_VERTICAL * CELL_SIZE) / 3);
        visualBoard.setStyle("-fx-border-color: white; -fx-border-width: " + STROKE_WIDTH + "px;");

        nextBlocks = new BlockType[BLOCKS_TO_PREDICT];
        nextBlocks[0] = pickRandomBlockType();
        for (int i = 1; i < BLOCKS_TO_PREDICT; i++) {
            BlockType nextBlock;
            do {
                nextBlock = pickRandomBlockType();
            }
            while (nextBlock == nextBlocks[i - 1]);
            nextBlocks[i] = nextBlock;
        }

        prepareBoards();

        //All the UI setup:
        VBox statistics = new VBox(10);
        statistics.setAlignment(Pos.CENTER);
        statistics.setStyle("-fx-padding: 10px; -fx-border-color: white; -fx-border-width: " + STROKE_WIDTH + "px;");
        statistics.setLayoutX(50);
        statistics.setLayoutY(350);

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

        visualHeldBlock = new Group();
//        visualGhostBlock = new Group();

        Rectangle heldBlockOverlay = new Rectangle(100, 100, 100, 100);
        heldBlockOverlay.setStroke(COLOR);
        heldBlockOverlay.setStrokeWidth(STROKE_WIDTH);

        Text txtHold = new Text("HOLD:");
        txtHold.setLayoutX(115);
        txtHold.setLayoutY(80);
        txtHold.setFont(Font.font(FONT_SIZE * 1.5));
        txtHold.setFill(COLOR);

        visualNextBlocks = new VBox(10);
        visualNextBlocks.setAlignment(Pos.CENTER);
        visualNextBlocks.setStyle("-fx-padding: 10px; -fx-border-color: white; -fx-border-width: " + STROKE_WIDTH + "px;");
        visualNextBlocks.setLayoutX(600);
        visualNextBlocks.setLayoutY(200);

        Text txtNext = new Text("NEXT:");
        txtNext.setLayoutX(visualNextBlocks.getLayoutX() + 15);
        txtNext.setLayoutY(visualNextBlocks.getLayoutY() - 20);
        txtNext.setFont(Font.font(FONT_SIZE * 1.5));
        txtNext.setFill(COLOR);

        cbShowGhostBlock = new CheckBox("SHOW GHOST BLOCK");
        cbShowGhostBlock.setOnAction(event -> {
            showGhostBlock = !showGhostBlock;
            adjustGhostBlock();
        });

        for (int i = 0; i < BLOCKS_TO_PREDICT; i++) {
            Group visualBlock = createVisualBlock(nextBlocks[i]);
            visualNextBlocks.getChildren().add(visualBlock);
        }

        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().addAll(visualBoard, statistics, visualNextBlocks, txtNext, heldBlockOverlay, visualHeldBlock, txtHold, cbShowGhostBlock);



        placeNewBlock();


        Timeline gameloop = new Timeline(new KeyFrame(Duration.millis(1000/60), event -> {
            if(isInGame) {
                if (frameCount % 5 == 0) {
                    adjustForEmptyRows();
                    printBoard();
                }
                if (frameCount % 60 == 0) {
                    if (currentBlock.doesMovedBlockFit(Direction.Down)) {
                        currentBlock.move(Direction.Down, State.MOVING);
                    } else {
                        currentBlock.setBlockAreaToState(State.STATIC);

                        deleteFullRows();
                        placeNewBlock();
                    }

                    time++;
                    updateTime();

                    frameCount = 0;
                }

                frameCount++;
            }
        }));
        gameloop.setCycleCount(Animation.INDEFINITE);
        gameloop.play();
    }

    public static void switchHeldBlock()
    {
        if(hasSwitchedBlock) return;
        currentBlock.setBlockAreaToState(State.EMPTY);
        if(heldBlock == null)
        {
            heldBlock = currentBlock.getType();
            placeNewBlock();
        }
        else
        {
            BlockType temp = currentBlock.getType();

            placeNewBlock(heldBlock);
            heldBlock = temp;
        }
        adjustVisualBlock(heldBlock, visualHeldBlock);

        visualHeldBlock.setLayoutX(150 - visualHeldBlock.getBoundsInLocal().getWidth() / 2);
        visualHeldBlock.setLayoutY(150 - visualHeldBlock.getBoundsInLocal().getHeight() / 2);

        hasSwitchedBlock = true;
    }
    public static void adjustGhostBlock()
    {
        if(ghostBlock != null) ghostBlock.safeSetBlockAreaToState(State.EMPTY);
        if(!showGhostBlock) return;
        ghostBlock = new Block(currentBlock.getType(), board, rectangleBoard);
        ghostBlock.setAnchorX(currentBlock.getAnchorX());

        ghostBlock.setShape(currentBlock.getShape());

        Color currColor = ghostBlock.getColor();
        ghostBlock.setColor(new Color(currColor.getRed(), currColor.getGreen(), currColor.getBlue(), 0.5));

        while(ghostBlock.doesMovedBlockFit(Direction.Down))
        {
            ghostBlock.move(Direction.Down, State.GHOST);
        }
        currentBlock.setBlockAreaToState(State.MOVING);
    }
    public static void adjustForEmptyRows()
    {
        for (int i = board.length - 2; i >= 0; i--) {
            if(!isRowEmpty(i+1)) continue;

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

    public static void deleteFullRows()
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

    public static void prepareBoards()
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
    public static void printBoard()
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
                    case GHOST:
                        System.out.print(3);
                }
                if(j < board[i].length - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
        System.out.println();
    }
    public static void placeNewBlock()
    {
        currentBlock = new Block(drawNextBlock(), board, rectangleBoard);
        currentBlock.setAnchorY(0);
        currentBlock.setAnchorX((board[0].length - currentBlock.getSize()) / 2);
        if(!currentBlock.doesMovedBlockFit(Direction.Still))
        {
            gameOver();
            return;
        }
        adjustGhostBlock();
        currentBlock.setBlockAreaToState(State.MOVING);
        hasSwitchedBlock = false;
    }

    public static void placeNewBlock(BlockType type)
    {
        currentBlock = new Block(type, board, rectangleBoard);
        currentBlock.setAnchorY(0);
        currentBlock.setAnchorX((board[0].length - currentBlock.getSize()) / 2);
        if(!currentBlock.doesMovedBlockFit(Direction.Still))
        {
            gameOver();
            return;
        }
        adjustGhostBlock();
        currentBlock.setBlockAreaToState(State.MOVING);
    }

    private static void gameOver()
    {
        currentBlock.setBlockAreaToState(State.MOVING);
        isInGame = false;
        System.out.println("game over");
    }

    public static BlockType drawNextBlock()
    {
        BlockType nextBlock = nextBlocks[0];
        visualNextBlocks.getChildren().remove(0);
        for (int i = 0; i < nextBlocks.length - 1; i++) {
            nextBlocks[i] = nextBlocks[i + 1];
        }
        BlockType newBlock;
        do
        {
            newBlock = pickRandomBlockType();
        }
        while(newBlock == nextBlocks[nextBlocks.length - 2]);
        nextBlocks[nextBlocks.length - 1] = newBlock;
        visualNextBlocks.getChildren().add(createVisualBlock(newBlock));

        return nextBlock;
    }

    public static BlockType pickRandomBlockType()
    {
        return BlockType.values()[(int)(Math.random() * BlockType.values().length)];
    }
    public static boolean isWholeRowInTheSameState(int row, State state)
    {
        for (int i = 0; i < board[row].length; i++)
        {
            if(board[row][i] != state) return false;
        }
        return true;
    }
    public static boolean isRowEmpty(int row)
    {
        for (int i = 0; i < board[row].length; i++)
        {
            if(board[row][i] != State.EMPTY && board[row][i] != State.GHOST) return false;
        }
        return true;
    }
    public static Group createVisualBlock(BlockType blockType)
    {
        Group block = new Group();
        for (int i = 0; i < blockType.size; i++) {
            for (int j = 0; j < blockType.size; j++) {
                if(!blockType.shape[i][j]) continue;

                Rectangle rectangle = new Rectangle(CELL_SIZE, CELL_SIZE, blockType.color);
                rectangle.setX(CELL_SIZE * j);
                rectangle.setY(CELL_SIZE * i);

                block.getChildren().add(rectangle);
            }
        }
        return block;
    }
    public static void adjustVisualBlock(BlockType blockType, Group visualBlock)
    {
        visualBlock.getChildren().clear();
        for (int i = 0; i < blockType.size; i++) {
            for (int j = 0; j < blockType.size; j++) {
                if(!blockType.shape[i][j]) continue;

                Rectangle rectangle = new Rectangle(CELL_SIZE, CELL_SIZE, blockType.color);
                rectangle.setX(CELL_SIZE * j);
                rectangle.setY(CELL_SIZE * i);

                visualBlock.getChildren().add(rectangle);
            }
        }

    }
    public static void updateScore()
    {
        txtScoreNum.setText(Integer.toString(score));
    }
    public static void updateTime()
    {
        txtTimeNum.setText(Integer.toString(time));
    }
    public static void updateDeletedLines()
    {
        txtLinesNum.setText(Integer.toString(linesDeleted));
    }
    public static AnchorPane getRoot()
    {
        return root;
    }
}
