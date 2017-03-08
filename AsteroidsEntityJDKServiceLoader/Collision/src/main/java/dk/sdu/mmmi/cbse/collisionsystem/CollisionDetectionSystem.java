package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventType;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

/**
 *
 * @author Niels
 */
public class CollisionDetectionSystem implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity asteroid : world.getEntities(EntityType.ASTEROIDS)) {
            for (Entity player : world.getEntities(EntityType.PLAYER)) {
                if (checkCollision(player, asteroid)) {
                    player.setLife(0);
                }
            }
            for (Entity enemy : world.getEntities(EntityType.ENEMY)) {
                if (checkCollision(enemy, asteroid)) {
                    enemy.setLife(0);
                }
            }
            for (Entity bullet : world.getEntities(EntityType.BULLET)) {
                if (checkCollision(bullet, asteroid)) {
                    gameData.addEvent(new Event(EventType.ASTEROID_SPLIT, asteroid.getID()));
                    asteroid.setLife(asteroid.getLife() - 1);
                    bullet.setExpiration(0);
                }
            }
        }
        for (Entity bullet : world.getEntities(EntityType.BULLET)) {
            for (Entity player : world.getEntities(EntityType.PLAYER)) {
                if (checkCollision(bullet, player)) {
                    bullet.setExpiration(0);
                    player.setLife(player.getLife() - 1);
                }
            }
            for (Entity enemy : world.getEntities(EntityType.ENEMY)) {
                if (checkCollision(bullet, enemy)) {
                    bullet.setExpiration(0);
                    enemy.setLife(enemy.getLife() - 1);
                }
            }
        }
    }

    private boolean checkCollision(Entity entity1, Entity entity2) {
        float a = entity1.getX() - entity2.getX();
        float b = entity1.getY() - entity2.getY();

        double c = Math.sqrt((double) (a * a) + (double) (b * b));

        return c < entity1.getRadius() + entity2.getRadius();

        //return Math.sqrt(Math.pow(entity1.getX() - entity2.getX(), 2) + Math.pow(entity1.getY() - entity2.getY(), 2)) < entity1.getRadius() + entity2.getRadius();
    }

}
