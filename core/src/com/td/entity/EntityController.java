package com.td.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.td.TowerDefenseGame;
import com.td.map.MapEntryType;
import com.td.map.MatrixMap;
import com.td.render.MapRenderer;
import com.td.render.WorldRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityController {
    private static final int ENTITY_SIZE = 20;
    private static final int VELOCITY = 50;

    private Wave wave;

    private final List<Enemy> enemies = new ArrayList<>();
    private Vector2 spawnPoint;
    private MatrixMap matrixMap;
    private TowerDefenseGame game;

    public void init(TowerDefenseGame game, MatrixMap map) {
        this.game = game;
        matrixMap = map;

        Random random = new Random();
        List<Enemy> enemyList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            enemyList.add(new Enemy(random.nextInt(3)));
        }

        wave = new Wave(enemyList);
        spawnPoint = map.getSpawnPoint();
    }

    public void spawnWave(WorldRenderer worldRenderer) {
        List<Enemy> units = wave.getUnits();
        int x = (int) spawnPoint.x;
        int y = (int) spawnPoint.y - MapRenderer.SLOT_SIZE_IN_PIXELS;
        for (int i = 0; i < units.size(); i++) {
            Enemy enemy = units.get(i);
            enemy.setPosition(x + MapRenderer.SLOT_SIZE_IN_PIXELS / 2,
                    y + MapRenderer.SLOT_SIZE_IN_PIXELS / 2);
            enemy.setSize(ENTITY_SIZE, ENTITY_SIZE);
            enemy.setVelocity(0, VELOCITY);
            enemy.setPathPosition(0, 0);
            y -= MapRenderer.SLOT_SIZE_IN_PIXELS;
            enemies.add(enemy);
            worldRenderer.onEntitySpawned(enemy);
        }
    }

    public void update() {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (enemyCollideWithNextPath(enemy)) {
                setNextPathAndVelocity(enemy);
            }

            Vector2 velocity = enemy.getVelocity();
            Vector2 position = enemy.getPosition();
            enemy.setPosition(position.x + velocity.x * Gdx.graphics.getDeltaTime(),
                    position.y + velocity.y * Gdx.graphics.getDeltaTime());
        }
    }

    private void setNextPathAndVelocity(Enemy enemy) {
        Vector2 pathPosition = enemy.getPathPosition();
        MapEntryType[][] matrix = matrixMap.getMatrix();
        int nextY = (int) (pathPosition.y + 1);

        if (nextY == matrix.length) {
            onGameOver();
            return;
        }

        if (matrix[nextY][(int) pathPosition.x] == MapEntryType.PATH) {
            enemy.setPathPosition((int) pathPosition.x, nextY);
            enemy.setVelocity(0, VELOCITY);
            return;
        }


        int nextX = (int) (pathPosition.x + 1);

        if (nextX == matrix[0].length) {
            onGameOver();
            return;
        }

        if (matrix[(int) pathPosition.y][nextX] == MapEntryType.PATH) {
            enemy.setPathPosition(nextX, (int) pathPosition.y);
            enemy.setVelocity(VELOCITY, 0);
        }
    }

    private boolean enemyCollideWithNextPath(Enemy enemy) {
        Vector2 position = enemy.getPosition();
        Vector2 pathPosition = enemy.getPathPosition();
        return position.x >= pathPosition.x * MapRenderer.SLOT_SIZE_IN_PIXELS + MapRenderer.SLOT_SIZE_IN_PIXELS / 2
                && position.y >= pathPosition.y * MapRenderer.SLOT_SIZE_IN_PIXELS + MapRenderer.SLOT_SIZE_IN_PIXELS / 2;
    }

    private void onGameOver() {
        enemies.clear();
        game.onGameOver();
    }
}
