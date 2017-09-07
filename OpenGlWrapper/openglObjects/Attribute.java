package openglObjects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;

public class Attribute {

	protected final int attributeNumber;
	protected final int dataType;
	protected final boolean normalized;
	protected final int componentCount;
	protected final int bytesPerVertex;

	public Attribute(int attrNumber, int dataType, int componentCount) {
		this.attributeNumber = attrNumber;
		this.dataType = dataType;
		this.componentCount = componentCount;
		this.normalized = false;
		this.bytesPerVertex = calcBytesPerVertex();
	}

	public Attribute(int attrNumber, int dataType, int componentCount, boolean normalized) {
		this.attributeNumber = attrNumber;
		this.dataType = dataType;
		this.componentCount = componentCount;
		this.normalized = normalized;
		this.bytesPerVertex = calcBytesPerVertex();
	}

	protected void enable(boolean enable) {
		if (enable) {
			GL20.glEnableVertexAttribArray(attributeNumber);
		} else {
			GL20.glDisableVertexAttribArray(attributeNumber);
		}
	}

	protected void link(int offset, int stride) {
		GL20.glVertexAttribPointer(attributeNumber, componentCount, dataType, normalized, stride, offset);
	}

	private int calcBytesPerVertex() {
		if (dataType == GL11.GL_FLOAT || dataType == GL11.GL_UNSIGNED_INT || dataType == GL11.GL_INT) {
			return 4 * componentCount;
		} else if (dataType == GL11.GL_SHORT || dataType == GL11.GL_UNSIGNED_SHORT) {
			return 2 * componentCount;
		} else if (dataType == GL11.GL_BYTE || dataType == GL11.GL_UNSIGNED_BYTE) {
			return 1 * componentCount;
		} else if (dataType == GL12.GL_UNSIGNED_INT_2_10_10_10_REV) {
			return 4;
		}
		System.err.println("Unsupported data type for VAO attribute: " + dataType);
		return 0;
	}

}
