package application;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.Sys;
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
        Model3D dragonModel3D = OBJLoader.loadObjModel("dragon.obj", loader);
        Model3D bunnyModel3D  = OBJLoader.loadObjModel("bunny.obj", loader);
        Model3D lampModel3D   = OBJLoader.loadObjModel("lamp.obj", loader);

        // Textures
        TerrainTexture backgroundTex = new TerrainTexture(loader.loadTexture("grassy.png"));
        TerrainTexture rTex = new TerrainTexture(loader.loadTexture("mud.png"));
        TerrainTexture gTex = new TerrainTexture(loader.loadTexture("grassFlowers.png"));
        TerrainTexture bTex = new TerrainTexture(loader.loadTexture("path.png"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTex, rTex, gTex, bTex);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap.png"));

        // Textured Models
        TexturedModel dragonModel = new TexturedModel(dragonModel3D, new Texture(loader.loadTexture("circuits.png")));
        TexturedModel bunnyModel  = new TexturedModel(bunnyModel3D, new Texture(loader.loadTexture("cheetah.png")));
        TexturedModel lampModel = new TexturedModel(lampModel3D, new Texture(loader.loadTexture("lamp.png")));

        //Generate terrain
        final int mapSize = 1;
        final int xGenLimit = (int)Terrain.SIZE * mapSize;
        final int zGenLimit = (int)Terrain.SIZE * mapSize;
        List<Terrain> terrainList = new ArrayList<>();
        for(int i = 0; i < mapSize; i++) {
            for(int j = 0; j < mapSize; j++) {
                terrainList.add(new Terrain(i, j, loader, texturePack, blendMap, "heightMap.png"));
            }
        }

        //Generate entities
        List<Entity> entityList = new ArrayList<>();
        //Entity dragonEntity = new Entity(dragonModel, new Vector3f(600f, 20f, 430f), new Vector3f(0, 0, 0), 6);
        Entity bunnyEntity = new Entity(bunnyModel, new Vector3f(200f, 20f, 430f), new Vector3f(0, 0, 0), 6);
        //entityList.add(dragonEntity);
        entityList.add(bunnyEntity);
        entityList.add(new Entity(lampModel, new Vector3f(420f, 22f, 520f), new Vector3f(0, 0, 0), 1));


        List<Light> lightList = new ArrayList<>();
        Light sun = new Light(new Vector3f(10000, 10000, 10000), new Vector3f(0.8f, 0.8f, 0.8f));
        Light light1 = new Light(new Vector3f( 610f, 35f, 430f), new Vector3f(0f, 0f, 4f), new Vector3f(1, 0.002f, 0.001f));
        Light light2 = new Light(new Vector3f(200f, 80f, 430f), new Vector3f(0f, 4f, 0f), new Vector3f(1, 0.002f, 0.001f));
        Light light3 = new Light(new Vector3f(420f, 40f, 520f), new Vector3f(4f, 0f, 0), new Vector3f(1, 0.002f, 0.001f));
        lightList.add(sun);
        lightList.add(light1);
        lightList.add(light2);
        lightList.add(light3);

        Camera camera = new Camera();
        System.out.print(camera.toString());
        RenderManager renderManager = new RenderManager(loader, camera);

        float yRotStep = 0.1f;
        float xMoveStep = 0.5f;
        float xUpperBound = 700;
        float xLowerBound = 100;
        boolean direction = true;
        while(!Display.isCloseRequested()) {
            camera.move();

            renderManager.renderShadowMap(entityList, sun);

            for(Entity e : entityList) {
                renderManager.addEntity(e);
            }

            if (bunnyEntity.getPosition().x >= xUpperBound && direction) {
                direction = false;
            } else if (bunnyEntity.getPosition().x <= xLowerBound && !direction) {
                direction = true;
            }

            if (direction) {
                bunnyEntity.increasePosition(xMoveStep, 0, 0);
            } else {
                bunnyEntity.increasePosition(-xMoveStep, 0, 0);
            }

            bunnyEntity.increaseRotation(0, yRotStep, 0);

            for(Terrain t : terrainList) {
                renderManager.addTerrain(t);
            }

            renderManager.draw(lightList, camera);
            DisplayManager.updateDisplay();
        }

        renderManager.end();
        loader.delete();
        DisplayManager.closeDisplay();
    }

    private static Vector3f randGroundedPosition(int xGenLimit, int zGenLimit, List<Terrain> terrainList) {
        Random rand = new Random();
        float x = rand.nextFloat() * xGenLimit;
        float z = rand.nextFloat() * zGenLimit;
        int lineSize = (int)Math.sqrt(terrainList.size());
        int terrainIndex = (int)((z / zGenLimit) * lineSize + (x / xGenLimit));
        Terrain terrain = terrainList.get(terrainIndex);
        float y = terrain.getTerrainHeight(x, z);
        return new Vector3f(x, y, z);
    }

    private static Vector3f randRotation() {
        Random rand = new Random();
        return new Vector3f(0, rand.nextFloat() * 360, 0);
    }
}
