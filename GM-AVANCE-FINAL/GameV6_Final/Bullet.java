package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bullet implements Colisionable {

    private int xSpeed;
    private int ySpeed;
    private boolean destroyed = false;
    private Sprite spr;

    /**
     * Constructor para la clase Bullet.
     *
     * @param x       La posición x inicial de la bala.
     * @param y       La posición y inicial de la bala.
     * @param xSpeed  La velocidad en el eje x de la bala.
     * @param ySpeed  La velocidad en el eje y de la bala.
     * @param texture La textura de la bala.
     */
    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture texture) {
        spr = new Sprite(texture);
        spr.setPosition(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    /**
     * Actualiza la posición de la bala.
     */
    public void update() {
        spr.setPosition(spr.getX() + xSpeed, spr.getY() + ySpeed);
        if (spr.getX() < 0 || spr.getX() + spr.getWidth() > Gdx.graphics.getWidth()) {
            destroyed = true;
        }
        if (spr.getY() < 0 || spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) {
            destroyed = true;
        }
    }

    /**
     * Dibuja la bala en la pantalla.
     *
     * @param batch El SpriteBatch utilizado para dibujar la bala.
     */
    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    /**
     * Verifica si la bala ha colisionado con otro objeto.
     *
     * @param colisionable El objeto con el que se está verificando la colisión.
     * @return true si ha ocurrido una colisión, false en caso contrario.
     */
    public boolean checkCollision(Colisionable colisionable) {
        if (spr.getBoundingRectangle().overlaps(colisionable.getArea())) {
            this.destroyed = true;
            return true;
        }
        return false;
    }

    /**
     * Obtiene el área de la bala.
     *
     * @return El área de la bala como un Rectangle.
     */
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    // Getters y Setters

    public int getXSpeed() {
        return xSpeed;
    }

    public void setXSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getYSpeed() {
        return ySpeed;
    }

    public void setYSpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}