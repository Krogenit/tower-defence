package com.td.map;

import com.badlogic.gdx.math.Vector2;
import lombok.Getter;

import java.util.Arrays;
import java.util.Random;

public class MatrixMap {
    private static final int MAP_SIZE = 17;

    @Getter
    private final MapEntryType[][] matrix = new MapEntryType[MAP_SIZE][MAP_SIZE];

    @Getter
    private final Vector2 spawnPoint = new Vector2();
    @Getter
    private final Vector2 endPoint = new Vector2();
    private final Random random = new Random();

    public void generateMap() {
        int spawnIndexX = 0;
        int spawnIndexY = 0;
        spawnPoint.set(spawnIndexX, spawnIndexY);

        for (int i = 0; i < matrix.length; i++) {
            Arrays.fill(matrix[i], MapEntryType.TOWER);
        }

        int x = spawnIndexX;
        int y = spawnIndexY;
        do {
            matrix[x][y] = MapEntryType.PATH;

            if (random.nextBoolean()) {
                x++;
            } else {
                y++;
            }
        } while (x < MAP_SIZE && y < MAP_SIZE);

        endPoint.set(x, y);
    }
}
