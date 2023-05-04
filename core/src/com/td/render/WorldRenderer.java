package com.td.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.td.entity.Enemy;
import com.td.map.MatrixMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WorldRenderer {
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final OrthographicCamera camera = new OrthographicCamera();
    private final MapRenderer mapRenderer = new MapRenderer();
    private final EntityRenderer entityRenderer = new EntityRenderer();
    private final MatrixMap map;

    public void init(AssetManager assetManager) {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        entityRenderer.init(assetManager);
    }

    public void render() {
        ScreenUtils.clear(0.1f, 0.2f, 0.3f, 1.0f);

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        mapRenderer.render(camera, map);
        entityRenderer.render(spriteBatch);
    }

    public void onEntitySpawned(Enemy enemy) {
        entityRenderer.onEntitySpawned(enemy);
    }

    public void dispose() {
        spriteBatch.dispose();
        mapRenderer.dispose();
    }
}
