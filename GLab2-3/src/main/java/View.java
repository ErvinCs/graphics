import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.JFrame;

// View Window
public class View extends Canvas{
    private final JFrame frame;
    private final Bitmap frameBuffer;
    private final byte[] displayComponents; // The view components of the buffer image
    private final BufferedImage displayImage;
    private final BufferStrategy bufferStrategy;
    private final Graphics graphics;    // Draws into the canvas

    public View(int width, int height, String title)
    {
        // Configure Camera
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        frameBuffer = new Bitmap(width, height);
        displayImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        displayComponents = ((DataBufferByte)(displayImage.getRaster().getDataBuffer())).getData();

        frameBuffer.clear((byte)0x00);

        // Configure display JFrame
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

        // Create buffers to draw in
        createBufferStrategy(1);
        bufferStrategy = getBufferStrategy();
        graphics = bufferStrategy.getDrawGraphics();
    }

    public void swapBuffers()
    {
        // Copy the bitmap into the byte array of the display image
        frameBuffer.copyToByteArray(displayComponents);
        graphics.drawImage(displayImage, 0, 0, frameBuffer.getWidth(), frameBuffer.getHeight(), null);
        bufferStrategy.show();
    }

    public Bitmap getFrameBuffer() {
        return frameBuffer;
    }
}
