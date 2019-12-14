package shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public abstract class Shader {
    private int shaderID;
    private int vertexShaderID;
    private int fragmentShaderID;

    // Use whenever a matrix needs to be loaded
    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public Shader() {
        shaderID = -1;
        vertexShaderID = -1;
        fragmentShaderID = -1;
    }

    public Shader(String vertexPath, String fragmentPath) {
        vertexShaderID = loadShader(vertexPath, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentPath, GL20.GL_FRAGMENT_SHADER);
        shaderID = GL20.glCreateProgram();
        GL20.glAttachShader(shaderID, vertexShaderID);
        GL20.glAttachShader(shaderID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(shaderID);
        GL20.glValidateProgram(shaderID);
        getAllUniformLocations();
    }

    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(shaderID, uniformName);
    }

    protected void loadFloat(int location, float value)
    {
        GL20.glUniform1f(location, value);
    }

    protected void loadInt(int location, int value)
    {
        GL20.glUniform1i(location, value);
    }

    protected void loadVec2(int location, Vector2f vec) {
        GL20.glUniform2f(location, vec.x, vec.y);
    }

    protected void loadVec3(int location, Vector3f vec) {
        GL20.glUniform3f(location, vec.x, vec.y, vec.z);
    }

    protected void looadBoolean(int location, boolean value) {
        float boolValue = 0;
        if(value) {
            boolValue = 1;
        }
        GL20.glUniform1f(location, boolValue);
    }

    protected void loadMat4f(int location, Matrix4f mat) {
        mat.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(location, false, matrixBuffer);
    }

    protected abstract void getAllUniformLocations();

    public void begin() {
        GL20.glUseProgram(shaderID);
    }

    public void end() {
        GL20.glUseProgram(0);
    }

    public void delete() {
        end();
        GL20.glDetachShader(shaderID, vertexShaderID);
        GL20.glDetachShader(shaderID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(shaderID);
    }

    // Link the attributes in the shader to attributes in the VAOs
    protected abstract void bindAttributes();

    /**
     * @param attribute - attribute index in the attribute list
     * @param varName - variable name in the shader code
     */
    protected void bindAttribute(int attribute, String varName) {
        GL20.glBindAttribLocation(shaderID, attribute, varName);
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSrc = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSrc.append(line).append('\n');
            }
        } catch(IOException ex) {
            System.err.println("Cannot read file!");
            ex.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSrc);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;
    }
}
