package dk.sdu.mmmi.cbse.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Bullet extends SpaceObject {

    private boolean remove;
    private float timer;
    private SpaceObject owner;

    public Bullet(SpaceObject owner) {
        this.owner = owner;
        this.radians = owner.getRadians();
        this.x = owner.getX() + MathUtils.cos(radians) * 8;
        this.y = owner.getY() + MathUtils.sin(radians) * 8;

        timer = 1;

        speed = 300;
        dx = MathUtils.cos(radians) * speed;
        dy = MathUtils.sin(radians) * speed;
    }

    public boolean shouldRemove() {
        return remove;
    }

    public void update(float dt) {
        x += dx * dt;
        y += dy * dt;
        wrap();

        timer -= dt;
        if (timer < 0) {
            remove = true;
        }
        //System.out.println("x: " + x);
        //System.out.println("y: " + y);
    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(1, 1, 1, 1);
        sr.begin(ShapeRenderer.ShapeType.Line);

        sr.line(x, y, x + MathUtils.cos(radians) * 12, y + MathUtils.sin(radians) * 12);

        sr.end();
    }

    @Override
    public void checkCollision(SpaceObject other) {
        if (other != owner) {
            float otherX = other.getX();
            float otherY = other.getY();

            float a = x - otherX;
            float b = y - otherY;

            double c = Math.sqrt((double) (a * a) + (double) (b * b));

            if (c < 8) {
                System.out.println("hit");
                remove = true;
            }
        }
    }

}
