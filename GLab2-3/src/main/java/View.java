import java.awt.*;
import javax.swing.JFrame;

// View Window
public class View extends Canvas{
    private JFrame frame;

    public View(int width, int height, String title)
    {
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        frame = new JFrame();
        // Add this Canvas to the frame
        frame.add(this);
        // Resize the frame to the canvas size
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(title);
        // Set the frame in the center of the screen
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}
