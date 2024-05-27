
package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public interface Colisionable {
    
    boolean checkCollision(Colisionable other);
    public Rectangle getArea();
    public int getXSpeed();
    public int getYSpeed();
    public void setXSpeed(int xSpeed);
    public void setYSpeed(int xSpeed);
}
