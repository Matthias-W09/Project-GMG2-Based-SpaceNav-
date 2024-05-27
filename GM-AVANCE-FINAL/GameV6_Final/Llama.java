package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Llama implements Colisionable {

    private static final float MAX_SPEED = 5f;
    private static final float ACCELERATION = 0.1f;
    private static final float DECELERATION = 0.05f;
    private static final float BULLET_SPEED = 6f;
    private static final int TIEMPO_HERIDO_MAX = 50;

    private boolean destruida = false;
    private int vidas;
    private float xVel = 0;
    private float yVel = 0;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHerido;

    /**
     * Constructor para la clase Llama.
     *
     * @param x          La posición x inicial de la llama.
     * @param y          La posición y inicial de la llama.
     * @param tx         La textura de la llama.
     * @param soundChoque El sonido que se reproduce cuando la llama es herida.
     * @param txBala     La textura de la bala.
     * @param soundBala  El sonido que se reproduce cuando la llama dispara.
     */
    public Llama(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        this.sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        this.spr = new Sprite(tx);
        this.spr.setPosition(x, y);
        this.spr.setBounds(x, y, 45, 45);
    }

    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    /**
     * Dibuja la llama en la pantalla y maneja su movimiento y disparo.
     *
     * @param batch El SpriteBatch utilizado para dibujar la llama.
     * @param juego La instancia del juego que contiene la lógica del juego.
     */
    public void draw(SpriteBatch batch, PantallaJuego juego) {
        float x = spr.getX();
        float y = spr.getY();

        if (!herido) {
            handleMovement();
            handleRotation();
            keepWithinBounds();
            spr.setPosition(x + xVel, y + yVel);
            spr.draw(batch);
        } else {
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            shoot(juego);
        }
    }

    private void handleMovement() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) xVel = Math.max(xVel - ACCELERATION, -MAX_SPEED);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) xVel = Math.min(xVel + ACCELERATION, MAX_SPEED);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) yVel = Math.max(yVel - ACCELERATION, -MAX_SPEED);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) yVel = Math.min(yVel + ACCELERATION, MAX_SPEED);

        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xVel = xVel > 0 ? Math.max(xVel - DECELERATION, 0) : Math.min(xVel + DECELERATION, 0);
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.DOWN) && !Gdx.input.isKeyPressed(Input.Keys.UP)) {
            yVel = yVel > 0 ? Math.max(yVel - DECELERATION, 0) : Math.min(yVel + DECELERATION, 0);
        }
    }

    private void handleRotation() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) spr.setRotation(spr.getRotation() - 90);
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) spr.setRotation(spr.getRotation() + 90);
    }

    private void keepWithinBounds() {
        float x = spr.getX();
        float y = spr.getY();

        if (x + xVel < 0) {
            xVel = 0;
            x = 0;
        }
        if (x + xVel + spr.getWidth() > Gdx.graphics.getWidth()) {
            xVel = 0;
            x = Gdx.graphics.getWidth() - spr.getWidth();
        }
        if (y + yVel < 0) {
            yVel = 0;
            y = 0;
        }
        if (y + yVel + spr.getHeight() > Gdx.graphics.getHeight()) {
            yVel = 0;
            y = Gdx.graphics.getHeight() - spr.getHeight();
        }

        spr.setPosition(x + xVel, y + yVel);
    }

    private void shoot(PantallaJuego juego) {
        float rotation = spr.getRotation();
        float rad = (float) Math.toRadians(rotation);
        int  bulletVelX = (int) (BULLET_SPEED * Math.cos(rad));
        int bulletVelY =  (int) (BULLET_SPEED * Math.sin(rad));

        float bulletX = (float) (spr.getX() + spr.getWidth() / spr.getHeight() / 2 * Math.cos(rad));
        float bulletY = (float) (spr.getY() + spr.getHeight() / spr.getHeight() / 2 * Math.sin(rad));

        Bullet bala = new Bullet(bulletX, bulletY, bulletVelX, bulletVelY, txBala);
        juego.agregarBala(bala);
        soundBala.play();
    }

    @Override
    public boolean checkCollision(Colisionable b) {
        if (!herido && b.getArea().overlaps(spr.getBoundingRectangle())) {
            handleCollision(b);
            return true;
        }
        return false;
    }

    private void handleCollision(Colisionable b) {
        if (xVel == 0) xVel += b.getXSpeed() / 2;
        if (b.getXSpeed() == 0) b.setXSpeed(b.getXSpeed() + (int) xVel / 2);
        xVel = -xVel;
        b.setXSpeed(-b.getXSpeed());

        if (yVel == 0) yVel += b.getYSpeed() / 2;
        if (b.getYSpeed() == 0) b.setYSpeed(b.getYSpeed() + (int) yVel / 2);
        yVel = -yVel;
        b.setYSpeed(-b.getYSpeed());

        vidas--;
        herido = true;
        tiempoHerido = TIEMPO_HERIDO_MAX;
        sonidoHerido.play();
        if (vidas <= 0) destruida = true;
    }

    public boolean estaDestruido() {
        return !herido && destruida;
    }

    public boolean estaHerido() {
        return herido;
    }

    // Getters y Setters

    @Override
    public int getXSpeed() {
        return (int) xVel;
    }

    @Override
    public void setXSpeed(int xSpeed) {
        this.xVel = xSpeed;
    }

    @Override
    public int getYSpeed() {
        return (int) yVel;
    }

    @Override
    public void setYSpeed(int ySpeed) {
        this.yVel = ySpeed;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public int getX() {
        return (int) spr.getX();
    }

    public int getY() {
        return (int) spr.getY();
    }
}