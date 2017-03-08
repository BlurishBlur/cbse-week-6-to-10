package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

/**
 *
 * @author Niels
 */
public class EnemyPlugin implements IGamePluginService {

    private Entity enemy;

    public EnemyPlugin() {
    }

    @Override
    public void start(GameData gameData, World world) {
        enemy = createEnemyShip(gameData);
        world.addEntity(enemy);
    }

    private Entity createEnemyShip(GameData gameData) {
        Entity enemyShip = new Entity();
        enemyShip.setType(ENEMY);
        
        enemyShip.setPosition((float) Math.random() * gameData.getDisplayWidth(), (float) Math.random() * gameData.getDisplayHeight());

        enemyShip.setMaxSpeed((float) (Math.random() * 145) + 75);
        enemyShip.setAcceleration((float) (Math.random() * 125) + 75);
        enemyShip.setDeacceleration((float) (Math.random() * 25) + 5);
        
        enemyShip.setLife(3);
        
        enemyShip.setRadius(5);

        enemyShip.setRadians(3.1415f / 2);
        enemyShip.setRotationSpeed((int) (Math.random() * 3) + 1);

        return enemyShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(enemy);
    }
}
