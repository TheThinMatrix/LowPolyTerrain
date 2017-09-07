package rendering;

import org.lwjgl.util.vector.Matrix4f;

/**
 * Represents a camera in the scene.
 * @author Karl
 *
 */
public interface ICamera {
	
	public Matrix4f getViewMatrix();
	public Matrix4f getProjectionMatrix();
	public Matrix4f getProjectionViewMatrix();

}
