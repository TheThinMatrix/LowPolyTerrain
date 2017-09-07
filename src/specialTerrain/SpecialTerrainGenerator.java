package specialTerrain;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import generation.ColourGenerator;
import generation.PerlinNoise;
import openglObjects.Vao;
import rendering.TerrainRenderer;
import rendering.TerrainShader;
import terrains.Terrain;
import terrains.TerrainGenerator;
import utils.Colour;
import utils.MyFile;
import vertexDataStoring.VaoLoader;

/**
 * This is a method that I came up with this week to try and achieve correct
 * low-poly lighting while still using the "flat" type qualifier in the shaders.
 * It's a bit more complicated than the other techniques, and I'll explain it
 * more in the tutorial, but basically the idea is to duplicate just enough
 * vertices so that there is one vertex for every triangle in the terrain. Then,
 * each triangle can have its own provoking vertex, and therefore the provoking
 * vertex for each triangle can provide that triangle's correct normal vector.
 * This allows for correct lighting without duplicating all of the vertices, and
 * without using an expensive geometry shader stage.
 * 
 * @author Karl
 *
 */
public class SpecialTerrainGenerator extends TerrainGenerator {

	private static final MyFile VERTEX_SHADER = new MyFile("flatTerrain", "terrainVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("flatTerrain", "terrainFragment.glsl");

	private static final int VERTEX_SIZE_BYTES = 12 + 4 + 4;// position + normal

	private final TerrainRenderer renderer;

	public SpecialTerrainGenerator(PerlinNoise perlinNoise, ColourGenerator colourGen) {
		super(perlinNoise, colourGen);
		this.renderer = new TerrainRenderer(new TerrainShader(VERTEX_SHADER, FRAGMENT_SHADER), true);
	}

	@Override
	public void cleanUp() {
		renderer.cleanUp();
	}

	@Override
	protected Terrain createTerrain(float[][] heights, Colour[][] colours) {
		int vertexCount = calculateVertexCount(heights.length);
		byte[] terrainData = createMeshData(heights, colours, vertexCount);
		int[] indices = SpecialIndexGenerator.generateIndexBuffer(heights.length);
		Vao vao = VaoLoader.createVao(terrainData, indices);
		return new Terrain(vao, indices.length, renderer);
	}

	private int calculateVertexCount(int vertexLength) {
		int bottom2Rows = 2 * vertexLength;
		int remainingRowCount = vertexLength - 2;
		int topCount = remainingRowCount * (vertexLength - 1) * 2;
		return topCount + bottom2Rows;
	}

	private byte[] createMeshData(float[][] heights, Colour[][] colours, int vertexCount) {
		int byteSize = VERTEX_SIZE_BYTES * vertexCount;
		ByteBuffer buffer = ByteBuffer.allocate(byteSize).order(ByteOrder.nativeOrder());
		GridSquare[] lastRow = new GridSquare[heights.length - 1];
		for (int row = 0; row < heights.length - 1; row++) {
			for (int col = 0; col < heights[row].length - 1; col++) {
				GridSquare square = new GridSquare(row, col, heights, colours);
				square.storeSquareData(buffer);
				if (row == heights.length - 2) {
					lastRow[col] = square;
				}
			}
		}
		for (int i = 0; i < lastRow.length; i++) {
			lastRow[i].storeBottomRowData(buffer);
		}
		return buffer.array();
	}

}
