package application;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderer.*;
import models.Model3D;
import shaders.Shader;
import shaders.StaticShader;
import textures.Texture;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static void main(String[] args) {
        DisplayManager.createDisplay();

        ModelLoader loader = new ModelLoader();

        Model3D model3D = OBJLoader.loadObjModel("sphere_obj.obj", loader);
        Texture texture = new Texture(loader.loadTexture("flag.png"));
        TexturedModel modelTex = new TexturedModel(model3D, texture);
        texture.setShineDamp(10.0f);
        texture.setReflectivity(1f);

        Entity entity1 = new Entity(modelTex, new Vector3f(0, 0, -10), new Vector3f(0,0,0),1);
        Entity entity2 = new Entity(modelTex, new Vector3f(10, 10, -30), new Vector3f(0.5f,0.5f,0.5f),1);
        List<Entity> entities = new ArrayList<>();
        entities.add(entity1);
        entities.add(entity2);

        Light light = new Light(new Vector3f(0, 0, -5), new Vector3f(1, 1, 1));
        Camera camera = new Camera();
        RenderManager renderManager = new RenderManager();

        while(!Display.isCloseRequested()) {
           camera.move();

           for(Entity e : entities) {
                renderManager.addEntity(e);
           }
            renderManager.draw(light, camera);
            DisplayManager.updateDisplay();
        }

        renderManager.end();
        loader.delete();
        DisplayManager.closeDisplay();
    }
}
