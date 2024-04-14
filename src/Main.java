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

    @Override
    public void start(Stage primaryStage) throws Exception {
//        int[][] board = new int[20][10];
//        print(board);

        Block block = new Block(BlockType.LBlock);
        print(block.shape);

        block.rotate(true);
        print(block.shape);

        Timeline gameloop = new Timeline(new KeyFrame(Duration.millis(1000), event -> {

        }));
    }
    public void print(int[][] board)
    {
        for (int i = 0; i < board.length; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
        System.out.println();
    }
    public void print(boolean[][] board)
    {
        for (int i = 0; i < board.length; i++) {
            System.out.print("[");
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j]) System.out.print("1");
                else System.out.print("0");
                if(j < board.length - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
        System.out.println();
    }

    public void rotateArr(boolean[][] arr, boolean rightRotation)
    {
        boolean[][] copy = new boolean[arr.length][];
        for (int i = 0; i < arr.length; i++)
            copy[i] = arr[i].clone();



        int step = 1;
        int startJ = 0;
        int startI = arr.length - 1;

        if(rightRotation){
           startJ = arr.length - 1;
           startI = 0;
           step = -1;
        }
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                arr[i][j] = copy[startJ + j * step][startI - i * step];
            }
        }
    }
}