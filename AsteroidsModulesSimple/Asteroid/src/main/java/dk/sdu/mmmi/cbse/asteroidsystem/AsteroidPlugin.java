package dk.sdu.mmmi.cbse.asteroidsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventType;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Niels
 */
public class AsteroidPlugin implements IGamePluginService {

    private List<Entity> asteroids;

    public AsteroidPlugin() {
    }

    @Override
    public void start(GameData gameData, World world) {
        asteroids = new ArrayList<>();
        Entity asteroid;
        for (Event event : gameData.getEvents()) {
            if (event.getType().equals(EventType.ASTEROID_SPLIT)) { // creates two new, smaller asteroids
                for (int i = 0; i < 2; i++) {
                    asteroid = createSplittingAsteroid(world.getEntity(event.getEntityID()));
                    world.addEntity(asteroid);
                    asteroids.add(asteroid);
                }
                return;
            }
        }
        for (int i = 0; i < (int) (Math.random() * 5) + 1; i++) { // creates a random amount of asteroids between 1 and 6, at the start
            asteroid = createWholeAsteroid(gameData);
            world.addEntity(asteroid);
            asteroids.add(asteroid);
        }
    }

    private Entity createSplittingAsteroid(Entity parent) {
        Entity asteroid = new Entity();
        asteroid.setPosition(parent.getX(), parent.getY());
        asteroid.setRadius(parent.getRadius() / 2);
        asteroid.setLife(1);
        setAsteroidAttributes(asteroid);
        return asteroid;
    }

    private Entity createWholeAsteroid(GameData gameData) {
        Entity asteroid = new Entity();
        asteroid.setPosition((float) Math.random() * gameData.getDisplayWidth(), (float) Math.random() * gameData.getDisplayHeight());
        asteroid.setRadius(((int) (Math.random() * 10) + 10));
        asteroid.setLife(2);
        setAsteroidAttributes(asteroid);
        return asteroid;
    }

    private void setAsteroidAttributes(Entity asteroid) {
        asteroid.setType(EntityType.ASTEROIDS);
        asteroid.setMaxSpeed(((float) Math.random() * 120) + 10);
        asteroid.setRadians((float) Math.random() * 3.1415f * 2);
        asteroid.setRotationSpeed((int) (Math.random() * 3) - 1); //-1, 0 eller 1
        asteroid.setDx((float) Math.cos(asteroid.getRadians()) * asteroid.getMaxSpeed());
        asteroid.setDy((float) Math.sin(asteroid.getRadians()) * asteroid.getMaxSpeed());
        int size = (int) (asteroid.getRadius() / 2) + 5;
        float[] dists = new float[size];
        for (int i = 0; i < size; i++) {
            dists[i] = (float) (Math.random() * (asteroid.getRadius() / 2)) + (asteroid.getRadius() / 2);
        }
        asteroid.setDists(dists);
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity asteroid : asteroids) {
            world.removeEntity(asteroid);
        }
    }
}
