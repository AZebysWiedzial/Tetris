import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Arrays;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        int[][] board = new int[20][10];
        print(board);
    }
    public void print(int[][] board)
    {
        for (int i = 0; i < board.length; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }
}