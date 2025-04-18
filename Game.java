/*
 * Game is the main controller for the whole Puzzle Game.
 * It is responsible for setting up all the Menus and Levels,
 * and controls which Menu or Level is being displayed.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Game extends JComponent implements GameEventListener, MouseListener {
    private CardLayout cardLayout;

    private Menu mainMenu;
    private Menu selectLevelMenu;
    private Menu loadLevelMenu;

    private ArrayList<Button> mainMenuButtonList;
    private ArrayList<Button> selectLevelButtonList;
    private ArrayList<Button> loadLevelButtonList;

    public Game() {

        // Create and set up the window.
        JFrame frame = new JFrame("Block Sort Puzzle!");

        // Make the program close when the window closes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add the Game component to the frame
        frame.add(this);

        // Display the window.
        frame.setSize(800, 600);
        frame.setVisible(true);

        // Add this Game as a MouseListener to window so we can register where the Mouse Clicks
        addMouseListener(this);

        // Initialize all Menus, Buttons, and GameEvents
        setupMenus();

        // Set the Game's layout as Card Layout.
        // Card Layout allows for displaying 1 Menu/Level at a time and switching between them.
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        add(mainMenu, mainMenu.getName());
        add(selectLevelMenu, selectLevelMenu.getName());
        add(loadLevelMenu, loadLevelMenu.getName());

    }

    // Initialize Menus: Main Menu, Level Select, Load Game
    // Creates and arranges Menu Buttons into specific layouts for each Menu.
    // Adds GameEvents to each Button and adds this Game as a GameEventListener
    public void setupMenus(){

        /*
         * Setup Main Menu
         */
        mainMenu = new Menu("Main Menu");
        mainMenuButtonList = new ArrayList<>();

        // Main Menu Buttons
        Button selectLevelButton = new Button("Select Level", 300, 100, 200, 80);
        Button loadLevelButton = new Button("Load Level", 300, 250, 200, 80);

        // Main Menu Button Events
        selectLevelButton.setGameEvent(new GameEvent(EventType.GOTO_MENU_SELECT_LEVEL));
        loadLevelButton.setGameEvent(new GameEvent(EventType.GOTO_MENU_LOAD_LEVEL));

        // Add this Game as a GameEventListener to be notified with the Button is clicked.
        selectLevelButton.addGameEventListener(this);
        loadLevelButton.addGameEventListener(this);

        // Add Buttons to list of Main Menu Buttons
        mainMenuButtonList.add(selectLevelButton);
        mainMenuButtonList.add(loadLevelButton);

        // Add Buttons to Main Menu
        mainMenu.addButtons(mainMenuButtonList);

        /*
         * Setup Select Level Menu
         */
        selectLevelMenu = new Menu("Select Level");
        selectLevelButtonList = new ArrayList<>();

        // PLACEHOLDER CODE: add 10 Level Buttons to Select Level Menu
        // This code will be replaced when we load Levels from a file
        for(int i = 1; i <= 10; i++){
            int buttonX = 150;
            // Even number Buttons go in second column
            if(i % 2 == 0){
                buttonX = 450;
            }
            // Divide by 2 so that each Even number Button stays at say Y value as its previous Odd number,
            int buttonY = ((i-1)/2) * 90 + 25;

            // Create Button with Level number and add GameEvent to go to that Level number
            Button levelButton = new Button("Level: "+i, buttonX, buttonY, 200, 80);
            levelButton.setGameEvent(new GameEvent(EventType.GOTO_LEVEL, i));

            // Add this Game as a GameEventListener to be notified with the Button is clicked.
            levelButton.addGameEventListener(this);

            // Add Button to list of Level Select Buttons
            selectLevelButtonList.add(levelButton);
        }

        // Add Buttons to Level Select Menu
        selectLevelMenu.addButtons(selectLevelButtonList);

        /*
         * Setup Load Level Menu
         */
        loadLevelMenu = new Menu("Load Level");
        loadLevelButtonList = new ArrayList<>();

        // PLACEHOLDER CODE: add 3 Load Slots Buttons to Load Level Menu
        // This code will be replaced when we load Saves from a file
        for(int i = 1; i <= 3; i++){
            // Create Button with Level number and add GameEvent to go to that Level number
            Button levelButton = new Button("Load Slot: "+i, 300, (i*100), 200, 80);
            levelButton.setGameEvent(new GameEvent(EventType.LOAD_LEVEL, i));

            // Add this Game as a GameEventListener to be notified with the Button is clicked.
            levelButton.addGameEventListener(this);
            loadLevelButtonList.add(levelButton);
        }

        // Add Buttons to Load Level Menu
        loadLevelMenu.addButtons(loadLevelButtonList);
    }

    // This is where drawing to the screen can be done.
    // We can manually draw here, or pass this Graphics object
    // to other methods to let them draw to the screen.
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Force the screen to keep re-painting itself in case anything has changed
        revalidate();
        repaint();

    }

    // GameEventListener method
    // Called by any Button that is clicked if it has added this Game object as a Listener.
    // Game objects are usually Listeners for Buttons that make changes outside a single Level.
    // Examples: Navigating Menus and Loading a Level
    @Override
    public void gameEventPerformed(GameEvent event){
        switch(event.getEventType()){
            case GOTO_MENU_MAIN_MENU:
                //Display Main Menu
                cardLayout.show(this, mainMenu.getName());
                break;
            case GOTO_MENU_SELECT_LEVEL:
                // Display Select Level Menu
                cardLayout.show(this, selectLevelMenu.getName());
                break;
            case GOTO_MENU_LOAD_LEVEL:
                // Display Load Level Menu
                cardLayout.show(this, loadLevelMenu.getName());
                break;
            case GOTO_LEVEL:
            case LOAD_LEVEL:
                //PLACEHOLDER CODE: We will replace this once we build the Level class.
                System.out.println("(PLACEHOLDER CODE)Event Triggered: "+event);
                cardLayout.show(this, mainMenu.getName());
                break;
        }
    }

    // This method is called any time a Mouse is clicked in the Game window.
    @Override
    public void mouseClicked(MouseEvent e) {
        // PLACEHOLDER CODE: This just shows how the MouseListener can work.
        System.out.println("Mouse Clicked at " + e.getX() + ", " + e.getY());
    }

    // This method is called any time a Mouse button is pressed down in the Game window.
    @Override
    public void mousePressed(MouseEvent e) {
        // PLACEHOLDER CODE: This just shows how the MouseListener can work.
        System.out.println("Mouse Pressed at " + e.getX() + ", " + e.getY());
    }

    // This method is called any time a Mouse button is released down in the Game window.
    @Override
    public void mouseReleased(MouseEvent e) {
        // PLACEHOLDER CODE: This just shows how the MouseListener can work.
        System.out.println("Mouse Released at " + e.getX() + ", " + e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
