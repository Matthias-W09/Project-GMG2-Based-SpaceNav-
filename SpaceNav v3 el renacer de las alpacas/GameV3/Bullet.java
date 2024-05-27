package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {

    private float xSpeed;
    private float ySpeed;
    private boolean destroyed = false;
    private Sprite spr;
    
    public Bullet(float x, float y, float xSpeed, float ySpeed, Texture tx) {
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }
    
    public void update() {
        spr.setPosition(spr.getX() + xSpeed, spr.getY() + ySpeed);
        if (spr.getX() < 0 || spr.getX() + spr.getWidth() > Gdx.graphics.getWidth()) {
            destroyed = true;
        }
        if (spr.getY() < 0 || spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) {
            destroyed = true;
        }
    }
    
    public Rectangle getArea() {
    	return spr.getBoundingRectangle();
    }
    
    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }
    
    public boolean checkCollision(Enemy b2) {
        if (spr.getBoundingRectangle().overlaps(b2.getArea())) {
            // Se destruyen ambos
            this.destroyed = true;
            return true;
        }
        return false;
    }
    
    	public int getXSpeed() {
		return (int) xSpeed;
	}
	public void setXSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}
	public int getySpeed() {
		return (int) ySpeed;
	}
	public void setySpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}
	
    
    public boolean isDestroyed() {
        return destroyed;
    }
}
