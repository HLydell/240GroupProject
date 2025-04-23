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

    public Level(Scanner in){
        this.id = id;
        bestScore = 0;
        currentScore = 0;

        tubeList = new ArrayList<>();
        moveList = new ArrayList<>();

        // Null Layout allows Components like Buttons to be drawn anywhere.
        setLayout(null);

        String header = in.nextLine();
        header = header.replaceAll("@","");
        String[] tokens = header.split(",");
        id = Integer.parseInt(tokens[0]);
        bestScore = Integer.parseInt(tokens[1]);

        while(in.hasNextLine()){
            String line = in.nextLine();
            if(line.isEmpty()){
                break;
            }
            Tube tube = new Tube(line.length());
            for(int i = 0; i < line.length(); i++){
                if(line.charAt(i) == '-'){
                    break;
                }
                tube.addBlock(new Block(line.charAt(i)));
            }
            tubeList.add(tube);
        }

        int windowWidth = 800;
        int windowHeight = 600;

        int deltaX = windowWidth/(tubeList.size()*2 +1);

        for(int i = 0; i<tubeList.size(); i++){
            int tubeX = ((i*2)+1)*deltaX;
            int tubeY = 200;
            int tubeWidth = deltaX;
            int tubeHeight = windowHeight/2;
            tubeList.get(i).setBounds( tubeX, tubeY, tubeWidth, tubeHeight);
        }
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
        g.drawString("Best Score:  "+ bestScore, 350, 55);
        g.drawString("Current Score:  "+ currentScore, 340, 72);

        for(int i = 0; i<tubeList.size(); i++){
            Tube tube = tubeList.get(i);
            tube.paintComponent(g);
            g.setColor(Color.black);
            g.drawRect(tube.getX(), tube.getY(), tube.getWidth(), tube.getHeight());
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
                //PLACEHOLDER CODE: Add code to restart the current level
                System.out.println("(PLACEHOLDER CODE)Event Triggered: "+event);
                break;
            case EventType.LEVEL_UNDO:
                //PLACEHOLDER CODE: Add code to undo the last move
                System.out.println("(PLACEHOLDER CODE)Event Triggered: "+event);
                undo();
                break;
            case EventType.LEVEL_HINT:
                //PLACEHOLDER CODE: Add code to get a hint or the next move toward the solution
                System.out.println("(PLACEHOLDER CODE)Event Triggered: "+event);
                getHint();
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

    public void moveBlock(Tube start, Tube end, boolean undo){
        int endSpace = end.getEmptySpace();
        Color startColor = start.viewTopBlock().getColor();
        if(!end.isEmpty() && !startColor.equals(end.viewTopBlock().getColor())){
            return;
        }
        if (endSpace >= start.getTopColorSize()){ //if the empty space is sufficient
            int top = start.getFillAmt() - 1;
            while (top >= 0 && start.viewTopBlock().getColor().equals(startColor)){
                Block temp = start.removeTopBlock(); //moves block over
                end.addBlock(temp);
                top -= 1;
            }
            if (!undo){
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
            Tube start = moveList.get(moveList.size() - 1); //get last item (which is the end of the last move
            Tube end = moveList.get(moveList.size() - 2); //start of last move
            moveBlock(start, end, true); //so they don't get re-added
            //score not decreased, user punished for undo
            moveList.remove(moveList.size() - 1);
            moveList.remove(moveList.size() - 1);
        } else {
            System.out.println("There is no more moves to undo");
        }
    }

    public void getHint(){
        SolveLevel solve = new SolveLevel(this);
        Point move = solve.getNextMove();
        if(move != null){
            moveBlock(tubeList.get(move.x), tubeList.get(move.y), false);
        }
        else{
            JOptionPane.showMessageDialog(null, "No Solution Found", "Hint", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isSolved(){
        for (int i = 0; i < tubeList.size(); i++){
            if (!tubeList.get(i).isTubeSolved()){
                return false;
            }
        }
        return true;
    }

    public void mouseReleased(MouseEvent e){
        for(Tube tube: tubeList){
            if(tube.contains(e.getX(), e.getY())){
                for(Tube t: tubeList){
                    if(t.equals(tube)){
                        continue;
                    }

                    if(t.isSelected()){
                        moveBlock(t, tube, false);
                        t.setSelected(false);
                        tube.setSelected(false);
                        return;
                    }
                }
                // No other tubes selected
                if(!tube.isEmpty() ){ // Tube is empty
                    tube.mouseClicked();
                    return;
                }
            }
        }

        for(Tube tube: tubeList){
            tube.setSelected(false);
        }
    }

    public ArrayList<Tube> getTubeList(){
        return tubeList;
    }
}
