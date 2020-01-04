package models;

import textures.Texture;

/**
 * Wraps a Model3D & a Texture.
 */
public class TexturedModel {
    private Model3D model;
    private Texture texture;

    public TexturedModel(Model3D model, Texture texture) {
        this.model = model;
        this.texture = texture;
    }

    public Model3D getModel() {
        return model;
    }

    public Texture getTexture() {
        return texture;
    }
}
