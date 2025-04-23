import java.awt.*;
import java.util.*;
public class Tube {
    private ArrayList<Block> tube = new ArrayList<>();
    private int maxCapacity;
    private Rectangle bounds;

    //init value of 10 if no size is specified
    public Tube(){
        this.maxCapacity = 10;
        bounds = new Rectangle(0, 0, 0, 0);
    }

    //allows for defining size
    public Tube(int maxCapacity){
        this.maxCapacity = maxCapacity;
        bounds = new Rectangle(0, 0, 0, 0);
    }

    public Tube(Tube copyTube){
        this.maxCapacity = copyTube.maxCapacity;
        bounds = new Rectangle(copyTube.bounds.x, copyTube.bounds.y, copyTube.bounds.width, copyTube.bounds.height);
        for(Block block : copyTube.tube){
            tube.add(new Block(block));
        }
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
        while (top >= 0 && tube.get(top).getColor().equals(topColor)){
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
        if(tube.isEmpty()){
            return true;
        }
        Color topColor = viewTopBlock().getColor();
        for(int i = 0; i < tube.size(); i++){ //starting at bottom since its more likely to have differences here
            if (!(topColor.equals(tube.get(i).getColor()))){ //stops if top colour is different from loop colour
                return false;
            }
        }
        return true;
    }

    public void setBounds(int x, int y, int width, int height){
        bounds.setBounds(x, y, width, height);
    }

    public int getX(){
        return bounds.x;
    }
    public int getY(){
        return bounds.y;
    }

    public int getWidth(){
        return bounds.width;
    }

    public int getHeight(){
        return bounds.height;
    }

    public boolean contains(int x, int y){
        return bounds.contains(x,y);
    }

    public void draw(Graphics g, int x, int y, int width, int height){
        int blockWidth = width;
        int blockHeight = height/maxCapacity;
        int blockX = x;

        for(int i = 0; i < tube.size(); i++){
            int blockY = y + height - ((i+1)*blockHeight);
            g.setColor(tube.get(i).getColor());
            g.fillRect(blockX, blockY, blockWidth, blockHeight);
        }
    }

    @Override
    public String toString() {
        String output = "";
        for(int i = 0; i < maxCapacity; i++) {
            if(i < tube.size()) {
                output += tube.get(i);
            }
            else{
                output += "-";
            }

        }
        return output;
    }
}
