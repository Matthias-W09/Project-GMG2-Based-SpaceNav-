package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class StrongEnemy extends Enemy {

    public StrongEnemy(int x, int y, int size, int xSpeed, int ySpeed, Texture tx) {
        super(x, y, size, xSpeed, ySpeed, tx, 2); // 2 hits to kill
    }

    @Override
    public void attack() {
        // No shooting for StrongEnemy, so this can be left empty or have collision damage logic
    }
}
