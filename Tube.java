import java.awt.*;
import java.util.*;
public class Tube {
    private ArrayList<Block> tube = new ArrayList<>();
    private int maxCapacity;

    //init value of 10 if no size is specified
    public Tube(){
        this.maxCapacity = 10;
    }

    //allows for defining size
    public Tube(int maxCapacity){
        this.maxCapacity = maxCapacity;
    }

    //adds block to end of tube, assumes it is not going over max capacity
    public void addBlock(Block block){
        tube.add(block);
    }

    //gets the top block (end of arraylist)
    public Block viewTopBlock(){
        return tube.get(tube.size() - 1);
    }

    //gets size of top block (as a collective)
    public int getTopColorSize(){
        Color topColor = viewTopBlock().getColor();
        int count = 0;
        int top = tube.size() - 1;
        while (tube.get(top).getColor().equals(topColor) && top >= 0){
            count += 1;
            top -= 1;
        }
        return count;
    }

    //removes top block
    public Block removeTopBlock(){
        return tube.remove(tube.size() - 1);
    }

    //amount of space left over
    public int getEmptySpace(){
        return maxCapacity - tube.size();
    }

    public ArrayList<Block> getTube(){
        return tube;
    }

    public int getFillAmt(){
        return tube.size();
    }

    //tube is empty if arraylist is empty
    public boolean isEmpty(){
        return tube.isEmpty();
    }

    //when nothing else can fit
    public boolean isFull(){
        return getEmptySpace() == 0;
    }

    //returns true if all colours of block in tube are the same
    public boolean isTubeSolved(){
        Color topColor = viewTopBlock().getColor();
        for(int i = 0; i <= tube.size(); i++){ //starting at bottom since its more likely to have differences here
            if (!(topColor.equals(tube.get(i).getColor()))){ //stops if top colour is different from loop colour
                return false;
            }
        }
        return true;
    }
}
