package dk.sdu.mmmi.cbse.entities;

import dk.sdu.mmmi.cbse.main.Game;

public class SpaceObject {

    protected float x;
    protected float y;

    protected float dx;
    protected float dy;

    protected float radians;
    protected float speed;
    protected float rotationSpeed;

    protected int width;
    protected int height;

    protected float[] shapex;
    protected float[] shapey;

    public float getRadians() {
        return radians;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
    public float[] getShapex() {
        return shapex;
    }
    
    public float[] getShapey() {
        return shapey;
    }

    protected void wrap() {
        if (x < 0) {
            x = Game.WIDTH;
        }
        if (x > Game.WIDTH) {
            x = 0;
        }
        if (y < 0) {
            y = Game.HEIGHT;
        }
        if (y > Game.HEIGHT) {
            y = 0;
        }
    }
    
    public void checkCollision(SpaceObject other) {
        float a = x - other.getX();
        float b = y - other.getY();
        
        double c = Math.sqrt((double) (a * a) + (double) (b * b));
        
        if(c < 8) {
            System.out.println("hit");
        }
        
        //Math.sqrt(Math.pow(x - other.getX(), 2) + Math.pow(y - other.getY(), 2));
    }

}
