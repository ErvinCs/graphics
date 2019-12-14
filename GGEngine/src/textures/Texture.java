package textures;

public class Texture {
    private int textureID;

    private float shineDamp;
    private float reflectivity;

    private boolean hasTransparent;
    private boolean useSimulatedLight;

    //Assume that any texture is a textureAtlas
    private int numberOfRows;

    public Texture(int textureID){
        this.textureID = textureID;
        this.numberOfRows = 1;
        this.hasTransparent = false;
        this.useSimulatedLight = false;
        this.shineDamp = 1;
        this.reflectivity = 0;
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

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }
}
