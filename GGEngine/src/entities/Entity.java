package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
    private TexturedModel model;
    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    //Which texture in the atlas is used
    private int textureIndex;

    public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.textureIndex = 0;
    }

    public Entity(TexturedModel model, int textureIndex, Vector3f position, Vector3f rotation, float scale) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.textureIndex = textureIndex;
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotation.x += dx;
        this.rotation.y += dy;
        this.rotation.y += dz;

    }

    public void increaseScale(float dscale) {
        this.scale += dscale;
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getTextureIndex() {
        return textureIndex;
    }

    public int getTextureXOffset() {
        int column = textureIndex % model.getTexture().getNumberOfRows();
        return column / model.getTexture().getNumberOfRows();
    }

    public int getTextureYOffset() {
        int row = textureIndex / model.getTexture().getNumberOfRows();
        return row / model.getTexture().getNumberOfRows();
    }
}
