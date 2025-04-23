import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class SolveLevel {
    public ArrayList <String> searchHistory = new ArrayList<>();
    private int maxSearchDepth = 10000;
    private ArrayList<Point> moveList;
    private long TIMELIMIT = 5000L;

    public SolveLevel(Level level) {
        moveList = new ArrayList<>();
        ArrayList<Tube> tubeList = level.getTubeList();
        Tube [] tubeArray = new Tube[tubeList.size()];
        for(int i = 0; i < tubeArray.length; i++){
            tubeArray[i] = tubeList.get(i);
        }
        String solved = "Solved: "+solve(tubeArray, 0, 0, 0, System.currentTimeMillis())+" "+moveList.size()+" moves.";
        System.out.println(solved);
        for(int i = moveList.size() - 1; i >= 0; i--){
            System.out.println((moveList.size() - i)+" "+moveList.get(i));
        }

    }

    public boolean solve(Tube [] tubeArray, int from, int to, int depth, long startTime) {
        if(isWinningState(tubeArray)){
            return true;
        }

        if(depth > maxSearchDepth){
            return false;
        }

        if(System.currentTimeMillis() - startTime > TIMELIMIT){
            return false;
        }

        for(int i = 0; i < tubeArray.length; i++){
            for(int j = 0; j < tubeArray.length; j++){
                if(isValidMove(tubeArray, i, j)){
                    //System.out.println("depth "+depth+": " + i + " " + j);
                    //print(tubeArray);
                    Tube [] newState = moveBlock(tubeArray,i, j);
                    if(isNewState(newState)) {
                        addToHistory(newState);
                        if(solve(newState, 0, 0, depth+1, startTime)) {
                            moveList.add(new Point(i,j));
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public Tube[] moveBlock(Tube [] tubeArray, int from, int to){
        Tube [] newTubeArray = new Tube[tubeArray.length];
        for(int i = 0; i < tubeArray.length; i++){
            newTubeArray[i] = new Tube(tubeArray[i]);
        }
        while(isValidMove(newTubeArray, from, to)){
            newTubeArray[to].addBlock(newTubeArray[from].removeTopBlock());
        }

        return newTubeArray;
    }

    public boolean isValidMove(Tube [] tubeArray, int from, int to){
        // Cannot move from the same tube to the same tube
        if(from == to){
            return false;
        }
        // Cannot remove from an empty tube or add to a full tube
        if (tubeArray[from].isEmpty() || tubeArray[to].isFull()){
            return false;
        }

        // Check if enough space to fit all the blocks of the same color
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

    public String convertToString(Tube [] tubeArray){
        String result = "";
        ArrayList<String> strings = new ArrayList<>();
        for(int i = 0; i < tubeArray.length; i++){
            strings.add(tubeArray[i].toString());
        }
        Collections.sort(strings);
        for(int i = 0; i < strings.size(); i++){
            result += strings.get(i);
        }
        return result;
    }

    public boolean isNewState(Tube [] tubeArray){
        String state = convertToString(tubeArray);
        for(String s : searchHistory){
            if(s.equals(state)){
                return false;
            }
        }
        return true;
    }

    public void addToHistory(Tube [] tubeArray){
        searchHistory.add(convertToString(tubeArray));
    }

    public void setMaxSearchDepth(int maxDepth){
        maxSearchDepth = maxDepth;
    }

    public void printMoves(){
        for(int i = 0; i < moveList.size(); i++){
            System.out.println(i+": "+moveList.get(i).x+" "+moveList.get(i).y);
        }
    }

    public ArrayList<Point> getMoveList(){
        return moveList;
    }

    public Point getNextMove(){
        if(moveList.isEmpty()){
            return null;
        }
        return moveList.getLast();
    }
}
