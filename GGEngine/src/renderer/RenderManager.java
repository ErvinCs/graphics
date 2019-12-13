package renderer;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderManager {
    private StaticShader shader = new StaticShader();
    private Renderer renderer = new Renderer(shader);
    // Map the Entities to be rendered foreach TexturedModel
    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    public void end() {
        shader.end();
    }

    // Runs once per frame
    public void draw(Light worldLight, Camera camera) {
        renderer.clear();
        shader.begin();
        shader.loadLight(worldLight);
        shader.loadViewMatrix(camera);
        renderer.draw(entities);
        shader.end();
        entities.clear();
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
}
