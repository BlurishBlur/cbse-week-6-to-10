package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventType;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

/**
 *
 * @author Niels
 */
public class BulletControlSystem implements IEntityProcessingService {

    private int enemyShootTimer = 10;

    @Override
    public void process(GameData gameData, World world) {
        if (!world.getEntities(EntityType.PLAYER).isEmpty() && gameData.getKeys().isPressed(GameKeys.SPACE)) {
            gameData.addEvent(new Event(EventType.PLAYER_SHOOT, world.getEntities(EntityType.PLAYER).get(0).getID()));
        }
        if (!world.getEntities(EntityType.ENEMY).isEmpty()) {
            enemyShootTimer--;
            if (enemyShootTimer < 0) {
                gameData.addEvent(new Event(EventType.ENEMY_SHOOT, world.getEntities(EntityType.ENEMY).get(0).getID()));
                enemyShootTimer = (int) (Math.random() * 90) + 10;
            }
        }

        for (Event event : gameData.getEvents()) {
            if (event.getType().equals(EventType.PLAYER_SHOOT) || event.getType().equals(EventType.ENEMY_SHOOT)) {
                new BulletPlugin(world.getEntity(event.getEntityID())).start(gameData, world);
                gameData.removeEvent(event);
            }
        }

        float dt = gameData.getDelta();
        for (Entity bullet : world.getEntities(EntityType.BULLET)) {
            bullet.setX(bullet.getX() + bullet.getDx() * dt);
            bullet.setY(bullet.getY() + bullet.getDy() * dt);

            setShape(bullet);

            wrap(gameData, bullet);

            bullet.reduceExpiration(dt);
            if (bullet.getExpiration() < 0) {
                world.removeEntity(bullet);
            }
        }
    }

    private void setShape(Entity bullet) {
        float[] shapex = new float[2];
        float[] shapey = new float[2];

        shapex[0] = bullet.getX();
        shapey[0] = bullet.getY();

        shapex[1] = bullet.getX() - ((float) Math.cos(bullet.getRadians()) * 12);
        shapey[1] = bullet.getY() - ((float) Math.sin(bullet.getRadians()) * 12);

        bullet.setShapeX(shapex);
        bullet.setShapeY(shapey);
    }

    private void wrap(GameData gameData, Entity bullet) {
        if (bullet.getX() < 0) {
            bullet.setX(gameData.getDisplayWidth());
        }
        if (bullet.getX() > gameData.getDisplayWidth()) {
            bullet.setX(0);
        }
        if (bullet.getY() < 0) {
            bullet.setY(gameData.getDisplayHeight());
        }
        if (bullet.getY() > gameData.getDisplayHeight()) {
            bullet.setY(0);
        }
    }

}
