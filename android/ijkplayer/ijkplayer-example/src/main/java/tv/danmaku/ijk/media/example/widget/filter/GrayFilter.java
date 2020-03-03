package tv.danmaku.ijk.media.example.widget.filter;

/**
 * Created by xrealm on 2019/2/21.
 */
public class GrayFilter extends AbsIjkRender {

    @Override
    protected String getFragmentShader() {
        return "precision highp float;\n" +
                "\n" +
                "varying vec2 textureCoordinate;\n" +
                "uniform sampler2D inputImageTexture0;\n" +
                "\n" +
                "void main() {\n" +
                "    vec2 uv = textureCoordinate;\n" +
                "    vec4 color = texture2D(inputImageTexture0, uv);\n" +
                "    float gray = color.r * 0.6 + color.g * 0.3 + color.b * 0.1;\n" +
                "    gl_FragColor = vec4(gray, gray, gray, 1.0);\n" +
                "}";
    }
}
