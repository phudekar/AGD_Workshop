package in.hpc.Workshop;

import android.opengl.GLES20;

import java.nio.ShortBuffer;

public class Rectangle extends Shape {
    // lets setup coordinates for square
    private float[] coordinates = new float[]{
            -0.5f, 0.5f, 0.0f,  //  top left vertex
            -0.5f, -0.5f, 0.0f,    // bottom left vertex
            0.5f, -0.5f, 0.0f,      // bottom right vertex
            0.5f, 0.5f, 0.0f      // top right vertex
    };

    // shaders
    private final String vertexShaderCode =
            "uniform mat4 mMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = mMVPMatrix *vPosition ;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    // setup the index to draw
    private short[] drawOrder = new short[]{0, 1, 2, 0, 2, 3};
    private final int vertexCount;
    private final ShortBuffer indexBuffer;

    public Rectangle() {
        vertexCount = getCoordinates().length / COORDS_PER_VERTEX;
        color = new float[]{1, 0, 0, 1};
        indexBuffer = createIndexBuffer();
        scale(1, 1);
    }


    @Override
    protected short[] getDrawOrder() {
        return drawOrder;
    }

    @Override
    protected float[] getCoordinates() {
        return coordinates;
    }

    @Override
    protected String getFragmentShaderCode() {
        return fragmentShaderCode;
    }

    @Override
    protected String getVertexShaderCode() {
        return vertexShaderCode;
    }

    @Override
    protected void drawShape(float[] viewMatrix, float[] projectionMatrix) {
        setModelViewProjectionMatrixInShader(shaderProgram, viewMatrix, projectionMatrix);
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        GameActivity.checkGlError("glDrawElements");
    }
}
