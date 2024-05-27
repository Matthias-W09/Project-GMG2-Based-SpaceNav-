package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;



public class Nave4 {
	
	private boolean destruida = false;
    private int vidas = 3;
    private float xVel = 0;
    private float yVel = 0;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax=50;
    private int tiempoHerido;
    
    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
    	sonidoHerido = soundChoque;
    	this.soundBala = soundBala;
    	this.txBala = txBala;
    	spr = new Sprite(tx);
    	spr.setPosition(x, y);
    	//spr.setOriginCenter();
    	spr.setBounds(x, y, 45, 45);

    }
    
    public Rectangle getArea() {
    	return spr.getBoundingRectangle();
    }
    
public void draw(SpriteBatch batch, PantallaJuego juego) {
    float x = spr.getX();
    float y = spr.getY();
    float maxSpeed = 3f; // Velocidad máxima de la llama (más lenta que la bala)
    float acceleration = 0.1f; // Aceleración de la llama
    float deceleration = 0.05f; // Desaceleración de la llama
    float bulletSpeed = 6f; // Velocidad de la bala (más rápida que la llama)

    if (!herido) {
        // Movimiento continuo con aceleración
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) xVel = Math.max(xVel - acceleration, -maxSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) xVel = Math.min(xVel + acceleration, maxSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) yVel = Math.max(yVel - acceleration, -maxSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) yVel = Math.min(yVel + acceleration, maxSpeed);

        // Desaceleración gradual cuando no se presionan las teclas
        if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xVel = xVel > 0 ? Math.max(xVel - deceleration, 0) : Math.min(xVel + deceleration, 0);
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.DOWN) && !Gdx.input.isKeyPressed(Input.Keys.UP)) {
            yVel = yVel > 0 ? Math.max(yVel - deceleration, 0) : Math.min(yVel + deceleration, 0);
        }

        // Rotación de la llama
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) spr.setRotation(spr.getRotation() - 90);
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) spr.setRotation(spr.getRotation() + 90);

        // Que se mantenga dentro de los bordes de la ventana
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
        spr.draw(batch);
    } else {
        spr.setX(spr.getX() + MathUtils.random(-2, 2));
        spr.draw(batch);
        spr.setX(x);
        tiempoHerido--;
        if (tiempoHerido <= 0) herido = false;
    }

    // Disparo
    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
        float rotation = spr.getRotation();
        float rad = (float) Math.toRadians(rotation);
        float bulletVelX = (float) (bulletSpeed * Math.cos(rad));
        float bulletVelY = (float) (bulletSpeed * Math.sin(rad));

        // Calcular la posición de la bala en el frente de la llama
        float bulletX = (float) (spr.getX() + spr.getWidth() / spr.getHeight() / 2 * Math.cos(rad));
        float bulletY = (float) (spr.getY() + spr.getHeight() / spr.getHeight() / 2 * Math.sin(rad));

        Bullet bala = new Bullet(bulletX, bulletY, bulletVelX, bulletVelY, txBala);
        juego.agregarBala(bala);
        soundBala.play();
    }
}
   
    public boolean checkCollision(Enemy b) {
        if(!herido && b.getArea().overlaps(spr.getBoundingRectangle())){
        	// rebote
            if (xVel ==0) xVel += b.getXSpeed()/2;
            if (b.getXSpeed() ==0) b.setXSpeed(b.getXSpeed() + (int)xVel/2);
            xVel = - xVel;
            b.setXSpeed(-b.getXSpeed());
            
            if (yVel ==0) yVel += b.getySpeed()/2;
            if (b.getySpeed() ==0) b.setySpeed(b.getySpeed() + (int)yVel/2);
            yVel = - yVel;
            b.setySpeed(- b.getySpeed());
            vidas--;
            herido = true;
  		    tiempoHerido=tiempoHeridoMax;
  		    sonidoHerido.play();
            if (vidas<=0) 
          	    destruida = true; 
            return true;
        }
        return false;
    }
    
        public boolean checkCollision(Bullet b) {
        if(!herido && b.getArea().overlaps(spr.getBoundingRectangle())){
        	// rebote
            if (xVel ==0) xVel += b.getXSpeed()/2;
            if (b.getXSpeed() ==0) b.setXSpeed(b.getXSpeed() + (int)xVel/2);
            xVel = - xVel;
            b.setXSpeed(-b.getXSpeed());
            
            if (yVel ==0) yVel += b.getySpeed()/2;
            if (b.getySpeed() ==0) b.setySpeed(b.getySpeed() + (int)yVel/2);
            yVel = - yVel;
            b.setySpeed(- b.getySpeed());
            vidas--;
            herido = true;
  		    tiempoHerido=tiempoHeridoMax;
  		    sonidoHerido.play();
            if (vidas<=0) 
          	    destruida = true; 
            return true;
        }
        return false;
    }
    
    public boolean estaDestruido() {
       return !herido && destruida;
    }
    public boolean estaHerido() {
 	   return herido;
    }
    
    public int getVidas() {return vidas;}
    //public boolean isDestruida() {return destruida;}
    public int getX() {return (int) spr.getX();}
    public int getY() {return (int) spr.getY();}
    public void setVidas(int vidas2) {vidas = vidas2;}
}
