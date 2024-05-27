package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaMenu implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;
    private Music mainMenuMusic;
    private Texture backgroundImage;
    private Rectangle startButton;

    public PantallaMenu(SpaceNavigation game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);
        
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("mainMenuSong.wav"));
        mainMenuMusic.setLooping(true);
        mainMenuMusic.setVolume(0.5f);


        backgroundImage = new Texture(Gdx.files.internal("portada.jpg"));

        // Posición y tamaño del botón de inicio
        startButton = new Rectangle(400, 100, 400, 100); // Ajusta las coordenadas y dimensiones según sea necesario
        
        mainMenuMusic.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        game.getBatch().begin();
        game.getBatch().draw(backgroundImage, 0, 0, 1200, 800);
        game.getBatch().end();

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        // Dibujar el botón de inicio
        game.getBatch().begin();
        game.getBatch().draw(new Texture("startButton.png"), startButton.x, startButton.y, startButton.width, startButton.height);
        game.getBatch().end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            // Verificar si se hizo clic en el botón de inicio
            if (startButton.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                // Iniciar el juego
                Screen ss = new PantallaJuego(game, 1, 3, 0, 1, 1, 10);
                ss.resize(1200, 800);
                game.setScreen(ss);
                dispose();
            }
        }
    }

    @Override
    public void show() {
        // Método show
        mainMenuMusic.play();
    }

    @Override
    public void resize(int width, int height) {
        // Método resize
    }

    @Override
    public void pause() {
        // Método pause
    }

    @Override
    public void resume() {
        // Método resume
    }

    @Override
    public void hide() {
        // Método hide
    }

    @Override
    public void dispose() {
        // Método dispose
        mainMenuMusic.dispose(); // Liberar los recursos de la música
        backgroundImage.dispose(); // Liberar los recursos de la imagen de fondo
    }
}