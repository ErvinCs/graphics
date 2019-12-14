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
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import textures.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    public static void main(String[] args) {
        DisplayManager.createDisplay();
        ModelLoader loader = new ModelLoader();

        // OBJ Models
        Model3D treeModel3D   = OBJLoader.loadObjModel("tree.obj", loader);
        Model3D grassModel3D  = OBJLoader.loadObjModel("grassModel.obj", loader);
        Model3D fernModel3D   = OBJLoader.loadObjModel("fern.obj", loader);
        Model3D dragonModel3D = OBJLoader.loadObjModel("dragon.obj", loader);
        Model3D bunnyModel3D  = OBJLoader.loadObjModel("bunny.obj", loader);

        // Textures
        TerrainTexture backgroundTex = new TerrainTexture(loader.loadTexture("grassy.png"));
        TerrainTexture rTex = new TerrainTexture(loader.loadTexture("mud.png"));
        TerrainTexture gTex = new TerrainTexture(loader.loadTexture("grassFlowers.png"));
        TerrainTexture bTex = new TerrainTexture(loader.loadTexture("path.png"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTex, rTex, gTex, bTex);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap.png"));

        // Textured Models
        TexturedModel dragonModel = new TexturedModel(dragonModel3D, new Texture(loader.loadTexture("circuits.png")));
        TexturedModel bunnyModel  = new TexturedModel(bunnyModel3D, new Texture(loader.loadTexture("circuits.png")));
        TexturedModel treeModel   = new TexturedModel(treeModel3D, new Texture(loader.loadTexture("tree.png")));
        TexturedModel grassModel  = new TexturedModel(grassModel3D, new Texture(loader.loadTexture("grassTexture.png")));
        grassModel.getTexture().setTransparent(true);
        grassModel.getTexture().setUseSimulatedLight(true);
        TexturedModel flowerModel = new TexturedModel(grassModel3D, new Texture(loader.loadTexture("flower.png")));
        flowerModel.getTexture().setTransparent(true);
        flowerModel.getTexture().setUseSimulatedLight(true);
        TexturedModel fernModel   = new TexturedModel(fernModel3D, new Texture(loader.loadTexture("fern.png")));
        fernModel.getTexture().setTransparent(true);
        fernModel.getTexture().setUseSimulatedLight(true);

        final int mapSize = 1;
        final int xGenLimit = (int)Terrain.SIZE * mapSize;
        final int zGenLimit = (int)Terrain.SIZE * mapSize;
        List<Entity> entityList = new ArrayList<>();
        Random rand = new Random();
        for(int i = 0; i < 300 * mapSize; i++) {
            entityList.add(new Entity(treeModel, new Vector3f(rand.nextFloat() * xGenLimit, 0, rand.nextFloat() * zGenLimit),
                    new Vector3f(0, 0, 0), 3));
            entityList.add(new Entity(grassModel, new Vector3f(rand.nextFloat() * xGenLimit, 0, rand.nextFloat() * zGenLimit),
                    new Vector3f(0, 0, 0), 1));
            entityList.add(new Entity(flowerModel, new Vector3f(rand.nextFloat() * xGenLimit, 0, rand.nextFloat() * zGenLimit),
                    new Vector3f(0, 0, 0), 0.5f));
            entityList.add(new Entity(fernModel, new Vector3f(rand.nextFloat() * xGenLimit, 0, rand.nextFloat() * zGenLimit),
                    new Vector3f(0, 0, 0), 0.8f));
        }
        entityList.add(new Entity(dragonModel, new Vector3f(200, 200, 200), new Vector3f(0, 0, 0), 10));
        entityList.add(new Entity(bunnyModel, new Vector3f(xGenLimit - 200, 200, zGenLimit - 200), new Vector3f(0, 0, 0), 10));

        Light sun = new Light(new Vector3f(6000, 4000, 2000), new Vector3f(1f, 1f, 1f));

        List<Terrain> terrainList = new ArrayList<>();
        for(int i = 0; i < mapSize; i++) {
            for(int j = 0; j < mapSize; j++) {
                terrainList.add(new Terrain(i, j, loader, texturePack, blendMap));
            }
        }

        Camera camera = new Camera();
        System.out.print(camera.toString());
        RenderManager renderManager = new RenderManager();

        while(!Display.isCloseRequested()) {
            camera.move();

            for(Entity e : entityList) {
                renderManager.addEntity(e);
            }

            for(Terrain t : terrainList) {
                renderManager.addTerrain(t);
            }

            renderManager.draw(sun, camera);
            DisplayManager.updateDisplay();
        }

        renderManager.end();
        loader.delete();
        DisplayManager.closeDisplay();
    }
}
