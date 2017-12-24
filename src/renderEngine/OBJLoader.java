package renderEngine;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    public static RawModel loadOBJModel(String filename, Loader loader) {
        FileReader fileReader = getFileReader(filename);
        if (fileReader == null) {
            return null;
        }

        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();
        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] textureArray = null;
        int[] indicesArray = null;

        try {
            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");

                boolean isVerticeLine = line.startsWith("v ");
                boolean isTextureLine = line.startsWith("vt ");
                boolean isNormalLine = line.startsWith("vn ");
                boolean isIndiceLine = line.startsWith("f ");

                float firstNumber = 0;
                float secondNumber = 0;
                float thirdNumber = 0;

                if (isVerticeLine || isNormalLine) {
                    firstNumber = Float.parseFloat(currentLine[1]);
                    secondNumber = Float.parseFloat(currentLine[2]);
                    thirdNumber = Float.parseFloat(currentLine[3]);

                    if(isVerticeLine) {
                        Vector3f lineContent = new Vector3f(firstNumber, secondNumber, thirdNumber);
                        vertices.add(lineContent);
                    } else {
                        Vector3f lineContent = new Vector3f(firstNumber, secondNumber, thirdNumber);
                        normals.add(lineContent);
                    }
                } else if (isTextureLine) {
                    firstNumber = Float.parseFloat(currentLine[1]);
                    secondNumber = Float.parseFloat(currentLine[2]);

                    Vector2f lineContent = new Vector2f(firstNumber, secondNumber);
                    textures.add(lineContent);
                } else if (isIndiceLine) {
                    textureArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }
            while (line != null) {
                boolean isIndiceLine = line.startsWith("f ");
                if (!isIndiceLine) {
                    line = reader.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;

        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

        return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);
    }

    private static void processVertex(String[] vertexData, List<Integer> indices,
                                      List<Vector2f> textures, List<Vector3f> normals,
                                      float[] textureArray, float[] normalsArray) {

        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);

        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currentVertexPointer * 2] = currentTex.x;
        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;

        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertexPointer * 3] = currentNorm.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
    }

    private static FileReader getFileReader(String filename) {
        FileReader fileReader = null;
        File file = new File("./data/res/" + filename + ".obj");
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load file!");
            e.printStackTrace();
        }
        return fileReader;
    }
}
