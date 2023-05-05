package com.td.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import lombok.Getter;

@Getter
public class Enemy extends Image {
    private final float maxHp;
    private float hp;
    private final Vector2 pathPosition = new Vector2();
    private final ShapeRenderer shapeRenderer;
    private final float moveSpeed;

    public Enemy(int type, Texture texture, ShapeRenderer shapeRenderer) {
        super(texture);
        this.shapeRenderer = shapeRenderer;

        if (type == 1) {
            maxHp = 6.0f;
            moveSpeed = 0.7f;
        } else if (type == 2) {
            maxHp = 2.25f;
            moveSpeed = 3.25f;
        } else {
            maxHp = 3.25f;
            moveSpeed = 1.0f;
        }

        hp = maxHp;
    }

    public void setPathPosition(int x, int y) {
        pathPosition.set(x, y);
    }

    public void attack(float damage) {
        hp -= damage;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        int hpBarWidth = 34;
        int hpBarHeight = 4;
        int yOffset = -14;
        float x = getX() - hpBarWidth / 2 + getWidth() / 2;
        float y = getY() + yOffset;

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(x, y, hpBarWidth, hpBarHeight);

        float hpNormalizedValue = hp / maxHp;
        float coloredHpBarWidth = hpBarWidth * hpNormalizedValue;
        shapeRenderer.setColor(1 - hpNormalizedValue, hpNormalizedValue, 0, 1);
        shapeRenderer.rect(x, y, coloredHpBarWidth, hpBarHeight);
        shapeRenderer.end();

        batch.begin();
    }
}
