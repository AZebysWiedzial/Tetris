package Tetris;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static Tetris.GameLoop.*;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static boolean isInGame = false;
    public static final int STROKE_WIDTH = 2;
    public static final int FONT_SIZE = 15;
    public static final int SCENE_WIDTH = 800;
    public static final int SCENE_HEIGHT = 600;
    public static AnchorPane mainMenuRoot = new AnchorPane();
    public static AnchorPane gameLoopRoot = new AnchorPane();
    public static Scene scene = new Scene(mainMenuRoot, SCENE_WIDTH, SCENE_HEIGHT);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Font.loadFont(getClass().getResourceAsStream("nintendo-nes-font.ttf"), 20);
        MainMenu.setup();

        primaryStage.setScene(scene);
        primaryStage.show();

//        playMusic("music1.mp3");


        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE) isInGame = !isInGame;
            if(isInGame) {
                switch (event.getCode()) {
                    case LEFT:
                        currentBlock.move(Direction.Left);
//                        adjustGhostBlock();
                        break;
                    case RIGHT:
                        currentBlock.move(Direction.Right);
//                        adjustGhostBlock();
                        break;
                    case DOWN:
                        currentBlock.move(Direction.Down);
//                        adjustGhostBlock();
                        score += POINTS_PER_SOFT_DROP;
                        updateScore();
                        break;
                    case SPACE:
                        int tilesMovedDown = currentBlock.hardDrop();
                        score += tilesMovedDown * POINTS_PER_HARD_DROP;
                        updateScore();

                        GameLoop.deleteFullRows();
                        placeNewBlock();
                        break;
                    case R:
                        currentBlock.rotate(true);
                        break;
                    case C:
                        switchHeldBlock();
                        break;
                }
            }
        });


    }

//    void switchScene(boolean toGame)
//    {
//        if(toGame)
//    }


}