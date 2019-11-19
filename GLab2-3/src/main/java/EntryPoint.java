public class EntryPoint {
    public static void main(String[] args) {
        View view = new View(800, 600, "Graphics");
        Bitmap target = view.getFrameBuffer();

        StarFX stars = new StarFX(4096, 64.0f, 20.0f);

        long prevTime = System.nanoTime();
        while(true)
        {
            long currTime = System.nanoTime();
            float delta = (float)((currTime - prevTime) / 1000000000.0);
            stars.updateAndRender(target, delta);

            prevTime = currTime;
            view.swapBuffers();
        }
    }
}
