/*
*  Menus represent a screen with buttons for navigating to other Menus or Levels
*  Examples: Main Menu, Select Level Menu, Load Level Menu, Save Level Menu, Win Level Menu
*/

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// JComponent allows this class to be drawn in the JFrame that is created in Game.
public class Menu extends JComponent {
    private String menuName;
    private JLabel headerLabel;
    private JPanel headerPanel;
    private JPanel buttonPanel;

    public Menu(String menuName) {
        this.menuName = menuName;

        // Display large text at the top center of the screen with the Menu name.
        headerLabel = new JLabel(menuName);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD, 48f));

        // Transparent JPanel for the Header text
        headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.add(headerLabel);

        // Transparent JPanel to hold the Menu Buttons
        buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(null);

        // Add Header panel and Button panel to the Menu to be displayed.
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(headerPanel);
        add(buttonPanel);
    }

    // This is where all the drawing to the screen gets done.
    // Our Original UML showed a method called draw(Graphics g),
    // but we can use this instead because it is part of JComponent.
    // Other JComponent Objects, such as Buttons are drawn automatically.
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    // Add a list of Buttons to the Button Panel
    public void addButtons(ArrayList<Button> buttons){
        for(Button b : buttons){
            buttonPanel.add(b);
        }
    }

    // Add a single Button to the Button Panel
    public void addButton(Button button){
        buttonPanel.add(button);
    }

    // Change header text at the top of the Menu.
    // Default: Menu name
    public void setHeader(String header) {
        headerLabel.setText(header);
    }

    public String getName() {
        return menuName;
    }

}
