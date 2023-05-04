package com.td;

import com.badlogic.gdx.ApplicationAdapter;
import com.td.map.MatrixMap;
import com.td.render.WorldRenderer;

public class TowerDefenseGame extends ApplicationAdapter {
    private MatrixMap map;
    private WorldRenderer worldRenderer;

    @Override
    public void create() {
        map = new MatrixMap();
        map.generateMap();
        worldRenderer = new WorldRenderer(map);
        worldRenderer.init();
    }

    @Override
    public void render() {
        worldRenderer.render();
    }

    @Override
    public void dispose() {
        worldRenderer.dispose();
    }
}
