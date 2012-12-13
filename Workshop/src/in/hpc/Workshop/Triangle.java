package in.hpc.Workshop;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {

    // lets setup coordinates for triangle
    private float[] coordinates = new float[]{
            0.0f, 0.5f, 0.0f,  //  top vertex
            -0.5f, -0.5f, 0.0f,    // left vertex
            0.5f, -0.5f, 0.0f      // right vertex
    };

    // shaders
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private FloatBuffer vertexBuffer;
    private int shaderProgram;
    private float[] color = new float[]{1, 1, 1, 1};
    private int COORDS_PER_VERTEX = 3;
    int vertexCount = coordinates.length/COORDS_PER_VERTEX;

    public Triangle(){
          createVertexBuffer();
        createShaderProgram();
    }

    // initialize buffers
    private void createVertexBuffer() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(coordinates.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        vertexBuffer = buffer.asFloatBuffer();
        vertexBuffer.put(coordinates);
        vertexBuffer.position(0);
    }


    // load shader
    public int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    // link shader program
    private void createShaderProgram() {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram);
    }

    // set vertex buffer in shader
    public int setVertexPositionInShader(int shaderProgram, FloatBuffer vertexBuffer) {
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
    private void setColorInShader(int shaderProgram, float[] color) {
        int colorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");
        int count = 1;
        int offset = 0;
        GLES20.glUniform4fv(colorHandle, count, color, offset);
    }

    // draw the vertices
    public void draw() {

        GLES20.glUseProgram(shaderProgram);
        int vertexPositionHandle = setVertexPositionInShader(shaderProgram, vertexBuffer);
        setColorInShader(shaderProgram, color);
        int offset = 0;
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, offset, vertexCount);
        GLES20.glDisableVertexAttribArray(vertexPositionHandle);
    }


}
