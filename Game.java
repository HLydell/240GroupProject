/*
 * Game is the main controller for the whole Puzzle Game.
 * It is responsible for setting up all the Menus and Levels,
 * and controls which Menu or Level is being displayed.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game extends JComponent implements GameEventListener, MouseListener {
    private JFrame frame;

    // Layout for the Game. Shows 1 Menu/Level at a time and allows switching between different Menus/Levels
    private CardLayout cardLayout;

    // Game Menus
    private Menu mainMenu;
    private Menu loadLevelMenu;
    private Menu saveLevelMenu;
    private Menu winLevelMenu;

    // Select Level Menu can have multiple pages depending on how many Levels are available
    private ArrayList<Menu> selectLevelMenus;

    // Lists of Buttons for each Menu
    private ArrayList<Button> mainMenuButtonList;
    private ArrayList<Button> loadLevelButtonList;
    private ArrayList<Button> saveLevelButtonList;
    private ArrayList<Button> winLevelButtonList;

    // Each page of Select Level Menu needs a Button List
    private ArrayList<ArrayList<Button>> selectLevelButtonLists;

    // List of Pre-made Levels and Saved Levels
    private ArrayList<Level> levelList;
    private ArrayList<Level> savedLevelList;

    // Current Level being Played. Defaults to 1
    private Level currentLevel;

    public Game() {

        // Create and set up the window.
        frame = new JFrame("Block Sort Puzzle!");

        // Make the program close when the window closes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add the Game component to the frame
        frame.add(this);

        // Display the window.
        frame.setSize(800, 600);
        frame.setVisible(true);

        // Add this Game as a MouseListener to window so we can register where the Mouse Clicks
        addMouseListener(this);

        // Load Levels and Saved Levels from Files into levelList and savedLevelList (Not implemented yet)
        loadLevelsFromFile();
        loadSavedLevelsFromFile();

        // Add Buttons and GameEvents to all Levels and Saved Levels
        setupLevels(levelList);
        setupLevels(savedLevelList);

        // Initialize all Menus and add Buttons and GameEvents
        setupMainMenu();
        setupSelectLevelMenus();
        setupLoadLevelMenu();
        setupSaveLevelMenu();
        setupWinLevelMenu();

        // Set the Game's layout as Card Layout.
        // Shows 1 Menu/Level at a time and allows switching between different Menus/Levels
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Each Component is added with a name that can be used to get that Component later with cardLayout
        add(mainMenu, mainMenu.getName());
        add(loadLevelMenu, loadLevelMenu.getName());
        add(saveLevelMenu, saveLevelMenu.getName());
        add(winLevelMenu, winLevelMenu.getName());

        // Add all the Select Level Menu pages
        for(Menu menu : selectLevelMenus) {
            add(menu, menu.getName());
        }

        // Add every Level Component to the Game window with the name "Level #" starting with 1.
        // Example: Level 1, Level 2,..., Level 15, Level 16,...
        for(int i = 0; i < levelList.size(); i++){
            Level level = levelList.get(i);
            add(level, "Level "+level.getId());
        }
    }

    // Load all pre-made Levels from File and store in an ArrayList
    public void loadLevelsFromFile() {
        levelList = new ArrayList<>();

        // File containing multiple Levels
        String filename = "levels/levels.lvl";
        try {
            Scanner in = new Scanner(new File(filename));

            //Level reads up to and including a blank line, then check to see if there is another Level in the File.
            while (in.hasNext()) {
                levelList.add(new Level(in));
            }
        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, filename+" file not found", "File Not Found", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
        currentLevel = levelList.getFirst();
    }

    // Load all Saved Levels from File and store in an ArrayList
    public void loadSavedLevelsFromFile() {
        savedLevelList = new ArrayList<>();

        // PLACEHOLDER CODE: Initializes 2 levels and 1 empty Save slot for testing.
        for(int i = 1; i <= 2; i++){
            Level level = new Level(i);
            savedLevelList.add(level);
        }
        savedLevelList.add(null);
    }

    // Add Buttons with Events to each Level in the List. Used for levelsList and savedLevelList
    public void setupLevels(ArrayList<Level> levels){
        for(Level level : levels){

            //Skip any Levels that are null, like empty Save slots when Loading Saved Levels
            if(level == null){
                continue;
            }

            // Create Buttons across the top of each Level.
            // Leave Header text blank on Buttons and put text in Line2 to use smaller font.
            Button backButton = new Button("", "Back", 15, 28, 80, 30);
            Button restartButton = new Button("", "Restart", 115, 28, 80, 30);
            Button saveButton = new Button("", "Save", 210, 28, 80, 30);
            Button undoButton = new Button("", "Undo", 535, 28, 80, 30);
            Button hintButton = new Button("", "Hint", 635, 28, 80, 30);

            // Back Button and Save Button send their GameEvents to the Game Object
            backButton.setGameEvent(new GameEvent(EventType.GOTO_MENU_MAIN_MENU));
            saveButton.setGameEvent(new GameEvent(EventType.GOTO_MENU_SAVE_LEVEL));
            backButton.addGameEventListener(this);
            saveButton.addGameEventListener(this);

            // Restart, Undo, and Hint Buttons send their GameEvents to the Level Object
            restartButton.setGameEvent(new GameEvent(EventType.LEVEL_RESTART));
            undoButton.setGameEvent(new GameEvent(EventType.LEVEL_UNDO));
            hintButton.setGameEvent(new GameEvent(EventType.LEVEL_HINT));
            restartButton.addGameEventListener(level);
            undoButton.addGameEventListener(level);
            hintButton.addGameEventListener(level);

            // Add Buttons to the Level
            level.add(backButton);
            level.add(restartButton);
            level.add(saveButton);
            level.add(undoButton);
            level.add(hintButton);
        }
    }

    // Initialize Main Menu
    // Create and arrange Buttons. Add GameEvents to each Button and add this Game as GameEventListener
    public void setupMainMenu(){
        mainMenu = new Menu("Main Menu");
        mainMenuButtonList = new ArrayList<>();

        // Main Menu Buttons
        Button selectLevelButton = new Button("Select Level", 300, 100, 200, 80);
        Button loadLevelButton = new Button("Load Level", 300, 250, 200, 80);

        // Main Menu Button Events
        selectLevelButton.setGameEvent(new GameEvent(EventType.GOTO_MENU_SELECT_LEVEL,1));
        loadLevelButton.setGameEvent(new GameEvent(EventType.GOTO_MENU_LOAD_LEVEL));

        // Add this Game as a GameEventListener to be notified with the Button is clicked.
        selectLevelButton.addGameEventListener(this);
        loadLevelButton.addGameEventListener(this);

        // Add Buttons to list of Main Menu Buttons
        mainMenuButtonList.add(selectLevelButton);
        mainMenuButtonList.add(loadLevelButton);

        // Add all the Buttons to Main Menu
        mainMenu.addButtons(mainMenuButtonList);
    }

    // Initialize Select Level Menus - Displayed when Save Button is clicked in a Level
    // Each Menu holds 16 Level buttons.
    // Create and arrange Buttons. Add GameEvents to each Button and add this Game as GameEventListener
    public void setupSelectLevelMenus(){
        selectLevelMenus = new ArrayList<>();
        selectLevelButtonLists = new ArrayList<>();

        // Get total number of Menus needed all the Levels with 16 Buttons per page
        int menuCount = (int) Math.ceil(levelList.size()/16.0);

        // Create each Select Level Menu and setup Buttons
        for(int i = 1; i <= menuCount; i++){
            Menu selectMenu = new Menu("Select Level: Page "+i);
            ArrayList<Button> buttonList = new ArrayList<>();

            // Add Back Button to go back to Main Menu
            Button backButton = new Button("", "Back", 15, 0, 80, 30);
            backButton.setGameEvent(new GameEvent(EventType.GOTO_MENU_MAIN_MENU));
            backButton.addGameEventListener(this);
            selectMenu.addButton(backButton);

            // Add Previous Page Button to go to previous page of Levels if page exists
            if(i-1 >= 1) {
                Button prevButton = new Button("", "Prev Page", 295, 450, 80, 30);
                prevButton.setGameEvent(new GameEvent(EventType.GOTO_MENU_SELECT_LEVEL, i - 1));
                prevButton.addGameEventListener(this);
                selectMenu.addButton(prevButton);
            }

            // Add Next Page Button to go to next page of Levels if more pages exist
            if(i+1 <= menuCount) {
                Button nextButton = new Button("", "Next Page", 400, 450, 80, 30);
                nextButton.setGameEvent(new GameEvent(EventType.GOTO_MENU_SELECT_LEVEL, i + 1));
                nextButton.addGameEventListener(this);
                selectMenu.addButton(nextButton);
            }

            // First index on this page. Starts at 0
            int first = (i-1)*16;
            // Add up to 16 Buttons per page
            for(int j = 0; j < 16; j++){
                int levelIndex = j+first;

                // Stop adding Buttons if no more Levels exist
                if(levelIndex >= levelList.size()){
                    break;
                }

                // Divide Buttons into 4 columns
                int buttonX = 100;
                if(j % 4 == 1){
                    buttonX = 250;
                }
                else if(j % 4 == 2){
                    buttonX = 400;
                }
                else if (j % 4 == 3){
                    buttonX = 550;
                }

                // Divide Buttons into 4 rows
                int buttonY = (j/4) * 100 + 50;

                // Create Button with Level number and add GameEvent to go to that Level number
                int levelId = levelList.get(levelIndex).getId();
                int bestScore = levelList.get(levelIndex).getBestScore();
                Button levelButton = new Button("Level "+levelId,"Best Score: "+bestScore,
                        buttonX, buttonY, 125, 60);
                levelButton.setGameEvent(new GameEvent(EventType.GOTO_LEVEL, levelId));

                // Add this Game as a GameEventListener to be notified with the Button is clicked.
                levelButton.addGameEventListener(this);

                // Add Button to list of Level Select Buttons
                buttonList.add(levelButton);
            }

            // Add Buttons to the Menu
            selectMenu.addButtons(buttonList);

            // Add Menu to the list of Select Level Menus and add List of Buttons to the List of Button Lists
            selectLevelMenus.add(selectMenu);
            selectLevelButtonLists.add(buttonList);
        }
    }

    // Initialize Load Level Menu
    // Create and arrange Buttons. Add GameEvents to each Button and add this Game as GameEventListener
    public void setupLoadLevelMenu(){
        loadLevelMenu = new Menu("Load Level");
        loadLevelButtonList = new ArrayList<>();

        // Add Back Button to go back to Main Menu
        Button backButton = new Button("", "Back", 15, 0, 80, 30);
        backButton.setGameEvent(new GameEvent(EventType.GOTO_MENU_MAIN_MENU));
        backButton.addGameEventListener(this);
        loadLevelMenu.addButton(backButton);

        // PLACEHOLDER data to test Load Menu
        String [][] placeholderSavedLevels = new String[3][3];
        // Save Slot 1
        placeholderSavedLevels[0][0] = "1 (PLACEHOLDER)";
        placeholderSavedLevels[0][1] = "3";
        placeholderSavedLevels[0][2] = "04-22-2025 21:53:31";
        // Save Slot 2
        placeholderSavedLevels[1][0] = "6 (PLACEHOLDER)";
        placeholderSavedLevels[1][1] = "46";
        placeholderSavedLevels[1][2] = "4/22/2025 21:53:31";
        // Save Slot 3
        placeholderSavedLevels[2][0] = "18 (PLACEHOLDER)";
        placeholderSavedLevels[2][1] = "1028";
        placeholderSavedLevels[2][2] = "12/25/2024";

        // PLACEHOLDER CODE: add 3 Load Save Buttons to Load Level Menu
        // This code will be replaced when we load Saves from a file
        for(int i = 0; i < savedLevelList.size(); i++){
            Button levelButton;

            // if Save in this slot does not exist, mark Button as Empty and Deactivate
            if(savedLevelList.get(i) == null){
                levelButton =  new Button("Load Save "+(i+1),"[Empty]",
                        300, (i*110)+100,200,100);
                levelButton.setEnabled(false);
            }
            else {
                // PLACEHOLDER CODE: Using test data to fill in Saved Level information
                levelButton =  new Button("Load Save "+(i+1),"Level "+ placeholderSavedLevels[i][0],
                        "Current Score: "+ placeholderSavedLevels[i][1], placeholderSavedLevels[i][2],
                        300, (i*110)+100,200,100);
            }
            levelButton.setGameEvent(new GameEvent(EventType.LOAD_LEVEL, i));

            // Add this Game as a GameEventListener to be notified with the Button is clicked.
            levelButton.addGameEventListener(this);
            loadLevelButtonList.add(levelButton);
        }

        // Add all the Buttons to Load Level Menu
        loadLevelMenu.addButtons(loadLevelButtonList);
    }

    // Initialize Save Level Menu - Displayed when Save Button is clicked in a Level
    // Create and arrange Buttons. Add GameEvents to each Button and add this Game as GameEventListener
    public void setupSaveLevelMenu(){
        saveLevelMenu = new Menu("Save Level");
        saveLevelButtonList = new ArrayList<>();

        // Add Back Button to go back to Current Level where the Save button was clicked
        Button backButton = new Button("", "Back", 15, 0, 80, 30);
        backButton.setGameEvent(new GameEvent(EventType.RETURN_TO_LEVEL));
        backButton.addGameEventListener(this);
        saveLevelMenu.addButton(backButton);

        // PLACEHOLDER data to test Save Menu
        String [][] placeholderSavedLevels = new String[3][3];
        // Save Slot 1
        placeholderSavedLevels[0][0] = "1 (PLACEHOLDER)";
        placeholderSavedLevels[0][1] = "3";
        placeholderSavedLevels[0][2] = "04-22-2025 21:53:31";
        // Save Slot 2
        placeholderSavedLevels[1][0] = "6 (PLACEHOLDER)";
        placeholderSavedLevels[1][1] = "46";
        placeholderSavedLevels[1][2] = "04/22/2025 21:53:31";
        // Save Slot 3
        placeholderSavedLevels[2][0] = "18 (PLACEHOLDER)";
        placeholderSavedLevels[2][1] = "1028";
        placeholderSavedLevels[2][2] = "12/25/2024";

        // PLACEHOLDER CODE: add 3 Save Slots Buttons to Save Level Menu
        // This code will be replaced when we load Saves from a file
        for(int i = 0; i < savedLevelList.size(); i++){
            Button levelButton;

            // if Save in this slot does not exist, mark Button as Empty
            if(savedLevelList.get(i) == null){
                levelButton =  new Button("Save Slot "+(i+1),"[Empty]",300, (i*110)+100,200,100);
            }
            else {
                // PLACEHOLDER CODE: Using test data to fill in Saved Level information
                levelButton =  new Button("Save Slot "+(i+1),"Level "+ placeholderSavedLevels[i][0],
                        "Current Score: "+ placeholderSavedLevels[i][1], placeholderSavedLevels[i][2],
                        300, (i*110)+100,200,100);
            }
            levelButton.setGameEvent(new GameEvent(EventType.SAVE_LEVEL, i));

            // Add this Game as a GameEventListener to be notified with the Button is clicked.
            levelButton.addGameEventListener(this);
            saveLevelButtonList.add(levelButton);
        }

        // Add all the Buttons to Save Level Menu
        saveLevelMenu.addButtons(saveLevelButtonList);
    }

    // Initialize Win Level Menu - Displayed when a Level is completed
    // Create and arrange Buttons. Add GameEvents to each Button and add this Game as GameEventListener
    public void setupWinLevelMenu(){
        winLevelMenu = new Menu("Level Complete!");
        winLevelButtonList = new ArrayList<>();

        // Add Back Button to go back to Main Menu
        Button backButton = new Button("", "Back", 15, 0, 80, 30);
        backButton.setGameEvent(new GameEvent(EventType.GOTO_MENU_MAIN_MENU));
        backButton.addGameEventListener(this);
        winLevelMenu.addButton(backButton);

        // Get the Level just played using the currentLevelId.
        // Level IDs start at 1 and List index starts at 0. Subtract 1 to match ID to index.
        Level level = levelList.get(0);

        // Use a Deactivated Button to display Level information
        Button levelInfo = new Button("Level "+level.getId(), "Best Score: "+level.getBestScore(),
                "Current Score: "+level.getCurrentScore(), 300, 25,200,125);
        levelInfo.setEnabled(false);
        levelInfo.setContentAreaFilled(false);
        winLevelButtonList.add(levelInfo);

        // Win Menu Buttons
        Button retryLevelButton = new Button("Retry Level", 175, 175, 200, 80);
        Button nextLevelButton = new Button("Next Level", 425, 175, 200, 80);

        // Win Menu Button Events
        retryLevelButton.setGameEvent(new GameEvent(EventType.GOTO_LEVEL,currentLevel.getId()));
        nextLevelButton.setGameEvent(new GameEvent(EventType.GOTO_LEVEL,currentLevel.getId()+1));

        // Add this Game as a GameEventListener to be notified with the Button is clicked.
        retryLevelButton.addGameEventListener(this);
        nextLevelButton.addGameEventListener(this);

        // Add Buttons to list of Main Menu Buttons
        winLevelButtonList.add(retryLevelButton);
        winLevelButtonList.add(nextLevelButton);

        // Add Buttons to Main Menu
        winLevelMenu.addButtons(winLevelButtonList);
    }

    // Update Best Score info on all Level Select Buttons on given Page #
    public void updateSelectLevelMenu(int id){
        // Get list of Buttons for this Page
        ArrayList<Button> buttons = selectLevelButtonLists.get(id-1);

        // Index for first Level on this Page
        int firstLevel = (id-1)*16;

        // Loop through all Buttons on this page and update Best Score
        for(int i = 0; i < buttons.size(); i++){
            Button button = buttons.get(i);
            Level level = levelList.get(firstLevel+i);
            button.setLine2("Best Score: "+level.getBestScore());
        }
    }

    // PLACEHOLDER CODE: This will update info and Buttons on Load Level Menu with any changes to the Save Games
    public void updateLoadLevelMenu(){
        // Waiting for Load/Save Levels to Files to be implemented
    }

    // PLACEHOLDER CODE: This will update info and Buttons on Save Level Menu with any changes to the Save Games
    public void updateSaveLevelMenu(){
        // Waiting for Load/Save Levels to Files to be implemented
    }

    // Update the info and Buttons on the Win Menu to show Level just played.
    // Update Retry and Next Buttons to point to correct Levels.
    public void updateWinMenu(){
        // Get Level that was just played.
        Level winLevel = currentLevel;

        // Update Level info on Win Menu
        Button infoButton = winLevelButtonList.get(0);
        infoButton.setHeader("Level "+ winLevel.getId());
        infoButton.setLine2("Best Score: "+ winLevel.getBestScore());
        infoButton.setLine3("Current Score: "+ winLevel.getCurrentScore());

        // Update Retry and Next Level Buttons to point to correct Levels
        Button retryButton =  winLevelButtonList.get(1);
        Button nextButton =  winLevelButtonList.get(2);
        retryButton.setGameEvent(new GameEvent(EventType.GOTO_LEVEL, currentLevel.getId()));
        nextButton.setGameEvent(new GameEvent(EventType.GOTO_LEVEL, currentLevel.getId()+1));

        // Deactivate Next Button with there are no more levels
        if(currentLevel.getId()+1 <= levelList.size()){
            nextButton.setEnabled(true);
        }
        else{
            nextButton.setEnabled(false);
        }
    }

    // This is where drawing to the screen can be done.
    // We can manually draw here, or pass this Graphics object
    // to other methods to let them draw to the screen.
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentLevel != null && currentLevel.isVisible() && currentLevel.isSolved()){
            gameEventPerformed(new GameEvent(EventType.GOTO_MENU_WIN_LEVEL));
        }
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
                currentLevel.restart();
                break;
            case GOTO_MENU_SELECT_LEVEL:
                // Update info and Buttons, then display Select Level Menu
                int id = event.getEventId();
                updateSelectLevelMenu(id);
                cardLayout.show(this, selectLevelMenus.get(id-1).getName());
                break;
            case GOTO_MENU_LOAD_LEVEL:
                // Update info and Buttons, then display Load Level Menu
                updateLoadLevelMenu();
                cardLayout.show(this, loadLevelMenu.getName());
                break;
            case GOTO_MENU_SAVE_LEVEL:
                // Update info and Buttons, then display Save Level Menu
                updateSaveLevelMenu();
                cardLayout.show(this, saveLevelMenu.getName());
                break;
            case GOTO_MENU_WIN_LEVEL:
                // Update info and Buttons, then display Win Level Menu
                updateWinMenu();
                currentLevel.restart();
                cardLayout.show(this, winLevelMenu.getName());
                break;
            case GOTO_LEVEL:
                // Go to Level from Event ID and update Current Level
                currentLevel = getLevel(event.getEventId());
                cardLayout.show(this, "Level "+currentLevel.getId());
                break;
            case LOAD_LEVEL:
                //PLACEHOLDER CODE: Add code to Load a saved Level
                System.out.println("(PLACEHOLDER CODE)Event Triggered: "+event);
                break;
            case SAVE_LEVEL:
                //PLACEHOLDER CODE: Add code to Save a Level
                System.out.println("(PLACEHOLDER CODE)Event Triggered: "+event);
                break;
            case RETURN_TO_LEVEL:
                // Resume playing current Level
                cardLayout.show(this, "Level "+currentLevel.getId());
                break;
        }
    }

    // Find Level with this Level ID
    public Level getLevel(int id){
        for(Level level : levelList){
            if(level.getId() == id){
                return level;
            }
        }
        return null;
    }

    // This method is called any time a Mouse button is pressed down in the Game window.
    @Override
    public void mousePressed(MouseEvent e) {
        // pass the MouseEvent to the current level while it is being played
        if (currentLevel.isVisible()){
            currentLevel.mousePressed(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
