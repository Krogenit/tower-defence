package com.td;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.td.gui.GameScreen;

public class TowerDefenseGame extends Game {
    private final AssetManager assetManager = new AssetManager();
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        loadAssets();
        shapeRenderer = new ShapeRenderer();
        GameScreen screen = new GameScreen(assetManager, shapeRenderer);
        screen.create();
        setScreen(screen);
    }

    private void loadAssets() {
        for (int i = 0; i < 3; i++) {
            assetManager.load("texture/entity/enemy/" + i + ".png", Texture.class);
        }

        assetManager.load("texture/entity/tower/0.png", Texture.class);
        assetManager.load("texture/gui/button.png", Texture.class);
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter parms = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parms.fontFileName = "font/consola.ttf";
        parms.fontParameters.size = 26;
        assetManager.load("font/consola.ttf", BitmapFont.class, parms);

        assetManager.load("texture/gui/menu.png", Texture.class);

        assetManager.finishLoading();
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
        shapeRenderer.dispose();
    }
}