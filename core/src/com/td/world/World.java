package com.td.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.td.entity.Enemy;
import com.td.entity.Tower;
import com.td.gui.GameScreen;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.td.gui.GameScreen.LEVEL_SIZE;
import static com.td.gui.GameScreen.SLOT_SIZE_IN_PIXELS;

public class World extends WidgetGroup {
    private static final int MIN_ENTITY_SPAWN_COUNT = 40;
    private static final int MAX_ENTITY_SPAWN_COUNT = 60;
    private static final int ENTITY_SIZE = 20;
    public static final float TOWER_RELOAD_TIME_IN_SECONDS = 0.25f;

    private final Level level;
    private final List<Enemy> enemyList = new ArrayList<>();
    @Getter
    private final List<Tower> towerList = new ArrayList<>();
    private final GameScreen screen;

    public World(GameScreen screen, ClickListener clickListener) {
        this.screen = screen;
        this.level = new Level(clickListener, screen.getShapeRenderer());
        this.level.left().bottom();
        addActor(level);
    }

    public void addEnemy(Enemy enemy) {
        enemyList.add(enemy);
        addActor(enemy);
    }

    public void addTower(Tower tower) {
        towerList.add(tower);
    }

    public void buildTower(Tower tower, SlotStack slotStack) {
        slotStack.setTower(tower);
        slotStack.add(tower);
        slotStack.validate();
        addTower(tower);
    }

    public void update() {
        for (int i = 0; i < towerList.size(); i++) {
            updateTower(towerList.get(i));
        }
    }

    private void updateTower(Tower tower) {
        if (tower.getReloadingTime() > 0) {
            tower.setReloadingTime(tower.getReloadingTime() - Gdx.graphics.getDeltaTime());
            Enemy target = tower.getTarget();
            if (target != null && target.isAlive()) {
                Vector2 targetPos = target.localToStageCoordinates(new Vector2());
                tower.getTargetPos().set(targetPos.x + target.getWidth() / 2,
                        targetPos.y + target.getHeight() / 2);
            }
        } else {
            Enemy target = tower.getTarget();
            if (target == null || !target.isAlive()) {
                target = searchNearestTarget(tower.localToStageCoordinates(new Vector2()));
                tower.setTarget(target);
            }

            if (target != null) {
                target.attack(tower.getDamage());
                Vector2 targetPos = target.localToStageCoordinates(new Vector2());
                tower.getTargetPos().set(targetPos.x + target.getWidth() / 2,
                        targetPos.y + target.getHeight() / 2);
                tower.setReloadingTime(TOWER_RELOAD_TIME_IN_SECONDS);
                if (target.getHp() <= 0) {
                    target.remove();
                    enemyList.remove(target);
                    screen.onEnemyDeath();
                }
            }
        }
    }

    private Enemy searchNearestTarget(Vector2 srcPoint) {
        if (enemyList.size() == 0) return null;

        Enemy nearestEnemy = enemyList.get(0);
        Vector2 enemyPos = nearestEnemy.localToStageCoordinates(new Vector2(0, 0));
        float minDistanceSq = srcPoint.dst2(enemyPos);
        for (int i = 1; i < enemyList.size(); i++) {
            Enemy enemy = enemyList.get(i);
            enemy.localToStageCoordinates(enemyPos.set(0, 0));
            float distanceSq = srcPoint.dst2(enemyPos);
            if (distanceSq < minDistanceSq) {
                nearestEnemy = enemy;
                minDistanceSq = distanceSq;
            }
        }

        return nearestEnemy;
    }

    public void generateWave() {
        Random random = new Random();
        int x = 0;
        int y = LEVEL_SIZE;
        int spawnCount = MIN_ENTITY_SPAWN_COUNT + random.nextInt(MAX_ENTITY_SPAWN_COUNT - MIN_ENTITY_SPAWN_COUNT + 1);
        for (int i = 0; i < spawnCount; i++) {
            int enemyType = random.nextInt(3);
            Enemy enemy = new Enemy(enemyType, screen.getEnemyTexture(enemyType), screen.getShapeRenderer());
            enemy.setPosition(
                    x + SLOT_SIZE_IN_PIXELS / 2 - ENTITY_SIZE / 2,
                    y + SLOT_SIZE_IN_PIXELS / 2 - ENTITY_SIZE / 2
            );
            enemy.setSize(ENTITY_SIZE, ENTITY_SIZE);

            addEnemy(enemy);

            Vector2 slotPosition = getSlotPosition(0, 0);
            float velocity = (enemy.getY() - slotPosition.y) / SLOT_SIZE_IN_PIXELS;
            enemy.addAction(Actions.sequence(Actions.moveTo(slotPosition.x, slotPosition.y,
                            velocity / enemy.getMoveSpeed()),
                    Actions.run(() -> findNextPath(enemy))));

            y += SLOT_SIZE_IN_PIXELS;
        }
    }

    private Vector2 getSlotPosition(int i, int j) {
        return new Vector2(
                j * SLOT_SIZE_IN_PIXELS + SLOT_SIZE_IN_PIXELS / 2 - ENTITY_SIZE / 2,
                -i * SLOT_SIZE_IN_PIXELS + LEVEL_SIZE - SLOT_SIZE_IN_PIXELS + ENTITY_SIZE / 2
        );
    }

    private void findNextPath(Enemy enemy) {
        Vector2 pathPosition = enemy.getPathPosition();
        SlotStack[][] matrix = level.getMatrix();
        int nextY = (int) (pathPosition.y + 1);
        if (nextY < matrix.length && matrix[nextY][(int) pathPosition.x].getSlot().getType() == SlotType.PATH) {
            setNextPathAndAction(enemy, nextY, (int) pathPosition.x);
            return;
        }

        int nextX = (int) (pathPosition.x + 1);
        if (nextX < matrix[0].length && matrix[(int) pathPosition.y][nextX].getSlot().getType() == SlotType.PATH) {
            setNextPathAndAction(enemy, (int) pathPosition.y, nextX);
            return;
        }

        pauseEnemies();
        screen.onGameOver();
    }

    private void setNextPathAndAction(Enemy enemy, int i, int j) {
        enemy.setPathPosition(j, i);
        Vector2 slotPosition = getSlotPosition(i, j);
        enemy.addAction(Actions.sequence(Actions.moveTo(slotPosition.x, slotPosition.y, 1.0f / enemy.getMoveSpeed()),
                Actions.run(() -> findNextPath(enemy))));
    }

    public void pauseEnemies() {
        for (int i = 0; i < enemyList.size(); i++) {
            enemyList.get(i).clearActions();
        }
    }

    public int getTowerCount() {
        return towerList.size();
    }

    public int getEnemiesCount() {
        return enemyList.size();
    }

    @Override
    public void clear() {
        super.clear();
        enemyList.clear();
        towerList.clear();
    }
}
