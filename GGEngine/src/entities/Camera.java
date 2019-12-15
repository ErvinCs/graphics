package entities;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import java.security.Key;

public class Camera {
    private Vector3f position;      // XYZ
    private float pitch;            // Rise/Fall
    private float yaw;              // Left/Right aiming
    private float roll;             // Rotation
    private float moveSpeed;

    //Good starting point:
    //Position: Vector3f[400.0, 5.0, 255.0]
    //Yaw:      265.0
    //Pitch:    5.0
    //Roll:     0.0f
    public Camera() {
        this.position = new Vector3f(400, 5,255);
        this.pitch = 5.0f;
        this.yaw = 265.0f;
        this.roll = 0.0f;
        this.moveSpeed = 1.0f;
    }

    public Camera(Vector3f position, float pitch, float yaw, float roll, float moveSpeed) {
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.moveSpeed = moveSpeed;
    }

    public void move() {
        // Z
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z -= moveSpeed;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z += moveSpeed;
        }
        // X
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x += moveSpeed;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x -= moveSpeed;
        }
        // Y
        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            position.y += moveSpeed;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            position.y -= moveSpeed;
        }

        // YAW
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            yaw += moveSpeed;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            yaw -= moveSpeed;
        }

        // PITCH
        if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
            pitch += moveSpeed;
        }else if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
            pitch -= moveSpeed;
        }

        // ROLL
        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            this.roll += moveSpeed;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
            this.roll -= moveSpeed;
        }

        System.out.println(this.toString());
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

    @Override
    public String toString() {
        //System.out.println();
        //System.out.println();
        //System.out.println();
        //System.out.println();
        return  "Camera: " + '\n' +
                "\tPosition: " + position.toString() + '\n' +
                "\tYaw: " + yaw + '\n' +
                "\tPitch: " + pitch + '\n' +
                "\tRoll: " + roll + '\n';
    }
}
