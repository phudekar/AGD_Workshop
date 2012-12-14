package in.hpc.Workshop;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public abstract class Shape {

    protected FloatBuffer vertexBuffer;
    protected int shaderProgram;

    protected float[] color = new float[]{1, 1, 1, 1};

    protected final int COORDS_PER_VERTEX = 3;

    public void initialize() {
        vertexBuffer = createVertexBuffer();
        shaderProgram = createShaderProgram();
    }

    // initialize buffers
    protected FloatBuffer createVertexBuffer() {
        float[] coordinates = getCoordinates();
        ByteBuffer buffer = ByteBuffer.allocateDirect(coordinates.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = buffer.asFloatBuffer();
        vertexBuffer.put(coordinates);
        vertexBuffer.position(0);
        return vertexBuffer;
    }

    protected ShortBuffer createIndexBuffer() {
        short[] drawOrder = getDrawOrder();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(drawOrder.length * 2);
        byteBuffer.order(ByteOrder.nativeOrder());
        ShortBuffer indexBuffer = byteBuffer.asShortBuffer();
        indexBuffer.put(drawOrder);
        indexBuffer.position(0);
        return indexBuffer;
    }

    protected abstract short[] getDrawOrder();

    protected abstract float[] getCoordinates();


    // load shader
    public int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    // link shader program
    protected int createShaderProgram() {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, getVertexShaderCode());
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, getFragmentShaderCode());
        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram);
        return shaderProgram;
    }

    protected abstract String getFragmentShaderCode();

    protected abstract String getVertexShaderCode();

    // set vertex buffer in shader
    protected int setVertexPositionInShader(int shaderProgram, FloatBuffer vertexBuffer) {
        int vertexPositionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vertexPositionHandle);
        GLES20.glVertexAttribPointer(vertexPositionHandle,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                COORDS_PER_VERTEX * 4,
                vertexBuffer);
        return vertexPositionHandle;
    }

    // set color in shader
    protected void setColorInShader(int shaderProgram, float[] color) {
        int colorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");
        int count = 1;
        int offset = 0;
        GLES20.glUniform4fv(colorHandle, count, color, offset);
    }

    public void draw() {
        GLES20.glUseProgram(shaderProgram);
        int vertexPositionHandle = setVertexPositionInShader(shaderProgram, vertexBuffer);
        setColorInShader(shaderProgram, color);
        drawShape();
        GLES20.glDisableVertexAttribArray(vertexPositionHandle);
    }


    protected abstract void drawShape();
}
