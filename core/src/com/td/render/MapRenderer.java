package com.td.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.td.map.MapEntryType;
import com.td.map.MatrixMap;

public class MapRenderer {
    public static final int SLOT_SIZE_IN_PIXELS = 40;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public void render(Camera camera, MatrixMap map) {
        MapEntryType[][] matrix = map.getMatrix();
        shapeRenderer.setProjectionMatrix(camera.combined);
        renderOutlines(matrix);
        renderRectangles(matrix);
    }

    private void renderOutlines(MapEntryType[][] matrix) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.2f, 0.8f, 0.6f, 1.0f);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                MapEntryType type = matrix[i][j];
                if (type == MapEntryType.TOWER) {
                    int x = j * SLOT_SIZE_IN_PIXELS;
                    int y = i * SLOT_SIZE_IN_PIXELS;
                    shapeRenderer.rect(x, y, SLOT_SIZE_IN_PIXELS, SLOT_SIZE_IN_PIXELS);
                }
            }
        }
        shapeRenderer.end();
    }

    private void renderRectangles(MapEntryType[][] matrix) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.5f, 0.8f, 0.6f, 0.1f);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                MapEntryType type = matrix[i][j];
                if (type == MapEntryType.TOWER) {
                    int x = j * SLOT_SIZE_IN_PIXELS;
                    int y = i * SLOT_SIZE_IN_PIXELS;
                    shapeRenderer.rect(
                            x - 1, y - 1, SLOT_SIZE_IN_PIXELS, SLOT_SIZE_IN_PIXELS
                    );
                }
            }
        }
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
