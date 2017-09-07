package rendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import utils.Colour;

/**
 * Represents a directional light in the scene. This has a direction, a colour,
 * and also indicates how much diffuse lighting should be used and how much
 * ambient lighting should be used.
 * 
 * @author Karl
 *
 */
public class Light {

	private Vector3f direction;
	private Colour colour;
	private Vector2f lightBias;// how much ambient light and how much diffuse
								// light

	public Light(Vector3f direction, Colour colour, Vector2f lightBias) {
		this.direction = direction;
		this.direction.normalise();
		this.colour = colour;
		this.lightBias = lightBias;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public Colour getColour() {
		return colour;
	}

	/**
	 * @return A vector with 2 float values. The x value is how much ambient
	 *         lighting should be used, and the y value is how much diffuse
	 *         lighting should be used.
	 */
	public Vector2f getLightBias() {
		return lightBias;
	}

}
