/*
 * Attempts to solve a Level using Depth-First search.
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class SolveLevel {
    private ArrayList <String> searchHistory; // Record of all game states already checked to avoid repeating
    private ArrayList <Point> moveList; // List of moves to solve the puzzle. Empty if no solution is found
    private boolean solved;
    private int maxSearchDepth = 10000; // Depth allowed to search before stopping
    private long TIMELIMIT = 5000L; // Time in ms allowed to search before stopping

    public SolveLevel(Level level) {
        searchHistory = new ArrayList<>();
        moveList = new ArrayList<>();

        // Convert ArrayList of Tubes from Level into Array
        ArrayList<Tube> tubeList = level.getTubeList();
        Tube [] tubeArray = new Tube[tubeList.size()];
        for(int i = 0; i < tubeArray.length; i++){
            tubeArray[i] = tubeList.get(i);
        }

        // Attempt to solve level recursively. Pass in the current time as the start time
        solved = solve(tubeArray, 0, System.currentTimeMillis());
    }

    // Depth First: See if the game has been won, if not move 1 block and repeat until depth or time limit is reached.
    public boolean solve(Tube [] tubeArray, int depth, long startTime) {
        // If the last move made a winning game return true
        if(isWinningState(tubeArray)){
            return true;
        }

        // If this branch reached the depth limit do not go any farther
        if(depth > maxSearchDepth){
            return false;
        }

        // If time limit is reached stopped searching. This avoids getting stuck in a search.
        if(System.currentTimeMillis() - startTime > TIMELIMIT){
            return false;
        }

        // Try to move 1 block from every tube to every tube
        for(int i = 0; i < tubeArray.length; i++){
            for(int j = 0; j < tubeArray.length; j++){
                if(isValidMove(tubeArray, i, j)){
                    Tube [] newState = moveBlock(tubeArray,i, j);

                    // Check to see if this is a Game State that we have already seen to avoid repeating loops
                    if(isNewState(newState)) {

                        // Add new Game States to the search history
                        addToHistory(newState);

                        // if a solution was found on this branch, save the last move made
                        if(solve(newState, depth+1, startTime)) {
                            moveList.add(new Point(i,j));
                            return true;
                        }
                    }
                }
            }
        }

        // If none of the moves at this depth on this branch lead to a solution return false.
        return false;
    }

    // Copy Tube Array into new Array and move a Block from one Tube to another Tube
    public Tube[] moveBlock(Tube [] tubeArray, int from, int to){
        Tube [] newTubeArray = new Tube[tubeArray.length];
        for(int i = 0; i < tubeArray.length; i++){
            newTubeArray[i] = tubeArray[i].clone();
        }
        // Move all blocks of the same type
        while(isValidMove(newTubeArray, from, to)){
            newTubeArray[to].addBlock(newTubeArray[from].removeTopBlock());
        }

        return newTubeArray;
    }

    // Check to see if moving a Block from one Tube to another Tube is allowed and is good
    public boolean isValidMove(Tube [] tubeArray, int from, int to){
        // Cannot move from the same tube to the same tube
        if(from == to){
            return false;
        }

        // Cannot remove from an empty tube or add to a full tube
        if (tubeArray[from].isEmpty() || tubeArray[to].isFull()){
            return false;
        }

        // All blocks of same color must move together
        // Check if enough space to fit all the blocks of the same color in the new Tube
        int endSpace = tubeArray[to].getEmptySpace();
        int topColorSize = tubeArray[from].getTopColorSize();
        if (endSpace < topColorSize){
            return false;
        }

        // Do not move block to empty tube if there is only 1 type in the current tube
        if(tubeArray[to].isEmpty()) {
            if(topColorSize == tubeArray[from].getFillAmt()){
                return false;
            }
            else{
                return true;
            }
        }

        // Can only move a block onto the same type
        if(tubeArray[from].viewTopBlock().equals(tubeArray[to].viewTopBlock())){
            return true;
        }
        return false;
    }

    // Check if All Blocks have been sorted into separate Tubes
    public boolean isWinningState(Tube [] tubeArray){
        for(int i = 0; i < tubeArray.length; i++){
            // Each tube must contain only 1 type of block to win
            if(!tubeArray[i].isTubeSolved()){
                return false;
            }
            // All tubes must be full or empty to win
            if(!tubeArray[i].isFull() && !tubeArray[i].isEmpty()){
                return false;
            }
        }
        return true;
    }

    // Convert the current state of the game into a single String
    public String convertToString(Tube [] tubeArray){
        String result = "";
        // Convert each Tube into a String
        ArrayList<String> strings = new ArrayList<>();
        for(int i = 0; i < tubeArray.length; i++){
            strings.add(tubeArray[i].toString());
        }
        // Sort Tube Strings alphabetically to disregard Tube order when comparing game states
        Collections.sort(strings);
        // Combine Tube Strings into single String
        for(int i = 0; i < strings.size(); i++){
            result += strings.get(i);
        }
        return result;
    }

    // Check if this state of the game has been seen already by converting to a String and searching the history
    public boolean isNewState(Tube [] tubeArray){
        String state = convertToString(tubeArray);
        for(String s : searchHistory){
            if(s.equals(state)){
                return false;
            }
        }
        return true;
    }

    // Convert a game state to a String and add it to the History of seen game states
    public void addToHistory(Tube [] tubeArray){
        searchHistory.add(convertToString(tubeArray));
    }

    // get the next move toward the solution. Return null if no solution has been found
    public Point getNextMove(){
        if(moveList.isEmpty()){
            return null;
        }
        return moveList.getLast();
    }
}
