package game;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import textures.ModelTexture;

import static renderEngine.OBJLoader.loadOBJModel;

public class MainGame {

    private static Loader loader;
    private static Entity entity;
    private static Camera camera;
    private static MasterRenderer renderer;
    private static StaticShader shader;
    private static Light light;

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
            camera.move();

            renderer.processEntity(entity);

            renderer.render(light, camera);
            entity.addRotation(new Vector3f(0,0.2f,0));

            DisplayManager.updateDisplay();
        }
    }

    private static void close() {
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    private static void initGameEntities() {
        RawModel rawModel = loadOBJModel("dragon", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("purple"));
        texture.setShineDamper(10);
        texture.setReflectivity(0.5f);
        TexturedModel model = new TexturedModel(rawModel, texture);
        entity = new Entity(model, new Vector3f(-0, 0, -25), new Vector3f(0, 0, 0), 1);
        light = new Light(new Vector3f(0,0,25), new Vector3f(1, 1, 1));
        camera = new Camera();
    }
}
