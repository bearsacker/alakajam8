package com.guillot.engine.opengl;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.lwjgl.opengl.ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.glAttachObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glCompileShaderARB;
import static org.lwjgl.opengl.ARBShaderObjects.glCreateProgramObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glCreateShaderObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glDeleteObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glGetInfoLogARB;
import static org.lwjgl.opengl.ARBShaderObjects.glGetObjectParameteriARB;
import static org.lwjgl.opengl.ARBShaderObjects.glLinkProgramARB;
import static org.lwjgl.opengl.ARBShaderObjects.glShaderSourceARB;
import static org.lwjgl.opengl.ARBShaderObjects.glUseProgramObjectARB;
import static org.lwjgl.opengl.ARBShaderObjects.glValidateProgramARB;
import static org.lwjgl.opengl.ARBVertexShader.GL_VERTEX_SHADER_ARB;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.stream.Collectors;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.util.ResourceLoader;

public class Shader {

    private int program;

    private int loc;

    public Shader() {
        this.program = 0;
    }

    public int getProgram() {
        return program;
    }

    public boolean load(String vertexFile, String fragmentFile) {
        int vertShader = 0, fragShader = 0;

        try {
            vertShader = createShader(vertexFile, GL_VERTEX_SHADER_ARB);
            fragShader = createShader(fragmentFile, GL_FRAGMENT_SHADER_ARB);
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        } finally {
            if (vertShader == 0 || fragShader == 0) {
                return false;
            }
        }

        program = glCreateProgramObjectARB();
        if (program == 0) {
            return false;
        }

        glAttachObjectARB(program, vertShader);
        glAttachObjectARB(program, fragShader);

        glLinkProgramARB(program);
        if (glGetObjectParameteriARB(program, GL_OBJECT_LINK_STATUS_ARB) == GL_FALSE) {
            System.err.println(getLogInfo(program));
            return false;
        }

        glValidateProgramARB(program);
        if (glGetObjectParameteriARB(program, GL_OBJECT_VALIDATE_STATUS_ARB) == GL_FALSE) {
            System.err.println(getLogInfo(program));
            return false;
        }

        return true;
    }

    public void setUniform(String name, Vector2f object) {
        loc = glGetUniformLocation(program, name);
        GL20.glUniform2f(loc, object.x, object.y);
    }

    public void setUniform(String name, Vector3f object) {
        loc = glGetUniformLocation(program, name);
        glUniform3f(loc, object.x, object.y, object.z);
    }

    public void setUniform(String name, int value) {
        loc = glGetUniformLocation(program, name);
        glUniform1i(loc, value);
    }

    public void setUniform(String name, float value) {
        loc = glGetUniformLocation(program, name);
        glUniform1f(loc, value);
    }

    public void setUniform(String name, boolean value) {
        loc = glGetUniformLocation(program, name);
        glUniform1i(loc, value ? 1 : 0);
    }

    public void setUniform(String name, int[] value) {
        IntBuffer buffer = BufferUtils.createIntBuffer(value.length);
        buffer.put(value);
        buffer.rewind();

        loc = glGetUniformLocation(program, name);
        glUniform1(loc, buffer);
    }

    public void setUniform(String name, float[] value) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(value.length);
        buffer.put(value);
        buffer.rewind();

        loc = glGetUniformLocation(program, name);
        glUniform1(loc, buffer);
    }

    public void bind() {
        glUseProgramObjectARB(program);
    }

    public void unbind() {
        glUseProgramObjectARB(0);
    }

    private int createShader(String file, int shaderType) throws Exception {
        int shader = 0;
        try {
            shader = glCreateShaderObjectARB(shaderType);
            if (shader == 0) {
                return 0;
            }

            BufferedReader buffer = new BufferedReader(new InputStreamReader(ResourceLoader.getResourceAsStream(file), UTF_8));
            glShaderSourceARB(shader, buffer.lines().collect(Collectors.joining("\n")));
            glCompileShaderARB(shader);

            if (glGetObjectParameteriARB(shader, GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE) {
                throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
            }

            return shader;
        } catch (Exception e) {
            glDeleteObjectARB(shader);
            throw e;
        }
    }

    private static String getLogInfo(int obj) {
        return glGetInfoLogARB(obj, glGetObjectParameteriARB(obj, GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }
}
