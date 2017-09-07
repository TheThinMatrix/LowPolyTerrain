package flatTerrain;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.util.vector.Vector3f;

import generation.ColourGenerator;
import generation.PerlinNoise;
import generation.SmoothNormalsGenerator;
import indicesGenerators.IndicesGenerator;
import openglObjects.Vao;
import rendering.TerrainRenderer;
import rendering.TerrainShader;
import terrains.Terrain;
import terrains.TerrainGenerator;
import utils.Colour;
import utils.MyFile;
import vertexDataStoring.DataStoring;
import vertexDataStoring.VaoLoader;

/**
 * This terrain generator generates a simple terrain, similar to the basic
 * terrain used in my previous tutorials. The difference here is in the shader
 * programs, with the use of the "flat" type qualifier when passing the colour
 * from vertex shader to fragment shader. This stops the colour being
 * interpolated over each triangle, and instead the entire triangle uses the
 * colour value from just one of the vertices, the "provoking" vertex. This is
 * by default the last specified vertex of the triangle, but it's possible to
 * make it use the first vertex instead.
 * 
 * @author Karl
 *
 */
public class FlatTerrainGenerator extends TerrainGenerator {

	private static final MyFile VERTEX_SHADER = new MyFile("flatTerrain", "terrainVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("flatTerrain", "terrainFragment.glsl");

	private static final int VERTEX_SIZE_BYTES = 12 + 4 + 4;// position + normal
															// + colour

	private final IndicesGenerator indicesGenerator;
	private final TerrainRenderer renderer;

	/**
	 * Sets up the generator and initializes the renderer which is going to be
	 * used to render terrains that use this generator.
	 * 
	 * @param perlinNoise
	 *            - The generator used to generate the vertex heights.
	 * @param colourGen
	 *            - The generator used to generate the colours of the vertices.
	 * @param indicesGenerator
	 *            - The generator used to generate the index buffer for the
	 *            terrain which connects the vertices into triangles.
	 */
	public FlatTerrainGenerator(PerlinNoise perlinNoise, ColourGenerator colourGen, IndicesGenerator indicesGenerator) {
		super(perlinNoise, colourGen);
		this.indicesGenerator = indicesGenerator;
		this.renderer = new TerrainRenderer(new TerrainShader(VERTEX_SHADER, FRAGMENT_SHADER), true);
	}

	@Override
	public Terrain createTerrain(float[][] heights, Colour[][] colours) {
		Vector3f[][] normals = SmoothNormalsGenerator.generateNormals(heights);
		byte[] meshData = getMeshData(heights, normals, colours);
		int[] indices = indicesGenerator.generateIndexBuffer(heights.length);
		Vao vao = VaoLoader.createVao(meshData, indices);
		return new Terrain(vao, indices.length, renderer);
	}

	@Override
	public void cleanUp() {
		renderer.cleanUp();
	}

	/**
	 * @param heights
	 *            - The heights of all the vertices in the terrain.
	 * @param normals
	 *            - The smooth normal vectors for every vertex of the terrain.
	 * @param colours
	 *            - The colour of every vertex.
	 * @return The interleaved vertex data for all the vertices in this terrain.
	 *         Each vertex has a position, normal, and colour. The vertex data
	 *         is already in byte format, ready to be stored into a VBO.
	 */
	private static byte[] getMeshData(float[][] heights, Vector3f[][] normals, Colour[][] colours) {
		int byteSize = VERTEX_SIZE_BYTES * heights.length * heights[0].length;
		ByteBuffer buffer = ByteBuffer.allocate(byteSize).order(ByteOrder.nativeOrder());
		for (int z = 0; z < heights.length; z++) {
			for (int x = 0; x < heights[z].length; x++) {
				DataStoring.packVertexData(x, heights[z][x], z, normals[z][x], colours[z][x], buffer);
			}
		}
		return buffer.array();
	}

}
