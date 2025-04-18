/*
 * Level represents a single puzzle Level.
 * Levels contain Menu Buttons and Tubes of colored Blocks
 */

import javax.swing.*;
import java.awt.*;

// JComponent allows this class to be drawn in the JFrame that is created in Game.
// GameEventListener allows this class to get notified when a Button is clicked
// if the Button adds the Level as a Listener.
public class Level extends JComponent implements GameEventListener{

    public Level(){

    }

    // This is where all the drawing to the screen gets done.
    // Our Original UML showed a method called draw(Graphics g),
    // but we can use this instead because it is part of JComponent.
    // Other JComponent Objects, such as Buttons are drawn automatically.
    @Override
    public void paintComponent(Graphics g){

    }

    // GameEventListener method
    // Called by any Button that is clicked if it has added this Level object as a Listener.
    // Game objects are usually Listeners for Buttons that make changes inside a single Level.
    // Examples: Restart Level, Undo Move, Hint
    @Override
    public void gameEventPerformed(GameEvent event) {
        switch(event.getEventType()){
            case EventType.LEVEL_RESTART:
                // Add code to restart the current level
                break;
            case EventType.LEVEL_UNDO:
                // Add code to undo the last move
                break;
            case EventType.LEVEL_HINT:
                // Add code to get a hint or the next move toward the solution
                break;
        }
    }
}
