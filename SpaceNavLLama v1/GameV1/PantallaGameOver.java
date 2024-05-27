package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;


public class PantallaGameOver implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;
        private Music gameOverMusic;
        private Texture backgroundImage;
        private ShapeRenderer shapeRenderer;

	public PantallaGameOver(SpaceNavigation game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);

        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("gameover-sound.wav"));
        gameOverMusic.setLooping(true);
        gameOverMusic.setVolume(0.5f);

        // Cargar la imagen de fondo
        backgroundImage = new Texture(Gdx.files.internal("gameOverScreen.jpg"));

        // Iniciar la música de game over
        gameOverMusic.play();
    }
        
@Override
public void render(float delta) {
    // Dibujar la imagen de fondo
    game.getBatch().begin();
    game.getBatch().draw(backgroundImage, 0, 0, 1200, 800); // Ajusta las dimensiones según sea necesario
    game.getBatch().end();

    camera.update();
    game.getBatch().setProjectionMatrix(camera.combined);

    game.getBatch().begin(); 
    game.getBatch().end();

    if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
        Screen ss = new PantallaJuego(game, 1, 3, 0, 1, 1, 10);
        ss.resize(1200, 800);
        game.setScreen(ss);
        dispose();
    }
}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		gameOverMusic.play();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		this.gameOverMusic.dispose();
	}
   
}