import java.awt.*;

public class Block {
    private Color color;
    private char id; // Single character used to identify Block in files

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
            case 'A' -> Color.red;
            case 'B' -> Color.green;
            case 'C' -> Color.blue;
            case 'D' -> Color.yellow;
            case 'E' -> Color.orange;
            case 'F' -> Color.pink;
            case 'G' -> Color.cyan;
            case 'H' -> Color.magenta;
            case 'I' -> Color.darkGray;
            default -> Color.black;
        };
    }

    // Convert a Color into a corresponding letter. Limit 10
    private char convertColorToChar(Color c){
        if(c.equals(Color.red)){
            return 'A';
        }
        else if(c.equals(Color.green)){
            return 'B';
        }
        else if(c.equals(Color.blue)){
            return 'C';
        }
        else if(c.equals(Color.yellow)){
            return 'D';
        }
        else if(c.equals(Color.orange)){
            return 'E';
        }
        else if(c.equals(Color.pink)){
            return 'F';
        }
        else if(c.equals(Color.cyan)){
            return 'G';
        }
        else if(c.equals(Color.magenta)){
            return 'H';
        }
        else if(c.equals(Color.darkGray)){
            return 'I';
        }
        else {
            return 'J';
        }
    }

    public void draw (Graphics g) {

    }

    // Printing out this block will just return the char used to ID this block
    @Override
    public String toString(){
        return ""+id;
    }
}
