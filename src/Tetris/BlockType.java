package Tetris;

import javafx.scene.paint.Color;

public enum BlockType {
        OBlock(4, Color.YELLOW, new boolean[][]{
                {false, false, false, false},
                {false, true, true, false},
                {false, true, true, false},
                {false, false, false, false}
        }),
        IBlock(4, Color.DEEPSKYBLUE, new boolean[][]{
                {false, false, false, false},
                {true, true, true, true},
                {false, false, false, false},
                {false, false, false, false},
        }),
        JBlock(3, Color.BLUE, new boolean[][]{
                {true, false, false},
                {true, true, true},
                {false, false, false},
        }),
        LBlock(3, Color.ORANGE, new boolean[][]{
                {false, false, true},
                {true, true, true},
                {false, false, false}
        }),
        TBlock(3, Color.DARKMAGENTA, new boolean[][]{
                {false, true, false},
                {true, true, true},
                {false, false, false}
        }),
        SBlock(3, Color.FORESTGREEN, new boolean[][]{
                {false, true, true},
                {true, true, false},
                {false, false, false}
        }),
        ZBlock(3, Color.RED, new boolean[][]{
                {true, true, false},
                {false, true, true},
                {false, false, false}
        });

        public final int size;
        public final Color color;
        public final boolean[][] shape;

        BlockType(int size, Color color, boolean[][] shape) {
                this.size = size;
                this.color = color;
                this.shape = shape;
        }

}