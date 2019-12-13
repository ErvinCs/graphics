package textures;

public class Texture {
    private int textureID;
    private float shineDamp = 1;
    private float reflectivity = 0;

    public Texture(int textureID){
        this.textureID = textureID;
    }

    public int getID() {
        return textureID;
    }

    public float getShineDamp() {
        return shineDamp;
    }

    public void setShineDamp(float shineDamp) {
        this.shineDamp = shineDamp;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
