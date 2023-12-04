package brickGame.factory;

import brickGame.model.Block;
import brickGame.factory.blockhithandler.*;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code BlockHitHandlerFactoryProvider} class provides a centralized factory for creating block hit handlers.
 * It maps different block types to their corresponding handler factories, allowing dynamic creation of handlers
 * based on the type of block hit.
 * @author Leong Tin Jet
 * @version 1.0
 */
public class BlockHitHandlerFactoryProvider {
    private static final Map<Block.BLOCK_TYPE, BlockHitHandlerFactory> factories = new HashMap<>();

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
