import java.awt.*;
import java.util.*;
public class Tube {
    private ArrayList<Block> tube = new ArrayList<>();
    private int maxCapacity;
    private Rectangle shape;
    private boolean isSelected;

    //init value of 10 if no size is specified
    public Tube(){
        this.maxCapacity = 10;
    }

    //allows for defining size
    public Tube(int maxCapacity){
        this.maxCapacity = maxCapacity;
        shape = new Rectangle();
        isSelected = false;
    }

    //adds block to end of tube, assumes it is not going over max capacity
    public void addBlock(Block block){
        tube.add(block);
    }

    //gets the top block (end of arraylist)
    public Block viewTopBlock(){
        if (tube.isEmpty()){
            return null;
        }
        return tube.get(tube.size() - 1);

    }

    //gets size of top block (as a collective)
    public int getTopColorSize(){
        Color topColor = viewTopBlock().getColor();
        int count = 0;
        int top = tube.size() - 1;
        while (top >= 0 && tube.get(top).getColor().equals(topColor)){
            top -= 1;
            count += 1;
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

    public Rectangle getShape(){
        return shape;
    }

    public void assignBlockShape(){
        int numBlocks = maxCapacity;
        int sizeBlock = 350/numBlocks; //vertical size of block, 350 size of tube

        int xStart = shape.x;
        int yStart = shape.y + 350;

        for (int i = 0; i < tube.size(); i++){
            yStart -= sizeBlock;
            Rectangle temp = tube.get(i).getShape();
            temp.setBounds(xStart, yStart, shape.width, sizeBlock);
        }
    }

    //returns true if all colours of block in tube are the same
    public boolean isTubeSolved(){
        if (viewTopBlock() == null){ //is empty
            return true;
        }
        if(!isFull()){
            return false;
        }
        Color topColor = viewTopBlock().getColor();
        for(int i = 0; i < tube.size(); i++){ //starting at bottom since its more likely to have differences here
            if (!(topColor.equals(tube.get(i).getColor()))){ //stops if top colour is different from loop colour
                return false;
            }
        }
        return true;
    }

    public boolean isSelected(){
        return isSelected;
    }

    public void changeSelect(){
        isSelected = !isSelected;
    }

    public void setSelect(boolean selected){
        isSelected = selected;
    }

    @Override
    public Tube clone(){
        Tube clone = new Tube(maxCapacity);
        for (Block block : tube){
            clone.addBlock(block.clone());
        }
        return clone;
    }
}
