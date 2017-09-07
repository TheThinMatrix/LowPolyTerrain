package splitTerrain;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.util.vector.Vector3f;

import generation.ColourGenerator;
import generation.PerlinNoise;
import openglObjects.Vao;
import rendering.TerrainRenderer;
import rendering.TerrainShader;
import terrains.Terrain;
import terrains.TerrainGenerator;
import utils.Colour;
import utils.Maths;
import utils.MyFile;
import vertexDataStoring.DataStoring;
import vertexDataStoring.VaoLoader;

/**
 * This generator splits up all of the triangles of the terrain by duplicating
 * all the vertices. Each triangle then has its own 3 vertices, each of which
 * holds the same normal vector, the normal of that triangle. No vertices are
 * shared between triangles. That way, when the normals are interpolated over
 * the triangle, the entire triangle will use the same normal vector, creating
 * the low poly look.
 * 
 * No index buffer is needed for this terrain seeing as there are no shared
 * vertices, and so an index buffer would be pointless.
 * 
 * @author Karl
 *
 */
public class SplitTerrainGenerator extends TerrainGenerator {

	private static final MyFile VERTEX_SHADER = new MyFile("splitTerrain", "terrainVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("splitTerrain", "terrainFragment.glsl");

	private static final int VERTEX_SIZE_BYTES = 12 + 4 + 4;// position + normal
															// + colour
	private final TerrainRenderer renderer;

	public SplitTerrainGenerator(PerlinNoise perlinNoise, ColourGenerator colourGen) {
		super(perlinNoise, colourGen);
		this.renderer = new TerrainRenderer(new TerrainShader(VERTEX_SHADER, FRAGMENT_SHADER), false);
	}

	@Override
	public void cleanUp() {
		renderer.cleanUp();
	}

	@Override
	protected Terrain createTerrain(float[][] heights, Colour[][] colours) {
		int vertexCount = calculateVertexCount(heights.length);
		byte[] terrainData = createMeshData(heights, colours, vertexCount);
		Vao vao = VaoLoader.createVao(terrainData, null);
		return new Terrain(vao, vertexCount, renderer);
	}

	/**
	 * Loops through the grid squares of the terrain, storing the all the vertex
	 * data for each one in a byte buffer.
	 * 
	 * @param heights
	 *            - The heights of all the vertices.
	 * @param colours
	 *            - The colours of all the vertices.
	 * @param vertexCount
	 *            - The number of vertices along one edge of the terrain.
	 * @return The interleaved vertex data for the entire terrain.
	 */
	private byte[] createMeshData(float[][] heights, Colour[][] colours, int vertexCount) {
		int byteSize = VERTEX_SIZE_BYTES * vertexCount;
		ByteBuffer buffer = ByteBuffer.allocate(byteSize).order(ByteOrder.nativeOrder());
		for (int row = 0; row < heights.length - 1; row++) {
			for (int col = 0; col < heights[row].length - 1; col++) {
				storeGridSquare(col, row, heights, colours, buffer);
			}
		}
		return buffer.array();
	}

	/**
	 * Stores the vertex data for a grid square of the terrain. It first
	 * calculates the 4 positions of the corners, and then uses those positions
	 * to calculate the normals of the two triangles that make up this quad. The
	 * vertex data for the two triangles is then stored.
	 * 
	 * @param col
	 *            - Column of the grid square in the terrain.
	 * @param row
	 *            - Row of the grid square.
	 * @param heights
	 *            - All the vertex heights.
	 * @param colours
	 *            - All the vertex colours.
	 * @param buffer
	 *            - The byte buffer where all the vertex data is being stored.
	 */
	private void storeGridSquare(int col, int row, float[][] heights, Colour[][] colours, ByteBuffer buffer) {
		Vector3f[] cornerPos = calculateCornerPositions(col, row, heights);
		Colour[] cornerCols = calculateCornerColours(col, row, colours);
		Vector3f normalTopLeft = Maths.calcNormal(cornerPos[0], cornerPos[1], cornerPos[2]);
		Vector3f normalBottomRight = Maths.calcNormal(cornerPos[2], cornerPos[1], cornerPos[3]);
		storeTriangle(cornerPos, cornerCols, normalTopLeft, buffer, 0, 1, 2);
		storeTriangle(cornerPos, cornerCols, normalBottomRight, buffer, 2, 1, 3);
	}

	/**
	 * Stores the data for the 3 vertices of this triangle. All three vertices
	 * have the same normal vector - the normal that was calculated for this
	 * triangle.
	 * 
	 * @param cornerPos
	 * @param cornerCols
	 * @param normal
	 * @param buffer
	 * @param index0
	 * @param index1
	 * @param index2
	 */
	private void storeTriangle(Vector3f[] cornerPos, Colour[] cornerCols, Vector3f normal, ByteBuffer buffer,
			int index0, int index1, int index2) {
		DataStoring.packVertexData(cornerPos[index0], normal, cornerCols[index0], buffer);
		DataStoring.packVertexData(cornerPos[index1], normal, cornerCols[index1], buffer);
		DataStoring.packVertexData(cornerPos[index2], normal, cornerCols[index2], buffer);
	}

	private Colour[] calculateCornerColours(int col, int row, Colour[][] colours) {
		Colour[] cornerCols = new Colour[4];
		cornerCols[0] = colours[row][col];
		cornerCols[1] = colours[row + 1][col];
		cornerCols[2] = colours[row][col + 1];
		cornerCols[3] = colours[row + 1][col + 1];
		return cornerCols;
	}

	private Vector3f[] calculateCornerPositions(int col, int row, float[][] heights) {
		Vector3f[] vertices = new Vector3f[4];
		vertices[0] = new Vector3f(col, heights[row][col], row);
		vertices[1] = new Vector3f(col, heights[row + 1][col], row + 1);
		vertices[2] = new Vector3f(col + 1, heights[row][col + 1], row);
		vertices[3] = new Vector3f(col + 1, heights[row + 1][col + 1], row + 1);
		return vertices;
	}

	/**
	 * Calculates the total number of vertices in the terrain. There are 3
	 * vertices per triangle, and 2 triangles per quad.
	 * 
	 * @param vertexLength
	 * @return
	 */
	private int calculateVertexCount(int vertexLength) {
		int gridSquareLength = vertexLength - 1;
		int totalGridSquares = gridSquareLength * gridSquareLength;
		return totalGridSquares * 2 * 3;// 2 triangles with 3 verts each
	}

}
