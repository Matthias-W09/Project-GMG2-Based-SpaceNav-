package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpaceNavigation extends Game {

    private static final String NOMBRE_JUEGO = "Space Navigation";
    private SpriteBatch batch;
    private BitmapFont font;
    private int highScore;

    @Override
    public void create() {
        highScore = 0;
        batch = new SpriteBatch();
        font = new BitmapFont(); // Usa Arial font por defecto
        font.getData().setScale(2f);
        setInitialScreen();
    }

    private void setInitialScreen() {
        Screen initialScreen = new PantallaMenu(this);
        setScreen(initialScreen);
    }

    @Override
    public void render() {
        super.render(); // Importante para que se realicen las llamadas a render() de las pantallas
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
}