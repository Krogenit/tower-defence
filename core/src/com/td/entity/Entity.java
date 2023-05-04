package com.td.entity;

import com.badlogic.gdx.math.Vector2;
import lombok.Getter;

@Getter
public class Entity {
    protected final Vector2 position = new Vector2();
    protected final Vector2 size = new Vector2();

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public void setSize(float width, float height) {
        size.set(width, height);
    }
}
