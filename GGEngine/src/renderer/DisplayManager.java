package renderer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

public class DisplayManager {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 1024;
    private static final int FPSCAP = 80;
    private static final String TITLE = "GG";

    public static void createDisplay() {
        ContextAttribs attributes = new ContextAttribs(3,2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attributes);
            Display.setTitle(TITLE);
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }

        // Set the viewport
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    public static void updateDisplay() {
        Display.sync(FPSCAP);
        Display.update();
    }

    public static void closeDisplay() {
        Display.destroy();
    }
}
