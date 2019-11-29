import java.io.IOException;

public class EntryPoint {
    public static void main(String[] args) throws IOException {
        View view = new View(800, 600, "Graphics");
        RenderContext target = view.getFrameBuffer();

        Bitmap texture = new Bitmap("./res/carpet_tex.jpg");
        Mesh mesh = new Mesh("./res/monkey_obj.obj");

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
            // (S)QT Transformations
            Matrix4f translation = new Matrix4f().initTranslation(0.0f, 0.0f, 3.0f);
            Matrix4f rotation = new Matrix4f().initRotation(rotationCounter, 0.0f, rotationCounter);
            Matrix4f transform = projection.mul(translation.mul(rotation));

            byte zero = 0x00;
            target.clear(zero);
            target.clearDepthBuffer();
            target.DrawMesh(mesh, transform, texture);

            view.swapBuffers();
        }
    }
}
