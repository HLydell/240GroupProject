/*
 *  Buttons are added to Menus and Levels to trigger Game Events.
 *  Buttons can have Bold header text and up to 3 additional lines of smaller text.
 *  Each Button has a specific Game Event that gets sent to its GameEventListeners.
 *  GameEventListeners are usually the Game object or a Level object.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Button extends JButton {

    private ArrayList <GameEventListener> gameEventListeners;
    private GameEvent event; // Event triggered when Button is clicked

    // Buttons can have up to 4 lines of text, with the Header being larger and Bold
    private JLabel headerLabel;
    private JLabel line2Label;
    private JLabel line3Label;
    private JLabel line4Label;

    // Button created with Header and up to 3 Lines of additional text.
    public Button(String header, String line2, String line3, String line4, int x, int y, int width, int height){
        // Add text to labels - Empty Strings will make JLabels not take up any space
        headerLabel = new JLabel(header);
        line2Label = new JLabel(line2);
        line3Label = new JLabel(line3);
        line4Label = new JLabel(line4);

        // Allow text to get closer to edge of Button
        setMargin(new Insets(0,0,0,0));

        // Header is Larger and Bold. Additional lines are plain.
        headerLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 22));
        line2Label.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
        line3Label.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
        line4Label.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));

        // Center all the text on the Button
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        line2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        line3Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        line4Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add Labels to the Button from the top down. Any blank labels will not take up any space.
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        add(Box.createVerticalGlue());
        add(headerLabel);
        add(line2Label);
        add(line3Label);
        add(line4Label);
        add(Box.createVerticalGlue());

        // Set position and size of Button
        setBounds(x, y, width, height);

        gameEventListeners = new ArrayList<>();
        event = null;
    }

    // Button created with only a Header
    public Button(String header, int x, int y, int width, int height) {
        this(header, "", "", "", x, y, width, height);
    }

    // Button created with Header and 1 Line of additional text
    public Button(String header, String line2, int x, int y, int width, int height){
        this(header, line2, "", "", x, y, width, height);
    }

    // Button created with Header and 2 Lines of additional text
    public Button(String header, String line2, String line3, int x, int y, int width, int height){
        this(header, line2, line3, "", x, y, width, height);
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

    public String getHeader(){
        return headerLabel.getText();
    }

    public String getLine2(){
        return line2Label.getText();
    }

    public String getLine3(){
        return line3Label.getText();
    }

    public String getLine4(){
        return line4Label.getText();
    }

    public void setHeader(String header){
        headerLabel.setText(header);
    }

    public void setLine2(String line2){
        line2Label.setText(line2);
    }

    public void setLine3(String line3){
        line3Label.setText(line3);
    }

    public void setLine4(String line4){
        line4Label.setText(line4);
    }

    // Set what event happens when this Button is clicked
    public void setGameEvent(GameEvent event) {
        this.event = event;
    }
}
