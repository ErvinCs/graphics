public class EntryPoint {
    public static void main(String[] args) {
        View view = new View(800, 600, "Graphics");
        RenderContext target = view.getFrameBuffer();

        StarFX stars = new StarFX(4096, 64.0f, 20.0f);
        StarFX triangles = new StarFX(3, 64.0f, 4.0f);

        Vertex minYVert = new Vertex(100, 100);
        Vertex midYVert = new Vertex(150, 200);
        Vertex maxYVert = new Vertex(80, 300);

        long prevTime = System.nanoTime();
        while(true)
        {
            long currTime = System.nanoTime();
            float delta = (float)((currTime - prevTime) / 1000000000.0);
            prevTime = currTime;

            //target.fillTriangle(minYVert, midYVert, maxYVert);
            //stars.updateAndRenderParticles(target, delta);
            triangles.updateAndRenderGenTriangles(target, delta);


            view.swapBuffers();
        }
    }
}
