package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

/**
 *
 * @author Niels
 */
public class BulletPlugin implements IGamePluginService {

    private Entity bullet, owner;

    public BulletPlugin(Entity owner) {
        this.owner = owner;
    }

    @Override
    public void start(GameData gameData, World world) {
        bullet = createBullet();
        world.addEntity(bullet);
    }

    private Entity createBullet() {
        Entity newBullet = new Entity();
        newBullet.setType(EntityType.BULLET);
        
        newBullet.setExpiration(1);

        newBullet.setPosition(owner.getX() + ((float) Math.cos(owner.getRadians())) * 12, owner.getY() + ((float) Math.sin(owner.getRadians())) * 12);

        newBullet.setMaxSpeed(200);

        newBullet.setRadians(owner.getRadians());
        newBullet.setDx(((float) Math.cos(owner.getRadians())) * newBullet.getMaxSpeed());
        newBullet.setDy(((float) Math.sin(owner.getRadians())) * newBullet.getMaxSpeed());

        return newBullet;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(bullet);
    }

}
