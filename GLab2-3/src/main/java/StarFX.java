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
        // Normalized resolution: -1/x - left of the screen,  1/x - right of the screen
        starX[index] = 2 * ((float)Math.random() - 0.05f) * spread;
        starY[index] = 2 * ((float)Math.random() - 0.05f) * spread;
        starZ[index] = ((float)Math.random() + 0.00001f) * spread;

    }

    // Draw the star field
    // Update position of each star & draw it to target
    public void updateAndRenderParticles(Bitmap target, float deltaRender)
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
                byte colorByte =  (byte)(0xFF);
                target.drawPixel(x, y, colorByte, colorByte, colorByte, colorByte);
            }
        }
    }

    // Triangle Generator
    public void updateAndRenderGenTriangles(RenderContext target, float deltaRender)
    {
        final float tanHalfFOV = (float)Math.tan(Math.toRadians(90.0/2.0));
        //Stars are drawn on a black background
        target.clear((byte)0x00);

        float halfWidth  = target.getWidth()/2.0f;
        float halfHeight = target.getHeight()/2.0f;
        int triangleBuilderCounter = 0;

        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        for(int i = 0; i < starX.length; i++)
        {
            starZ[i] -= deltaRender * speed;

            //If star is at or behind the camera, generate a new position for it.
            if(starZ[i] <= 0) {
                initStar(i);
            }

            int x = (int)((starX[i]/(starZ[i] * tanHalfFOV)) * halfWidth + halfWidth);
            int y = (int)((starY[i]/(starZ[i] * tanHalfFOV)) * halfHeight + halfHeight);

            // If the star is not within range of the screen, then generate a new position for it.
            if(x < 0 || x >= target.getWidth() || y < 0 || y >= target.getHeight()) {
                initStar(i);
                continue;
            }

            triangleBuilderCounter++;
            if(triangleBuilderCounter == 1)
            {
                x1 = x;
                y1 = y;
            }
            else if(triangleBuilderCounter == 2)
            {
                x2 = x;
                y2 = y;
            }
            else if(triangleBuilderCounter == 3)
            {
                triangleBuilderCounter = 0;
                Vertex v1 = new Vertex(x1, y1);
                Vertex v2 = new Vertex(x2, y2);
                Vertex v3 = new Vertex(x, y);

                target.fillTriangle(v1, v2, v3);
            }
        }
    }
}
