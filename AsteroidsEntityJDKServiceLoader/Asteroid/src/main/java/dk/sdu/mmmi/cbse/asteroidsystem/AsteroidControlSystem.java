package dk.sdu.mmmi.cbse.asteroidsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventType;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

/**
 *
 * @author Niels
 */
public class AsteroidControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity asteroid : world.getEntities(EntityType.ASTEROIDS)) {
            float dt = gameData.getDelta();
            // set position
            asteroid.setRadians(asteroid.getRadians() + asteroid.getRotationSpeed() * dt);

            asteroid.setDx(asteroid.getDx() + (float) Math.cos(asteroid.getRadians()) * dt);
            asteroid.setDy(asteroid.getDy() + (float) Math.sin(asteroid.getRadians()) * dt);

            asteroid.setX(asteroid.getX() + asteroid.getDx() * dt);
            asteroid.setY(asteroid.getY() + asteroid.getDy() * dt);

            for (Event event : gameData.getEvents(EventType.ASTEROID_SPLIT, asteroid.getID())) {
                if (asteroid.getLife() > 0) {
                    new AsteroidPlugin().start(gameData, world);
                }
                world.removeEntity(asteroid);
                gameData.removeEvent(event);
            }

            // set shape
            setShape(asteroid);

            // screen wrap
            wrap(gameData, asteroid);
        }
    }

    private void setShape(Entity asteroid) {
        int size = (int) (asteroid.getRadius() * 0.6f) + 1;
        float[] shapex = new float[size];
        float[] shapey = new float[size];
        float angle = 0;

        for (int i = 0; i < size; i++) {
            shapex[i] = asteroid.getX() + (float) (Math.cos(angle + asteroid.getRadians()) * asteroid.getDists()[i]);
            shapey[i] = asteroid.getY() + (float) (Math.sin(angle + asteroid.getRadians()) * asteroid.getDists()[i]);
            angle += (3.1415f * 2) / size;
        }

        asteroid.setShapeX(shapex);
        asteroid.setShapeY(shapey);
    }

    private void wrap(GameData gameData, Entity asteroid) {
        if (asteroid.getX() < 0) {
            asteroid.setX(gameData.getDisplayWidth());
        }
        if (asteroid.getX() > gameData.getDisplayWidth()) {
            asteroid.setX(0);
        }
        if (asteroid.getY() < 0) {
            asteroid.setY(gameData.getDisplayHeight());
        }
        if (asteroid.getY() > gameData.getDisplayHeight()) {
            asteroid.setY(0);
        }
    }
}
