package tv.danmaku.ijk.media.player;

/**
 * Created by xrealm on 17/11/18.
 */
public interface IIjkFilter {
    void onCreated();

    void onSizeChanged(int width, int height);

    void onDrawFrame(int textureId);
}
