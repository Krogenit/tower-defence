package com.td.render.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.td.entity.Enemy;

public class EnemyRender extends EntityRender {
    public EnemyRender(Enemy enemy, Texture texture) {
        super(enemy, texture);
    }

    public void render(SpriteBatch batch) {
        Vector2 position = entity.getPosition();
        Vector2 size = entity.getSize();
        batch.draw(texture, position.x - size.x / 2, position.y - size.y / 2, size.x, size.y);
    }
}
