package com.td.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.td.entity.Tower;
import com.td.world.Level;
import com.td.world.Slot;
import com.td.world.SlotStack;
import com.td.world.World;
import lombok.Getter;

import java.util.List;

public class GameScreen extends ScreenAdapter {
    public static final int SLOT_SIZE_IN_PIXELS = 40;
    public static final int LEVEL_SIZE = Level.MAP_SIZE * SLOT_SIZE_IN_PIXELS;
    private static final int TOWERS_TO_START = 5;
    private static final float TOWER_FIRE_EFFECT_DRAW_TIME_IN_SECONDS = 0.2f;
    private static final Matrix4 IDENTITY_MATRIX = new Matrix4();

    private final AssetManager assetManager;
    @Getter
    private final ShapeRenderer shapeRenderer;
    private final Texture towerTexture;

    private Stage stage;
    private boolean pause = true;
    private Table root;
    private Label gameStateLabel;
    private Label howToStartLabel;
    private Window failWindow;
    private Window successWindow;

    private Tower selectedTower;
    private Tower previewTower;
    private World world;

    public GameScreen(AssetManager assetManager, ShapeRenderer shapeRenderer) {
        this.assetManager = assetManager;
        this.shapeRenderer = shapeRenderer;
        this.towerTexture = assetManager.get("texture/entity/tower/0.png");
    }

    public void create() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.setDebug(true); // This is optional, but enables debug lines for tables.

        successWindow = createWindow(
                "Congratulations! Level has passed.",
                "Exit",
                "Restart",
                () -> Gdx.app.exit(),
                this::restart);
        failWindow = createWindow(
                "Level failed.",
                "Exit",
                "Restart",
                () -> Gdx.app.exit(),
                this::restart);

