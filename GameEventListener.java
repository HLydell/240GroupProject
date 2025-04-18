/*
 *  GameEventListener allows a class to be notified when a GameEvent is triggered.
 *  Game class and Level class implement GameEventListener
 */

public interface GameEventListener {

    // Any class that implements this interface needs to Override this method.
    void gameEventPerformed(GameEvent event);
}
