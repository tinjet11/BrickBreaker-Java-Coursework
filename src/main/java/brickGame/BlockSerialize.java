package brickGame;

import java.io.Serializable;

public class BlockSerialize implements Serializable {
    public final int row;
    public final int j;
    public final int type;

    public BlockSerialize(int row , int j , int type) {
        this.row = row;
        this.j = j;
        this.type = type;
    }
}
