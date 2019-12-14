package renderer;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrain.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderManager {
    private static final float FOV_ANGLE = 45;
    private static final float NEAR_Z = 0.1f;
    private static final float FAR_Z = 1000f;

    private static float RED   = 0.5F;
    private static float GREEN = 0.5F;
    private static float BLUE  = 0.5F;

    private StaticShader shader = new StaticShader();
    private EntityRenderer entityRenderer;
    // Map the Entities to be rendered foreach TexturedModel
    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private Matrix4f projectionMatrix;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();
    private List<Terrain> terrainList = new ArrayList<>();

    public RenderManager() {
        enableCulling();
        createProjectionMatrix();
        entityRenderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    public static void enableCulling() {
        // Cull the back faces of a model
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void end() {
        shader.end();
        terrainShader.end();
    }

    public void clear(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(RED, GREEN, BLUE, 1.0f);
    }

    // Runs once per frame
    public void draw(Light worldLight, Camera camera) {
        this.clear();
        shader.begin();
        // Sky
        shader.loadSkyColor(RED, GREEN, BLUE);
        // World
        shader.loadLight(worldLight);
        shader.loadViewMatrix(camera);
        // Entities
        entityRenderer.draw(entities);
        shader.end();
        //Terrain
        terrainShader.begin();
        terrainShader.loadSkyColor(RED, GREEN, BLUE);
        terrainShader.loadLight(worldLight);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrainList);
        terrainShader.end();
        terrainList.clear();
        entities.clear();
    }

    public void addTerrain(Terrain terrain) {
        terrainList.add(terrain);
    }

    public void addEntity(Entity entity) {
        TexturedModel model = entity.getModel();
        List<Entity> batch = entities.get(model);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(model, newBatch);
        }
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV_ANGLE / 2f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_Z - NEAR_Z;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((FAR_Z + NEAR_Z) / frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_Z * FAR_Z) / frustumLength);
        projectionMatrix.m33 = 0;
    }
}
