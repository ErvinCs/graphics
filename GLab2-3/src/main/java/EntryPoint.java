public class EntryPoint {
    public static void main(String[] args) {
        View view = new View(800, 600, "Graphics");
        RenderContext target = view.getFrameBuffer();

        Vector4f r = new Vector4f(1.0f, 0.0f, 0.0f, 0.0f);
        Vector4f g = new Vector4f(0.0f, 1.0f, 0.0f, 0.0f);
        Vector4f b = new Vector4f(0.0f, 0.0f, 1.0f, 0.0f);

        Vertex minYVert = new Vertex(new Vector4f(-1, -1, 0, 1), r);
        Vertex midYVert = new Vertex(new Vector4f(0, 1, 0, 1), g);
        Vertex maxYVert = new Vertex(new Vector4f(1, -1, 0, 1), b);
        Matrix4f projection = new Matrix4f().initPerspective((float)Math.toRadians(70.0f),
                (float)target.getWidth()/(float)target.getHeight(), 0.1f, 1000.0f);

        float rotationCounter = 0.0f;
        long prevTime = System.nanoTime();
        while(true)
        {
            long currTime = System.nanoTime();
            float delta = (float)((currTime - prevTime) / 1000000000.0);
            prevTime = currTime;

            rotationCounter += delta;
            // MVP - matrix
            Matrix4f translation = new Matrix4f().initTranslation(0.0f, 0.0f, 3.0f);
            Matrix4f rotation = new Matrix4f().initRotation(0.0f, rotationCounter, 0.0f);
            Matrix4f transform = projection.mul(translation.mul(rotation));

            byte zero = 0x00;
            target.clear(zero);
            target.fillTriangle(maxYVert.transform(transform), midYVert.transform(transform), minYVert.transform(transform));


            view.swapBuffers();
        }
    }
}
