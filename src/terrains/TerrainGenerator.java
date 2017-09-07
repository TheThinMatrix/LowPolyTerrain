package terrains;

import generation.ColourGenerator;
import generation.PerlinNoise;
import utils.Colour;

/**
 * Generates a terrain. This is in charge of creating the VAO for the terrain,
 * and providing the renderer to the terrain. In this abstract class the heights
 * and colours for the terrain are generated. Child classed have to implement
 * the createTerrain method which creates the mesh for the terrain and loads it
 * up to a VAO.
 * 
 * @author Karl
 *
 */
public abstract class TerrainGenerator {

	private final PerlinNoise perlinNoise;
	private final ColourGenerator colourGen;

	public TerrainGenerator(PerlinNoise perlinNoise, ColourGenerator colourGen) {
		this.perlinNoise = perlinNoise;
		this.colourGen = colourGen;
	}

	/**
	 * Generates a terrain. First the heights and colours of all the vertices
	 * are generated.
	 * 
	 * @param gridSize
	 *            - The number of grid squares along one side of the terrain.
	 * @return The generated terrain.
	 */
	public Terrain generateTerrain(int gridSize) {
		float[][] heights = generateHeights(gridSize, perlinNoise);
		Colour[][] colours = colourGen.generateColours(heights, perlinNoise.getAmplitude());
		return createTerrain(heights, colours);
	}

	/**
	 * For use when the app closes.
	 */
	public abstract void cleanUp();

	/**
	 * Generates the terrain mesh data, loads it up to a VAO, and initializes
	 * the terrain.
	 * 
	 * @param heights
	 *            - The heights of all the vertices in the terrain.
	 * @param colours
	 *            - The colours of all the vertices.
	 * @return The new terrain.
	 */
	protected abstract Terrain createTerrain(float[][] heights, Colour[][] colours);

	/**
	 * Uses the perlin noise generator (which might actually not be using the
	 * Perlin Noise algorithm - I'm not quite sure if it is or isn't) to
	 * generate heights for all of the terrain's vertices.
	 * 
	 * @param gridSize - The number of grid squares along one edge of the terrain.
	 * @param perlinNoise - The heights generator.
	 * @return All the heights for the vertices.
	 */
	private float[][] generateHeights(int gridSize, PerlinNoise perlinNoise) {
		float heights[][] = new float[gridSize + 1][gridSize + 1];
		for (int z = 0; z < heights.length; z++) {
			for (int x = 0; x < heights[z].length; x++) {
				heights[z][x] = perlinNoise.getPerlinNoise(x, z);
			}
		}
		return heights;
	}
}
