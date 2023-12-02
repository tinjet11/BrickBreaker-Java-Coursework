package brickGame.serialization;

import brickGame.model.Block;

import java.io.Serializable;

/**
 * The BlockSerialize class represents a serialized version of the Block class.
 * It includes information about the row, column, and the type of a block.
 * <p>
 * This class is used for serializing and deserializing Block objects to and from a file
 * in order to save and load the game state.
 * </p>
 * <p>
 * Instances of this class are created to store information about individual blocks in the game,
 * including their position (row and column) and type.
 * </p>
 * <p>
 * This class implements the Serializable interface to allow its instances to be serialized
 * and deserialized using object streams.
 * </p>
 *
 * @author Leong Tin Jet
 * @version 1.0
 */
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
