public class StarFX {
    private final float spread;
    private final float speed;
    private final float starX[];
    private final float starY[];
    private final float starZ[];

    public StarFX(int numStars, float spread, float speed)
    {
        this.spread = spread;
        this.speed = speed;
        starX = new float[numStars];
        starY = new float[numStars];
        starZ = new float[numStars];

        for(int i = 0; i < starX.length; i++)
        {
            initStar(i);
        }
    }

    // Init each star in the array
    private void initStar(int index)
    {
        // Normalized resolution: -1/x - left of the screen,  1/x - right of the scrren
        starX[index] = 2 * ((float)Math.random() - 0.05f) * spread;
        starY[index] = 2 * ((float)Math.random() - 0.05f) * spread;
        starZ[index] = ((float)Math.random() + 0.000001f) * spread;

    }

    // Draw the star field
    // Update position of each star & draw it to target
    public void updateAndRender(Bitmap target, float deltaRender)
    {
        final float tanHalfFOV = (float)(Math.tan(Math.toRadians(70/2.0)));
        target.clear((byte) 0x00);

        float halfWidth = target.getWidth() / 2.0f;
        float halfHeight = target.getHeight() / 2.0f;
        for(int i = 0; i < starX.length; i++)
        {
            // Set star spawn position
            starZ[i] -= deltaRender * speed;
            if(starZ[i] <= 0) {
                initStar(i);
            }

            // Convert to screen space & Draw star
            // Simulate a FOV: 2 * ((x,y) / tan(angle)). For angle = 45, tan(angle) = 1, FOV = 90
            int x = (int)((starX[i] / (starZ[i] * tanHalfFOV) * halfWidth + halfWidth));
            int y = (int)((starY[i] / (starZ[i] * tanHalfFOV) * halfHeight + halfHeight));
            if (x < 0 || x >= target.getWidth() || y < 0 || y >= target.getHeight()) {
                initStar(i);
            } else {
                // Draw a white star
                Byte colorByte =  (byte)(0xFF);
                target.drawPixel(x, y, colorByte, colorByte, colorByte, colorByte);
            }
        }
    }
}
