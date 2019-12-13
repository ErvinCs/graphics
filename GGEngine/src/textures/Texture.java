package textures;

public class Texture {
    private int textureID;
    private float shineDamp = 1;
    private float reflectivity = 0;
    private boolean hasTransparent = false;
    private boolean useSimulatedLight = false;

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

    public boolean getTransparent() {
        return this.hasTransparent;
    }

    public void setTransparent(boolean hasTransparent) {
        this.hasTransparent = hasTransparent;
    }

    public boolean getSimulatedLight() { return this.useSimulatedLight; }

    public void setUseSimulatedLight(boolean useSimulatedLight) { this.useSimulatedLight = useSimulatedLight; }
}
