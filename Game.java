import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Game extends JComponent implements GameEventListener, MouseListener {

    public Game() {

        // create and set up the window.
        JFrame frame = new JFrame("Block Sort Puzzle!");

        // make the program close when the window closes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add the Game component to the frame
        frame.add(this);

        // display the window.
        frame.setSize(800, 600);
        frame.setVisible(true);

        // add this Game as a MouseListener to this Game's JComponent (The screen we are drawing)
        addMouseListener(this);
    }
    @Override
    // this is where all the drawing to the screen gets done.
    // We can pass this Graphics object to other methods so they can draw too.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // example of drawing the outline of a blue rectangle
        g.setColor(Color.BLUE);
        g.drawRect(25, 50, 150, 100);

        // example of filling in a green solid oval
        g.setColor(Color.GREEN);
        g.fillOval(300, 200, 150, 250);

        // force an update of the screen
        revalidate();
        repaint();

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse Clicked at " + e.getX() + ", " + e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse Pressed at " + e.getX() + ", " + e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse Released at " + e.getX() + ", " + e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
