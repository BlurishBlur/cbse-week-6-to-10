package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventType;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})
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
                createBullet(world, world.getEntity(event.getEntityID()));
                gameData.removeEvent(event);
            }
        }

        float dt = gameData.getDelta();
        for (Entity bullet : world.getEntities(EntityType.BULLET)) {
            bullet.setX(bullet.getX() + bullet.getDx() * dt);
            bullet.setY(bullet.getY() + bullet.getDy() * dt);

            setShape(bullet);

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

    private void createBullet(World world, Entity owner) {
        Entity newBullet = new Entity();
        newBullet.setType(EntityType.BULLET);
        
        newBullet.setExpiration(1);

        newBullet.setPosition(owner.getX() + ((float) Math.cos(owner.getRadians())) * 12, owner.getY() + ((float) Math.sin(owner.getRadians())) * 12);

        newBullet.setMaxSpeed(200);

        newBullet.setRadians(owner.getRadians());
        newBullet.setDx(((float) Math.cos(owner.getRadians())) * newBullet.getMaxSpeed());
        newBullet.setDy(((float) Math.sin(owner.getRadians())) * newBullet.getMaxSpeed());
        world.addEntity(newBullet);
    }

}
