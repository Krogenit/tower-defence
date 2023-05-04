package com.td;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.td.entity.EntityController;
import com.td.map.MatrixMap;
import com.td.render.WorldRenderer;

public class TowerDefenseGame extends ApplicationAdapter {
    private final AssetManager assetManager = new AssetManager();
    private MatrixMap map;
    private WorldRenderer worldRenderer;
    private final EntityController entityController = new EntityController();

    @Override
    public void create() {
        map = new MatrixMap();
        map.generateMap();
        worldRenderer = new WorldRenderer(map);
        worldRenderer.init(assetManager);
        entityController.init(this, map);
        entityController.spawnWave(worldRenderer);
    }

    @Override
    public void render() {
        entityController.update();
        worldRenderer.render();
    }

    public void onGameOver() {

    }

    @Override
    public void dispose() {
        worldRenderer.dispose();
    }
}