        createWorld();
        createUI();
    }

    private Window createWindow(String description, String leftButtonText, String rightButtonText,
                                Runnable leftButtonRunnable, Runnable rightButtonRunnable) {
        Texture buttonTexture = assetManager.get("texture/gui/button.png");
        Texture windowTexture = assetManager.get("texture/gui/menu.png");
        BitmapFont font = assetManager.get("font/consola.ttf");
        Window window = new Window("", new Window.WindowStyle(
                font, Color.WHITE, new TextureRegionDrawable(windowTexture)
        ));
        window.setMovable(false);

        int pad = 10;
        int buttonWidth = 200;
        Label label = new Label(description, new Label.LabelStyle(font, Color.WHITE));
        label.setFontScale(1.0f);
        window.add(label).padTop(pad);
        window.row();
        Table buttonsTable = new Table();
        ImageTextButton leftButton = new ImageTextButton(leftButtonText, new ImageTextButton.ImageTextButtonStyle(
                new TextureRegionDrawable(buttonTexture), null, null, font
        ));
        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                leftButtonRunnable.run();
            }
        });
        buttonsTable.add(leftButton).pad(pad).width(buttonWidth).left().bottom();
        ImageTextButton rightButton = new ImageTextButton(rightButtonText, new ImageTextButton.ImageTextButtonStyle(
                new TextureRegionDrawable(buttonTexture), null, null, font
        ));
        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                rightButtonRunnable.run();
            }
        });
        buttonsTable.add(rightButton).pad(pad).width(buttonWidth).right().bottom();
        window.add(buttonsTable);
        window.pack();
        int width = (int) Math.max(label.getWidth() + (pad << 1), (buttonWidth << 1) + (pad << 2));
        int height = 160;
        window.setBounds((Gdx.graphics.getWidth() - width) / 2,
                (Gdx.graphics.getHeight() - height) / 2, width, height);
        return window;
    }

    private void createWorld() {
        world = new World(this, new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                Slot slot = (Slot) event.getTarget();
                SlotStack slotStack = (SlotStack) slot.getParent();

                if (selectedTower != null && slot.canBuild() && previewTower == null) {
                    Tower tower = new Tower(towerTexture);
                    tower.setTouchable(Touchable.disabled);
                    tower.setSize(SLOT_SIZE_IN_PIXELS, SLOT_SIZE_IN_PIXELS);
                    slotStack.add(tower);
                    previewTower = tower;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if (previewTower != null) {
                    previewTower.remove();
                    previewTower = null;
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                Slot slot = (Slot) event.getTarget();
                SlotStack slotStack = (SlotStack) slot.getParent();
                if (world.getTowerCount() < TOWERS_TO_START && previewTower != null) {
                    world.buildTower(previewTower, slotStack);
                    previewTower = null;
                    onTowerAdded();
                }
            }
        });

        root.add(world).width(LEVEL_SIZE).height(LEVEL_SIZE).center();
    }

    private void createUI() {
        Table table = new Table();
        root.add(table).width(300).top();
        BitmapFont font = assetManager.get("font/consola.ttf");
        float headerFontSize = 1.0f;
        float descFontSize = 0.65f;
        gameStateLabel = new Label("Paused", new Label.LabelStyle(font, Color.WHITE));
        gameStateLabel.setFontScale(headerFontSize);
        table.add(gameStateLabel).padTop(10);
        table.row();

        howToStartLabel = new Label("Place 5 towers to begin",
                new Label.LabelStyle(font, Color.WHITE));
        howToStartLabel.setFontScale(descFontSize);
        table.add(howToStartLabel);
        table.row();

        Label towersLabel = new Label("Towers",
                new Label.LabelStyle(font, Color.WHITE));
        towersLabel.setFontScale(headerFontSize);
        table.add(towersLabel).padTop(50);
        table.row();

        Label towerSelectionHint = new Label("Select tower to place",
                new Label.LabelStyle(font, Color.WHITE));
        towerSelectionHint.setFontScale(descFontSize);
        table.add(towerSelectionHint);
        table.row();

        Table towersTable = new Table();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addTowerButton(towersTable, 0.6f, font);
            }

            towersTable.row();
        }
        table.add(towersTable).padTop(10);
    }

    private void addTowerButton(Table table, float descFontSize, BitmapFont font) {
        ImageButton button = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(towerTexture)
        ));
        Label towerLabel = new Label("Laser tower",
                new Label.LabelStyle(font, Color.WHITE));
        towerLabel.setFontScale(descFontSize);
        table.add(button).width(40).height(60).padLeft(30).padRight(30);
        button.row();
        button.add(towerLabel);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (pause) {
                    selectedTower = new Tower(towerTexture);
                }
            }
        });
    }

    private void restart() {
        clear();
        createWorld();
        createUI();
    }

    private void onTowerAdded() {
        if (world.getTowerCount() == TOWERS_TO_START) {
            beginGame();
            selectedTower = null;
        }
    }

    private void beginGame() {
        world.generateWave();
        pause = false;
        gameStateLabel.setText("Playing");
        howToStartLabel.setText("Remaining enemies: " + world.getEnemiesCount());
    }

    public void update() {
        if (!pause) {
            world.update();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.075f, 0.1f, 0.15f, 1.0f);

        update();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        renderTowerEffects();
    }

    private void renderTowerEffects() {
        List<Tower> towers = world.getTowerList();
        shapeRenderer.setTransformMatrix(IDENTITY_MATRIX);
        shapeRenderer.setProjectionMatrix(stage.getViewport().getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);
        for (int i = 0; i < towers.size(); i++) {
            Tower tower = towers.get(i);
            if (World.TOWER_RELOAD_TIME_IN_SECONDS - tower.getReloadingTime() <= TOWER_FIRE_EFFECT_DRAW_TIME_IN_SECONDS) {
                drawAttackEffect(tower);
            }
        }
        shapeRenderer.end();
    }

    private void drawAttackEffect(Tower tower) {
        Vector2 towerPos = tower.localToStageCoordinates(new Vector2());
        Vector2 targetPos = tower.getTargetPos();
        shapeRenderer.line(
                towerPos.x + tower.getWidth() / 2, towerPos.y + tower.getHeight() / 2,
                targetPos.x, targetPos.y
        );
    }

    public void onEnemyDeath() {
        howToStartLabel.setText("Remaining enemies: " + world.getEnemiesCount());
        if (world.getEnemiesCount() == 0) {
            onWin();
        }
    }

    private void onWin() {
        root.addActor(successWindow);
    }

    public void onGameOver() {
        root.addActor(failWindow);
        pause = true;
    }

    public Texture getEnemyTexture(int index) {
        return assetManager.get("texture/entity/enemy/" + index + ".png");
    }

    private void clear() {
        root.clear();
        world.clear();
        pause = true;
    }
}
