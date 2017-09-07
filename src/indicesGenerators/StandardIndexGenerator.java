package indicesGenerators;

/**
 * Generates a standard index buffer for a terrain. Each quad of the terrain is
 * made of two triangles, one top left triangle and one bottom right. Every quad
 * looks the same using this index buffer generator.
 * 
 * @author Karl
 *
 */
public class StandardIndexGenerator implements IndicesGenerator {

	@Override
	public int[] generateIndexBuffer(int vertexCount) {
		int indexCount = (vertexCount - 1) * (vertexCount - 1) * 6;
		int[] indices = new int[indexCount];
		int pointer = 0;
		for (int col = 0; col < vertexCount - 1; col++) {
			for (int row = 0; row < vertexCount - 1; row++) {
				int topLeft = (row * vertexCount) + col;
				int topRight = topLeft + 1;
				int bottomLeft = ((row + 1) * vertexCount) + col;
				int bottomRight = bottomLeft + 1;
				pointer = storeQuad(indices, pointer, topLeft, topRight, bottomLeft, bottomRight);
			}
		}
		return indices;
	}

	/**
	 * Stores the indices for one quad of the terrain. This creates a triangle
	 * in the top-left of the quad, and the bottom-right.
	 * 
	 * @param indices
	 *            - The array of ints where all the indices are being stored.
	 * @param pointer
	 * @param topLeft
	 * @param topRight
	 * @param bottomLeft
	 * @param bottomRight
	 * @return The current pointer for the indices array.
	 */
	private int storeQuad(int[] indices, int pointer, int topLeft, int topRight, int bottomLeft, int bottomRight) {
		indices[pointer++] = topLeft;
		indices[pointer++] = bottomLeft;
		indices[pointer++] = bottomRight;
		indices[pointer++] = topLeft;
		indices[pointer++] = bottomRight;
		indices[pointer++] = topRight;
		return pointer;
	}

}
