package com.mygdx.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaJuego implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Sound explosionSound;
    private Music gameMusic;
    private int score;
    private int ronda;
    private int velXEnemy; 
    private int velYEnemy; 
    private int cantEnemy;
    private Texture backgroundImage;


    private Llama llama;
    private List<Enemy> enemies = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
        int velXEnemy, int velYEnemy, int cantEnemy) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXEnemy = velXEnemy;
        this.velYEnemy = velYEnemy;
        this.cantEnemy = cantEnemy;

        this.batch = game.getBatch();
        this.camera = new OrthographicCamera();    
        this.camera.setToOrtho(false, 1200, 800);

        // Initialize assets
        this.explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        this.gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
        setupAudio();

        // Load textures and initialize game objects
        backgroundImage = new Texture(Gdx.files.internal("lava.png"));
        this.llama = initializeLlama(vidas);
        initializeEnemies();
    }

    private void setupAudio() {
        explosionSound.setVolume(1, 0.5f);
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();
    }

    private Llama initializeLlama(int vidas) {
        Llama llama =  new Llama(
            Gdx.graphics.getWidth() / 2 - 50, 30,
            new Texture(Gdx.files.internal("MainShip3.png")),
            Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
            new Texture(Gdx.files.internal("Rocket2.png")),
            Gdx.audio.newSound(Gdx.files.internal("pop-sound.ogg"))
        );
        llama.setVidas(vidas);
        return llama;
    }

    private void initializeEnemies() {
        Random random = new Random();
        Texture weakEnemyTexture = new Texture(Gdx.files.internal("weakEnemy.png"));
        Texture strongEnemyTexture = new Texture(Gdx.files.internal("strongEnemy.png"));
        Texture bulletTexture = new Texture(Gdx.files.internal("rocket3.png"));

        for (int i = 0; i < cantEnemy; i++) {
            if (random.nextBoolean()) {
                enemies.add(new WeakEnemy(
                    random.nextInt(Gdx.graphics.getWidth()),
                    50 + random.nextInt(Gdx.graphics.getHeight() - 50),
                    20 + random.nextInt(10),
                    velXEnemy, velYEnemy, weakEnemyTexture, bulletTexture
                ));
            } else {
                enemies.add(new StrongEnemy(
                    random.nextInt(Gdx.graphics.getWidth()),
                    50 + random.nextInt(Gdx.graphics.getHeight() - 50),
                    20 + random.nextInt(10),
                    velXEnemy, velYEnemy, strongEnemyTexture
                ));
            }
        }
    }

    private void dibujaEncabezado() {
        CharSequence str = "Vidas: " + llama.getVidas() + " Ronda: " + ronda;
        game.getFont().getData().setScale(2f);
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score:" + this.score, Gdx.graphics.getWidth() - 150, 30);
        game.getFont().draw(batch, "HighScore:" + game.getHighScore(), Gdx.graphics.getWidth() / 2 - 100, 30);
    }

    @Override
    public void render(float delta) {
        game.getBatch().begin();
        game.getBatch().draw(backgroundImage, 0, 0, 1200, 800); // Ajusta las dimensiones según sea necesario
        game.getBatch().end();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        dibujaEncabezado();
        if (!llama.estaHerido()) {
            updateGameLogic();
            checkLevelCompletion();
        }
        drawEntities();
        checkGameOver();
        batch.end();
    }

    private void updateGameLogic() {
        updateBullets();
        updateEnemies();
    }

    private void updateBullets() {
        for (Iterator<Bullet> it = balas.iterator(); it.hasNext(); ) {
            Bullet bullet = it.next();
            bullet.update();
            if (checkBulletCollisions(bullet)) {
                it.remove();
            }
        }
    }

    private boolean checkBulletCollisions(Bullet bullet) {
        for (Iterator<Enemy> enemyIt = enemies.iterator(); enemyIt.hasNext(); ) {
            Enemy enemy = enemyIt.next();
            if (bullet.checkCollision(enemy)) {
                explosionSound.play();
                enemy.takeDamage(1);
                if (enemy.isDead()) {
                    enemyIt.remove();
                    score += 10;
                }
                return true;
            }
        }
        return false;
    }

    private void updateEnemies() {
        for (Iterator<Enemy> enemyIt = enemies.iterator(); enemyIt.hasNext(); ) {
            Enemy enemy = enemyIt.next();
            enemy.update();
            if (enemy instanceof WeakEnemy) {
                updateWeakEnemyBullets((WeakEnemy) enemy);
            }
            if (llama.checkCollision(enemy)) {
                enemyIt.remove();
            }
        }
    }

    private void updateWeakEnemyBullets(WeakEnemy enemy) {
        for (Iterator<Bullet> bulletIt = enemy.getBullets().iterator(); bulletIt.hasNext(); ) {
            Bullet bullet = bulletIt.next();
            bullet.update();
            if (llama.checkCollision(bullet)) {
                bulletIt.remove();
            }
        }
    }

    private void checkLevelCompletion() {
        if (enemies.isEmpty()) {
            ronda++;
            llama.setVidas(llama.getVidas() + 3);
            velXEnemy++;
            velYEnemy++;
            cantEnemy += 5;

            Screen nextScreen = new PantallaJuego(game, ronda, llama.getVidas(), score, velXEnemy, velYEnemy, cantEnemy);
            nextScreen.resize(1200, 800);
            game.setScreen(nextScreen);
            dispose();
        }
    }

    private void drawEntities() {
        for (Enemy enemy : enemies) {
            enemy.draw(batch);
            if (enemy instanceof WeakEnemy) {
                for (Bullet bullet : ((WeakEnemy) enemy).getBullets()) {
                    bullet.draw(batch);
                }
            }
        }

        for (Bullet bullet : balas) {
            bullet.draw(batch);
        }

        llama.draw(batch, this);
    }

    private void checkGameOver() {
        if (llama.estaDestruido()) {
            if (score > game.getHighScore()) {
                game.setHighScore(score);
            }
            Screen gameOverScreen = new PantallaGameOver(game);
            gameOverScreen.resize(1200, 800);
            game.setScreen(gameOverScreen);
            dispose();
        }
    }

    public boolean agregarBala(Bullet bullet) {
        return balas.add(bullet);
    }

    @Override
    public void show() {
        gameMusic.play();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        explosionSound.dispose();
        gameMusic.dispose();
    }
}
