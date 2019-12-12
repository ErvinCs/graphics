package application;

import entities.Camera;
import entities.Entity;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderer.DisplayManager;
import models.Model3D;
import renderer.ModelLoader;
import renderer.OBJLoader;
import renderer.Renderer;
import shaders.Shader;
import shaders.StaticShader;
import textures.Texture;

public class Game {
    public static void main(String[] args) {
        DisplayManager.createDisplay();


        ModelLoader loader = new ModelLoader();
        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);

        Model3D model3D = OBJLoader.loadObjModel("sphere_obj.obj", loader);
        Texture texture = new Texture(loader.loadTexture("flag.png"));
        TexturedModel modelTex = new TexturedModel(model3D, texture);

        Entity entity = new Entity(modelTex, new Vector3f(0, 0, -10), new Vector3f(0,0,0),1);
        Camera camera = new Camera();
        while(!Display.isCloseRequested()) {
            entity.increaseRotation(0.4f,0,0);
            camera.move();
            renderer.clear();
            shader.begin();
            shader.loadViewMatrix(camera);
            renderer.draw(entity, shader);
            shader.end();
            DisplayManager.updateDisplay();
        }

        shader.delete();
        loader.delete();
        DisplayManager.closeDisplay();
    }
}
