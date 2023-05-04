package com.td.render;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.td.entity.Enemy;
import com.td.render.entity.EnemyRender;
import com.td.render.entity.EntityRender;

import java.util.ArrayList;
import java.util.List;

public class EntityRenderer {
    private final List<EntityRender> renderList = new ArrayList<>();
    private final Texture[] enemyTextures = new Texture[3];

    public void init(AssetManager assetManager) {
        for (int i = 0; i < 3; i++) {
            assetManager.load("texture/entity/enemy/" + i + ".png", Texture.class);
        }

        assetManager.finishLoading();

        for (int i = 0; i < 3; i++) {
            enemyTextures[i] = assetManager.get("texture/entity/enemy/" + i + ".png");
        }
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        for (int i = 0; i < renderList.size(); i++) {
            renderList.get(i).render(batch);
        }
        batch.end();
    }

    public void onEntitySpawned(Enemy enemy) {
        renderList.add(new EnemyRender(enemy, enemyTextures[enemy.getEnemyType()]));
    }

    public void onEnemyDeath(Enemy enemy) {
        renderList.removeIf(render -> render.getEntity() == enemy);
    }
}
