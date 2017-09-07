package generation;

import org.lwjgl.util.vector.Vector3f;

/**
 * Generates smooth normals for all the vertices in a terrain.
 * 
 * @author Karl
 *
 */
public class SmoothNormalsGenerator {

	/**
	 * Generates smooth normals for every vertex in the terrain, based on the
	 * terrain heights. The normal at each vertex is basically the average of
	 * the normals of all the surrounding faces.
	 * 
	 * @param heights - The heights of all the vertices.
	 * @return The normals of all the vertices.
	 */
	public static Vector3f[][] generateNormals(float[][] heights) {
		Vector3f[][] normals = new Vector3f[heights.length][heights.length];
		for (int z = 0; z < normals.length; z++) {
			for (int x = 0; x < normals[z].length; x++) {
				normals[z][x] = calculateNormal(x, z, heights);
			}
		}
		return normals;
	}

	private static Vector3f calculateNormal(int x, int z, float[][] heights) {
		float heightL = getHeight(x - 1, z, heights);
		float heightR = getHeight(x + 1, z, heights);
		float heightD = getHeight(x, z - 1, heights);
		float heightU = getHeight(x, z + 1, heights);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	private static float getHeight(int x, int z, float[][] heights) {
		x = x < 0 ? 0 : x;
		z = z < 0 ? 0 : z;
		x = x >= heights.length ? heights.length - 1 : x;
		z = z >= heights.length ? heights.length - 1 : z;
		return heights[z][x];
	}

}
