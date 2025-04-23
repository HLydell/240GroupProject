import java.awt.Color;

public class Block {
    private Color color;
    private char id;


    public Block(Color color){
        this.color = color;
        id = convertColorToChar(color);
    }

    public Block(char id){
        this.color = convertCharToColor(id);
        this.id = id;
    }

    public Block(Block block){
        this.color = block.color;
        this.id = block.id;
    }

    //returns color of block
    public Color getColor() {
        return color;
    }

    //just checks if block colours are equal, can change to overriding the object.equals
    public boolean equals(Block block){
        return color.equals(block.color);
    }

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

    @Override
    public String toString(){
        return ""+id;
    }

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
}
