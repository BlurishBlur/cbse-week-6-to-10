package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

/**
 *
 * @author Niels
 */
public class EnemyControlSystem implements IEntityProcessingService {

    private int actionTimer = 10;
    private double turnChance;
    private double upChance;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity enemy : world.getEntities(EntityType.ENEMY)) {
            float dt = gameData.getDelta();
            //random movement
            actionTimer--;
            if (actionTimer < 0) {
                turnChance = Math.random();
                upChance = Math.random();
                actionTimer = (int) (Math.random() * 90) + 10;
            }

            if (turnChance < 0.40) { // left
                enemy.setRadians(enemy.getRadians() + enemy.getRotationSpeed() * dt);
            }
            else if (turnChance < 0.80) { // right
                enemy.setRadians(enemy.getRadians() - enemy.getRotationSpeed() * dt);
            }

            if (upChance < 0.80) { // up
                enemy.setDx(enemy.getDx() + (float) Math.cos(enemy.getRadians()) * enemy.getAcceleration() * dt);
                enemy.setDy(enemy.getDy() + (float) Math.sin(enemy.getRadians()) * enemy.getAcceleration() * dt);
            }

            // deceleration
            float vec = (float) Math.sqrt(enemy.getDx() * enemy.getDx() + enemy.getDy() * enemy.getDy());
            if (vec > 0) {
                enemy.setDx(enemy.getDx() - (enemy.getDx() / vec) * enemy.getDeacceleration() * dt);
                enemy.setDy(enemy.getDy() - (enemy.getDy() / vec) * enemy.getDeacceleration() * dt);
            }
            if (vec > enemy.getMaxSpeed()) {
                enemy.setDx((enemy.getDx() / vec) * enemy.getMaxSpeed());
                enemy.setDy((enemy.getDy() / vec) * enemy.getMaxSpeed());
            }

            // set position
            enemy.setX(enemy.getX() + enemy.getDx() * dt);
            enemy.setY(enemy.getY() + enemy.getDy() * dt);

            // set shape
            setShape(enemy);

            // screen wrap
            wrap(gameData, enemy);
            
            if(enemy.getLife() < 1) {
                world.removeEntity(enemy);
            }
        }
    }

    private void setShape(Entity enemy) {
        float[] shapex = new float[4];
        float[] shapey = new float[4];

        shapex[0] = enemy.getX() + (float) Math.cos(enemy.getRadians()) * 8;
        shapey[0] = enemy.getY() + (float) Math.sin(enemy.getRadians()) * 8;

        shapex[1] = enemy.getX() + (float) Math.cos(enemy.getRadians() - 4 * 3.1415f / 5) * 8;
        shapey[1] = enemy.getY() + (float) Math.sin(enemy.getRadians() - 4 * 3.1415f / 5) * 8;

        shapex[2] = enemy.getX() + (float) Math.cos(enemy.getRadians() + 3.1415f) * 5;
        shapey[2] = enemy.getY() + (float) Math.sin(enemy.getRadians() + 3.1415f) * 5;

        shapex[3] = enemy.getX() + (float) Math.cos(enemy.getRadians() + 4 * 3.1415f / 5) * 8;
        shapey[3] = enemy.getY() + (float) Math.sin(enemy.getRadians() + 4 * 3.1415f / 5) * 8;

        enemy.setShapeX(shapex);
        enemy.setShapeY(shapey);
    }

    private void wrap(GameData gameData, Entity enemy) {
        if (enemy.getX() < 0) {
            enemy.setX(gameData.getDisplayWidth());
        }
        if (enemy.getX() > gameData.getDisplayWidth()) {
            enemy.setX(0);
        }
        if (enemy.getY() < 0) {
            enemy.setY(gameData.getDisplayHeight());
        }
        if (enemy.getY() > gameData.getDisplayHeight()) {
            enemy.setY(0);
        }
    }
}
