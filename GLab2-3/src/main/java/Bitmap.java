import java.util.Arrays;

public class Bitmap {
    private int width;
    private int height;
    // Holds ABGR values at multiples of 4
    private byte components[];

    public Bitmap(int width, int height)
    {
        this.width = width;
        this.height = height;
        components = new byte[width * height * 4];
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

//    // Java represents pixels in an integer array. Every integer represents a pixel.
//    public void copyToIntArray(int[] dest)
//    {
//        for(int i = 0; i < width * height; i++)
//        {
//            // Place each ARGB component in different bytes
//            int a = (int)components[i * 4] << 24;
//            int r = (int)components[i * 4 + 1] << 16;
//            int g = (int)components[i * 4 + 2] << 8;
//            int b = (int)components[i * 4 + 3];
//
//            dest[i] = a | r | g | b;
//        }
//    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
