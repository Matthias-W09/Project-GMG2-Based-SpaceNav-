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
    private int velXAsteroides; 
    private int velYAsteroides; 
    private int cantAsteroides;
    
    private Nave4 nave;
    private List<Enemy> enemies = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
            int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;
        
        batch = game.getBatch();
        camera = new OrthographicCamera();    
        camera.setToOrtho(false, 800, 640);
        //inicializar assets; musica de fondo y efectos de sonido
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1,0.5f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav")); //
        
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();
        
        // cargar imagen de la nave, 64x64   
        nave = new Nave4(Gdx.graphics.getWidth()/2-50,30,new Texture(Gdx.files.internal("MainShip3.png")),
                        Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
                        new Texture(Gdx.files.internal("Rocket2.png")), 
                        Gdx.audio.newSound(Gdx.files.internal("pop-sound.ogg"))); 

        Random r = new Random();
        Texture weakEnemyTexture = new Texture(Gdx.files.internal("weakEnemy.png"));
        Texture strongEnemyTexture = new Texture(Gdx.files.internal("strongEnemy.png"));
        Texture bulletTexture = new Texture(Gdx.files.internal("rocket3.png"));

        for (int i = 0; i < cantAsteroides; i++) {
            if (r.nextBoolean()) {
                WeakEnemy enemy = new WeakEnemy(
                    r.nextInt(Gdx.graphics.getWidth()),
                    50 + r.nextInt(Gdx.graphics.getHeight() - 50),
                    20 + r.nextInt(10),
                    velXAsteroides,
                    velYAsteroides,
                    weakEnemyTexture,
                    bulletTexture
                );
                enemies.add(enemy);
            } else {
                StrongEnemy enemy = new StrongEnemy(
                    r.nextInt(Gdx.graphics.getWidth()),
                    50 + r.nextInt(Gdx.graphics.getHeight() - 50),
                    20 + r.nextInt(10),
                    velXAsteroides,
                    velYAsteroides,
                    strongEnemyTexture
                );
                enemies.add(enemy);
            }
        }
    }
    
    public void dibujaEncabezado() {
        CharSequence str = "Vidas: "+nave.getVidas()+" Ronda: "+ronda;
        game.getFont().getData().setScale(2f);        
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score:"+this.score, Gdx.graphics.getWidth()-150, 30);
        game.getFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
    }

@Override
public void render(float delta) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
    // Iniciar el batch para dibujar
    batch.begin();
    
    // Dibujar encabezado
    dibujaEncabezado();
    
    if (!nave.estaHerido()) {
        // Actualizar lógica de juego solo si la nave no está herida

        // Colisiones entre balas y enemigos y su destrucción  
        for (Iterator<Bullet> it = balas.iterator(); it.hasNext(); ) {
            Bullet b = it.next();
            b.update();
            for (Iterator<Enemy> enemyIt = enemies.iterator(); enemyIt.hasNext(); ) {
                Enemy enemy = enemyIt.next();
                if (b.checkCollision(enemy)) {          
                    explosionSound.play();
                    enemy.takeDamage(1);
                    if (enemy.isDead()) {
                        enemyIt.remove();
                        score += 10;
                    }
                    it.remove();
                    break;
                }
            }
        }
        
        // Actualizar enemigos
        for (Iterator<Enemy> enemyIt = enemies.iterator(); enemyIt.hasNext(); ) {
            Enemy enemy = enemyIt.next();
            enemy.update();
            if (enemy instanceof WeakEnemy) {
                WeakEnemy weakEnemy = (WeakEnemy) enemy;
                for (Iterator<Bullet> bulletIt = weakEnemy.getBullets().iterator(); bulletIt.hasNext(); ) {
                    Bullet bullet = bulletIt.next();
                    bullet.update();
                    if (nave.checkCollision(bullet)) {
                        bulletIt.remove();
                    }
                }
            }
            if (nave.checkCollision(enemy)) {
                enemyIt.remove();
            }
        }
        
        // Nivel completado
        if (enemies.isEmpty()) {
            Screen ss = new PantallaJuego(game, ronda + 1, nave.getVidas() + 2, score, 
                    +1, velYAsteroides+1, cantAsteroides + 5);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
    }
    
    // Dibujar enemigos y balas siempre
    for (Enemy enemy : enemies) {
        enemy.draw(batch);
        if (enemy instanceof WeakEnemy) {
            WeakEnemy weakEnemy = (WeakEnemy) enemy;
            for (Bullet bullet : weakEnemy.getBullets()) {
                bullet.draw(batch);
            }
        }
    }
    
    // Dibujar balas de la nave siempre
    for (Bullet b : balas) {
        b.draw(batch);
    }
    
    nave.draw(batch, this);
    
    if (nave.estaDestruido()) {
        if (score > game.getHighScore())
            game.setHighScore(score);
        Screen ss = new PantallaGameOver(game);
        ss.resize(1200, 800);
        game.setScreen(ss);
        dispose();
    }
    
    // Fin del batch para dibujar
    batch.end();
}

    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }

    @Override
    public void show() {
        gameMusic.play();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        this.explosionSound.dispose();
        this.gameMusic.dispose();
    }
}
