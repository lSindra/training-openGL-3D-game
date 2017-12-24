package game;

import entities.Camera;
import entities.Entity;
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

    public static void main(String[] args) {

        initGame();

        initGameEntities();

        run();

        close();
    }

    private static void initGame() {
        DisplayManager.createDisplay();
        loader = new Loader();
        shader = new StaticShader();
        renderer = new Renderer(shader);
    }

    private static void run() {
        while (!Display.isCloseRequested()) {
            camera.move();

            renderer.prepare();
            shader.start();
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
        RawModel rawModel = loadOBJModel("stall", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
        TexturedModel model = new TexturedModel(rawModel, texture);
        entity = new Entity(model, new Vector3f(-0, 0, -1), new Vector3f(0, 0, 0), 1);
        camera = new Camera();
    }
}
