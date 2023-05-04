package com.td.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.td.map.MatrixMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WorldRenderer {
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final OrthographicCamera camera = new OrthographicCamera();
    private final MapRenderer mapRenderer = new MapRenderer();
    private final MatrixMap map;

    public void init() {
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void render() {
        ScreenUtils.clear(0.2f, 0.4f, 0.6f, 1.0f);

        camera.update();

        mapRenderer.render(camera, map);
    }

    public void dispose() {
        mapRenderer.dispose();
    }
}
