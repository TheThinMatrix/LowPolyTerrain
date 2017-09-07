package utils;

import org.lwjgl.util.vector.Vector3f;

public class Maths {
	
	public static float clamp(float value, float min, float max){
		return Math.max(Math.min(value, max), min);
	}

	/**
	 * Calculates the normal of the triangle made from the 3 vertices. The vertices must be specified in counter-clockwise order.
	 * @param vertex0
	 * @param vertex1
	 * @param vertex2
	 * @return
	 */
	public static Vector3f calcNormal(Vector3f vertex0, Vector3f vertex1, Vector3f vertex2) {
		Vector3f tangentA = Vector3f.sub(vertex1, vertex0, null);
		Vector3f tangentB = Vector3f.sub(vertex2, vertex0, null);
		Vector3f normal = Vector3f.cross(tangentA, tangentB, null);
		normal.normalise();
		return normal;
	}
	
}
