package tv.danmaku.ijk.media.example.widget.filter;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tv.danmaku.ijk.media.player.IIjkFilter;


/**
 * base renderer
 * Created by xrealm on 2019/2/21.
 */
public abstract class AbsIjkRender implements IIjkFilter {

    protected FloatBuffer renderVertices;
    protected FloatBuffer textureVertices;

    protected int width, height;

    protected int programHandle;
    protected int textureHandle;
    protected int positionHandle;
    protected int texCoordHandle;
    protected int textureIn;

    public AbsIjkRender() {
        float[] vertices = new float[]{
                -1f, -1f,
                1f, -1f,
                -1f, 1f,
                1f, 1f
        };
        renderVertices = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        renderVertices.put(vertices).position(0);

        float[] texData = new float[]{
                0.0f, 0.0f,
                1.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
        };

        textureVertices = ByteBuffer.allocateDirect(texData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(texData);
        textureVertices.position(0);
    }

    @Override
    public void onCreated() {
        programHandle = ShaderHelper.buildProgram(getVertexShader(), getFragmentShader());
        bindShaderAttributes();
        initShaderHandles();
    }

    @Override
    public void onSizeChanged(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void onDrawFrame(int textureId) {
        if (programHandle == 0) {
            onCreated();
        }
        textureIn = textureId;
        GLES20.glViewport(0, 0, width, height);
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(programHandle);
        passShaderValues();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        disableDrawArray();
    }

    protected String getVertexShader() {
        return "attribute vec4 position;\n"
                + "attribute vec2 inputTextureCoordinate;\n"
                + "varying vec2 textureCoordinate;\n"
                + "void main() {\n"
                + "    textureCoordinate = inputTextureCoordinate;\n"
                + "    gl_Position = position;\n"
                + "}\n";
    }

    protected String getFragmentShader() {
        return "precision mediump float;\n"
                + "uniform sampler2D inputImageTexture0;\n"
                + "varying vec2 textureCoordinate;\n"
                + "void main() {\n"
                + "    gl_FragColor = texture2D(inputImageTexture0, uv);\n"
                + "}";
    }

    protected void bindShaderAttributes() {
        GLES20.glBindAttribLocation(programHandle, 0, "position");
        GLES20.glBindAttribLocation(programHandle, 1, "inputTextureCoordinate");
    }

    protected void initShaderHandles() {
        positionHandle = GLES20.glGetAttribLocation(programHandle, "position");
        texCoordHandle = GLES20.glGetAttribLocation(programHandle, "inputTextureCoordinate");
        textureHandle = GLES20.glGetUniformLocation(programHandle, "inputImageTexture0");
    }

    protected void passShaderValues() {
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 8, renderVertices);

        GLES20.glEnableVertexAttribArray(texCoordHandle);
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 8, textureVertices);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIn);
        GLES20.glUniform1i(textureHandle, 0);
    }

    protected void disableDrawArray() {
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texCoordHandle);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
