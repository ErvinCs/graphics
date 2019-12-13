package entities;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
    private Vector3f position = new Vector3f(-180,80,0);
    private float pitch;    // Rise/Fall
    private float yaw = 160;      // Left/Right aiming
    private float roll;     // Self-explanatory
    private final float moveSpeed = 1f;

    public Camera() {}



    public void move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z -= moveSpeed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z += moveSpeed;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x += moveSpeed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x -= moveSpeed;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            position.y += moveSpeed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            position.y -= moveSpeed;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            yaw += moveSpeed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            yaw -= moveSpeed;
        }
        System.out.println("Position: " + position.toString());
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}
