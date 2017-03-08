package dk.sdu.mmmi.cbse.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import dk.sdu.mmmi.cbse.entities.Enemy;
import dk.sdu.mmmi.cbse.entities.Player;
import dk.sdu.mmmi.cbse.entities.Bullet;
import dk.sdu.mmmi.cbse.main.Game;
import dk.sdu.mmmi.cbse.managers.GameKeys;
import dk.sdu.mmmi.cbse.managers.GameStateManager;
import java.util.ArrayList;
import java.util.List;

public class PlayState extends GameState {

    private ShapeRenderer sr;

    private Player player;

    private Enemy enemy;
    
    private float timer = 10;

    private List<Bullet> bullets;
    
    private SpriteBatch batch;
    private BitmapFont font;
    
    private int fps = 0;
    private long fpsTimer;
    

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {
        sr = new ShapeRenderer();
        player = new Player();
        enemy = new Enemy();
        bullets = new ArrayList<>();
        batch = new SpriteBatch();
        font = new BitmapFont();
        fpsTimer = System.currentTimeMillis();
    }

    public void update(float dt) {
        //update stuff here
        if(System.currentTimeMillis() - fpsTimer > 1000) {
            fps = Math.round(dt * 3600.0f);
            fpsTimer = System.currentTimeMillis();
        }
        batch.begin();
        font.draw(batch, "FPS: " + fps, 10, Game.HEIGHT - 10);
        batch.end();
        
        handleInput();
        player.update(dt);
        enemy.update(dt);
        for(int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update(dt);
            bullets.get(i).checkCollision(player);
            bullets.get(i).checkCollision(enemy);
            if(bullets.get(i).shouldRemove()) {
                bullets.remove(bullets.get(i));
            }
        }
    }

    public void draw() {
        //draw stuff here
        player.draw(sr);
        enemy.draw(sr);
        for (Bullet bullet : bullets) {
            bullet.draw(sr);
        }
    }
    

    public void handleInput() {
        player.setLeft(GameKeys.isDown(GameKeys.LEFT));
        player.setRight(GameKeys.isDown(GameKeys.RIGHT));
        player.setUp(GameKeys.isDown(GameKeys.UP));
        if (GameKeys.isPressed(GameKeys.SPACE)) {
            bullets.add(new Bullet(player));
        }
        
        timer--;
        if(timer < 0) {
            enemy.setLeft(MathUtils.randomBoolean(0.40f));
            enemy.setRight(MathUtils.randomBoolean(0.40f));
            enemy.setUp(MathUtils.randomBoolean(0.80f));
            timer = MathUtils.random(10, 100);
        }
        if (MathUtils.random() < 0.03) {
            bullets.add(new Bullet(enemy));
        }
    }

    public void dispose() {
    }

}
