package engineTester;

import models.TexturedModel;
import org.lwjgl.opengl.Display;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();
        Renderer renderer = new Renderer();
        StaticShader shader = new StaticShader();

        float[] vertices = {
                //First triangle
                -0.5f, 0.5f, 0, //v0
                -0.5f, -0.5f, 0,//v1
                0.5f, -0.5f, 0, //v2
                0.5f, 0.5f, 0,  //v3
        };
        int[] indices = {
                0, 1, 3, //Top left triangle
                3, 1, 2  //Bottom right triangle
        };
        float[] textureCoords = {
                0, 0,  //V0
                0, 1,  //V1
                1, 1,  //V2
                1, 0   //V3
        };

        RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
        ModelTexture texture = new ModelTexture(loader.loadTexture("square"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        while (!Display.isCloseRequested()) {
            renderer.prepare();
            shader.start();
            renderer.render(texturedModel);
            shader.stop();

            DisplayManager.updateDisplay();
        }

        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
