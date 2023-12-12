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
 * <br>
 * <a href="https://github.com/kooitt/CourseworkGame/blob/master/src/main/java/brickGame/BlockSerializable.java">Original Code</a>
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/serialization/BlockSerialize.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public class BlockSerialize implements Serializable {
    /**
     * The row index of the block.
     */
    public final int row;
    /**
     * The column index of the block.
     */
    public final int column;

    /**
     * The type of the block, indicating its characteristics.
     */
    public final Block.BLOCK_TYPE type;

    /**
     * Constructs a new {@code BlockSerialize} object with the specified row, column, and type.
     *
     * @param row    The row index of the block.
     * @param column The column index of the block.
     * @param type   The type of the block, indicating its characteristics.
     */
    public BlockSerialize(int row, int column, Block.BLOCK_TYPE type) {
        this.row = row;
        this.column = column;
        this.type = type;
    }

}
