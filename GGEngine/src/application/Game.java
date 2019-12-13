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
import terrain.Terrain;
import textures.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    public static void main(String[] args) {
        DisplayManager.createDisplay();
        ModelLoader loader = new ModelLoader();

        Model3D treeModel3D   = OBJLoader.loadObjModel("tree.obj", loader);
        Model3D grassModel3D  = OBJLoader.loadObjModel("grassModel.obj", loader);
        Model3D fernModel3D   = OBJLoader.loadObjModel("fern.obj", loader);
        Model3D dragonModel3D = OBJLoader.loadObjModel("dragon.obj", loader);

        TexturedModel treeModel   = new TexturedModel(treeModel3D, new Texture(loader.loadTexture("tree.png")));
        TexturedModel grassModel  = new TexturedModel(grassModel3D, new Texture(loader.loadTexture("grassTexture.png")));
        grassModel.getTexture().setTransparent(true);
        grassModel.getTexture().setUseSimulatedLight(true);
        TexturedModel fernModel   = new TexturedModel(fernModel3D, new Texture(loader.loadTexture("fern.png")));
        fernModel.getTexture().setTransparent(true);
        grassModel.getTexture().setUseSimulatedLight(true);
        TexturedModel dragonModel = new TexturedModel(dragonModel3D, new Texture(loader.loadTexture("circuits.png")));

        List<Entity> entityList = new ArrayList<>();
        Random rand = new Random();
        for(int i = 0; i < 200; i++) {
            entityList.add(new Entity(treeModel, new Vector3f(rand.nextFloat() * 1024, 0, rand.nextFloat() * 800),
                    new Vector3f(0, 0, 0), 3));
            entityList.add(new Entity(grassModel, new Vector3f(rand.nextFloat() * 1024, 0, rand.nextFloat() * 800),
                    new Vector3f(0, 0, 0), 1));
            entityList.add(new Entity(fernModel, new Vector3f(rand.nextFloat() * 1024, 0, rand.nextFloat() * 800),
                    new Vector3f(0, 0, 0), 0.5f));
        }
        entityList.add(new Entity(dragonModel, new Vector3f(400, 200, 400), new Vector3f(0, 0, 0), 10));

        Light light = new Light(new Vector3f(3000, 2000, 1000), new Vector3f(0.9f, 0.9f, 0.9f));
        Texture terrainTex = new Texture(loader.loadTexture("grass.png"));
        List<Terrain> terrainList = new ArrayList<>();
        int maxMapSize = 8;
        terrainList.add(new Terrain(0, 0, loader, terrainTex));
        for(int i = 1; i < maxMapSize; i++) {
            terrainList.add(new Terrain(0, i, loader, terrainTex));
            terrainList.add(new Terrain(i, 0, loader, terrainTex));
            terrainList.add(new Terrain(i, i, loader, terrainTex));
        }


        Camera camera = new Camera();
        RenderManager renderManager = new RenderManager();

        while(!Display.isCloseRequested()) {
            camera.move();

            for(Entity e : entityList) {
                renderManager.addEntity(e);
            }

            for(Terrain t : terrainList) {
                renderManager.addTerrain(t);
            }

            renderManager.draw(light, camera);
            DisplayManager.updateDisplay();
        }

        renderManager.end();
        loader.delete();
        DisplayManager.closeDisplay();
    }
}
