package game;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import terrains.Terrain;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static renderEngine.OBJLoader.loadOBJModel;

public class MainGame {

    private static Loader loader;
    private static List<Entity> entities;
    private static Terrain terrain;
    private static Terrain terrain2;
    private static Camera camera;
    private static MasterRenderer renderer;
    private static Light light;
    private static Random random;

    public static void main(String[] args) {

        initGame();

        run();

        close();
    }

    private static void initGame() {
        DisplayManager.createDisplay();
        renderer = new MasterRenderer();
        loader = new Loader();

        initGameEntities();
    }

    private static void run() {
        while (!Display.isCloseRequested()) {
            prepareEntities();

            camera.move();
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }
    }

    private static void prepareEntities() {
        //Entities
        for (Entity entity : entities) {
            renderer.processEntity(entity);
        }

        //Terrain
        renderer.processTerrain(terrain);
        renderer.processTerrain(terrain2);
    }

    private static void initGameEntities() {
        entities = new ArrayList<>();
        random = new Random();

        //Trees
        for(int i = 0; i<=100; i++) {
            Vector3f position = new Vector3f(-25 + random.nextFloat() * 50, 0, -50 + random.nextFloat() * 50);
            Vector3f rotation = new Vector3f(0, 0, 0);
            createEntity("tree", "tree", 0, 0, position, rotation, 1.5f);
        }

        //Ferns
        for(int i = 0; i<=100; i++) {
            Vector3f position = new Vector3f(-25 + random.nextFloat() * 50, 0, -50 + random.nextFloat() * 50);
            Vector3f rotation = new Vector3f(0, 0, 0);
            createEntity("fern", "fern", 0, 0, position, rotation, 0.15f);
        }

        //Grass
        for(int i = 0; i<=100; i++) {
            Vector3f position = new Vector3f(-25 + random.nextFloat() * 50, 0, -50 + random.nextFloat() * 50);
            Vector3f rotation = new Vector3f(0, 0, 0);
            createEntity("grassModel", "grassTexture", 0, 0, position, rotation, 0.4f);
        }

        light = new Light(new Vector3f(0,0,25), new Vector3f(1, 1, 1));
        terrain = new Terrain(-1,-1, loader, new ModelTexture(loader.loadTexture("grass")));
        terrain2 = new Terrain(0,-1, loader, new ModelTexture(loader.loadTexture("grass")));
        camera = new Camera();
    }

    private static void createEntity(String modelName, String textureName,
                                     float shineDampness, float reflectivity,
                                     Vector3f position, Vector3f rotation, float scale) {
        RawModel rawModel = loadOBJModel(modelName, loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture(textureName));
        texture.setShineDamper(shineDampness);
        texture.setReflectivity(reflectivity);
        TexturedModel model = new TexturedModel(rawModel, texture);
        entities.add(new Entity(model, position, rotation, scale));
    }

    private static void close() {
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
