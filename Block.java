import java.awt.Color;

public class Block {
    private Color color;


    public Block(Color color){
        this.color = color;
    }

    //returns color of block
    public Color getColor() {
        return color;
    }

    //just checks if block colours are equal, can change to overriding the object.equals
    public boolean equals(Block block){
        return color.equals(block.color);
    }

}
