package dk.sdu.mmmi.cbse.player;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
    ,
    @ServiceProvider(service = IGamePluginService.class)
})
public class PlayerControlSystem implements IEntityProcessingService, IGamePluginService {
    
    private Entity player;
    
    @Override
    public void start(GameData gameData, World world) {
        player = createPlayerShip(gameData);
        world.addEntity(player);
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity player : world.getEntities(EntityType.PLAYER)) {
            GameKeys keys = gameData.getKeys();
            float dt = gameData.getDelta();

            if (keys.isDown(GameKeys.LEFT)) {
                player.setRadians(player.getRadians() + player.getRotationSpeed() * dt);
            } else if (keys.isDown(GameKeys.RIGHT)) {
                player.setRadians(player.getRadians() - player.getRotationSpeed() * dt);
            }

            // accelerating
            if (keys.isDown(GameKeys.UP)) {
                player.setDx(player.getDx() + (float) Math.cos(player.getRadians()) * player.getAcceleration() * dt);
                player.setDy(player.getDy() + (float) Math.sin(player.getRadians()) * player.getAcceleration() * dt);
            }

            // deceleration
            float vec = (float) Math.sqrt(player.getDx() * player.getDx() + player.getDy() * player.getDy());
            if (vec > 0) {
                player.setDx(player.getDx() - (player.getDx() / vec) * player.getDeacceleration() * dt);
                player.setDy(player.getDy() - (player.getDy() / vec) * player.getDeacceleration() * dt);
            }
            if (vec > player.getMaxSpeed()) {
                player.setDx((player.getDx() / vec) * player.getMaxSpeed());
                player.setDy((player.getDy() / vec) * player.getMaxSpeed());
            }

            // set position
            player.setX(player.getX() + player.getDx() * dt);
            player.setY(player.getY() + player.getDy() * dt);

            // set shape
            setShape(player);
            
            if(player.getLife() < 1) {
                world.removeEntity(player);
            }
        }
    }
    
    private void setShape(Entity player) {
        float[] shapex = new float[4];
        float[] shapey = new float[4];

        shapex[0] = player.getX() + (float) Math.cos(player.getRadians()) * 8;
        shapey[0] = player.getY() + (float) Math.sin(player.getRadians()) * 8;

        shapex[1] = player.getX() + (float) Math.cos(player.getRadians() - 4 * 3.1415f / 5) * 8;
        shapey[1] = player.getY() + (float) Math.sin(player.getRadians() - 4 * 3.1415f / 5) * 8;

        shapex[2] = player.getX() + (float) Math.cos(player.getRadians() + 3.1415f) * 5;
        shapey[2] = player.getY() + (float) Math.sin(player.getRadians() + 3.1415f) * 5;

        shapex[3] = player.getX() + (float) Math.cos(player.getRadians() + 4 * 3.1415f / 5) * 8;
        shapey[3] = player.getY() + (float) Math.sin(player.getRadians() + 4 * 3.1415f / 5) * 8;

        player.setShapeX(shapex);
        player.setShapeY(shapey);
    }
    
    private Entity createPlayerShip(GameData gameData) {
        Entity playerShip = new Entity();
        playerShip.setType(EntityType.PLAYER);

        playerShip.setPosition(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);

        playerShip.setMaxSpeed(300);
        playerShip.setAcceleration(200);
        playerShip.setDeacceleration(10);
        
        playerShip.setLife(3);
        
        playerShip.setRadius(5);

        playerShip.setRadians(3.1415f / 2);
        playerShip.setRotationSpeed(3);

        return playerShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(player);
    }

}
