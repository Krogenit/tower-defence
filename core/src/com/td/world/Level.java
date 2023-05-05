package com.td.world;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.td.gui.GameScreen;
import lombok.Getter;

import java.util.Random;

public class Level extends Table {
    public static final int MAP_SIZE = 17;

    @Getter
    private final SlotStack[][] matrix = new SlotStack[MAP_SIZE][MAP_SIZE];

    public Level(ClickListener slotClickListener, ShapeRenderer shapeRenderer) {
        generateMap(slotClickListener, shapeRenderer);
    }

    public void generateMap(ClickListener slotClickListener, ShapeRenderer shapeRenderer) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                SlotStack slotStack = new SlotStack();
                Slot slot = new Slot(SlotType.TOWER, shapeRenderer);
                slotStack.setSlot(slot);
                slot.setPosition(j * GameScreen.SLOT_SIZE_IN_PIXELS,
                        i * GameScreen.SLOT_SIZE_IN_PIXELS);
                slot.setSize(GameScreen.SLOT_SIZE_IN_PIXELS, GameScreen.SLOT_SIZE_IN_PIXELS);
                slot.addListener(slotClickListener);
                add(slotStack);
                matrix[i][j] = slotStack;
            }

            row();
        }

        Random random = new Random();

        int x = 0;
        int y = 0;

        matrix[x][y].getSlot().setSpawn(true);
        Slot lastSlot;

        do {
            lastSlot = matrix[x][y].getSlot();
            lastSlot.setType(SlotType.PATH);

            if (random.nextBoolean()) {
                x++;
            } else {
                y++;
            }
        } while (x < MAP_SIZE && y < MAP_SIZE);

        lastSlot.setEnd(true);
    }
}
