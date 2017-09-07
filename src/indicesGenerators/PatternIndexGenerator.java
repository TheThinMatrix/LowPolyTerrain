package indicesGenerators;

/**
 * This class generates an index buffer by connecting up the vertices of the
 * terrain into triangles. The direction of the triangles is flipped for every
 * other grid square, leading to a more interesting pattern. The order that the
 * indices are provided in is also rotated for some triangles, to change which
 * vertex is the "provoking vertex". This is important when using the "flat"
 * type qualifier in the shaders.
 * 
 * @author Karl
 *
 */
public class PatternIndexGenerator implements IndicesGenerator {

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
				if (row % 2 == 0) {
					pointer = storeQuad1(indices, pointer, topLeft, topRight, bottomLeft, bottomRight, col % 2 == 0);
				} else {
					pointer = storeQuad2(indices, pointer, topLeft, topRight, bottomLeft, bottomRight, col % 2 == 0);
				}
			}
		}
		return indices;
	}

	private int storeQuad1(int[] indices, int pointer, int topLeft, int topRight, int bottomLeft, int bottomRight,
			boolean mixed) {
		indices[pointer++] = topLeft;
		indices[pointer++] = bottomLeft;
		indices[pointer++] = mixed ? topRight : bottomRight;
		indices[pointer++] = bottomRight;
		indices[pointer++] = topRight;
		indices[pointer++] = mixed ? bottomLeft : topLeft;
		return pointer;
	}

	private int storeQuad2(int[] indices, int pointer, int topLeft, int topRight, int bottomLeft, int bottomRight,
			boolean mixed) {
		indices[pointer++] = topRight;
		indices[pointer++] = topLeft;
		indices[pointer++] = mixed ? bottomRight : bottomLeft;
		indices[pointer++] = bottomLeft;
		indices[pointer++] = bottomRight;
		indices[pointer++] = mixed ? topLeft : topRight;
		return pointer;
	}

}
