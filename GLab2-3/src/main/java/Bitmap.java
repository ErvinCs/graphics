import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Bitmap {
    private int width;
    private int height;
    // Holds ABGR values at multiples of 4
    private byte components[];

    // Create an empty bitmap
    public Bitmap(int width, int height)
    {
        this.width = width;
        this.height = height;
        components = new byte[width * height * 4];
    }

    // Read the bitmap at fileName
    public Bitmap(String fileName) throws IOException
    {
        int width = 0;
        int height = 0;
        byte[] components = null;

        // Read the bitmap file
        BufferedImage image = ImageIO.read(new File(fileName));

        width = image.getWidth();
        height = image.getHeight();

        int[] imgPixels = new int[width * height];
        image.getRGB(0, 0, width, height, imgPixels, 0, width);
        components = new byte[width * height * 4];

        // Set component values
        for(int i = 0; i < width * height; i++)
        {
            int pixel = imgPixels[i];

            // Each pixel is stored as an int (even though it's byte is the only relevant part)
            // Fill component with each byte for ABGR
            components[i * 4]     = (byte)((pixel >> 24) & 0xFF); // A
            components[i * 4 + 1] = (byte)((pixel      ) & 0xFF); // B
            components[i * 4 + 2] = (byte)((pixel >> 8 ) & 0xFF); // G
            components[i * 4 + 3] = (byte)((pixel >> 16) & 0xFF); // R
        }

        this.width = width;
        this.height = height;
        this.components = components;
    }

    // Clear the entire bitmap to shade
    public void clear(byte shade)
    {
        Arrays.fill(components, shade);
    }

    // Set 1 pixel to an ABGR format value
    public void drawPixel(int x, int y, byte a, byte r, byte g, byte b)
    {
        // Move to the pixel in the array
        int index = (x + y * width) * 4;

        components[index]     = a;
        components[index + 1] = b;
        components[index + 2] = g;
        components[index + 3] = r;
    }

    // Copy a pixel(component) from src:Bitmap into this
    //  Dest pixel from (destX, destY)
    //  Pixel in src at (srcX, srcY)
    public void CopyPixel(int destX, int destY, int srcX, int srcY, Bitmap src)
    {
        int destIndex = (destX + destY * width) * 4;
        int srcIndex = (srcX + srcY * src.getWidth()) * 4;

        components[destIndex    ] = src.getComponent(srcIndex);
        components[destIndex + 1] = src.getComponent(srcIndex + 1);
        components[destIndex + 2] = src.getComponent(srcIndex + 2);
        components[destIndex + 3] = src.getComponent(srcIndex + 3);
    }

    // Copies the component values into dest:byte[]
    public void copyToByteArray(byte[] dest)
    {
        for(int i = 0; i < width * height; i++)
        {
            // No A(alpha) value in dest
            dest[i * 3]     = components[i * 4 + 1];
            dest[i * 3 + 1] = components[i * 4 + 2];
            dest[i * 3 + 2] = components[i * 4 + 3];
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public byte getComponent(int index) { return components[index]; }
}
