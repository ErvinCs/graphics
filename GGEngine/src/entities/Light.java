package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a light in the game world.
 * Entails Vector3f objects for position, color & attenuation used in lighting calculations in the shaders.
 * The lighting calculations use the normal & the vector from a vertex of the object to the light source (called "direction vector").
 * For each vertex it is determined wether the normal & the direction vector are pointing in the same direction.
 * The more the 2 vectors overlap, the brighter that vertex is. The dot product is used to determine overlap (the vectors must be Unit Vectors).
 * Diffuse Lighting  - the light on the object's surface depends on how much it's facing the light.
 * Specular Lighting - a reflection of the light source on the surface of a shinny object.
 *      So in addition to lighting the object, the light is also reflected in the opposite direction to the incoming light's direction.
 * Ambient Lighting  - add a small amount of light to the rest of the model
 */
public class Light {
    private Vector3f position;
    private Vector3f color;
    private Vector3f attenuation;

    public Light(Vector3f position, Vector3f color) {
        this.position = position;
        this.color = color;
        this.attenuation = new Vector3f(1, 0, 0);
    }

    public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
        this.position = position;
        this.color = color;
        this.attenuation = attenuation;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
