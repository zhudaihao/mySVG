package cn.zdh.svg.empty.view;

import android.graphics.Path;

/**
 * 封装 path 和颜色
 */
public class PathUser {
    private Path path;
    private int color;

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public PathUser(Path path, int color) {

        this.path = path;
        this.color = color;
    }
}
