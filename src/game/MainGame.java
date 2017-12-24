package game;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

import static renderEngine.OBJLoader.loadOBJModel;

public class MainGame {

    private static Loader loader;
    private static Entity entity;
    private static Camera camera;
    private static Renderer renderer;
    private static StaticShader shader;
    private static Light light;

    public static void main(String[] args) {

        initGame();

        run();

        close();
    }

    private static void initGame() {
        DisplayManager.createDisplay();
        loader = new Loader();
        shader = new StaticShader();
        renderer = new Renderer(shader);

        initGameEntities();
    }

    private static void run() {
        while (!Display.isCloseRequested()) {
            camera.move();

            entity.addRotation(new Vector3f(0,0.2f,0));

            renderer.prepare();
            shader.start();
            shader.loadLight(light);
            shader.loadViewMatrix(camera);
            renderer.render(entity, shader);
            shader.stop();

            DisplayManager.updateDisplay();
        }
    }

    private static void close() {
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    private static void initGameEntities() {
        RawModel rawModel = loadOBJModel("dragon", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("purple"));
        texture.setShineDamper(10);
        texture.setReflectivity(1);
        TexturedModel model = new TexturedModel(rawModel, texture);
        entity = new Entity(model, new Vector3f(-0, 0, -25), new Vector3f(0, 0, 0), 1);
        light = new Light(new Vector3f(0,0,25), new Vector3f(1, 1, 1));
        camera = new Camera();
    }
}
