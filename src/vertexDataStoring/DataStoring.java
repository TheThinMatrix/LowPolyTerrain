package vertexDataStoring;

import java.nio.ByteBuffer;

import org.lwjgl.util.vector.Vector3f;

import utils.Colour;
import utils.DataUtils;

/**
 * This class is just used for converting vertex data into a load of bytes which
 * can then be stored in a VBO. It uses ByteBuffers for convenience, as these
 * are basically just a byte[] with a pointer. Previously we've just used
 * FloatBuffers for storing vertex data, but here I take a little bit more care
 * to pack the vertex data tightly. I haven't covered some of the data formats
 * that I use here, but it's not relevant to this tutorial. You can store the
 * vertex data in the VBO in whatever format you want - the low poly stuff will
 * still work the same.
 * 
 * @author Karl
 *
 */
public class DataStoring {

	public static void packVertexData(Vector3f position, Vector3f normal, Colour colour, ByteBuffer buffer) {
		packVertexData(position.x, position.y, position.z, normal, colour, buffer);
	}

	public static void packVertexData(float x, float y, float z, Vector3f normal, Colour colour, ByteBuffer buffer) {
		storePosition(buffer, x, y, z);
		storeNormal(buffer, normal);
		storeColour(buffer, colour);
	}

	public static void packVertexData(float x, float y, float z, Colour colour, ByteBuffer buffer) {
		storePosition(buffer, x, y, z);
		storeColour(buffer, colour);
	}

	private static void storePosition(ByteBuffer buffer, float x, float y, float z) {
		buffer.putFloat(x);
		buffer.putFloat(y);
		buffer.putFloat(z);
	}

	private static void storeNormal(ByteBuffer buffer, Vector3f normal) {
		int packedInt = DataUtils.pack_2_10_10_10_REV_int(normal.x, normal.y, normal.z, 0);
		buffer.putInt(packedInt);
	}

	private static void storeColour(ByteBuffer buffer, Colour colour) {
		byte[] colourBytes = colour.getAsBytes();
		buffer.put(colourBytes);
	}

}
