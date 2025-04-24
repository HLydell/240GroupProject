/*
 * Level represents a single puzzle Level.
 * Levels contain Menu Buttons and Tubes of colored Blocks
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
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
    private ArrayList<Tube> moveList;

    public Level(int id){
        this.id = id;
        bestScore = 0;
        currentScore = 0;

        tubeList = new ArrayList<>(); //hypothetically this allocation would be done as the return of a separate method which reads in from file
        //it could also be within this method, since we would want to assign best score as well.

        // Null Layout allows Components like Buttons to be drawn anywhere.
        setLayout(null);
    }

    // Create Level by reading in lines from a File
    public Level(Scanner in){
        this.id = id;
        currentScore = 0;

        tubeList = new ArrayList<>();
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
        for (int i = 0; i < tubeList.size(); i++){
            Rectangle temp = tubeList.get(i).getShape();
            tubeList.get(i).assignBlockShape();
            for (int j = 0; j < tubeList.get(i).getFillAmt(); j++){
                Rectangle tempBlock = tubeList.get(i).getTube().get(j).getShape();
                g.setColor(tubeList.get(i).getTube().get(j).getColor());
                g.fillRect(tempBlock.x, tempBlock.y, tempBlock.width, tempBlock.height);
            }
            if (tubeList.get(i).isSelected()){
                ((Graphics2D) g).setStroke(new BasicStroke(5));
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

    public void moveBlock(Tube start, Tube end){
        if (start.isEmpty()){
            return;
        }
        int endSpace = end.getEmptySpace();
        Color startColor = start.viewTopBlock().getColor();
        Block tempBlock = end.viewTopBlock();
        Color endColor;
        if (tempBlock == null){
            endColor = startColor;
        } else {
            endColor = end.viewTopBlock().getColor();
        }
        if (endSpace >= start.getTopColorSize() && (startColor.equals(endColor))){ //if the empty space is sufficient
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

    public void undo(){
        if (!moveList.isEmpty()) {
            Tube start = moveList.remove(moveList.size() - 1); //get last item (which is the end of the last move
            Tube end = moveList.remove(moveList.size() - 1); //start of last move
            Block temp = start.removeTopBlock(); //moves block over
            end.addBlock(temp);
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

    public void restart(){
        if (!moveList.isEmpty()) {
            while (!moveList.isEmpty()){
                undo();
            }
        } else {
            System.out.println("There is no more moves to restart");
        }
        currentScore = 0;
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

    public boolean isSolved(){
        for (int i = 0; i < tubeList.size(); i++){
            if (!tubeList.get(i).isTubeSolved()){
                return false;
            }
        }
        if (currentScore < bestScore || bestScore == 0){
            bestScore = currentScore;
        }
        return true;
    }

    public String levelToText () {
        int levelId = getId();
        int bestScore = getBestScore();
        String firstLine = "@" + levelId + ", " + bestScore + "\n";

        String tubes = "";
        for (Tube tube : tubeList) {
            ArrayList<Block> blocks = tube.getTube();
            for (Block block : blocks) {
                tubes = tubes.concat(block.toString());
            }
            tubes = tubes.concat("\n");
        }

        return firstLine + tubes + "\n\n";
    }

    private void assignTubeShape(){
        int numTubes = tubeList.size();
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
                contains = true;
                if (tempTube.isSelected()){
                    tempTube.setSelect(false);
                    return;
                } else {
                    for (int j = 0; j < tubeList.size(); j++){
                        Tube startTube = tubeList.get(j);
                        if (startTube.isSelected()){
                            moveBlock(startTube, tempTube);
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
}
