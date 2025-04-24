import java.awt.*;

public class Block {
    private Color color;
    private char id;// Single character used to identify Block in files
    private Rectangle shape = new Rectangle();

    public Block(Color color){
        this.color = color;
        this.id = convertColorToChar(color);
    }

    public Block(char id){
        this.color = convertCharToColor(id);
        this.id = id;
    }

    //returns color of block
    public Color getColor() {
        return color;
    }

    //just checks if block colours are equal, can change to overriding the object.equals
    public boolean equals(Block block){
        return color.equals(block.color);
    }

    // Convert a letter into a corresponding Color. Limit 10
    private Color convertCharToColor(char c){
        return switch(c){
            case 'A' -> ColorScheme.BLOCK_A;
            case 'B' -> ColorScheme.BLOCK_B;
            case 'C' -> ColorScheme.BLOCK_C;
            case 'D' -> ColorScheme.BLOCK_D;
            case 'E' -> ColorScheme.BLOCK_E;
            case 'F' -> ColorScheme.BLOCK_F;
            case 'G' -> ColorScheme.BLOCK_G;
            case 'H' -> ColorScheme.BLOCK_H;
            case 'I' -> ColorScheme.BLOCK_I;
            default -> ColorScheme.BLOCK_J;
        };
    }

    // Convert a Color into a corresponding letter. Limit 10
    private char convertColorToChar(Color c){
        if(c.equals(ColorScheme.BLOCK_A)){
            return 'A';
        }
        else if(c.equals(ColorScheme.BLOCK_B)){
            return 'B';
        }
        else if(c.equals(ColorScheme.BLOCK_C)){
            return 'C';
        }
        else if(c.equals(ColorScheme.BLOCK_D)){
            return 'D';
        }
        else if(c.equals(ColorScheme.BLOCK_E)){
            return 'E';
        }
        else if(c.equals(ColorScheme.BLOCK_F)){
            return 'F';
        }
        else if(c.equals(ColorScheme.BLOCK_G)){
            return 'G';
        }
        else if(c.equals(ColorScheme.BLOCK_H)){
            return 'H';
        }
        else if(c.equals(ColorScheme.BLOCK_I)){
            return 'I';
        }
        else {
            return 'J';
        }
    }

    public Rectangle getShape(){
        return shape;
    }

    // Printing out this block will just return the char used to ID this block
    @Override
    public String toString(){
        return ""+ id;
    }

    // return a copy of this Block by value
    @Override
    public Block clone(){
        Block clone = new Block(color);
        clone.id = id;
        clone.shape = new Rectangle(shape);
        return clone;
    }
}
