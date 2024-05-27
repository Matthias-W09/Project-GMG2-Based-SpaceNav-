package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Enemy {
    protected int x;
    protected int y;
    protected int xSpeed;
    protected int ySpeed;
    protected Sprite spr;
    protected int health;

    public Enemy(int x, int y, int size, int xSpeed, int ySpeed, Texture tx, int health) {
        spr = new Sprite(tx);
        this.x = x; 
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.health = health;

        // Validate position to ensure the enemy is within bounds
        if (x - size < 0) this.x = x + size;
        if (x + size > Gdx.graphics.getWidth()) this.x = x - size;
        if (y - size < 0) this.y = y + size;
        if (y + size > Gdx.graphics.getHeight()) this.y = y - size;

        spr.setPosition(this.x, this.y);
    }

    public void update() {
        x += xSpeed;
        y += ySpeed;

        if (x + xSpeed < 0 || x + xSpeed + spr.getWidth() > Gdx.graphics.getWidth())
            xSpeed *= -1;
        if (y + ySpeed < 0 || y + ySpeed + spr.getHeight() > Gdx.graphics.getHeight())
            ySpeed *= -1;
        spr.setPosition(x, y);
    }

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public abstract void attack();

    public void takeDamage(int damage) {
        health -= damage;
    }

    public boolean isDead() {
        return health <= 0;
    }
    
    	public int getXSpeed() {
		return xSpeed;
	}
	public void setXSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}
	public int getySpeed() {
		return ySpeed;
	}
	public void setySpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}
	
    

    public void checkCollision(Enemy enemy) {
        if (spr.getBoundingRectangle().overlaps(enemy.spr.getBoundingRectangle())) {
            // Bounce
            if (xSpeed == 0) xSpeed += enemy.xSpeed / 2;
            if (enemy.xSpeed == 0) enemy.xSpeed += xSpeed / 2;
            xSpeed *= -1;
            enemy.xSpeed *= -1;

            if (ySpeed == 0) ySpeed += enemy.ySpeed / 2;
            if (enemy.ySpeed == 0) enemy.ySpeed += ySpeed / 2;
            ySpeed *= -1;
            enemy.ySpeed *= -1;
        }
    }
}
