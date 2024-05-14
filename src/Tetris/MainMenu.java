package Tetris;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static Tetris.Main.*;

public class MainMenu {
    static final int TEXT_SIZE = 15;
    static AnchorPane root = Main.mainMenuRoot;
    static VBox buttons;
    static Button btnPlay;
    static Button btnExit;
    static Button btnLeaderBoard;
    static ImageView logo;
    static Text txtCopyRight;
    static void setup()
    {
        btnPlay = new Button("PLAY");
        btnExit = new Button("EXIT");
        btnLeaderBoard = new Button("LEADERBOARDS");

        btnPlay.setStyle("-fx-border-color: white; -fx-background-color: black; -fx-text-fill: white; -fx-font-family: 'Nintendo NES Font'; -fx-font-size: " + TEXT_SIZE + " px;");
        btnExit.setStyle("-fx-border-color: white; -fx-background-color: black; -fx-text-fill: white; -fx-font-family: 'Nintendo NES Font'; -fx-font-size: " + TEXT_SIZE + "px;");
        btnLeaderBoard.setStyle("-fx-border-color: white; -fx-background-color: black; -fx-text-fill: white; -fx-font-family: 'Nintendo NES Font'; -fx-font-size: " + TEXT_SIZE + " px;");
//        btnExit.setStyle("-fx-border-color: white; -fx-background-color: black;");
//        btnLeaderBoard.setStyle("-fx-border-width: 100; -fx-border-height: 100; -fx-border-color: white; -fx-background-color: black;");

//        btnPlay.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        logo = new ImageView(new Image("file:logo.png", 300, 300, true, false));
        logo.setX(SCENE_WIDTH / 2 - 150);
        logo.setY(40);

        txtCopyRight = new Text("1989 Nintendo©");
        txtCopyRight.setLayoutX(40);
        txtCopyRight.setLayoutY(SCENE_HEIGHT - 10);
        txtCopyRight.setFill(Color.WHITE);

        buttons = new VBox(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPrefWidth(300);
        buttons.setLayoutX(Main.SCENE_WIDTH / 2 - 150);
        buttons.setLayoutY(SCENE_HEIGHT / 3 + 90);
        buttons.getChildren().addAll(btnPlay, btnLeaderBoard, btnExit);

        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().addAll(logo, buttons, txtCopyRight);

        btnPlay.setOnAction(event -> {
            GameLoop.setup();
            Main.scene.setRoot(gameLoopRoot);
            isInGame = true;
        });
        btnExit.setOnAction(event -> {
            Platform.exit();
        });
    }
}
