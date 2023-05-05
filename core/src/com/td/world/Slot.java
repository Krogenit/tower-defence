package com.td.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Slot extends Actor {
    private SlotType type;
    private final ShapeRenderer renderer;
    private boolean spawn;
    private boolean end;

    public Slot(SlotType type, ShapeRenderer renderer) {
        this.type = type;
        this.renderer = renderer;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (type == SlotType.TOWER) {
            batch.end();

            renderer.setProjectionMatrix(batch.getProjectionMatrix());
            renderer.setTransformMatrix(batch.getTransformMatrix());
            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setColor(0.2f, 0.8f, 0.6f, 1.0f);
            renderer.rect(getX(), getY(), getWidth(), getHeight());
            renderer.end();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(0.5f, 0.8f, 0.6f, 0.1f);
            renderer.rect(getX() - 1, getY() - 1, getWidth() - 1, getHeight() - 1);
            renderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            batch.begin();
        } else if (spawn) {
            batch.end();

            int offset = 8;
            renderer.setProjectionMatrix(batch.getProjectionMatrix());
            renderer.setTransformMatrix(batch.getTransformMatrix());
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(0.1f, 0.8f, 0.4f, 1.0f);
            renderer.rect(getX() + offset, getY() + offset,
                    getWidth() - (offset << 1), getHeight() - (offset << 1));
            renderer.end();

            batch.begin();
        } else if (end) {
            batch.end();

            int offset = 8;
            renderer.setProjectionMatrix(batch.getProjectionMatrix());
            renderer.setTransformMatrix(batch.getTransformMatrix());
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(0.8f, 0.2f, 0.1f, 1.0f);
            renderer.rect(getX() + offset, getY() + offset,
                    getWidth() - (offset << 1), getHeight() - (offset << 1));
            renderer.end();

            batch.begin();
        }
    }

    public boolean canBuild() {
        return type == SlotType.TOWER && ((SlotStack) getParent()).isEmpty();
    }
}
