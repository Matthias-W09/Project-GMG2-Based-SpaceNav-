package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WeakEnemy extends Enemy {
    private List<Bullet> bullets;
    private Texture bulletTexture;
    private float shootCooldown;
    private float timeSinceLastShot;

    public WeakEnemy(int x, int y, int size, int xSpeed, int ySpeed, Texture tx, Texture bulletTx) {
        super(x, y, size, xSpeed, ySpeed, tx, 1); // 1 hit to kill
        this.bulletTexture = bulletTx;
        bullets = new ArrayList<>();
        shootCooldown = 1.0f; // Cooldown of 1 second between shots
        timeSinceLastShot = 0;
    }

    @Override
    public void update() {
        super.update();
        timeSinceLastShot += Gdx.graphics.getDeltaTime();
        if (timeSinceLastShot >= shootCooldown) {
            shoot();
            timeSinceLastShot = 0;
        }

        for (Iterator<Bullet> it = bullets.iterator(); it.hasNext(); ) {
            Bullet bullet = it.next();
            bullet.update();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        for (Bullet bullet : bullets) {
            bullet.draw(batch);
        }
    }

    @Override
    public void attack() {
        shoot();
    }

    private void shoot() {
        // Crear una bala que se mueva hacia abajo
        bullets.add(new Bullet(
            spr.getX() + spr.getWidth() / 2, // Posición x
            spr.getY(), // Posición y
            0, // Velocidad x
            -2, // Velocidad y (hacia abajo)
            bulletTexture // Textura de la bala
        ));
    }

    public List<Bullet> getBullets() {
        return bullets;
    }
}
