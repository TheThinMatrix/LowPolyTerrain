package specialTerrain;

public class SpecialIndexGenerator {

	public static int[] generateIndexBuffer(int vertexCount) {
		int[] indices = new int[(vertexCount - 1) * (vertexCount - 1) * 6];
		int rowLength = (vertexCount - 1) * 2;
		int pointer = storeTopSection(indices, rowLength, vertexCount);
		pointer = storeSecondLastLine(indices, pointer, rowLength, vertexCount);
		pointer = storeLastLine(indices, pointer, rowLength, vertexCount);
		return indices;
	}

	private static int storeTopSection(int[] indices, int rowLength, int vertexLength) {
		int pointer = 0;
		for (int row = 0; row < vertexLength - 3; row++) {
			for (int col = 0; col < vertexLength - 1; col++) {
				int topLeft = (row * rowLength) + (col * 2);
				int topRight = topLeft + 1;
				int bottomLeft = topLeft + rowLength;
				int bottomRight = bottomLeft + 1;
				pointer = storeQuad(topLeft, topRight, bottomLeft, bottomRight, indices, pointer, col % 2 != row % 2);
			}
		}
		return pointer;
	}

	private static int storeSecondLastLine(int[] indices, int pointer, int rowLength, int vertexLength) {
		int row = vertexLength - 3;
		for (int col = 0; col < vertexLength - 1; col++) {
			int topLeft = (row * rowLength) + (col * 2);
			int topRight = topLeft + 1;
			int bottomLeft = (topLeft + rowLength) - col;
			int bottomRight = bottomLeft + 1;
			pointer = storeQuad(topLeft, topRight, bottomLeft, bottomRight, indices, pointer, col % 2 != row % 2);
		}
		return pointer;
	}

	private static int storeLastLine(int[] indices, int pointer, int rowLength, int vertexLength) {
		int row = vertexLength - 2;
		for (int col = 0; col < vertexLength - 1; col++) {
			int topLeft = (row * rowLength) + col;
			int topRight = topLeft + 1;
			int bottomLeft = (topLeft + vertexLength);
			int bottomRight = bottomLeft + 1;
			pointer = storeLastRowQuad(topLeft, topRight, bottomLeft, bottomRight, indices, pointer, col % 2 != row % 2);
		}
		return pointer;
	}

	private static int storeQuad(int topLeft, int topRight, int bottomLeft, int bottomRight, int[] indices, int pointer,
			boolean rightHanded) {
		pointer = storeLeftTriangle(topLeft, topRight, bottomLeft, bottomRight, indices, pointer, rightHanded);
		indices[pointer++] = topRight;
		indices[pointer++] = rightHanded ? topLeft : bottomLeft;
		indices[pointer++] = bottomRight;
		return pointer;
	}
	
	private static int storeLastRowQuad(int topLeft, int topRight, int bottomLeft, int bottomRight, int[] indices, int pointer,
			boolean rightHanded) {
		pointer = storeLeftTriangle(topLeft, topRight, bottomLeft, bottomRight, indices, pointer, rightHanded);
		indices[pointer++] = bottomRight;
		indices[pointer++] = topRight;
		indices[pointer++] = rightHanded ? topLeft : bottomLeft;
		return pointer;
	}
	
	private static int storeLeftTriangle(int topLeft, int topRight, int bottomLeft, int bottomRight, int[] indices, int pointer,
			boolean rightHanded){
		indices[pointer++] = topLeft;
		indices[pointer++] = bottomLeft;
		indices[pointer++] = rightHanded ? bottomRight : topRight;
		return pointer;
	}

}
