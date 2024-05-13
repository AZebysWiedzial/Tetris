package Tetris;

public enum Direction {
    Up(0, -1),
    Down(0, 1),
    Left(-1, 0),
    Right(1, 0),
    Still(0 ,0);
    public final int x;
    public final int y;
    Direction(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
