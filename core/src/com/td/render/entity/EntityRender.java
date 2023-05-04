package com.td.render.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.td.entity.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class EntityRender {
    @Getter
    protected final Entity entity;
    protected final Texture texture;

    public abstract void render(SpriteBatch batch);
}
