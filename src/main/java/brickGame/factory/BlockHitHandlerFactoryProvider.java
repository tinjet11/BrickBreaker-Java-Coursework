package brickGame.factory;

import brickGame.model.Block;
import brickGame.factory.blockhithandler.*;

import java.util.HashMap;
import java.util.Map;

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

    public static BlockHitHandlerFactory getFactory(Block.BLOCK_TYPE blockType) {
        return factories.get(blockType);
    }
}
