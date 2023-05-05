package com.td.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Tower extends Image {
    private final float damage = 0.125f;
    @Setter
    private Enemy target;
    @Setter
    private float reloadingTime;
    @Getter
    private final Vector2 targetPos = new Vector2();

    public Tower(Texture texture) {
        super(texture);
    }
}
