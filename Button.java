/*
 *  Buttons are added to Menus and Levels to trigger Game Events.
 *  Each Button has a specific Game Event that gets sent to its GameEventListeners.
 *  GameEventListeners are usually the Game object or a Level object.
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Button extends JButton {

    private ArrayList <GameEventListener> gameEventListeners;
    private GameEvent event; // Event triggered when Button is clicked

    public Button(String text, int x, int y, int width, int height) {
        // Call JButton constructor with button text
        super(text);

        // Set the location and size of the Button
        setBounds(x, y, width, height);

        gameEventListeners = new ArrayList<>();
        event = null;
    }

    // Set what event happens when this Button is clicked
    public void setGameEvent(GameEvent event) {
        this.event = event;
    }

    // Objects that get added will be notified when button is clicked.
    // Usually Game object or Level object.
    public void addGameEventListener(GameEventListener gameEventListener) {
        gameEventListeners.add(gameEventListener);
    }

    // Overrides the JButton method that gets called when the Button is clicked.
    // Notify all the Listeners that have been added to the GameEventListener list
    // and send them this Button's GameEvent
    @Override
    protected void fireActionPerformed(ActionEvent e){
        for(GameEventListener gameEventListener : gameEventListeners) {
            gameEventListener.gameEventPerformed(event);
        }
    }
}
