package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaMenu implements Screen {

    private final SpaceNavigation game;
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private Music mainMenuMusic;
    private Texture backgroundImage;
    private Texture startButtonTexture;
    private Rectangle startButton;

    public PantallaMenu(SpaceNavigation game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.batch = game.getBatch();
        initializeCamera();
        initializeAssets();
        initializeStartButton();
        playMusic();
    }

    private void initializeCamera() {
        camera.setToOrtho(false, 1200, 800);
    }

    private void initializeAssets() {
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("mainMenuSong.wav"));
        mainMenuMusic.setLooping(true);
        mainMenuMusic.setVolume(0.5f);
        
        backgroundImage = new Texture(Gdx.files.internal("portada.jpg"));
        startButtonTexture = new Texture(Gdx.files.internal("startButton.png"));
    }

    private void initializeStartButton() {
        // Posición y tamaño del botón de inicio
        startButton = new Rectangle(400, 100, 400, 100); // Ajusta las coordenadas y dimensiones según sea necesario
    }

    private void playMusic() {
        mainMenuMusic.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        drawBackground();
        drawStartButton();
        batch.end();

        handleInput();
    }

    private void drawBackground() {
        batch.draw(backgroundImage, 0, 0, 1200, 800);
    }

    private void drawStartButton() {
        batch.draw(startButtonTexture, startButton.x, startButton.y, startButton.width, startButton.height);
    }

    private void handleInput() {
        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            if (startButton.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                startGame();
            }
        }
    }

    private void startGame() {
        Screen pantallaInstructivo = new PantallaInstrucciones(game);
        pantallaInstructivo.resize(1200, 800);
        game.setScreen(pantallaInstructivo);
        dispose();
    }

    @Override
    public void show() {
        playMusic();
    }

    @Override
    public void resize(int width, int height) {
        // Implementación si es necesaria
    }

    @Override
    public void pause() {
        // Implementación si es necesaria
    }

    @Override
    public void resume() {
        // Implementación si es necesaria
    }

    @Override
    public void hide() {
        // Implementación si es necesaria
    }

    @Override
    public void dispose() {
        mainMenuMusic.dispose(); // Liberar los recursos de la música
        backgroundImage.dispose(); // Liberar los recursos de la imagen de fondo
        startButtonTexture.dispose(); // Liberar los recursos del botón de inicio
    }
}