package brickGame.serialization;

import brickGame.model.Block;

import java.io.Serializable;

public class BlockSerialize implements Serializable {
    public final int row;
    public final int j;
    public final Block.BLOCK_TYPE type;

    public BlockSerialize(int row , int j , Block.BLOCK_TYPE type) {
        this.row = row;
        this.j = j;
        this.type = type;
    }

}
