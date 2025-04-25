/*
 * Level represents a single puzzle Level.
 * Levels contain Menu Buttons and Tubes of colored Blocks
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

// JComponent allows this class to be drawn in the JFrame that is created in Game.
// GameEventListener allows this class to get notified when a Button is clicked
// if the Button adds the Level as a Listener.
public class Level extends JComponent implements GameEventListener{

    private int id; // Level number
    private int bestScore;
    private int currentScore;
    private ArrayList<Tube> tubeList;
    private ArrayList<Tube> initialTubes;
    private ArrayList<Tube> moveList;
    private boolean isSavedLevel = false;

    public Level(int id){
        this.id = id;
        bestScore = 0;
        currentScore = 0;

        tubeList = new ArrayList<>();
        initialTubes = new ArrayList<>();
        // Null Layout allows Components like Buttons to be drawn anywhere.
        setLayout(null);
    }

    // Create Level by reading in lines from a File
    public Level(Scanner in){
        this.id = id;
        currentScore = 0;

        tubeList = new ArrayList<>();
        initialTubes = new ArrayList<>(); // Backup copy of the state of the Tubes at the start. Used for Restarting
        moveList = new ArrayList<>();

        // Null Layout allows Components like Buttons to be drawn anywhere.
        setLayout(null);

        // First line is formated: @levelId,bestScore
        String header = in.nextLine();
        header = header.replaceAll("@","");
        String[] tokens = header.split(",");
        id = Integer.parseInt(tokens[0]);
        bestScore = Integer.parseInt(tokens[1]);

        // Every line is a Tube
        while(in.hasNextLine()){
            String line = in.nextLine();

            //Blank line marks the end of this Level
            if(line.isEmpty()){
                break;
            }
            Tube tube = new Tube(line.length());
            for(int i = 0; i < line.length(); i++){
                // Empty spaces are marked with dashes
                if(line.charAt(i) == '-'){
                    break;
                }
                tube.addBlock(new Block(line.charAt(i)));
            }
            tubeList.add(tube);
        }
        assignTubeShape();

        // Create a backup of the initial state of the Tubes for Restarting the Level
        for(Tube tube : tubeList){
            initialTubes.add(tube.clone());
        }
    }

    // This is where all the drawing to the screen gets done.
    // Our Original UML showed a method called draw(Graphics g),
    // but we can use this instead because it is part of JComponent.
    // Other JComponent Objects, such as Buttons are drawn automatically.
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        Color c1 = ColorScheme.LEVEL_BG_COLOR_1;
        Color c2 = ColorScheme.LEVEL_BG_COLOR_2;

        g2.setPaint(new GradientPaint(new Point(0, 0), c1, new Point(0, getHeight()), c2));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();

        // Draw Header with Level info
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font(Font.DIALOG, Font.BOLD, 22));
        g.drawString("Level "+id, 360, 33);
        g.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
        g.drawString("Best Score:  "+ bestScore, 350, 55);
        g.drawString("Current Score:  "+ currentScore, 340, 72);
        for (int i = 0; i < tubeList.size(); i++){ //goes through every tube in tubeList
            Rectangle temp = tubeList.get(i).getShape();
            tubeList.get(i).assignBlockShape(); //assigns x,y,width,height coordinates for each block in a tube
            for (int j = 0; j < tubeList.get(i).getFillAmt(); j++){
                Rectangle tempBlock = tubeList.get(i).getTube().get(j).getShape(); //draws each block within a tube
                g.setColor(tubeList.get(i).getTube().get(j).getColor());
                g.fillRect(tempBlock.x, tempBlock.y, tempBlock.width, tempBlock.height);
            }
            if (tubeList.get(i).isSelected()){ //if selected, gives rectangle red border
                ((Graphics2D) g).setStroke(new BasicStroke(5)); //cast to Graphics2D object as Graphics does not have stroke size
                g.setColor(Color.RED);
                g.drawRect(temp.x, temp.y, temp.width, temp.height);
                ((Graphics2D) g).setStroke(new BasicStroke(1));
            } else {
                g.setColor(Color.GRAY);
                g.drawRect(temp.x, temp.y, temp.width, temp.height);
            }

        }

    }

    // GameEventListener method
    // Called by any Button that is clicked if it has added this Level object as a Listener.
    // Level objects are usually Listeners for Buttons that make changes inside a single Level.
    // Examples: Restart Level, Undo Move, Hint
    @Override
    public void gameEventPerformed(GameEvent event) {
        switch(event.getEventType()){
            case EventType.LEVEL_RESTART:
                restart();
                break;
            case EventType.LEVEL_UNDO:
                undo();
                break;
            case EventType.LEVEL_HINT:
                hint();
                break;
        }
    }

    //moves block from one tube to another tube, if two concurrent blocks are the same colour they are moved at same time
    public void moveBlock(Tube start, Tube end){
        if (start.isEmpty()){ //cannot move if no objects exist
            return;
        }
        int endSpace = end.getEmptySpace();
        Color startColor = start.viewTopBlock().getColor();
        Block tempBlock = end.viewTopBlock();
        Color endColor;
        if (tempBlock == null){ //if end tube is empty, end colour is considered to be same as start colour so moving algorithm works
            endColor = startColor;
        } else {
            endColor = end.viewTopBlock().getColor();
        }
        if (endSpace >= start.getTopColorSize() && (startColor.equals(endColor))){ //if the empty space is sufficient and colour matches
            int top = start.getFillAmt() - 1;
            while (start.viewTopBlock() != null && start.viewTopBlock().getColor().equals(startColor) && top >= 0){
                Block temp = start.removeTopBlock(); //moves block over
                end.addBlock(temp);
                top -= 1;
                moveList.add(start); // keep track of which tubes have been moved
                moveList.add(end);

            }
            currentScore += 1;
        } else {
            System.out.println("Color could not be moved");
        }
    }

    //undo blocks one by one
    public void undo(){
        if (!moveList.isEmpty()) {
            Tube start = moveList.remove(moveList.size() - 1); //get last item (which is the end of the last move)
            Tube end = moveList.remove(moveList.size() - 1); //start of last move
            Block temp = start.removeTopBlock(); //move first block over
            end.addBlock(temp);
            //if there is any additional block moves that are the same as previous undo as well (they were moved in same 'move')
            while (!moveList.isEmpty() && moveList.getLast().equals(start) && moveList.get(moveList.size() - 2).equals(end)){
                start = moveList.remove(moveList.size() - 1); //get last item (which is the end of the last move
                end = moveList.remove(moveList.size() - 1); //start of last move
                temp = start.removeTopBlock(); //moves block over
                end.addBlock(temp);
            }
            //score not decreased, user punished for undo
        } else {
            System.out.println("There is no more moves to undo");
        }
    }

    // Reset Level back to initial state and clear score
    public void restart(){
        currentScore = 0;
        moveList.clear();
        tubeList.clear();
        for(Tube tube : initialTubes){
            tubeList.add(tube.clone());
        }
    }

    // Attempt to find a solution to this Level in its current form and make the next best move
    public void hint(){
        SolveLevel solveLevel = new SolveLevel(this);
        Point hint = solveLevel.getNextMove();
        // If solution was found, make the next move, otherwise display popup window
        if(hint != null){
            moveBlock(tubeList.get(hint.x), tubeList.get(hint.y));
        }
        else{
            JOptionPane.showMessageDialog(null, "No Hints Available", "Hint", JOptionPane.WARNING_MESSAGE);
        }
    }

    //goes through all tubes in tubeList and sees if they are solved
    public boolean isSolved(){
        for (int i = 0; i < tubeList.size(); i++){
            if (!tubeList.get(i).isTubeSolved()){
                return false;
            }
        }
        if (currentScore < bestScore || bestScore == 0){ //updates best score if they have won
            bestScore = currentScore;
        }
        return true;
    }

    //assigns coordinates to the rectangle object within a tube, in order to draw them properly to screen
    private void assignTubeShape(){
        int numTubes = tubeList.size(); //size of tubes depends on number of tubes, gets smaller when there is more tubes
        int space = 600 - (10 * (numTubes)); //600 is size of playing space, 10 is space between tubes
        int sizeTube = space / numTubes;

        int xStart = 100;
        int yStart = 150;

        for (int i = 0; i < tubeList.size(); i++){
            Rectangle temp = tubeList.get(i).getShape();
            temp.setBounds(xStart, yStart, sizeTube, 350);
            xStart = xStart + sizeTube + 10;
        }
    }

    public void mousePressed(MouseEvent e){
        boolean contains = false;
        for (int i = 0; i < tubeList.size(); i++){
            Tube tempTube = tubeList.get(i);
            Rectangle temp = tempTube.getShape();
            if (temp.contains(e.getX(), e.getY())){
                if (tempTube.isSelected()){ //if tube is already selected, and it is selected again, unselect it
                    tempTube.setSelect(false);
                    return;
                } else {
                    for (int j = 0; j < tubeList.size(); j++){
                        Tube startTube = tubeList.get(j); //if there is another tube already selected when we select a new tube
                        if (startTube.isSelected()){
                            moveBlock(startTube, tempTube); //move block from pre-selected tube to current tube
                            startTube.setSelect(false);
                            return;
                        }
                    }
                    if(!tempTube.isEmpty()) {
                        tempTube.setSelect(true);
                    }

                }
                break;
            }
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

    public ArrayList<Tube> getTubeList() {
        return tubeList;
    }

    public boolean getIsSaved() {
        return isSavedLevel;
    }

    public void setIsSaved() {
        this.isSavedLevel = true;
    }


    //updated logic; dashes now included.
    // Pass in which score to save in the file
    // levels.lvl saves bestScore and save slots save currentScore
    public String levelToText(int score) {
        isSolved(); //added this to update the best score
        int levelId = getId();
        String firstLine = "@" + levelId + "," + score + "\n";

        ArrayList<Tube> tubesList = getTubeList();
        String tubes = "";
        for (Tube tube : tubesList) {//
            ArrayList<Block> blocks = tube.getTube();
            if (!tube.isEmpty()){ //if tube NOT empty
                if (tube.isFull()) { //FULL TUBE
                    for (Block block : blocks) {
                        tubes = tubes.concat(block.toString());
                    }
                }
                else { //NOT FULL TUBE
                    for (Block block : blocks) {
                        tubes = tubes.concat(block.toString());
                    }
                    int numDashes = tube.getEmptySpace();
                    String dashes = "-".repeat(numDashes);
                    tubes = tubes.concat(dashes);
                }
            } else { //COMPLETELY EMPTY TUBE
                int numDashes = tube.getEmptySpace();
                String dashes = "-".repeat(numDashes);
                tubes = tubes.concat(dashes);
            }
            tubes = tubes.concat("\n");
        }

        return firstLine + tubes + "\n\n";
    }

    // Save the current state of the Level to a Save Slot file
    public void saveToFile(int id) {
        try {
            int saveFileId = id + 1;
            String savePath = "saves/save" + saveFileId + ".lvl";

            // Save Slot files save Current Score
            String levelText = levelToText(currentScore);

            Files.write(Paths.get(savePath), levelText.getBytes());
            System.out.println("saved file");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't save file");
        }
    }

    public void saveBestScore() {
        try {
            String filename = "levels/levels.lvl";
            ArrayList<String> lines;
            lines = (ArrayList<String>) Files.readAllLines(Paths.get(filename));
            int lineIndex = -1;
            int levelId = getId();

            //find line with ID
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.startsWith("@" + levelId + ",")) {
                    lineIndex = i;
                }
            }

            //remove the old lines
            lines.remove(lineIndex);

            //add new line with updated best score
            String[] newLines = levelToText(bestScore).split("\n");
            lines.add(lineIndex, newLines[0]);

            //save to levels.lvl
            Files.write(Paths.get(filename), lines);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't save file");
        }
    }

    // Create a Level from a Save File
    public static Level loadFromFile(int slot) {
        int saveFileId = slot + 1;
        String filename = "saves/save" + saveFileId + ".lvl";
        try {
            File file = new File(filename);

            // Create the Save file if it does not exist
            file.createNewFile();

            Scanner scanner = new Scanner(file);
            // Create Level is there is info in the Save file.
            if(scanner.hasNext()){
                Level level = new Level(scanner);
                // Level reads score from file as BestScore. Change to currentScore for Save files
                level.currentScore = level.bestScore;
                level.setIsSaved();
                scanner.close();
                return level;
            }
            else{
                System.out.println("Couldn't load file");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("couldn't load level");
            return null;
        }
    }

    // Set the state of the current Level to match the Saved level
    public void resumeFromSave(Level savedLevel) {
        restart();
        tubeList.clear();
        currentScore = savedLevel.currentScore;
        for(Tube tube : savedLevel.tubeList){
            tubeList.add(tube.clone());
        }
        // Calculate Tube and Block sizes and positions for drawing
        assignTubeShape();

    }

    // Return a copy of this Level and all of its Tubes by value
    @Override
    public Level clone(){
        Level level = new Level(getId());
        level.currentScore = currentScore;
        level.bestScore = bestScore;
        for(Tube tube : tubeList){
            level.tubeList.add(tube.clone());
        }
        return level;
    }
}
