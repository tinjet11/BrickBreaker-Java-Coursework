package brickGame.factory;

import brickGame.model.Block;
import brickGame.factory.blockhithandler.*;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code BlockHitHandlerFactoryProvider} class provides a centralized factory for creating
 * block hit handlers. It maps different block types to their corresponding handler factories,
 * allowing dynamic creation of handlers based on the type of block hit.
 * <p>
 * This class collaborates with various {@code BlockHitHandlerFactory} implementations, such as
 * {@code ChocoBlockHitHandlerFactory}, {@code PenaltyBlockHitHandlerFactory},
 * {@code ConcreteBlockHitHandlerFactory}, {@code HeartBlockHitHandlerFactory},
 * {@code StarBlockHitHandlerFactory}, and {@code NormalBlockHitHandlerFactory}.
 * </p>
 * <p>
 * The factory mapping is established using a {@code Map} to associate each block type with its
 * corresponding handler factory. The static initializer block initializes the mapping.
 * </p>
 *
 * @see BlockHitHandlerFactory
 * @see ChocoBlockHitHandlerFactory
 * @see PenaltyBlockHitHandlerFactory
 * @see ConcreteBlockHitHandlerFactory
 * @see HeartBlockHitHandlerFactory
 * @see StarBlockHitHandlerFactory
 * @see NormalBlockHitHandlerFactory
 * @see Block
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/factory/BlockHitHandlerFactoryProvider.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */

public class BlockHitHandlerFactoryProvider {
    /**
     * The mapping of block types to their corresponding handler factories.
     */
    private static final Map<Block.BLOCK_TYPE, BlockHitHandlerFactory> factories = new HashMap<>();

    /**
     * Static initializer block to populate the factory mapping.
     */
    static {
        factories.put(Block.BLOCK_TYPE.BLOCK_CHOCO, new ChocoBlockHitHandlerFactory());
        factories.put(Block.BLOCK_TYPE.BLOCK_PENALTY, new PenaltyBlockHitHandlerFactory());
        factories.put(Block.BLOCK_TYPE.BLOCK_CONCRETE, new ConcreteBlockHitHandlerFactory());
        factories.put(Block.BLOCK_TYPE.BLOCK_HEART, new HeartBlockHitHandlerFactory());
        factories.put(Block.BLOCK_TYPE.BLOCK_STAR, new StarBlockHitHandlerFactory());
        factories.put(Block.BLOCK_TYPE.BLOCK_NORMAL, new NormalBlockHitHandlerFactory());
    }

    /**
     * Retrieves the block hit handler factory associated with the specified block type.
     *
     * @param blockType The type of block for which the handler factory is requested.
     * @return The block hit handler factory for the given block type, or null if not found.
     */
    public static BlockHitHandlerFactory getFactory(Block.BLOCK_TYPE blockType) {
        return factories.get(blockType);
    }
}
