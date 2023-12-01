package brickGame.handler;

public interface Actionable {
    void onUpdate();

    void onInit();

    void onPhysicsUpdate();

    void onTime(long time);
}