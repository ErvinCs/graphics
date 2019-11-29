import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.JFrame;

// View Window
public class View extends Canvas{
    private final JFrame frame;                     // Window
    private final RenderContext frameBuffer;        // Holds the bitmap to be displayed
    private final byte[] displayComponents;         // The view components of the buffered image
    private final BufferedImage displayImage;       // Used to display the bitmap of frameBuffer:RenderContext in the window
    private final BufferStrategy bufferStrategy;    // Defines how memory is organized on the Window
    private final Graphics graphics;                // Draws into the canvas

    public View(int width, int height, String title)
    {
        // Configure Camera
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        // Create the image
        frameBuffer = new RenderContext(width, height);
        displayImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        displayComponents = ((DataBufferByte)(displayImage.getRaster().getDataBuffer())).getData();

        frameBuffer.clear((byte)0x00);

        // Configure the display JFrame
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

        // Create 1 buffer to draw in
        createBufferStrategy(1);
        // Used to access the buffer
        bufferStrategy = getBufferStrategy();
        // The graphics object to be drawn into
        graphics = bufferStrategy.getDrawGraphics();
    }

    public void swapBuffers()
    {
        // Copy the bitmap framebuffer into the byte array of the display image
        frameBuffer.copyToByteArray(displayComponents);
        graphics.drawImage(displayImage, 0, 0, frameBuffer.getWidth(), frameBuffer.getHeight(), null);
        bufferStrategy.show();
    }

    public RenderContext getFrameBuffer() {
        return frameBuffer;
    }
}
