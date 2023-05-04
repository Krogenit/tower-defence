package com.td.entity;

import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Enemy extends Entity {
    private final int enemyType;
    private float hp;
    @Getter
    private Vector2 velocity = new Vector2();
    @Getter
    private Vector2 pathPosition = new Vector2();

    public void setVelocity(float x, float y) {
        velocity.set(x, y);
    }

    public void setPathPosition(int x, int y) {
        pathPosition.set(x, y);
    }
}
