package brickGame.handler;

/**
 * The Actionable interface provides a set of methods to handle various events in the game.
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/handler/Actionable.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public interface Actionable {
    /**
     * Called when a regular update is needed in the game. Typically used for UI updates.
     */
    void onUpdate();

    /**
     * Called when a physics update is needed in the game. This includes handling collisions and movements.
     */
    void onPhysicsUpdate();

    /**
     * Called when the game time is updated.
     *
     * @param time The updated time value.
     */
    void onTime(long time);
}