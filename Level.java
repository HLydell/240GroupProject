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

    private int id; // Level number
    private int bestScore;
    private int currentScore;

    public Level(int id){
        this.id = id;
        bestScore = 0;
        currentScore = 0;

        // Null Layout allows Components like Buttons to be drawn anywhere.
        setLayout(null);
    }

    // This is where all the drawing to the screen gets done.
    // Our Original UML showed a method called draw(Graphics g),
    // but we can use this instead because it is part of JComponent.
    // Other JComponent Objects, such as Buttons are drawn automatically.
    @Override
    public void paintComponent(Graphics g){

        // Draw Header with Level info
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font(Font.DIALOG, Font.BOLD, 22));
        g.drawString("Level "+id, 360, 33);
        g.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
        g.drawString("Best Score:  "+bestScore, 350, 55);
        g.drawString("Current Score:  "+currentScore, 340, 72);
    }

    // GameEventListener method
    // Called by any Button that is clicked if it has added this Level object as a Listener.
    // Level objects are usually Listeners for Buttons that make changes inside a single Level.
    // Examples: Restart Level, Undo Move, Hint
    @Override
    public void gameEventPerformed(GameEvent event) {
        switch(event.getEventType()){
            case EventType.LEVEL_RESTART:
                //PLACEHOLDER CODE: Add code to restart the current level
                System.out.println("(PLACEHOLDER CODE)Event Triggered: "+event);
                break;
            case EventType.LEVEL_UNDO:
                //PLACEHOLDER CODE: Add code to undo the last move
                System.out.println("(PLACEHOLDER CODE)Event Triggered: "+event);
                break;
            case EventType.LEVEL_HINT:
                //PLACEHOLDER CODE: Add code to get a hint or the next move toward the solution
                System.out.println("(PLACEHOLDER CODE)Event Triggered: "+event);
                break;
        }
    }

    public int getId(){
        return id;
    }

    public int getBestScore(){
        return bestScore;
    }

    public int getCurrentScore(){
        return currentScore;
    }
}
